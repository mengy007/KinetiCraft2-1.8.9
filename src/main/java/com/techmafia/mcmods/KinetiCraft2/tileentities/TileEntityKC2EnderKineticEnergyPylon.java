package com.techmafia.mcmods.KinetiCraft2.tileentities;

import com.techmafia.mcmods.KinetiCraft2.tileentities.base.TileEntityKC2Powered;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;

import java.util.Random;

/**
 * Created by myang on 11/5/15.
 */
public class TileEntityKC2EnderKineticEnergyPylon extends TileEntityKC2Powered {
    public TileEntityKC2EnderKineticEnergyPylon() {
        super();
    }
    int searchRadius = 4;
    int spawnRadius = 3;
    int energyNetworkUpdateTickInterval = 3;
    int networkUpdateTickCount = 3;
    public TileEntityKC2EnderKineticGenerator generatorTileEntity;

    @Override
    public int getMaxEnergyStored() {
        return 1000;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return false;
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public void update() {
        if (generatorTileEntity != null) {
            if (!generatorTileEntity.isFull()) {
                generatorTileEntity.receiveEnergy(null, getMaxEnergyStored(), false);
                Random randomGen = new Random();

                if (worldObj.isRemote) {
                    if (randomGen.nextInt(100) < 25) {
                        worldObj.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, (randomGen.nextDouble() * 2.0D - 1.0D), (randomGen.nextDouble() * 2.0D - 1.0D), (randomGen.nextDouble() * 2.0D - 1.0D));
                    }

                    double motionX = (randomGen.nextDouble() * 2.0D) - 1.0D;
                    double motionY = -0.5D;
                    double motionZ = (randomGen.nextDouble() * 2.0D) - 1.0D;

                    if (generatorTileEntity.getPos().getX() > pos.getX()) {
                        motionX -= (generatorTileEntity.getPos().getX() - pos.getX());
                    } else if (generatorTileEntity.getPos().getX() < pos.getX()) {
                        motionX += ((pos.getX() - generatorTileEntity.getPos().getX()));
                    }

                    if (generatorTileEntity.getPos().getZ() > pos.getZ()) {
                        motionZ -= (generatorTileEntity.getPos().getZ() - pos.getZ());
                    } else if (generatorTileEntity.getPos().getZ() < pos.getZ()) {
                        motionZ += (getPos().getZ() - generatorTileEntity.getPos().getZ());
                    }

                    worldObj.spawnParticle(
                            EnumParticleTypes.PORTAL,
                            generatorTileEntity.getPos().getX() + 0.5f,
                            generatorTileEntity.getPos().getY() + 0.5f,
                            generatorTileEntity.getPos().getZ() + 0.5f,
                            motionX,
                            motionY,
                            motionZ
                    );
                } else {
                    if ((randomGen.nextDouble() * 100.0D) < 0.5D) {
                        Entity enderman = new EntityEnderman(worldObj);
                        enderman.setPosition(pos.getX() + (randomGen.nextInt(spawnRadius * 2) - spawnRadius), pos.getY() + 1, pos.getZ() + (randomGen.nextInt(spawnRadius * 2) - spawnRadius));
                        worldObj.spawnEntityInWorld(enderman);
                    }
                }
            }
        }

        if (networkUpdateTickCount >= energyNetworkUpdateTickInterval) {
            if (generatorTileEntity == null) {
                generatorSearch:
                for (int x = (pos.getX() - searchRadius); x <= (pos.getX() + searchRadius); x++) {
                    for (int z = (pos.getZ() - searchRadius); z <= (pos.getZ() + searchRadius); z++) {
                        TileEntity te = worldObj.getTileEntity(new BlockPos(x, pos.getY(), z));

                        if (te != null && te instanceof TileEntityKC2EnderKineticGenerator) {
                            generatorTileEntity = (TileEntityKC2EnderKineticGenerator) te;
                            break generatorSearch;
                        }
                    }
                }
            } else {
                TileEntity te = worldObj.getTileEntity(new BlockPos(generatorTileEntity.getPos().getX(), generatorTileEntity.getPos().getY(), generatorTileEntity.getPos().getZ()));
                if (te == null) {
                    generatorTileEntity = null;
                }
            }
            networkUpdateTickCount = 0;
        }

        networkUpdateTickCount++;
    }
}
