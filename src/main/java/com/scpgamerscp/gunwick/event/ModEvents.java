package com.scpgamerscp.gunwick.event;

import com.scpgamerscp.gunwick.GunWickMod;
import com.scpgamerscp.gunwick.config.GunWickConfig;
import com.scpgamerscp.gunwick.entity.GunWickEntity;
import com.scpgamerscp.gunwick.entity.WicksWolfEntity;
import com.scpgamerscp.gunwick.init.ModEntities;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunWickMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEvents {
    private ModEvents() {
    }

    @SubscribeEvent
    public static void attributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GUN_WICK.get(), GunWickEntity.createAttributes().build());
        event.put(ModEntities.WICKS_WOLF.get(), WicksWolfEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void spawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                ModEntities.WICKS_WOLF.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WicksWolfEntity::checkSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    @Mod.EventBusSubscriber(modid = GunWickMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static final class ForgeEvents {
        private ForgeEvents() {
        }

        @SubscribeEvent
        public static void capDamage(LivingDamageEvent event) {
            if (event.getEntity() instanceof GunWickEntity) {
                float cap = GunWickConfig.DAMAGE_CAP.get().floatValue();
                if (event.getAmount() > cap) {
                    event.setAmount(cap);
                }
            }
        }
    }
}
