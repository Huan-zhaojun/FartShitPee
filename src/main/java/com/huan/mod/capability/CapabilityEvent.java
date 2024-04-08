package com.huan.mod.capability;

import com.huan.mod.fart_shit_pee;
import com.huan.mod.network.Networking;
import com.huan.mod.network.SendPack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

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

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event){
        if (!event.player.world.isRemote){
            ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            LazyOptional<drainCapability> cap = player.getCapability(fart_shit_pee.Drain_Capability);
            cap.ifPresent(c-> Networking.INSTANCE.send(PacketDistributor.PLAYER.with(()-> player)
                    , new SendPack(c.urineLevel_Max,c.shitLevel_Max,c.urineLevel,c.shitLevel,c.flatusLevel,player.getUniqueID())));
        }
    }

}
