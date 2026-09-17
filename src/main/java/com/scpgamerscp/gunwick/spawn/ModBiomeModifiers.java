package com.scpgamerscp.gunwick.spawn;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scpgamerscp.gunwick.GunWickMod;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBiomeModifiers {
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, GunWickMod.MODID);

    public static final RegistryObject<Codec<WicksWolfBiomeModifier>> WICKS_WOLF_SPAWNS =
            BIOME_MODIFIER_SERIALIZERS.register("wicks_wolf_spawns", () ->
                    RecordCodecBuilder.create(instance -> instance.group(
                            Biome.LIST_CODEC.fieldOf("biomes").forGetter(WicksWolfBiomeModifier::biomes)
                    ).apply(instance, WicksWolfBiomeModifier::new)));

    private ModBiomeModifiers() {
    }
}
