package com.huan.fart_shit_pee.common.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;

public class urineParticle extends SpriteTexturedParticle {
    public urineParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        maxAge = 100;
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        setColor(173 / 255F, 132 / 255F, 43 / 255F);
        setAlphaF(255 / 255F);
        this.particleScale = 0.125F;
        this.canCollide = false;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
