package com.huan.fart_shit_pee.common.Fluid;

import com.huan.fart_shit_pee.common.TileEntity.TileEntityTypeRegistry;
import com.huan.fart_shit_pee.common.Block.blockRegistry;
import com.huan.fart_shit_pee.network.Client.entityMotionSendPack;
import com.huan.fart_shit_pee.network.Client.urineTileParticle_Pack;
import com.huan.fart_shit_pee.network.Network;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Random;

public class urineFluidTileEntity extends TileEntity implements ITickableTileEntity {
    private int tickCount = 0;//刻的计数器
    Random random = new Random();

    public urineFluidTileEntity() {
        super(TileEntityTypeRegistry.urineFluidTileEntity.get());
    }

    public void increase() {
        tickCount++;
        markDirty();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        tickCount = nbt.getInt("urineFluid_tickCount");
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("urineFluid_tickCount", tickCount);
        return super.write(compound);
    }

    @Override
    public void tick() {
        if (world != null && !world.isRemote) {
            if (world.getFluidState(pos).getBlockState().getBlock().getBlock() == blockRegistry.urineFluid.get()) {
                BlockPos abovePos = pos.up();
                if (world.isDaytime() && world.isAirBlock(abovePos) && tickCount > 30) {
                    int i = random.nextInt(100) + 1;
                    if (i <= 4) {
                        Network.INSTANCE.send(PacketDistributor.ALL.noArg(), new urineTileParticle_Pack(pos));
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                } else if (world.isDaytime() && tickCount > 100) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                } else if (tickCount > 200) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                }
            }
            this.increase();
        }
    }
}
