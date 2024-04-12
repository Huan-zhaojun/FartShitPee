package com.huan.fart_shit_pee.common.Fluid;

import com.huan.fart_shit_pee.fart_shit_pee;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = fart_shit_pee.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderTypeRegistry {
    @SubscribeEvent
    public static void onRenderTypeSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(FluidRegistry.urineFluid.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(FluidRegistry.urineFluidFlowing.get(), RenderType.getTranslucent());
        });
    }
}
