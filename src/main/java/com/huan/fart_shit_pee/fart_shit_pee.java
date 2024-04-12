package com.huan.fart_shit_pee;

import com.huan.fart_shit_pee.TileEntity.TileEntityTypeRegistry;
import com.huan.fart_shit_pee.capability.CapabilityEvent;
import com.huan.fart_shit_pee.capability.drainCapability;
import com.huan.fart_shit_pee.common.Block.blockRegistry;
import com.huan.fart_shit_pee.common.Fluid.FluidRegistry;
import com.huan.fart_shit_pee.common.Item.itemRegistry;
import com.huan.fart_shit_pee.network.Networking;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod(fart_shit_pee.MOD_ID)
public class fart_shit_pee {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "fart_shit_pee";
    public static boolean hunger_plus_mod = false;//是否存在该模组

    @CapabilityInject(drainCapability.class)
    public static Capability<drainCapability> Drain_Capability;

    public fart_shit_pee() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        itemRegistry.ITEMS.register(modEventBus);
        blockRegistry.BLOCKS.register(modEventBus);
        FluidRegistry.FLUIDS.register(modEventBus);
        TileEntityTypeRegistry.TILE_ENTITIES.register(modEventBus);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::receive);

    }

    public void setup(final FMLCommonSetupEvent event) {
        if (ModList.get().getModFileById("hunger_plus") != null) modExist("hunger_plus");
        modExist("hunger_plus");
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
                () -> null
        );

        //注册网络包
        Networking.registerMessage();

        event.enqueueWork(() -> {
        });//渲染线程任务
    }

    /**
     *  在模组加载时接受信息<br>
     *  发送消息参数：<br>
     *  “method”=“{@link fart_shit_pee#MOD_ID}:start”<br>
     *  “Supplier<?> thing”接受字符串执行相应的操作
     *  @see InterModComms#sendTo(String, String, Supplier) 
     */
    public void receive(InterModProcessEvent event) {
        String function = "null";
        List<InterModComms.IMCMessage> list = event.getIMCStream(s -> s.equals("fart_shit_pee:start")).collect(Collectors.toList());
        for (InterModComms.IMCMessage imcMessage : list) {
            function = (String) imcMessage.getMessageSupplier().get();
        }
    }

    public static void modExist(String modId) {//存在其他模组
        if (modId.equals("hunger_plus")) {
            hunger_plus_mod = true;
            CapabilityEvent.hunger_plus_mod();
        }
    }
}
