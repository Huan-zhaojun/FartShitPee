package com.huan.fart_shit_pee.network.Client;

import com.huan.fart_shit_pee.network.IModPack;
import com.huan.fart_shit_pee.render.urineRender;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class urine_lineSendPack implements IModPack {
    private final double a;
    private final double b;//抛物线参数
    private final Vector3d from;//起始点
    private final float radianYaw;//旋转弧度角
    private final double endx;

    public urine_lineSendPack(double a, double b, Vector3d from, float radianYaw, double endx) {
        this.a = a;
        this.b = b;
        this.from = from;
        this.radianYaw = radianYaw;
        this.endx = endx;
    }

    public urine_lineSendPack(PacketBuffer buffer) {
        a = buffer.readDouble();
        b = buffer.readDouble();
        from = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        radianYaw = buffer.readFloat();
        endx = buffer.readDouble();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeDouble(a);
        buf.writeDouble(b);
        buf.writeDouble(from.x);
        buf.writeDouble(from.y);
        buf.writeDouble(from.z);
        buf.writeFloat(radianYaw);
        buf.writeDouble(endx);
    }

    @Override
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().world != null && Minecraft.getInstance().world.isRemote) {
                urineRender.a = a;
                urineRender.b = b;
                urineRender.from = from;
                urineRender.radianYaw = radianYaw;
                urineRender.endx = endx;
                urineRender.pee = true;//绘制指令
            }
        });
        context.setPacketHandled(true);
    }
}
