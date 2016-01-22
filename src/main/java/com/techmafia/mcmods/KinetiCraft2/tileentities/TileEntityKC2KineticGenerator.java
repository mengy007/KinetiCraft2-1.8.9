package com.techmafia.mcmods.KinetiCraft2.tileentities;

import com.techmafia.mcmods.KinetiCraft2.tileentities.base.TileEntityKC2Powered;
import net.minecraft.util.EnumFacing;
//import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Meng on 10/18/2015.
 */
public class TileEntityKC2KineticGenerator extends TileEntityKC2Powered {
    public TileEntityKC2KineticGenerator() {
        super();
    }

    @Override
    public int getMaxEnergyStored() {
        return 50000;
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
}
