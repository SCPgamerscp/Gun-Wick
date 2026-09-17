package com.scpgamerscp.gunwick.init;

import com.scpgamerscp.gunwick.GunWickMod;
import com.scpgamerscp.gunwick.entity.GunWickEntity;
import com.scpgamerscp.gunwick.entity.WicksWolfEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GunWickMod.MODID);

    public static final RegistryObject<EntityType<GunWickEntity>> GUN_WICK = ENTITIES.register("gun_wick",
            () -> EntityType.Builder.of(GunWickEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build(new ResourceLocation(GunWickMod.MODID, "gun_wick").toString()));

    public static final RegistryObject<EntityType<WicksWolfEntity>> WICKS_WOLF = ENTITIES.register("wicks_wolf",
            () -> EntityType.Builder.of(WicksWolfEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 0.85F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(GunWickMod.MODID, "wicks_wolf").toString()));

    private ModEntities() {
    }
}
