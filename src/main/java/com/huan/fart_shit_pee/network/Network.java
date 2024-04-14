package com.huan.fart_shit_pee.network;

import com.huan.fart_shit_pee.fart_shit_pee;
import com.huan.fart_shit_pee.network.Client.hubSendPack;
import com.huan.fart_shit_pee.network.Client.peeEnd_SendPack;
import com.huan.fart_shit_pee.network.Client.urine_lineSendPack;
import com.huan.fart_shit_pee.network.Server.drainSendPack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Network {
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(fart_shit_pee.MOD_ID, "network"),
            () -> VERSION, VERSION::equals, VERSION::equals);

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE.messageBuilder(hubSendPack.class, nextID()).encoder(hubSendPack::toBytes).decoder(hubSendPack::new).consumer(hubSendPack::handler).add();
        INSTANCE.registerMessage(nextID(), drainSendPack.class, drainSendPack::toBytes, drainSendPack::new, drainSendPack::handler);
        INSTANCE.registerMessage(nextID(), urine_lineSendPack.class, urine_lineSendPack::toBytes, urine_lineSendPack::new, urine_lineSendPack::handler);
        INSTANCE.registerMessage(nextID(), peeEnd_SendPack.class, peeEnd_SendPack::toBytes, peeEnd_SendPack::new, peeEnd_SendPack::handler);
    }
}
