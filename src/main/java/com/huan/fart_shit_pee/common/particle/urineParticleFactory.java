package com.huan.fart_shit_pee.common.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

import javax.annotation.Nullable;

public class urineParticleFactory implements IParticleFactory<BasicParticleType> {
    private final IAnimatedSprite sprites;

    public urineParticleFactory(IAnimatedSprite sprites) {
        this.sprites = sprites;
    }

    @Nullable
    @Override
    public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        urineParticle particle = new urineParticle(worldIn,x,y,z,xSpeed,ySpeed,zSpeed);
        particle.selectSpriteRandomly(sprites);
        return particle;
    }
}
