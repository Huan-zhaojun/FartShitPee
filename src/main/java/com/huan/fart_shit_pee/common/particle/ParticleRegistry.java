package com.huan.fart_shit_pee.common.particle;

import com.huan.fart_shit_pee.fart_shit_pee;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, fart_shit_pee.MOD_ID);
    public static final RegistryObject<BasicParticleType> urineParticle = PARTICLE_TYPES.register("urine_particle", () -> new BasicParticleType(true));
}
