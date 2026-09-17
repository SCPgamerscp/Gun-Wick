package com.scpgamerscp.gunwick.entity.ai;

import com.scpgamerscp.gunwick.config.GunWickConfig;
import com.scpgamerscp.gunwick.entity.GunWickEntity;
import com.scpgamerscp.gunwick.item.TaczGuns;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ShootResult;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GunWickRangedGoal extends Goal {
    private final GunWickEntity wick;
    private int seeTime;
    private int strafeClock;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int switchCooldown;

    public GunWickRangedGoal(GunWickEntity wick) {
        this.wick = wick;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.wick.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        this.wick.setAggressive(true);
        this.switchCooldown = 0;
    }

    @Override
    public void stop() {
        this.wick.setAggressive(false);
        this.seeTime = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.wick.getTarget();
        if (target == null) {
            return;
        }

        double dist = this.wick.distanceTo(target);
        boolean canSee = this.wick.getSensing().hasLineOfSight(target);
        this.seeTime = canSee ? this.seeTime + 1 : 0;

        TaczGuns.Loadout wanted = this.pickLoadout(dist);
        this.ensureGun(wanted);

        this.aimAt(target);
        this.strafe(target, dist, canSee);

        if (canSee && this.seeTime > 8 && this.switchCooldown <= 0) {
            this.tryShoot(target);
        }
        if (this.switchCooldown > 0) {
            this.switchCooldown--;
        }

        TaczGuns.refill(this.wick.getMainHandItem());
    }

    private TaczGuns.Loadout pickLoadout(double dist) {
        if (dist <= GunWickConfig.CLOSE_RANGE.get()) {
            return TaczGuns.Loadout.SHOTGUN;
        }
        return this.wick.isPhaseTwo() ? TaczGuns.Loadout.MINIGUN : TaczGuns.Loadout.GLOCK;
    }

    private void ensureGun(TaczGuns.Loadout wanted) {
        ItemStack held = this.wick.getMainHandItem();
        if (TaczGuns.isLoadout(held, wanted)) {
            return;
        }
        ItemStack next = TaczGuns.create(wanted);
        if (next.isEmpty()) {
            return;
        }
        this.wick.setGun(next);
        IGunOperator.fromLivingEntity(this.wick).draw(this.wick::getMainHandItem);
        this.switchCooldown = 12;
    }

    private void aimAt(LivingEntity target) {
        double dx = target.getX() - this.wick.getX();
        double dy = target.getEyeY() - this.wick.getEyeY();
        double dz = target.getZ() - this.wick.getZ();
        float yaw = (float) (Mth.atan2(dz, dx) * (180.0F / Math.PI)) - 90.0F;
        float pitch = (float) -(Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * (180.0F / Math.PI));
        this.wick.setYRot(yaw);
        this.wick.setXRot(pitch);
        this.wick.yHeadRot = yaw;
        this.wick.yBodyRot = yaw;
        this.wick.getLookControl().setLookAt(target, 90.0F, 90.0F);
    }

    private void strafe(LivingEntity target, double dist, boolean canSee) {
        double preferred = this.wick.isPhaseTwo() ? 14.0 : 12.0;
        if (dist <= GunWickConfig.CLOSE_RANGE.get()) {
            preferred = 5.5;
        }

        if (dist > preferred + 4.0) {
            this.wick.getNavigation().moveTo(target, this.wick.isPhaseTwo() ? 1.25 : 1.1);
        } else {
            this.wick.getNavigation().stop();
        }

        if (++this.strafeClock >= 16) {
            this.strafeClock = 0;
            this.strafingClockwise = this.wick.getRandom().nextBoolean();
            this.strafingBackwards = dist < preferred - 1.5;
        }

        if (canSee && dist < 24.0) {
            float forward = this.strafingBackwards ? -0.45F : 0.35F;
            float strafe = this.strafingClockwise ? 0.45F : -0.45F;

            if (this.isSafeToStrafe(forward, strafe)) {
                this.wick.getMoveControl().strafe(forward, strafe);
            } else if (this.isSafeToStrafe(forward, -strafe)) {
                this.strafingClockwise = !this.strafingClockwise;
                this.wick.getMoveControl().strafe(forward, -strafe);
            } else if (this.isSafeToStrafe(-forward, strafe)) {
                this.strafingBackwards = !this.strafingBackwards;
                this.wick.getMoveControl().strafe(-forward, strafe);
            } else {
                this.wick.getMoveControl().strafe(0.0F, 0.0F);
            }
            this.wick.lookAt(target, 90.0F, 90.0F);
        }
    }

    private boolean isSafeToStrafe(float forward, float strafe) {
        float rad = this.wick.getYRot() * (float) (Math.PI / 180.0);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);

        double dx = -sin * forward + cos * strafe;
        double dz = cos * forward + sin * strafe;
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len < 1.0E-4) {
            return true;
        }

        double normX = dx / len;
        double normZ = dz / len;

        for (double step : new double[]{0.75, 1.4}) {
            double checkX = this.wick.getX() + normX * step;
            double checkY = this.wick.getY();
            double checkZ = this.wick.getZ() + normZ * step;
            if (!this.isSafePosition(checkX, checkY, checkZ)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSafePosition(double x, double y, double z) {
        Level level = this.wick.level();
        BlockPos feetPos = BlockPos.containing(x, y, z);
        BlockPos bodyPos = feetPos.above();

        if (this.isHazard(level, feetPos) || this.isHazard(level, bodyPos)) {
            return false;
        }

        BlockPos groundPos = feetPos.below();
        if (this.isHazardFloor(level, groundPos)) {
            return false;
        }

        return !this.isCliff(level, feetPos);
    }

    private boolean isHazard(Level level, BlockPos pos) {
        FluidState fluid = level.getFluidState(pos);
        if (fluid.is(FluidTags.LAVA)) {
            return true;
        }

        BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)) {
            return true;
        }
        if (state.is(Blocks.SWEET_BERRY_BUSH)) {
            return true;
        }
        if (state.is(Blocks.CACTUS)) {
            return true;
        }
        if (state.is(Blocks.POWDER_SNOW)) {
            return true;
        }
        if (state.is(Blocks.MAGMA_BLOCK)) {
            return true;
        }
        if (state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE)) {
            return !state.hasProperty(CampfireBlock.LIT) || state.getValue(CampfireBlock.LIT);
        }

        return false;
    }

    private boolean isHazardFloor(Level level, BlockPos pos) {
        FluidState fluid = level.getFluidState(pos);
        if (fluid.is(FluidTags.LAVA)) {
            return true;
        }

        BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.CACTUS) || state.is(Blocks.POWDER_SNOW)) {
            return true;
        }
        if (state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)) {
            return true;
        }
        if (state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE)) {
            return !state.hasProperty(CampfireBlock.LIT) || state.getValue(CampfireBlock.LIT);
        }

        return false;
    }

    private boolean isCliff(Level level, BlockPos feetPos) {
        BlockPos groundPos = feetPos.below();
        BlockState groundState = level.getBlockState(groundPos);
        if (!groundState.isAir() && !groundState.getFluidState().is(FluidTags.LAVA) && groundState.isSolid()) {
            return false;
        }

        BlockPos below2 = feetPos.below(2);
        BlockState below2State = level.getBlockState(below2);
        if (!below2State.isAir() && !below2State.getFluidState().is(FluidTags.LAVA) && below2State.isSolid()) {
            return false;
        }

        return true;
    }

    private void tryShoot(LivingEntity target) {
        IGunOperator operator = IGunOperator.fromLivingEntity(this.wick);
        Vec3 to = target.getEyePosition().subtract(this.wick.getEyePosition());
        float yaw = (float) (Mth.atan2(to.z, to.x) * (180.0F / Math.PI)) - 90.0F;
        float pitch = (float) -(Mth.atan2(to.y, Math.sqrt(to.x * to.x + to.z * to.z)) * (180.0F / Math.PI));
        ShootResult result = operator.shoot(() -> pitch, () -> yaw);
        switch (result) {
            case NEED_BOLT -> operator.bolt();
            case NO_AMMO, ID_NOT_EXIST -> {
                TaczGuns.refill(this.wick.getMainHandItem());
                operator.reload();
            }
            case NOT_DRAW -> operator.draw(this.wick::getMainHandItem);
            case OVERHEATED -> TaczGuns.refill(this.wick.getMainHandItem());
            default -> {
            }
        }
    }
}
