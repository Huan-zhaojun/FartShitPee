package com.huan.fart_shit_pee.common.Fluid;

import com.huan.fart_shit_pee.common.Block.blockRegistry;
import com.huan.fart_shit_pee.common.Item.itemRegistry;
import com.huan.fart_shit_pee.fart_shit_pee;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidRegistry {
    public static final ResourceLocation STILL_OIL_TEXTURE = new ResourceLocation("block/water_still");
    public static final ResourceLocation FLOWING_OIL_TEXTURE = new ResourceLocation("block/water_flow");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, fart_shit_pee.MOD_ID);
    public static RegistryObject<urineForgeFlowingFluid.Source> urineFluid = FLUIDS.register("urine_fluid",
            () -> new urineForgeFlowingFluid.Source(FluidRegistry.PROPERTIES));
    public static RegistryObject<urineForgeFlowingFluid.Flowing> urineFluidFlowing = FLUIDS.register("urine_fluid_flowing",
            () -> new urineForgeFlowingFluid.Flowing(FluidRegistry.PROPERTIES));
    public static ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
            urineFluid,
            urineFluidFlowing,
            FluidAttributes.builder(STILL_OIL_TEXTURE, FLOWING_OIL_TEXTURE)
                    .color(0xFFad842b).density(1100).viscosity(1100).temperature(310))
            .bucket(itemRegistry.urineFluidBucket).block(blockRegistry.urineFluid)
            .levelDecreasePerBlock(2)
            .explosionResistance(100F);
}
