package com.scpgamerscp.gunwick.entity;

import com.scpgamerscp.gunwick.config.GunWickConfig;
import com.scpgamerscp.gunwick.entity.ai.GunWickRangedGoal;
import com.scpgamerscp.gunwick.init.ModEntities;
import com.scpgamerscp.gunwick.item.TaczGuns;
import com.tacz.guns.api.entity.IGunOperator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

public class GunWickEntity extends Monster {
    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.translatable("entity.gunwick.gun_wick"),
            BossEvent.BossBarColor.RED,
            BossEvent.BossBarOverlay.PROGRESS);

    public GunWickEntity(EntityType<? extends GunWickEntity> type, Level level) {
        super(type, level);
        this.xpReward = 200;
        this.setPersistenceRequired();
        this.setCanPickUpLoot(false);

        this.setPathfindingMalus(BlockPathTypes.LAVA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_OTHER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_CAUTIOUS, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 500.0)
                .add(Attributes.ARMOR, 30.0)
                .add(Attributes.ARMOR_TOUGHNESS, 12.0)
                .add(Attributes.MOVEMENT_SPEED, 0.32)
                .add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GunWickRangedGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.85));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        this.applyConfigAttributes();
        this.setGun(TaczGuns.create(TaczGuns.Loadout.GLOCK));
        IGunOperator.fromLivingEntity(this).initialData();
        IGunOperator.fromLivingEntity(this).draw(this::getMainHandItem);
        return result;
    }

    public void applyConfigAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(GunWickConfig.BOSS_HEALTH.get());
        this.getAttribute(Attributes.ARMOR).setBaseValue(GunWickConfig.BOSS_ARMOR.get());
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(GunWickConfig.MOVE_SPEED.get());
        this.setHealth(this.getMaxHealth());
    }

    public void setGun(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    public boolean isPhaseTwo() {
        return this.getHealth() <= this.getMaxHealth() * GunWickConfig.PHASE_TWO_PERCENT.get().floatValue();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        double speed = this.isPhaseTwo() ? GunWickConfig.PHASE_TWO_MOVE_SPEED.get() : GunWickConfig.MOVE_SPEED.get();
        if (this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() != speed) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
        }
        TaczGuns.refill(this.getMainHandItem());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        this.bossEvent.removeAllPlayers();
    }

    @Override
    public boolean shouldDropExperience() {
        return true;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PLAYER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    public static void summonFromWolf(WicksWolfEntity wolf, LivingEntity killer) {
        if (!(wolf.level() instanceof ServerLevel server)) {
            return;
        }
        GunWickEntity wick = ModEntities.GUN_WICK.get().create(server);
        if (wick == null) {
            return;
        }
        wick.moveTo(wolf.getX(), wolf.getY(), wolf.getZ(), wolf.getYRot(), 0.0F);
        wick.setTarget(killer instanceof Player player ? player : null);
        wick.finalizeSpawn(server, server.getCurrentDifficultyAt(wick.blockPosition()), MobSpawnType.EVENT, null, null);
        server.addFreshEntity(wick);
        Component message = Component.translatable("boss.gunwick.appeared");
        server.getServer().getPlayerList().broadcastSystemMessage(message, false);
    }
}
