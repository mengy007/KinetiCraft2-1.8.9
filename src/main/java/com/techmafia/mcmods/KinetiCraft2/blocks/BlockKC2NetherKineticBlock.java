package com.techmafia.mcmods.KinetiCraft2.blocks;

import com.techmafia.mcmods.KinetiCraft2.creativetab.CreativeTabKC2;
import com.techmafia.mcmods.KinetiCraft2.init.KinetiCraft2Items;
import com.techmafia.mcmods.KinetiCraft2.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by myang on 11/5/15.
 */
public class BlockKC2NetherKineticBlock extends Block {
    public BlockKC2NetherKineticBlock() {
        super(Material.ground);
        this.setCreativeTab(CreativeTabKC2.KC2_TAB);
        this.setUnlocalizedName("netherKineticBlock");
        this.setHardness(0.5f);
        this.setStepSound(Block.soundTypeGravel);
    }

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
        return KinetiCraft2Items.netherKineticDust;
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    public void onEntityWalking(World world, BlockPos blockPos, Entity entity) {
        if ( ! world.isRemote && entity instanceof EntityPlayerMP) {
            this.makeKineticDust(world, blockPos, 2);
        }
    }

    /**
     * Block's chance to react to an entity falling on it.
     */
    public void onFallenUpon(World world, BlockPos blockPos, Entity entity, float speed) {
        if ( ! world.isRemote && entity instanceof EntityPlayerMP) {
            this.makeKineticDust(world, blockPos, 2);
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
        world.spawnEntityInWorld(new EntityItem(world, blockPos.getX()+0.5D, blockPos.getY()+0.5D, blockPos.getZ()+0.5D, new ItemStack(KinetiCraft2Items.netherKineticDust, amount)));
    }
}