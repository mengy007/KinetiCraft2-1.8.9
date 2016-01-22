package com.techmafia.mcmods.KinetiCraft2.blocks;

import com.techmafia.mcmods.KinetiCraft2.creativetab.CreativeTabKC2;
import com.techmafia.mcmods.KinetiCraft2.tileentities.TileEntityKC2HardenedKineticEnergyCube;
import com.techmafia.mcmods.KinetiCraft2.tileentities.TileEntityKC2KineticEnergyCube;
import com.techmafia.mcmods.KinetiCraft2.tileentities.TileEntityKC2ReinforcedKineticEnergyCube;
import com.techmafia.mcmods.KinetiCraft2.tileentities.TileEntityKC2ResonantKineticEnergyCube;
import com.techmafia.mcmods.KinetiCraft2.tileentities.base.TileEntityKC2Powered;
import com.techmafia.mcmods.KinetiCraft2.utility.ItemNBTHelper;
import com.techmafia.mcmods.KinetiCraft2.utility.StaticUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Meng on 7/31/2015.
 */
public class BlockKC2EnergyCube extends Block {
    public static final String[] _subBlocks = {
            "kineticEnergyCube",
            "hardenedKineticEnergyCube",
            "reinforcedKineticEnergyCube",
            "resonantKineticEnergyCube"
    };

    public BlockKC2EnergyCube(Material material) {
        super(material);

        setStepSound(soundTypeMetal);
        setHardness(0.1f);
        setUnlocalizedName("kc2EnergyCube");
        this.setCreativeTab(CreativeTabKC2.KC2_TAB);
    }

    @Override
    public int getRenderType() {
        return 0;
    }

    /*
    @Override
    public int damageDropped(IBlockState blockState) {
        return getBlockState().;
    }
    */

    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState) {
        if (blockState.equals(0)) {
            return new TileEntityKC2KineticEnergyCube();
        } else if (blockState.equals(1)) {
            return new TileEntityKC2HardenedKineticEnergyCube();
        } else if (blockState.equals(2)) {
            return new TileEntityKC2ReinforcedKineticEnergyCube();
        } else if (blockState.equals(3)) {
            return new TileEntityKC2ResonantKineticEnergyCube();
        } else {
            throw new IllegalArgumentException("Unknown metadata for tile entity");
        }
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", 0));
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", 400000));

        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 1), "Energy", 0));
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 1), "Energy", 2000000));

        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 2), "Energy", 0));
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 2), "Energy", 20000000));

        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 3), "Energy", 0));
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 3), "Energy", 80000000));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(blockPos);
        if (te == null) {
            return false;
        } else {
            world.markBlockForUpdate(blockPos);

            if (entityPlayer.isSneaking()) {
                return false;
            }

            if (entityPlayer.inventory.getCurrentItem() == null) {
                // Return stored power if bare hand
                if (world.isRemote) {
                    entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "" + ((TileEntityKC2Powered) te).getEnergyStored(null) + " / " + ((TileEntityKC2Powered)te).getMaxEnergyStored(null) + " RF"));
                    return true;
                }
                return false;
            }
        }

        return false;
    }

    // Copy energy level when broken
    @Override
    public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos blockPos, IBlockState blockState, int fortune) {
        ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();
        TileEntity te = world.getTileEntity(blockPos);
        ItemStack stack = new ItemStack(world.getBlockState(blockPos).getBlock());

        if (te != null && te instanceof TileEntityKC2Powered) {
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            stack.getTagCompound().setInteger("Energy", ((TileEntityKC2Powered)te).getEnergyStored(null));
            itemStacks.add(stack);
        }

        return itemStacks;
    }

    /*
    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ, int metadata, EntityLivingBase placer) {
        TileEntity te = world.getTileEntity(blockPos);

        if (te != null && te instanceof TileEntityKC2Powered) {
        }

        return metadata;
    }

    // IDismantleable
    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, NBTTagCompound blockTag, World world, int x, int y, int z, boolean returnDrops, boolean simulate) {
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        int metadata = world.getBlockMetadata(x, y, z);
        stacks.add(new ItemStack(getItemDropped(metadata, world.rand, 0), 1, damageDropped(metadata)));

        if(returnDrops && !simulate)
        {
            TileEntity te = world.getTileEntity(x, y, z);

            if(te instanceof IInventory) {
                IInventory invTe = (IInventory)te;
                for(int i = 0; i < invTe.getSizeInventory(); i++) {
                    ItemStack stack = invTe.getStackInSlot(i);
                    if(stack != null) {
                        stacks.add(stack);
                        invTe.setInventorySlotContents(i, null);
                    }
                }
            }
        }

        if(!simulate) {
            world.setBlockToAir(x, y, z);

            if(!returnDrops) {
                for(ItemStack stack: stacks) {
                    CoreUtils.dropItemStackIntoWorldWithVelocity(stack, world, x, y, z);
                }
            }
        }

        return stacks;
    }

    // IInitializer (unused)
    @Override
    public boolean initialize() {
        return false;
    }

    @Override
    public boolean postInit() {
        return false;
    }
    */
}
