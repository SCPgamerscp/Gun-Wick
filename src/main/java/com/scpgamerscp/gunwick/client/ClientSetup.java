package com.scpgamerscp.gunwick.client;

import com.scpgamerscp.gunwick.GunWickMod;
import com.scpgamerscp.gunwick.init.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunWickMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.GUN_WICK.get(), GunWickRenderer::new);
        event.registerEntityRenderer(ModEntities.WICKS_WOLF.get(), WicksWolfRenderer::new);
    }
}
