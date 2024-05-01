package com.huan.fart_shit_pee.common;

import com.huan.fart_shit_pee.api.Config;
import com.huan.fart_shit_pee.common.Block.blockRegistry;
import com.huan.fart_shit_pee.common.Fluid.FluidRegistry;
import com.huan.fart_shit_pee.fart_shit_pee;
import com.huan.fart_shit_pee.network.Client.urine_lineSendPack;
import com.huan.fart_shit_pee.network.Network;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = fart_shit_pee.MOD_ID)
public class worldEvent {
    @ObjectHolder("ifscience:furnace_tnt")
    public static EntityType<? extends TNTEntity> furnaceTNT;

    private final static HashMap<UUID, Double> list = new HashMap<>();//用于记录玩家当前尿射线延伸多少部分

    @SubscribeEvent
    public static void tick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (!world.isRemote) {
            for (PlayerEntity player : world.getPlayers()) {
                player.getCapability(fart_shit_pee.Drain_Capability).ifPresent(c -> {
                    if (c.isPee()) {
                        double part = 0.0;//用于刚开始尿尿射击的部分
                        if (list.get(player.getUniqueID()) == null) {
                            part += 1.0 / 20.0;
                            list.put(player.getUniqueID(), part);
                        } else {
                            part = list.get(player.getUniqueID()) + 1.0 / 20.0;
                            list.put(player.getUniqueID(), part);
                        }

                        Vector3d playerVec = player.getPositionVec().add(0, 0.65, 0);
                        float yaw = player.rotationYaw, pitch = player.rotationPitch;
                        float radianYaw = (float) ((yaw * Math.PI) / 180);//角度值转弧度值
                        double a = Config.a_default.get(), b = Config.b_default.get();//抛物线公式参数
                        if (c.a == 0 || c.b == 0) {
                            c.a = a;
                            c.b = b;
                        } else {
                            a = c.a;
                            b = c.b;
                        }
                        double leftPoint = parabolaX(a, b);//抛物线左零点

                        //浅度检测
                        double gap1 = 0.5;//离散点间隔
                        double xFrom = leftPoint, xEnd;//大概有方块阻挡的范围
                        for (double x = leftPoint + gap1; ; x += gap1) {
                            double Y = parabolaY(a, b, x) + playerVec.y;
                            double X = (-(x - leftPoint) * Math.sin(radianYaw)) + playerVec.x;
                            double Z = ((x - leftPoint) * Math.cos(radianYaw)) + playerVec.z;
                            if (!world.getBlockState(new BlockPos(X, Y, Z)).getBlock().equals(Blocks.AIR) &&
                                    !world.getBlockState(new BlockPos(X, Y, Z)).getBlock().equals(blockRegistry.urineFluid.get()) &&
                                    !world.getBlockState(new BlockPos(X, Y, Z)).getBlock().equals(Blocks.WATER.getBlock()) &&
                                    !world.getBlockState(new BlockPos(X, Y, Z)).getBlock().equals(Blocks.LAVA.getBlock())) {
                                xEnd = x;
                                break;
                            } else xFrom = x;
                        }
                        double xPart = part < 1.0 ? part * (xEnd - leftPoint) : (xEnd - leftPoint);//延展部分
                        for (double x = leftPoint + gap1; (x - leftPoint) < xPart; x += gap1) {
                            double Y = parabolaY(a, b, x) + playerVec.y;
                            double X = (-(x - leftPoint) * Math.sin(radianYaw)) + playerVec.x;
                            double Z = ((x - leftPoint) * Math.cos(radianYaw)) + playerVec.z;
                            List<Entity> entityList = world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(new BlockPos(X, Y, Z)).grow(1));
                            for (Entity entity : entityList) {
                                if (furnaceTNT != null && entity.getType().equals(furnaceTNT)) continue;
                                entity.attackEntityFrom(DamageSource.IN_WALL, 2f);
                                entity.setMotion(-Math.sin(radianYaw) * 3, 1, Math.cos(radianYaw) * 3);//击飞生物
                            }
                        }

                        //深度检测
                        double gap2 = 0.1;
                        double endx = leftPoint + gap2;
                        Vector3d lastVec = playerVec;//上一个没有方块的离散点
                        for (double x = leftPoint + gap2; (x - leftPoint) < xPart; x += gap2) {
                            double Y = parabolaY(a, b, x) + playerVec.y;
                            double X = (-(x - leftPoint) * Math.sin(radianYaw)) + playerVec.x;
                            double Z = ((x - leftPoint) * Math.cos(radianYaw)) + playerVec.z;
                            if (x >= xFrom & x <= xEnd) {//只对大概有方块阻挡的范围进行检测
                                if (!world.getBlockState(new BlockPos(X, Y, Z)).getBlock().equals(Blocks.AIR) &&
                                        !world.getBlockState(new BlockPos(X, Y, Z)).getBlock().equals(blockRegistry.urineFluid.get()) &&
                                        !world.getBlockState(new BlockPos(X, Y, Z)).getBlock().equals(Blocks.WATER.getBlock()) &&
                                        !world.getBlockState(new BlockPos(X, Y, Z)).getBlock().equals(Blocks.LAVA.getBlock())) {
                                    endx = x;//记录最后有方块时的抛物线x点

                                    if (lastVec == playerVec) break;
                                    if (world.getBlockState(new BlockPos(lastVec).down()).getBlock().equals(Blocks.AIR)) {//下1个方块是空气
                                        if (world.getBlockState(new BlockPos(lastVec).down(2)).getBlock().equals(Blocks.AIR)) {//下2个方块是空气
                                            if (!world.getBlockState(new BlockPos(lastVec).down(3)).getBlock().equals(Blocks.AIR)) {//下3个方块不是空气
                                                world.setBlockState(new BlockPos(lastVec).down(2),//放置尿液方块
                                                        FluidRegistry.urineFluidFlowing.get().getFlowingFluidState(1, false).getBlockState());
                                            }
                                        } else world.setBlockState(new BlockPos(lastVec).down(),//放置尿液方块
                                                FluidRegistry.urineFluidFlowing.get().getFlowingFluidState(1, false).getBlockState());
                                    } else world.setBlockState(new BlockPos(lastVec)/*.down()*/,//放置尿液方块
                                            FluidRegistry.urineFluidFlowing.get().getFlowingFluidState(1, false).getBlockState());
                                    break;
                                } else lastVec = new Vector3d(X, Y, Z);
                            }
                            endx = part < 1.0 ? endx = x : endx;
                        }

                        Network.INSTANCE.send(PacketDistributor.ALL.noArg(),
                                new urine_lineSendPack(a, b, playerVec, radianYaw, endx));
                    } else {
                        list.put(player.getUniqueID(), 0.0);
                    }
                });
            }
        }
    }

    //抛物线计算
    private static double parabolaY(double a, double b, double x) {
        return -a * Math.pow(x, 2) + b;
    }

    //求左端零点
    private static double parabolaX(double a, double b) {
        return -Math.sqrt((0 - b) / -a);
    }
}
