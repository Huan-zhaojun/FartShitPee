package com.huan.mod.network;

import com.huan.mod.capability.drainCapability;
import com.huan.mod.fart_shit_pee;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SendPack {
    private final int urineLevel_Max, shitLevel_Max;
    private final int urineLevel, shitLevel;
    private final int flatusLevel;
    private final UUID uuid;

    public SendPack(PacketBuffer buffer) {
        this.urineLevel_Max = buffer.readInt();
        this.shitLevel_Max = buffer.readInt();
        this.urineLevel = buffer.readInt();
        this.shitLevel = buffer.readInt();
        this.flatusLevel = buffer.readInt();
        this.uuid = buffer.readUniqueId();
    }

    public SendPack(int urineLevel_Max, int shitLevel_Max, int urineLevel, int shitLevel, int flatusLevel, UUID uuid) {
        this.urineLevel_Max = urineLevel_Max;
        this.shitLevel_Max = shitLevel_Max;
        this.urineLevel = urineLevel;
        this.shitLevel = shitLevel;
        this.flatusLevel = flatusLevel;
        this.uuid = uuid;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(urineLevel_Max);
        buf.writeInt(shitLevel_Max);
        buf.writeInt(urineLevel);
        buf.writeInt(shitLevel);
        buf.writeInt(urineLevel_Max);
        buf.writeUniqueId(uuid);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().world != null && Minecraft.getInstance().world.isRemote){
                ClientPlayerEntity player = (ClientPlayerEntity) Minecraft.getInstance().world.getPlayerByUuid(uuid);
                if (player != null) {
                    LazyOptional<drainCapability> cap = player.getCapability(fart_shit_pee.Drain_Capability);
                    cap.ifPresent(c-> {
                        c.setUrineLevel_Max(urineLevel_Max);
                        c.setShitLevel_Max(shitLevel_Max);
                        c.setUrineLevel(urineLevel);
                        c.setShitLevel(shitLevel);
                        c.setFlatusLevel(flatusLevel);
                    });
                }
            }
        });
        context.setPacketHandled(true);
    }
}
