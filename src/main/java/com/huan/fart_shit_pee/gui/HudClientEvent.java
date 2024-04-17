package com.huan.fart_shit_pee.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class HudClientEvent {
    public static int urineLevel_Max = 100, shitLevel_Max = 100;
    public static int urineLevel = 0, shitLevel = 0;
    public static int flatusLevel = 0;

    @SubscribeEvent
    public static void onOverlayRender(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (Minecraft.getInstance().player != null && !Minecraft.getInstance().player.abilities.isCreativeMode && !Minecraft.getInstance().player.isSpectator()) {
            Hub hub = new Hub(event.getMatrixStack());
            hub.render(urineLevel_Max , shitLevel_Max,  urineLevel,  shitLevel, flatusLevel);
        }
    }
}
