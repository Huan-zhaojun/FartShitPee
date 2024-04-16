package com.huan.fart_shit_pee.network.Client;

import com.huan.fart_shit_pee.network.IModPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class entityMotionSendPack implements IModPack {
    private UUID uuid;
    private Vector3d vector3d;

    public entityMotionSendPack(UUID uuid, double x, double y, double z) {
        this.uuid = uuid;
        vector3d = new Vector3d(x, y, z);
    }

    public entityMotionSendPack(UUID uuid, Vector3d vector3d) {
        this.uuid = uuid;
        this.vector3d = vector3d;
    }

    public entityMotionSendPack(PacketBuffer buffer) {
        uuid = buffer.readUniqueId();
        vector3d = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeUniqueId(uuid);
        buf.writeDouble(vector3d.x);
        buf.writeDouble(vector3d.y);
        buf.writeDouble(vector3d.z);
    }

    @Override
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().world != null && Minecraft.getInstance().world.isRemote) {
                ClientPlayerEntity player = (ClientPlayerEntity) Minecraft.getInstance().world.getPlayerByUuid(uuid);
                if (player != null) {
                    player.setMotion(vector3d);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
