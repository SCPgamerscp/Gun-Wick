package com.scpgamerscp.gunwick.entity;

import com.scpgamerscp.gunwick.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class WicksWolfEntity extends Wolf {
    public WicksWolfEntity(EntityType<? extends WicksWolfEntity> type, Level level) {
        super(type, level);
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return Wolf.createAttributes();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        this.setCustomName(Component.translatable("entity.gunwick.wicks_wolf"));
        this.setCustomNameVisible(true);
        this.setBaby(false);
        return result;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (this.level().isClientSide) {
            return;
        }
        if (source.getEntity() instanceof Player player) {
            GunWickEntity.summonFromWolf(this, player);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return !this.isTame();
    }

    @Nullable
    @Override
    public Wolf getBreedOffspring(ServerLevel level, AgeableMob partner) {
        WicksWolfEntity pup = ModEntities.WICKS_WOLF.get().create(level);
        if (pup != null) {
            pup.setCustomName(Component.translatable("entity.gunwick.wicks_wolf"));
            pup.setCustomNameVisible(true);
            if (this.isTame()) {
                pup.setTame(true);
                pup.setOwnerUUID(this.getOwnerUUID());
            }
        }
        return pup;
    }

    public static boolean checkSpawnRules(EntityType<WicksWolfEntity> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (spawnType == MobSpawnType.SPAWNER || spawnType == MobSpawnType.SPAWN_EGG || spawnType == MobSpawnType.COMMAND) {
            return true;
        }
        return level.getBlockState(pos.below()).isSolid()
                && !level.getBlockState(pos).is(Blocks.WATER)
                && level.getWorldBorder().isWithinBounds(pos);
    }
}
