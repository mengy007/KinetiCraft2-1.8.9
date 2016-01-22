package com.techmafia.mcmods.KinetiCraft2.tileentities;

import com.techmafia.mcmods.KinetiCraft2.tileentities.base.TileEntityKC2Powered;
import net.minecraft.util.EnumFacing;

/**
 * Created by myang on 11/5/15.
 */
public class TileEntityKC2EnderKineticGenerator extends TileEntityKC2Powered {
    public TileEntityKC2EnderKineticGenerator() {
        super();
    }

    @Override
    public int getMaxEnergyStored() {
        return 10000000;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return (from != EnumFacing.UP);
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        if(!m_ProvidesEnergy || (from == EnumFacing.UP)) { return 0; }
        return energyStorage.extractEnergy(maxExtract, simulate);
    }

    public boolean isFull() {
        return (getEnergyStored(null) == getMaxEnergyStored());
    }
}
