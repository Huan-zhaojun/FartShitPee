package com.huan.fart_shit_pee.network.Client;

import com.huan.fart_shit_pee.network.IModPack;
import com.huan.fart_shit_pee.render.urineRender;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class peeEnd_SendPack implements IModPack {
    public peeEnd_SendPack() {
    }

    public peeEnd_SendPack(PacketBuffer buffer) {
    }

    @Override
    public void toBytes(PacketBuffer buf) {
    }

    @Override
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().world != null && Minecraft.getInstance().world.isRemote) {
                urineRender.pee = false;//终止绘制
            }
        });
        context.setPacketHandled(true);
    }
}
