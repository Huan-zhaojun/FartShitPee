package com.huan.fart_shit_pee.api;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.DoubleValue a_default, b_default;//尿液抛物线参数

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("General settings");
        COMMON_BUILDER.push("pee").comment("parabola:(-a*x^2)+b");
        a_default = COMMON_BUILDER.comment("The smaller it is, the farther the distance is")
                .defineInRange("a", 0.05,0.0000001,1);
        b_default = COMMON_BUILDER.comment("The larger it is, the higher the height is")
                .defineInRange("b", 2,0.1,255);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("fart");
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
