package com.huan.fart_shit_pee.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
    public static final DamageSource smellyShit_DamageSource1 = new DamageSource("smellyShit").setDamageBypassesArmor();
    private static final smellyShitDamageSource smellyShit_DamageSource2 = (smellyShitDamageSource) new smellyShitDamageSource("smellyShit").setDamageBypassesArmor();

    private customDamageSource() {
    }

    public static class smellyShitDamageSource extends DamageSource {
        public PlayerEntity player;

        public smellyShitDamageSource(String damageTypeIn) {
            super(damageTypeIn);
        }

        public smellyShitDamageSource setPlayer(PlayerEntity player) {
            this.player = player;
            return this;
        }


        @Override
        public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
            String s = "death.attack.smellyShit.player";
            return player != null ? new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), player.getDisplayName()) : new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), null);
        }
    }
}
