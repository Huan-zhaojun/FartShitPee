package com.huan.fart_shit_pee.common.Block;

import com.huan.fart_shit_pee.common.Fluid.FluidRegistry;
import com.huan.fart_shit_pee.common.Fluid.urineFluidTileEntity;
import com.huan.fart_shit_pee.fart_shit_pee;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class blockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, fart_shit_pee.MOD_ID);

    public static RegistryObject<FlowingFluidBlock> urineFluid = BLOCKS.register("urine_fluid",
            () -> new FlowingFluidBlock(FluidRegistry.urineFluid, Block.Properties.create(Material.WATER)
                    .doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()) {
                @Override
                public boolean hasTileEntity(BlockState state) {
                    return true;
                }

                @Nullable
                @Override
                public TileEntity createTileEntity(BlockState state, IBlockReader world) {
                    return new urineFluidTileEntity();
                }
            });
}
