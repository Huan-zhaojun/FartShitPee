package com.huan.fart_shit_pee.TileEntity;

import com.huan.fart_shit_pee.common.Block.blockRegistry;
import com.huan.fart_shit_pee.common.Fluid.urineFluidTileEntity;
import com.huan.fart_shit_pee.fart_shit_pee;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypeRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, fart_shit_pee.MOD_ID);
    public static final RegistryObject<TileEntityType<urineFluidTileEntity>> urineFluidTileEntity = TILE_ENTITIES.register("urine_fluid_tileentity", () -> TileEntityType.Builder.create(urineFluidTileEntity::new, blockRegistry.urineFluid.get()).build(null));
}
