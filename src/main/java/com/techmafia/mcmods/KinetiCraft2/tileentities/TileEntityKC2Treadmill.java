package com.techmafia.mcmods.KinetiCraft2.tileentities;

import com.techmafia.mcmods.KinetiCraft2.tileentities.base.TileEntityKC2Powered;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Meng on 10/25/2015.
 */
public class TileEntityKC2Treadmill extends TileEntityKC2Powered {
    private static final int powerOutputMultiplier = 1;
    private static final int maxSpeed = 20;
    protected int entityId;
    protected EntityLiving latchedEntity;

    int treadmillSpeed = 0;

    int ticksBetweenChecks = 3;
    int tickCount = 3;

    public boolean resync;

    public TileEntityKC2Treadmill() {
        super();
        resync = false;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setInteger("treadmillSpeed", treadmillSpeed);
        nbt.setInteger("entityId", entityId);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        treadmillSpeed = nbt.getInteger("treadmillSpeed");
        entityId = nbt.getInteger("entityId");

        resync = true;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(pos, 0, tag);
    }

    @Override
    public void update() {
        super.update();

        if(resync) {
            if(entityId != -1) {
                if(worldObj.isRemote) {
                    Entity ent = worldObj.getEntityByID(entityId);
                    if(ent != null && ent.getDistance(pos.getX()+0.5D, pos.getY() + 1D, pos.getZ()+0.5D) < 7D) {
                        latchedEntity = (EntityLiving)ent;
                    }
                } else {
                    scanForEntity();
                }
            } else if(latchedEntity != null) {
                latchedEntity = null;
                worldObj.markBlockForUpdate(pos);
            }
        }
        resync = false;

        if (latchedEntity != null) {
            if (treadmillSpeed < maxSpeed) {
                treadmillSpeed++;
            }
            latchedEntity.setLocationAndAngles(pos.getX()+0.5D, pos.getY()+1.0D, pos.getZ()+0.5D, -2 * 90F, 0.0F);
            latchedEntity.setAIMoveSpeed((float)(treadmillSpeed/maxSpeed));
            latchedEntity.getNavigator().clearPathEntity();

            if (worldObj.isRemote && treadmillSpeed >= maxSpeed) {
                worldObj.spawnParticle(
                        EnumParticleTypes.SMOKE_NORMAL,
                        (pos.getX() + 0.5D) + ((worldObj.rand.nextDouble() * 1D) - 0.5D),
                        pos.getY() + 1D,
                        (pos.getZ() + 0.5D) + ((worldObj.rand.nextDouble() * 1D) - 0.5D),
                        0D,
                        0D,
                        0D
                );

                worldObj.spawnParticle(
                        EnumParticleTypes.WATER_SPLASH,
                        (pos.getX() + 0.5D) + ((worldObj.rand.nextDouble() * 1D) - 0.5D),
                        pos.getY() + 2.5D,
                        (pos.getZ() + 0.5D) + ((worldObj.rand.nextDouble() * 1D) - 0.5D),
                        0D,
                        0D,
                        0D
                );
            }

            if (treadmillSpeed > 0 && latchedEntity != null && latchedEntity.getClass().isAssignableFrom(EntityVillager.class)) {
                if (!worldObj.isRemote) {
                    int powerToAdd = powerOutputMultiplier * treadmillSpeed;
                    receiveEnergy(null, powerToAdd, false);

                    if (getEnergyStored(null) >= getMaxEnergyStored()) {
                        if (latchedEntity != null && latchedEntity.getClass().isAssignableFrom(EntityVillager.class)) {
                            latchedEntity.moveEntity(
                                    (worldObj.rand.nextInt(6)-3),
                                    (worldObj.rand.nextDouble() * 9D),
                                    (worldObj.rand.nextInt(6)-3)
                            );
                            //latchedEntity.setVelocity(worldObj.rand.nextDouble(), worldObj.rand.nextDouble() * 3D, worldObj.rand.nextDouble());
                            latchedEntity.setAIMoveSpeed(0F);
                            latchedEntity = null;
                            entityId = -1;
                            resync = true;
                            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, 0D, 0D, 0D);
                        }
                    }
                }
            }
        } else {
            if (tickCount >= ticksBetweenChecks) {
                scanForEntity();
            }
        }
    }

    public int getTreadmillSpeed() {
        return treadmillSpeed;
    }

    public void scanForEntity() {
        AxisAlignedBB aabb = AxisAlignedBB.fromBounds(pos.getX() - 0.175D, pos.getY() - 0.175D, pos.getZ() - 0.175D, pos.getX() + 1.175D, pos.getY() + 1.175D, pos.getZ() + 1.175D);
        List list = worldObj.getEntitiesWithinAABB(EntityAgeable.class, aabb);

        for (Object aList : list) {
            EntityLiving ent = (EntityLiving) aList;

            if (isValidForTreadmill(ent)) {
                if (ent.posX > aabb.minX && ent.posX < aabb.maxX && ent.posY > aabb.minY && ent.posY < aabb.maxY && ent.posZ > aabb.minZ && ent.posZ < aabb.maxZ) {
                    latchedEntity = ent;
                    latchedEntity.setLocationAndAngles(pos.getX()+0.5D, pos.getY()+1.0D, pos.getZ()+0.5D, -2 * 90F, 0.0F);
                    entityId = latchedEntity.getEntityId();
                    worldObj.markBlockForUpdate(pos);
                    break;
                }
            }
        }
    }

    public boolean isValidForTreadmill(EntityLiving entity) {
        return entity.getClass().isAssignableFrom(EntityVillager.class);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.fromBounds(pos.getX()-2, pos.getY()-1, pos.getZ()-2, pos.getX()+2, pos.getY()+1, pos.getZ()+2);
    }

    @Override
    public boolean canTransmitPower(EnumFacing dir) {
        return dir != EnumFacing.UP;
    }

    @Override
    public int getMaxEnergyStored() {
        return 10000;
    }
}