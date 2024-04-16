package com.huan.fart_shit_pee.network.Server;

import com.huan.fart_shit_pee.capability.drainCapability;
import com.huan.fart_shit_pee.fart_shit_pee;
import com.huan.fart_shit_pee.network.IModPack;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class upDown_urineSendPack implements IModPack {
    private boolean up;

    public upDown_urineSendPack(boolean up) {
        this.up = up;
    }

    public upDown_urineSendPack(PacketBuffer buffer) {
        this.up = buffer.readBoolean();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBoolean(up);
    }

    @Override
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null && !player.world.isRemote) {
                LazyOptional<drainCapability> cap = player.getCapability(fart_shit_pee.Drain_Capability);
                cap.ifPresent(c -> {
                    if (up) {//up
                        if ((c.a <= 0.069 || Math.abs(c.a - 0.069) < 0.00001) && c.a > 0.02) {
                            c.a -= 0.001;
                        }
                        if ((c.b >= 2.0) && c.b < 92.0) {
                            c.b += 3.0;
                        } else if ((c.b >= 0.11 || Math.abs(c.b - 0.11) < 0.00001) && c.b < 2.0) {
                            c.b += 0.01;
                        }
                    } else {//down
                        if (c.a < 0.069 && (c.a >= 0.02 || Math.abs(c.a - 0.02) < 0.00001)) {
                            c.a += 0.001;
                        }
                        if ((c.b > 2.0) && c.b <= 92.0) {
                            c.b -= 3.0;
                        } else if (c.b > 0.11 && (c.b <= 2.0 || Math.abs(c.b - 2.0) < 0.00001)) {
                            c.b -= 0.01;
                        }
                    }
                });
            }
        });
        context.setPacketHandled(true);
    }
}
