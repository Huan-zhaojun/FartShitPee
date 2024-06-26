package com.huan.fart_shit_pee.network.Client;

import com.huan.fart_shit_pee.capability.drainCapability;
import com.huan.fart_shit_pee.fart_shit_pee;
import com.huan.fart_shit_pee.gui.HudClientEvent;
import com.huan.fart_shit_pee.network.IModPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class hubSendPack implements IModPack {
    private final int urineLevel_Max, shitLevel_Max;
    private final int urineLevel, shitLevel;
    private final int flatusLevel;
    private final UUID uuid;
    private final boolean pee;

    public hubSendPack(PacketBuffer buffer) {
        this.urineLevel_Max = buffer.readInt();
        this.shitLevel_Max = buffer.readInt();
        this.urineLevel = buffer.readInt();
        this.shitLevel = buffer.readInt();
        this.flatusLevel = buffer.readInt();
        this.uuid = buffer.readUniqueId();
        this.pee = buffer.readBoolean();
    }

    public hubSendPack(int urineLevel_Max, int shitLevel_Max, int urineLevel, int shitLevel, int flatusLevel, UUID uuid, boolean pee) {
        this.urineLevel_Max = urineLevel_Max;
        this.shitLevel_Max = shitLevel_Max;
        this.urineLevel = urineLevel;
        this.shitLevel = shitLevel;
        this.flatusLevel = flatusLevel;
        this.uuid = uuid;
        this.pee = pee;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(urineLevel_Max);
        buf.writeInt(shitLevel_Max);
        buf.writeInt(urineLevel);
        buf.writeInt(shitLevel);
        buf.writeInt(flatusLevel);
        buf.writeUniqueId(uuid);
        buf.writeBoolean(pee);
    }

    @Override
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().world != null && Minecraft.getInstance().world.isRemote) {
                ClientPlayerEntity player = (ClientPlayerEntity) Minecraft.getInstance().world.getPlayerByUuid(uuid);
                if (player != null) {
                    LazyOptional<drainCapability> cap = player.getCapability(fart_shit_pee.Drain_Capability);
                    cap.ifPresent(c -> {
                        c.setUrineLevel_Max(urineLevel_Max);
                        c.setShitLevel_Max(shitLevel_Max);
                        c.setUrineLevel(urineLevel);
                        c.setShitLevel(shitLevel);
                        c.setFlatusLevel(flatusLevel);
                        c.setPee(pee);
                        HudClientEvent.urineLevel_Max = urineLevel_Max;
                        HudClientEvent.shitLevel_Max = shitLevel_Max;
                        HudClientEvent.urineLevel = urineLevel;
                        HudClientEvent.shitLevel = shitLevel;
                        HudClientEvent.flatusLevel = flatusLevel;
                    });
                }
            }
        });
        context.setPacketHandled(true);
    }
}
