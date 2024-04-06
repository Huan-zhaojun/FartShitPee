package com.huan.mod;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(fart_shit_pee.MOD_ID)
public class fart_shit_pee
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "fart_shit_pee";
    public fart_shit_pee(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);

    }
    public void setup(final FMLCommonSetupEvent event){
        LOGGER.info("hello~");
    }
}
