package com.huan.mod.capability;

import com.huan.mod.fart_shit_pee;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = fart_shit_pee.MOD_ID)
public class CapabilityEvent {
    @SubscribeEvent//给玩家注册上能力
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(fart_shit_pee.MOD_ID, "drain_capability"), new drainCapabilityProvider());
        }
    }

    @SubscribeEvent//能力保留
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {//死亡不保存
            LazyOptional<drainCapability> originalCap = event.getOriginal().getCapability(fart_shit_pee.Drain_Capability);
            LazyOptional<drainCapability> nowCap = event.getPlayer().getCapability(fart_shit_pee.Drain_Capability);
            if (originalCap.isPresent() && nowCap.isPresent()) {
                nowCap.ifPresent((cap) -> {
                    originalCap.ifPresent((oldCap) -> cap.deserializeNBT(oldCap.serializeNBT()));
                });
            }
        }
    }

}
