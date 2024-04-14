package com.huan.fart_shit_pee.network.Server;

import com.huan.fart_shit_pee.capability.drainCapability;
import com.huan.fart_shit_pee.fart_shit_pee;
import com.huan.fart_shit_pee.network.Client.peeEnd_SendPack;
import com.huan.fart_shit_pee.network.IModPack;
import com.huan.fart_shit_pee.network.Network;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class drainSendPack implements IModPack {
    public drainSendPack() {

    }

    public drainSendPack(PacketBuffer buffer) {

    }

    @Override
    public void toBytes(PacketBuffer buf) {

    }

    @Override
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player == null && player.world.isRemote) return;
            if (!player.isCrouching() && !player.isCreative()) {//撒尿
                LazyOptional<drainCapability> cap = player.getCapability(fart_shit_pee.Drain_Capability);
                cap.ifPresent(c -> {
                    if (c.urineLevel > 0) {
                        c.setPee(!c.pee);//控制撒尿的开始或终止
                        if (!c.pee) //终止绘制
                            Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new peeEnd_SendPack());
                    }
                });
            } else if (!player.isCreative()) {//拉屎

            }
        });
        context.setPacketHandled(true);
    }
}
