package com.scpgamerscp.gunwick.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class GunWickConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue BOSS_HEALTH;
    public static final ForgeConfigSpec.DoubleValue BOSS_ARMOR;
    public static final ForgeConfigSpec.DoubleValue DAMAGE_CAP;
    public static final ForgeConfigSpec.DoubleValue PHASE_TWO_PERCENT;
    public static final ForgeConfigSpec.DoubleValue CLOSE_RANGE;
    public static final ForgeConfigSpec.DoubleValue MOVE_SPEED;
    public static final ForgeConfigSpec.DoubleValue PHASE_TWO_MOVE_SPEED;

    public static final ForgeConfigSpec.IntValue WOLF_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue WOLF_MIN_COUNT;
    public static final ForgeConfigSpec.IntValue WOLF_MAX_COUNT;
    public static final ForgeConfigSpec.BooleanValue WOLF_SPAWN_ENABLED;

    public static final ForgeConfigSpec.ConfigValue<String> GLOCK_ID;
    public static final ForgeConfigSpec.ConfigValue<String> SHOTGUN_ID;
    public static final ForgeConfigSpec.ConfigValue<String> MINIGUN_ID;
    public static final ForgeConfigSpec.ConfigValue<String> SHOTGUN_FALLBACK_ID;
    public static final ForgeConfigSpec.ConfigValue<String> MINIGUN_FALLBACK_ID;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("boss");
        BOSS_HEALTH = builder.comment("Gun Wick max health").defineInRange("health", 500.0, 20.0, 100000.0);
        BOSS_ARMOR = builder.comment("Gun Wick armor points").defineInRange("armor", 30.0, 0.0, 100.0);
        DAMAGE_CAP = builder.comment("Maximum damage Gun Wick can take from a single hit").defineInRange("damageCap", 3.0, 0.1, 1000.0);
        PHASE_TWO_PERCENT = builder.comment("Health ratio that starts phase 2 (minigun)").defineInRange("phaseTwoPercent", 0.5, 0.05, 0.95);
        CLOSE_RANGE = builder.comment("Distance in blocks treated as close range (shotgun)").defineInRange("closeRange", 8.0, 2.0, 32.0);
        MOVE_SPEED = builder.comment("Phase 1 movement speed").defineInRange("moveSpeed", 0.32, 0.1, 1.0);
        PHASE_TWO_MOVE_SPEED = builder.comment("Phase 2 movement speed").defineInRange("phaseTwoMoveSpeed", 0.38, 0.1, 1.0);
        builder.pop();

        builder.push("wolf");
        WOLF_SPAWN_ENABLED = builder.comment("If false, Wick's Wolf will not naturally spawn").define("spawnEnabled", true);
        WOLF_SPAWN_WEIGHT = builder.comment("Natural spawn weight. 0 disables natural spawning.").defineInRange("spawnWeight", 8, 0, 200);
        WOLF_MIN_COUNT = builder.comment("Minimum wolves per spawn attempt").defineInRange("minCount", 1, 1, 8);
        WOLF_MAX_COUNT = builder.comment("Maximum wolves per spawn attempt").defineInRange("maxCount", 1, 1, 8);
        builder.pop();

        builder.push("guns");
        GLOCK_ID = builder.comment("Phase 1 mid/long gun id").define("glockId", "tacz:glock_17");
        SHOTGUN_ID = builder.comment("Close range shotgun id").define("shotgunId", "tacz:spas_12");
        MINIGUN_ID = builder.comment("Phase 2 mid/long gun id").define("minigunId", "tacz:minigun");
        SHOTGUN_FALLBACK_ID = builder.comment("Fallback if shotgun id is missing").define("shotgunFallbackId", "tacz:m870");
        MINIGUN_FALLBACK_ID = builder.comment("Fallback if minigun id is missing").define("minigunFallbackId", "tacz:m249");
        builder.pop();

        SPEC = builder.build();
    }

    private GunWickConfig() {
    }
}
