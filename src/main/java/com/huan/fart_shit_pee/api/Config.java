package com.huan.fart_shit_pee.api;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.DoubleValue a_default, b_default;//尿液抛物线参数
    public static ForgeConfigSpec.IntValue pee_tick, pee_value;//撒尿消耗时间刻和消耗值
    public static ForgeConfigSpec.IntValue urineLevel_Max_default, shitLevel_Max_default;
    public static ForgeConfigSpec.LongValue lastTime;//超出屎值或尿值的上限判定时间间隔
    public static ForgeConfigSpec.DoubleValue drain_damage;
    public static ForgeConfigSpec.IntValue bladder_xOffset, bladder_yOffset, intestine_xOffset, intestine_yOffset;//hub位置
    public static ForgeConfigSpec.BooleanValue shit_isDamage;
    public static ForgeConfigSpec.DoubleValue shit_damage;
    public static ForgeConfigSpec.IntValue shit_tick;
    // TODO：配置食物增加的屁屎尿值

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("General settings");
        COMMON_BUILDER.push("Pee").comment("parabola:(-a*x^2)+b");
        a_default = COMMON_BUILDER.comment("The smaller it is, the farther the distance is")
                .defineInRange("a", 0.05, 0.0000001, 1);
        b_default = COMMON_BUILDER.comment("The larger it is, the higher the height is")
                .defineInRange("b", 2, 0.1, 255);
        pee_tick = COMMON_BUILDER.comment("How often to determine urine value deduction")
                .defineInRange("tick", 10, 1, Integer.MAX_VALUE);
        pee_value = COMMON_BUILDER.comment("How much urine value is deducted each time")
                .defineInRange("value", 1, 1, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Drain");
        urineLevel_Max_default = COMMON_BUILDER.defineInRange("urineLevel_Max", 50, 1, Integer.MAX_VALUE);
        shitLevel_Max_default = COMMON_BUILDER.defineInRange("shitLevel_Max", 50, 1, Integer.MAX_VALUE);
        lastTime = COMMON_BUILDER.comment("The time interval between causing harm when exceeded maximum value")
                .defineInRange("lastTime", 1000, 1, Long.MAX_VALUE);
        drain_damage = COMMON_BUILDER.comment("The damage value each time")
                .defineInRange("damage", 2, 1, Float.MAX_VALUE);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Hub").comment("The location of the hub");
        bladder_xOffset = COMMON_BUILDER.defineInRange("bladder_xOffset", -125, -1024, 1024);
        bladder_yOffset = COMMON_BUILDER.defineInRange("bladder_yOffset", -35, -1024, 1024);
        intestine_xOffset = COMMON_BUILDER.defineInRange("intestine_xOffset", 95, -1024, 1024);
        intestine_yOffset = COMMON_BUILDER.defineInRange("intestine_yOffset", -35, -1024, 1024);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Shit");
        shit_isDamage = COMMON_BUILDER.comment("Will it stink to death for players")
                .define("isDamage", true);
        shit_damage = COMMON_BUILDER.defineInRange("damage", 2, 1, Float.MAX_VALUE);
        shit_tick = COMMON_BUILDER.defineInRange("tick", 20, 1, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
