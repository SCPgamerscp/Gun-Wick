package com.scpgamerscp.gunwick;

import com.scpgamerscp.gunwick.config.GunWickConfig;
import com.scpgamerscp.gunwick.init.ModEntities;
import com.scpgamerscp.gunwick.init.ModItems;
import com.scpgamerscp.gunwick.spawn.ModBiomeModifiers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GunWickMod.MODID)
public class GunWickMod {
    public static final String MODID = "gunwick";

    public GunWickMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GunWickConfig.SPEC);

        ModEntities.ENTITIES.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModItems.CREATIVE_TABS.register(modBus);
        ModBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
