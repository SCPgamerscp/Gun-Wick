package com.scpgamerscp.gunwick.spawn;

import com.mojang.serialization.Codec;
import com.scpgamerscp.gunwick.config.GunWickConfig;
import com.scpgamerscp.gunwick.init.ModEntities;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record WicksWolfBiomeModifier(HolderSet<Biome> biomes) implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) {
            return;
        }
        if (!this.biomes.contains(biome)) {
            return;
        }
        if (!GunWickConfig.WOLF_SPAWN_ENABLED.get() || GunWickConfig.WOLF_SPAWN_WEIGHT.get() <= 0) {
            return;
        }
        builder.getMobSpawnSettings().addSpawn(
                MobCategory.CREATURE,
                new MobSpawnSettings.SpawnerData(
                        ModEntities.WICKS_WOLF.get(),
                        GunWickConfig.WOLF_SPAWN_WEIGHT.get(),
                        GunWickConfig.WOLF_MIN_COUNT.get(),
                        GunWickConfig.WOLF_MAX_COUNT.get()));
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ModBiomeModifiers.WICKS_WOLF_SPAWNS.get();
    }
}
