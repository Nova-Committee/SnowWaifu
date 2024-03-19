package committee.nova.snowwaifu.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SWConfig {
    public static final ForgeConfigSpec CFG;
    public static final ForgeConfigSpec.DoubleValue snowWaifuMaxHealth;
    public static final ForgeConfigSpec.DoubleValue snowWaifuBreathAttackDamage;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("SnowWaifu Settings").push("General");
        snowWaifuMaxHealth = builder.defineInRange("snowWaifuMaxHealth", 200.0, 1.0, Float.MAX_VALUE);
        snowWaifuBreathAttackDamage = builder.defineInRange("snowWaifuBreathAttackDamage", 4.0, 1.0, Float.MAX_VALUE);
        builder.pop();
        CFG = builder.build();
    }
}
