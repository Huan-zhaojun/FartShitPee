package com.huan.fart_shit_pee.network.Server;

import com.huan.fart_shit_pee.capability.drainCapability;
import com.huan.fart_shit_pee.common.Block.blockRegistry;
import com.huan.fart_shit_pee.common.Block.shitTileEntity;
import com.huan.fart_shit_pee.fart_shit_pee;
import com.huan.fart_shit_pee.network.Client.peeEnd_SendPack;
import com.huan.fart_shit_pee.network.IModPack;
import com.huan.fart_shit_pee.network.Network;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class drainSendPack implements IModPack {
    public drainSendPack() {

    }

    public drainSendPack(PacketBuffer buffer) {

    }

    @Override
    public void toBytes(PacketBuffer buf) {

    }

    @Override
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null && !player.world.isRemote) {
                if (!player.isCrouching() && !player.isCreative() && !player.isSpectator()) {//撒尿
                    LazyOptional<drainCapability> cap = player.getCapability(fart_shit_pee.Drain_Capability);
                    cap.ifPresent(c -> {
                        if (c.urineLevel > 0) {
                            c.setPee(!c.pee);//控制撒尿的开始或终止
                            if (!c.pee) //终止绘制
                                Network.INSTANCE.send(PacketDistributor.ALL.noArg(), new peeEnd_SendPack());
                        }
                    });
                } else if (!player.isCreative() && !player.isSpectator()) {//拉屎
                    LazyOptional<drainCapability> cap = player.getCapability(fart_shit_pee.Drain_Capability);
                    cap.ifPresent(c -> {
                        World world = player.world;
                        if (c.shitLevel > 0) {
                            int size = (double) c.shitLevel / c.shitLevel_Max <= 0.2 ? 1
                                    : ((double) c.shitLevel / c.shitLevel_Max <= 0.6 ? 2 : 3);
                            double radians = Math.toRadians(player.rotationYaw);
                            BlockPos blockPos = new BlockPos(player.getPositionVec().add(Math.sin(radians), 0, -Math.cos(radians)));
                            Block block = world.getBlockState(blockPos).getBlock();
                            if (block.equals(Blocks.AIR)) {
                                world.setBlockState(blockPos, blockRegistry.shitBlock.get().getStateContainer().getValidStates().get(size - 1));
                            } else if (block.equals(blockRegistry.shitBlock.get())) {
                                world.destroyBlock(blockPos, true);
                                world.setBlockState(blockPos, blockRegistry.shitBlock.get().getStateContainer().getValidStates().get(size - 1));
                            } else {
                                world.setBlockState(player.getPosition(), blockRegistry.shitBlock.get().getStateContainer().getValidStates().get(size - 1));
                            }

                            if (world.getTileEntity(blockPos) != null) {
                                shitTileEntity tileEntity = (shitTileEntity) world.getTileEntity(blockPos);
                                if (tileEntity != null) {
                                    tileEntity.setUuid(player.getUniqueID());
                                    tileEntity.setRadius(size == 1 ? 3 : (size == 2 ? 5 : 7));
                                }
                            }
                            c.setShitLevel(0);
                        }
                    });
                }
            }
        });
        context.setPacketHandled(true);
    }
}
