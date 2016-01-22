package com.techmafia.mcmods.KinetiCraft2.blocks;

import com.techmafia.mcmods.KinetiCraft2.creativetab.CreativeTabKC2;
import com.techmafia.mcmods.KinetiCraft2.init.KinetiCraft2Items;
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
 * Created by myang on 11/4/15.
 */
public class BlockKC2EnderKineticBlock extends Block {
    //private IIcon blockIcon;

    public BlockKC2EnderKineticBlock() {
        super(Material.ground);
        this.setCreativeTab(CreativeTabKC2.KC2_TAB);
        this.setUnlocalizedName("enderKineticBlock");
        this.setHardness(0.5f);
        this.setStepSound(Block.soundTypeGravel);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    /*
    //
    // Icon stuff
    //
    @Override
    public IIcon getIcon(int size, int metadata) {
        return this.blockIcon;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(Reference.TEXTURE_NAME_PREFIX + "enderKineticBlock");
    }
    */

    //
    // Interaction stuff
    //
    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) { return KinetiCraft2Items.enderKineticDust; }

    /**
     * Triggered whenever an entity collides with this block (enters into the block)
     */
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, Entity entity) {
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
        world.spawnEntityInWorld(new EntityItem(world, blockPos.getX()+0.5D, blockPos.getY()+0.5D, blockPos.getZ()+0.5D, new ItemStack(KinetiCraft2Items.enderKineticDust, amount)));
    }
}
