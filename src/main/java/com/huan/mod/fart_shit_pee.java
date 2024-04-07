package com.huan.mod;

import com.huan.mod.capability.drainCapability;
import com.huan.mod.network.Networking;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@Mod(fart_shit_pee.MOD_ID)
public class fart_shit_pee {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "fart_shit_pee";

    @CapabilityInject(drainCapability.class)
    public static Capability<drainCapability> Drain_Capability;

    public fart_shit_pee() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);

    }

    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(()->{
            //注册能力
            CapabilityManager.INSTANCE.register(
                    drainCapability.class,
                    new Capability.IStorage<drainCapability>() {
                        @Nullable
                        @Override
                        public INBT writeNBT(Capability<drainCapability> capability, drainCapability instance, Direction side) {
                            return null;
                        }

                        @Override
                        public void readNBT(Capability<drainCapability> capability, drainCapability instance, Direction side, INBT nbt) {

                        }
                    },
                    ()-> null
            );

            //注册网络包
            Networking.registerMessage();
        });
    }
}
