package com.huan.fart_shit_pee.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class drainCapability implements INBTSerializable<CompoundNBT> {
    public int urineLevel_Max = 50, shitLevel_Max = 50;
    public int urineLevel = 0, shitLevel = 0;
    /**
     * 屁的等级，10个状态，屁等级最高为9
     */
    public int flatusLevel = 0;

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("urineLevel_Max", urineLevel_Max);
        nbt.putInt("shitLevel_Max", shitLevel_Max);
        nbt.putInt("urineLevel", urineLevel);
        nbt.putInt("shitLevel", shitLevel);
        nbt.putInt("flatusLevel", flatusLevel);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.urineLevel_Max = nbt.getInt("urineLevel_Max");
        this.shitLevel_Max = nbt.getInt("shitLevel_Max");
        this.urineLevel = nbt.getInt("urineLevel");
        this.shitLevel = nbt.getInt("shitLevel");
        this.flatusLevel = nbt.getInt("flatusLevel");
    }

    public drainCapability() {
    }

    public int getUrineLevel_Max() {
        return urineLevel_Max;
    }

    public void setUrineLevel_Max(int urineLevel_Max) {
        if (urineLevel_Max <= 0) return;
        this.urineLevel_Max = urineLevel_Max;
    }

    public int getUrineLevel() {
        return urineLevel;
    }

    public boolean setUrineLevel(int urineLevel) {
        if (urineLevel >= 0 && urineLevel <= urineLevel_Max) {
            this.urineLevel = urineLevel;
            return true;
        } else return false;
    }

    public int getShitLevel_Max() {
        return shitLevel_Max;
    }

    public void setShitLevel_Max(int shitLevel_Max) {
        if (shitLevel_Max <= 0) return;
        this.shitLevel_Max = shitLevel_Max;
    }

    public int getShitLevel() {
        return shitLevel;
    }

    public boolean setShitLevel(int shitLevel) {
        if (shitLevel >= 0 && shitLevel <= shitLevel_Max) {
            this.shitLevel = shitLevel;
            return true;
        } else return false;
    }

    public int getFlatusLevel() {
        return flatusLevel;
    }

    public boolean setFlatusLevel(int flatusLevel) {
        if (flatusLevel >= 0 && flatusLevel <= 9) {
            this.flatusLevel = flatusLevel;
            return true;
        } else return false;
    }
}
