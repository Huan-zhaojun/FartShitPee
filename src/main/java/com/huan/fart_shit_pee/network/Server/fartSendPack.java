package com.huan.fart_shit_pee.network.Server;

import com.huan.fart_shit_pee.capability.drainCapability;
import com.huan.fart_shit_pee.common.SoundEventRegistry;
import com.huan.fart_shit_pee.fart_shit_pee;
import com.huan.fart_shit_pee.network.Client.entityMotionSendPack;
import com.huan.fart_shit_pee.network.IModPack;
import com.huan.fart_shit_pee.network.Network;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class fartSendPack implements IModPack {
    public fartSendPack() {
    }

    public fartSendPack(PacketBuffer buffer) {
    }

    @Override
    public void toBytes(PacketBuffer buf) {
    }

    @Override
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null && !player.world.isRemote && !player.isSpectator()) {
                World world = player.world;
                Vector3d vector3d = player.getMotion();
                int[] flatus = {1};
                double radian = Math.toRadians(player.rotationYaw);
                if (!player.isCreative()) {//创造模式无限喷屁
                    boolean[] flag = {false};
                    LazyOptional<drainCapability> cap = player.getCapability(fart_shit_pee.Drain_Capability);
                    cap.ifPresent(c -> {
                        int flatusLevel = c.flatusLevel;
                        if (flatusLevel > 0) {
                            flatus[0] = c.flatusLevel;
                            c.setFlatusLevel(0);
                        } else flag[0] = true;
                    });
                    if (flag[0]) return;
                }
                //爆炸
                world.playSound(null, player.getPosition(), SoundEventRegistry.fartSound.get(), SoundCategory.PLAYERS, 5.0f, 1.0f);
                if (!player.isCrouching()) {
                    double levelSpeed = 3.0 * flatus[0];
                    Vector3d newMotion = new Vector3d(vector3d.x + (-Math.sin(radian) * levelSpeed), vector3d.y, vector3d.z + (Math.cos(radian) * levelSpeed));
                    player.setMotion(newMotion);
                    Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new entityMotionSendPack(player.getUniqueID(), newMotion));
                } else {
                    world.createExplosion(player, player.getPosX(), player.getPosY(), player.getPosZ(), flatus[0] + 1, true, Explosion.Mode.DESTROY);
                    world.addParticle(ParticleTypes.EXPLOSION_EMITTER, player.getPosX(), player.getPosY(), player.getPosZ(), 0, 0, 0);
                    for (int i = 2; i <= flatus[0]; i++) {
                        double r = 0;
                        r += 3.0;
                        double x = player.getPosX();
                        double y = player.getPosY();
                        double z = player.getPosZ();
                        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x + r, y, z + r, 0, 0, 0);
                        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x - r, y, z - r, 0, 0, 0);
                        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x + r, y, z - r, 0, 0, 0);
                        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x - r, y, z + r, 0, 0, 0);
                    }
                    double levelSpeed = (19 / 16.0) * Math.pow(flatus[0], 2) + (61 / 16.0), highSpeed = 0.1 * Math.pow(flatus[0], 2) + 0.9;
                    Vector3d newMotion = new Vector3d(vector3d.x + (-Math.sin(radian) * levelSpeed), vector3d.y + highSpeed, vector3d.z + (Math.cos(radian) * levelSpeed));
                    player.setMotion(newMotion);
                    Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new entityMotionSendPack(player.getUniqueID(), newMotion));
                }
            }
        });
        context.setPacketHandled(true);
    }
}
