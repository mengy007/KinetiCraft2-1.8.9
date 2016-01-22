package com.techmafia.mcmods.KinetiCraft2.blocks;

import com.techmafia.mcmods.KinetiCraft2.creativetab.CreativeTabKC2;
import com.techmafia.mcmods.KinetiCraft2.tileentities.TileEntityKC2KineticGenerator;
import com.techmafia.mcmods.KinetiCraft2.tileentities.base.TileEntityKC2Powered;
import com.techmafia.mcmods.KinetiCraft2.utility.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Meng on 10/18/2015.
 */
public class BlockKC2KineticGenerator extends Block {
    public int powerFromWalkedOn = 5;
    public int powerFromJumpedOn = 10;

    //IIcon sideIcon;
    //IIcon topIcon;

    public BlockKC2KineticGenerator(Material material) {
        super(material);
        setCreativeTab(CreativeTabKC2.KC2_TAB);
        setStepSound(soundTypeMetal);
        setHardness(0.1f);
        setUnlocalizedName("kc2KineticGenerator");
        //setBlockName("kc2KineticGenerator");
        //setBlockTextureName(Reference.MOD_NAME + ":" + this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(":") + 1));
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(Reference.MOD_NAME + ":" + this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(":") + 1));
        this.sideIcon = par1IconRegister.registerIcon(Reference.MOD_NAME + ":" + this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(":") + 1) + "_side");
        this.topIcon = par1IconRegister.registerIcon(Reference.MOD_NAME + ":" + this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(":") + 1) + "_top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (side == 1) {
            return topIcon;
        } else {
            return sideIcon;
        }
    }
    */

    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState) {
        return new TileEntityKC2KineticGenerator();
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", 0));
        list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", 50000));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(blockPos);
        if (te == null) {
            return false;
        } else {
            if (entityPlayer.inventory.getCurrentItem() == null) {
                // Return stored power if bare hand
                if (world.isRemote) {
                    entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "" + ((TileEntityKC2Powered) te).getEnergyStored(null) + " / " + ((TileEntityKC2Powered) te).getMaxEnergyStored(null) + " RF"));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block)
     */
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, Entity entity) {
        if ( ! world.isRemote && entity instanceof EntityPlayerMP) {
            TileEntity te = world.getTileEntity(blockPos);

            if (te != null && te instanceof TileEntityKC2Powered) {
                ((TileEntityKC2Powered)te).receiveEnergy(null, powerFromWalkedOn, false);
                world.markBlockForUpdate(blockPos);
            }
        }
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World world, BlockPos blockPos, Entity entity, float fallDistance) {
        if ( ! world.isRemote && entity instanceof EntityPlayerMP) {
            TileEntity te = world.getTileEntity(blockPos);

            if (te != null && te instanceof TileEntityKC2Powered) {
                ((TileEntityKC2Powered)te).receiveEnergy(null, powerFromJumpedOn, false);
                world.markBlockForUpdate(blockPos);
            }
        }
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
    */
}
