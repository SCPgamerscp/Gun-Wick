package com.scpgamerscp.gunwick.init;

import com.scpgamerscp.gunwick.GunWickMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, GunWickMod.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GunWickMod.MODID);

    public static final RegistryObject<Item> GUN_WICK_SPAWN_EGG = ITEMS.register("gun_wick_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.GUN_WICK, 0x1A1A1A, 0xC8C8C8, new Item.Properties()));

    public static final RegistryObject<Item> WICKS_WOLF_SPAWN_EGG = ITEMS.register("wicks_wolf_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.WICKS_WOLF, 0xC4B89A, 0x3A2A1A, new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("gunwick",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gunwick"))
                    .icon(() -> new ItemStack(GUN_WICK_SPAWN_EGG.get()))
                    .displayItems((params, output) -> {
                        output.accept(GUN_WICK_SPAWN_EGG.get());
                        output.accept(WICKS_WOLF_SPAWN_EGG.get());
                    })
                    .build());

    private ModItems() {
    }
}
