package com.techmafia.mcmods.KinetiCraft2.blocks;

import com.techmafia.mcmods.KinetiCraft2.creativetab.CreativeTabKC2;
import com.techmafia.mcmods.KinetiCraft2.init.KinetiCraft2Items;
import com.techmafia.mcmods.KinetiCraft2.reference.Reference;
import com.techmafia.mcmods.KinetiCraft2.utility.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
//import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//import net.minecraft.util.IIcon;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * Created by Meng on 10/16/2015.
 */
public class BlockKC2KineticBlock extends Block {
    public BlockKC2KineticBlock() {
        super(Material.ground);
        this.setCreativeTab(CreativeTabKC2.KC2_TAB);
        this.setUnlocalizedName("kineticBlock");
        this.setHardness(0.5f);
        this.setStepSound(Block.soundTypeGravel);
    }

    //
    // Render stuff
    //
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() { return EnumWorldBlockLayer.SOLID; }
    @Override
    public boolean isOpaqueCube() { return true; }
    @Override
    public boolean isFullCube() { return true; }
    @Override
    public int getRenderType() { return 3; }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    //
    // Interaction stuff
    //
    /**
     * This makes simply breaking the block drop 1 dust
     * @param blockState
     * @param random
     * @param fortune
     * @return
     */
    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int fortune) {
        return KinetiCraft2Items.kineticDust;
    }


    /**
     * Triggered whenever an entity collides with this block (enters into the block)
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
        if ( ! worldIn.isRemote && entityIn instanceof EntityPlayerMP) {
            this.makeKineticDust(worldIn, pos, 2);
        }
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        if ( ! worldIn.isRemote && entityIn instanceof EntityPlayerMP) {
            this.makeKineticDust(worldIn, pos, 2);
        }
    }

    /**
     * Drop some kinetic dust in the world
     * @param world
     * @param blockPos
     * @param amount
     */
    public void makeKineticDust(World world, BlockPos blockPos, int amount) {
        world.destroyBlock(blockPos, false);
        world.spawnEntityInWorld(new EntityItem(world, blockPos.getX()+0.5D, blockPos.getY()+0.5D, blockPos.getZ()+0.5D, new ItemStack(KinetiCraft2Items.kineticDust, amount)));
    }
}