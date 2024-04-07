package com.huan.mod.capability;

import com.huan.mod.fart_shit_pee;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class drainCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    private final drainCapability INSTANCE;

    public drainCapabilityProvider() {
        INSTANCE = new drainCapability();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == fart_shit_pee.Drain_Capability ? LazyOptional.of(() -> this.INSTANCE).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return INSTANCE.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        INSTANCE.deserializeNBT(nbt);
    }
}
