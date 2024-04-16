package com.huan.fart_shit_pee.common.Block;

import com.huan.fart_shit_pee.common.TileEntity.TileEntityTypeRegistry;
import com.huan.fart_shit_pee.common.customDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class shitTileEntity extends TileEntity implements ITickableTileEntity {
    private int tickCount = 0;//存活时间
    private UUID uuid;
    private int radius = 3;

    public shitTileEntity() {
        super(TileEntityTypeRegistry.shitTileEntity.get());
    }

    public void increase() {
        tickCount++;
        markDirty();
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
        markDirty();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        markDirty();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        tickCount = nbt.getInt("shit_tickCount");
        radius = nbt.getInt("shit_radius");
        if (nbt.contains("shit_uuid")) uuid = nbt.getUniqueId("shit_uuid");
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putInt("shit_tickCount", tickCount);
        nbt.putInt("shit_radius", radius);
        if (uuid != null) nbt.putUniqueId("shit_uuid", uuid);
        return super.write(nbt);
    }

    @Override
    public void tick() {
        if (world != null && !world.isRemote) {
            List<Entity> entityList = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos).grow(radius));
            for (Entity entity : entityList) {
                if (tickCount % 20 == 0) {
                    if (uuid != null && !entity.equals(world.getPlayerByUuid(uuid))) {
                        customDamageSource.smellyShitDamageSource smellyShit = (customDamageSource.smellyShitDamageSource) new customDamageSource.smellyShitDamageSource("smellyShit").setPlayer(world.getPlayerByUuid(uuid)).setDamageBypassesArmor();
                        entity.attackEntityFrom(smellyShit, 2);
                    } else if (uuid != null && entity.equals(world.getPlayerByUuid(uuid)) && tickCount <= 300)
                        ;
                    else if (uuid != null && entity.equals(world.getPlayerByUuid(uuid)) && tickCount > 300) {
                        customDamageSource.smellyShitDamageSource smellyShit = (customDamageSource.smellyShitDamageSource) new customDamageSource.smellyShitDamageSource("smellyShit").setPlayer(world.getPlayerByUuid(uuid)).setDamageBypassesArmor();
                        entity.attackEntityFrom(smellyShit, 2);
                    } else if (entity instanceof PlayerEntity) {
                        entity.attackEntityFrom(customDamageSource.smellyShit_DamageSource1, 2);
                    }
                }
                if (!(entity instanceof PlayerEntity)) {
                    float amount = 2;
                    if (entity instanceof LivingEntity) amount = ((LivingEntity) entity).getMaxHealth() / 4.0f;
                    entity.attackEntityFrom(customDamageSource.smellyShit_DamageSource1, amount);
                }
            }
            //world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        } else if (world != null) {
            double h = 0.5;
            if (tickCount % 30 == 0) {
                world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + h, pos.getZ() + 0.5, 0, 0.05, 0);
                world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.3, pos.getY() + h, pos.getZ() + 0.3, 0, 0.05, 0);
            } else if (tickCount % 25 == 0) {
                world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.3, pos.getY() + h, pos.getZ() + 0.7, 0, 0.05, 0);
            } else if (tickCount % 20 == 0) {
                world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.7, pos.getY() + h, pos.getZ() + 0.3, 0, 0.05, 0);
            } else if (tickCount % 15 == 0) {
                world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.7, pos.getY() + h, pos.getZ() + 0.7, 0, 0.05, 0);
            }
        }
        this.increase();
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.putInt("shit_tickCount", tickCount);
        nbt.putInt("shit_radius", radius);
        if (uuid != null) nbt.putUniqueId("shit_uuid", uuid);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        tickCount = nbt.getInt("shit_tickCount");
        radius = nbt.getInt("shit_radius");
        if (nbt.contains("shit_uuid")) uuid = nbt.getUniqueId("shit_uuid");
    }
}
