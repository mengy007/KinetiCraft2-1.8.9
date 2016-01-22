package com.techmafia.mcmods.KinetiCraft2.tileentities;

import com.techmafia.mcmods.KinetiCraft2.tileentities.base.TileEntityKC2Powered;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;

import java.util.Random;

/**
 * Created by myang on 11/5/15.
 */
public class TileEntityKC2ThermalKineticGenerator extends TileEntityKC2Powered implements ITickable {
    int ticksBetweenChecks = 3;
    int tickCount = 3;
    int powerOutputMultiplier = 0;
    int powerOutputBase = 25;
    int lavaRadius = 4;

    public TileEntityKC2ThermalKineticGenerator() {
        super();
    }

    @Override
    public int getMaxEnergyStored() {
        return 1000000;
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

    @Override
    public void update() {
        super.update();

        if (tickCount >= ticksBetweenChecks) {
            checkForLavaBlocks();
        }

        if (powerOutputMultiplier > 0) {
            receiveEnergy(null, powerOutputBase*powerOutputMultiplier, false);
            if (worldObj.isRemote) {
                Random randomGen = new Random();
                worldObj.spawnParticle(
                        EnumParticleTypes.SMOKE_NORMAL,
                        (pos.getX()+0.5F)+((randomGen.nextFloat() * 0.5F) - 0.25F),
                        pos.getY()+0.9F,
                        (pos.getZ()+0.5F)+((randomGen.nextFloat() * 0.5F) - 0.25F),
                        0.0D,
                        (randomGen.nextDouble()/128.0D)+(powerOutputMultiplier/64.0D),
                        0.0D);
            }
        }
    }

    public void checkForLavaBlocks() {
        powerOutputMultiplier = 0;
        for (int x = (pos.getX()-lavaRadius); x <= (pos.getX()+lavaRadius); x++) {
            for (int z = (pos.getZ()-lavaRadius); z <= (pos.getZ()+lavaRadius); z++) {
                if (worldObj.getBlockState(new BlockPos(x, pos.getY(), z)).getBlock().equals(Blocks.lava) && worldObj.getBlockState(new BlockPos(x, pos.getY(), z)).equals(0)) {
                    powerOutputMultiplier++;
                }
            }
        }
    }
}
