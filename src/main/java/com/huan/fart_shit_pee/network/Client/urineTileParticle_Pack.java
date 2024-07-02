package com.huan.fart_shit_pee.network.Client;

import com.huan.fart_shit_pee.network.IModPack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class urineTileParticle_Pack implements IModPack {
    BlockPos pos;

    public urineTileParticle_Pack(BlockPos pos) {
        this.pos = pos;
    }

    public urineTileParticle_Pack(int x, int y, int z) {
        this.pos = new BlockPos(x,y,z);
    }

    public urineTileParticle_Pack(PacketBuffer buffer) {
        this.pos = new BlockPos(buffer.readInt(),buffer.readInt(),buffer.readInt());
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().world != null && Minecraft.getInstance().world.isRemote) {
                Minecraft.getInstance().world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0.05, 0);
                Minecraft.getInstance().world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.3, pos.getY() + 0.3, pos.getZ() + 0.5, 0, 0.05, 0);
                Minecraft.getInstance().world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.3, pos.getY() + 0.7, pos.getZ() + 0.5, 0, 0.05, 0);
                Minecraft.getInstance().world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.7, pos.getY() + 0.3, pos.getZ() + 0.5, 0, 0.05, 0);
                Minecraft.getInstance().world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.7, pos.getY() + 0.7, pos.getZ() + 0.5, 0, 0.05, 0);

            }
        });
        context.setPacketHandled(true);
    }
}
