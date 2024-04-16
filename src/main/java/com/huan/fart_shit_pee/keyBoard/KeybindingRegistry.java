package com.huan.fart_shit_pee.keyBoard;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeybindingRegistry {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRegistry.registerKeyBinding(KeyBoardInput.pee_KEY);
            ClientRegistry.registerKeyBinding(KeyBoardInput.fart_KEY);
            ClientRegistry.registerKeyBinding(KeyBoardInput.up_KEY);
            ClientRegistry.registerKeyBinding(KeyBoardInput.down_KEY);
        });
    }
}
