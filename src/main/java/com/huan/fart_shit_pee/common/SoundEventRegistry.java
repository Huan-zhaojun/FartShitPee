package com.huan.fart_shit_pee.common;

import com.huan.fart_shit_pee.fart_shit_pee;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundEventRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, fart_shit_pee.MOD_ID);
    public static final RegistryObject<SoundEvent> fartSound = SOUNDS.register("fart", () -> new SoundEvent(new ResourceLocation(fart_shit_pee.MOD_ID, "fart")));
}
