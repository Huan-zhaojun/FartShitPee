package com.huan.fart_shit_pee.network;

import com.huan.fart_shit_pee.fart_shit_pee;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(fart_shit_pee.MOD_ID, "networking"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(SendPack.class, nextID())
                .encoder(SendPack::toBytes)
                .decoder(SendPack::new)
                .consumer(SendPack::handler)
                .add();
    }
}
