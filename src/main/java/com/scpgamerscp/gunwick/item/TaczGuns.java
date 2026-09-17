package com.scpgamerscp.gunwick.item;

import com.scpgamerscp.gunwick.config.GunWickConfig;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import com.tacz.guns.api.item.gun.FireMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public final class TaczGuns {
    public enum Loadout {
        GLOCK,
        SHOTGUN,
        MINIGUN
    }

    private TaczGuns() {
    }

    public static ItemStack create(Loadout loadout) {
        return switch (loadout) {
            case GLOCK -> build(GunWickConfig.GLOCK_ID.get(), null, FireMode.SEMI, 30);
            case SHOTGUN -> build(GunWickConfig.SHOTGUN_ID.get(), GunWickConfig.SHOTGUN_FALLBACK_ID.get(), FireMode.SEMI, 8);
            case MINIGUN -> build(GunWickConfig.MINIGUN_ID.get(), GunWickConfig.MINIGUN_FALLBACK_ID.get(), FireMode.AUTO, 200);
        };
    }

    public static boolean isLoadout(ItemStack stack, Loadout loadout) {
        if (stack.isEmpty() || !(stack.getItem() instanceof IGun iGun)) {
            return false;
        }
        ResourceLocation held = iGun.getGunId(stack);
        ResourceLocation expected = primaryId(loadout);
        ResourceLocation fallback = fallbackId(loadout);
        return expected.equals(held) || (fallback != null && fallback.equals(held));
    }

    public static void refill(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof IGun iGun)) {
            return;
        }
        iGun.setCurrentAmmoCount(stack, 999);
        iGun.setBulletInBarrel(stack, true);
        iGun.setDummyAmmoAmount(stack, 9999);
        iGun.setHeatAmount(stack, 0.0F);
    }

    private static ResourceLocation primaryId(Loadout loadout) {
        return switch (loadout) {
            case GLOCK -> parse(GunWickConfig.GLOCK_ID.get());
            case SHOTGUN -> parse(GunWickConfig.SHOTGUN_ID.get());
            case MINIGUN -> parse(GunWickConfig.MINIGUN_ID.get());
        };
    }

    private static ResourceLocation fallbackId(Loadout loadout) {
        return switch (loadout) {
            case GLOCK -> null;
            case SHOTGUN -> parse(GunWickConfig.SHOTGUN_FALLBACK_ID.get());
            case MINIGUN -> parse(GunWickConfig.MINIGUN_FALLBACK_ID.get());
        };
    }

    private static ItemStack build(String id, String fallback, FireMode mode, int ammo) {
        ItemStack stack = buildOne(parse(id), mode, ammo);
        if (stack.isEmpty() && fallback != null) {
            stack = buildOne(parse(fallback), mode, ammo);
        }
        refill(stack);
        return stack;
    }

    private static ItemStack buildOne(ResourceLocation id, FireMode mode, int ammo) {
        ItemStack built = GunItemBuilder.create()
                .setId(id)
                .setFireMode(mode)
                .setAmmoCount(ammo)
                .setAmmoInBarrel(true)
                .build();
        if (built.isEmpty()) {
            built = GunItemBuilder.create()
                    .setId(id)
                    .setFireMode(mode)
                    .setAmmoCount(ammo)
                    .setAmmoInBarrel(true)
                    .forceBuild();
        }
        return built;
    }

    private static ResourceLocation parse(String raw) {
        ResourceLocation loc = ResourceLocation.tryParse(raw);
        return loc != null ? loc : new ResourceLocation("tacz", "glock_17");
    }
}
