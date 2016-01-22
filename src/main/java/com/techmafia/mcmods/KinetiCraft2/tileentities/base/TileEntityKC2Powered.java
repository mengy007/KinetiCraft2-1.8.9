package com.techmafia.mcmods.KinetiCraft2.tileentities.base;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

/**
 * Created by Meng on 10/16/2015.
 */
public abstract class TileEntityKC2Powered extends TileEntityKC2Base implements IEnergyHandler, ITickable {
    public static float energyPerRF = 1f;

    protected boolean m_ReceivesEnergy = true;
    protected boolean m_ProvidesEnergy = true;

    // Internal power
    protected EnergyStorage energyStorage;

    public TileEntityKC2Powered() {
        super();

        energyStorage = new EnergyStorage(getMaxEnergyStored());
    }

    // Internal energy methods
    /**
     * The amount of energy stored in this type of TileEntity
     * @return The amount of energy stored. 0 or more. Only called at construction time.
     */
    protected abstract int getMaxEnergyStored();

    @Override
    public boolean isActive() {
        return true;
    }

    // TileEntity overrides
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("energyStorage")) {
            this.energyStorage.readFromNBT(tag.getCompoundTag("energyStorage"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagCompound energyTag = new NBTTagCompound();
        this.energyStorage.writeToNBT(energyTag);
        tag.setTag("energyStorage", energyTag);
    }

    // TileEntity methods
    @Override
    public void update() {
        // Distribute power
        if (!worldObj.isRemote && isActive()) {
            for (EnumFacing direction : EnumFacing.VALUES) {
                if (canTransmitPower(direction)) {
                    TileEntity adjacentTile = worldObj.getTileEntity(new BlockPos(pos.getX() + direction.getFrontOffsetX(), pos.getY() + direction.getFrontOffsetY(), pos.getZ() + direction.getFrontOffsetZ()));

                    if (adjacentTile instanceof IEnergyReceiver) {
                        IEnergyReceiver handler = (IEnergyReceiver) adjacentTile;
                        energyStorage.extractEnergy(handler.receiveEnergy(direction.getOpposite(), energyStorage.extractEnergy(energyStorage.getMaxExtract(), true), false), false);
                    }
                }
            }
        }
    }

    public boolean canTransmitPower(EnumFacing dir) {
        return true;
    }

    // TileEntityKC2Base methods
    @Override
    protected void onSendUpdate(NBTTagCompound updateTag) {
        super.onSendUpdate(updateTag);
        NBTTagCompound energyTag = new NBTTagCompound();
        this.energyStorage.writeToNBT(energyTag);
        updateTag.setTag("energyStorage", energyTag);
    }

    @Override
    public void onReceiveUpdate(NBTTagCompound updateTag) {
        super.onReceiveUpdate(updateTag);
        this.energyStorage.readFromNBT(updateTag.getCompoundTag("energyStorage"));
    }

    /* IEnergyHandler */
    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        // Only receive from Cores
        if (from == null) {
            int energyReceived;

            if (!m_ReceivesEnergy) {
                return 0;
            }
            energyReceived = energyStorage.receiveEnergy(maxReceive, simulate);
            worldObj.markBlockForUpdate(pos);

            return energyReceived;
        }
        return 0;
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        int energyExtracted;

        if(!m_ProvidesEnergy) { return 0; }

        energyExtracted = energyStorage.extractEnergy(maxExtract, simulate);
        worldObj.markBlockForUpdate(pos);

        return energyExtracted;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return energyStorage.getEnergyStored();
    }

    public void setEnergyStored(int energyLevel) {
        energyStorage.setEnergyStored(energyLevel);
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return energyStorage.getMaxEnergyStored();
    }
}