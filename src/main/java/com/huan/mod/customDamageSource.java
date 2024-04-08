package com.huan.mod;

import net.minecraft.util.DamageSource;

public class customDamageSource {
    //膀胱爆炸
    public static final DamageSource bladderDamageSource1 = new DamageSource("bladder1").setDamageBypassesArmor();
    public static final DamageSource bladderDamageSource2 = new DamageSource("bladder2").setDamageBypassesArmor();

    //肛门爆炸
    public static final DamageSource intestineDamageSource1 = new DamageSource("intestine1").setDamageBypassesArmor();
    public static final DamageSource intestineDamageSource2 = new DamageSource("intestine2").setDamageBypassesArmor();

    //吃屎而亡
    public static final DamageSource eatShit_DamageSource = new DamageSource("eatShit").setDamageBypassesArmor();

    //被屎臭死
    public static final DamageSource smellyShit_DamageSource = new DamageSource("smellyShit").setDamageBypassesArmor();

    private customDamageSource(){}
}
