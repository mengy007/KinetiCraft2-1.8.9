package com.techmafia.mcmods.KinetiCraft2.blocks;

import com.techmafia.mcmods.KinetiCraft2.creativetab.CreativeTabKC2;
import com.techmafia.mcmods.KinetiCraft2.tileentities.TileEntityKC2EnderKineticEnergyPylon;
import com.techmafia.mcmods.KinetiCraft2.tileentities.TileEntityKC2EnderKineticGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;

/**
 * Created by myang on 11/5/15.
 */
public class BlockKC2EnderKineticEnergyPylon extends Block {

    public BlockKC2EnderKineticEnergyPylon(Material material) {
        super(material);
        setCreativeTab(CreativeTabKC2.KC2_TAB);
        setStepSound(soundTypeMetal);
        setHardness(0.1f);
        setUnlocalizedName("kc2EnderKineticEnergyPylon");
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState) {
        return new TileEntityKC2EnderKineticEnergyPylon();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            TileEntity te = world.getTileEntity(blockPos);
            if (te != null && te instanceof TileEntityKC2EnderKineticEnergyPylon) {
                if (((TileEntityKC2EnderKineticEnergyPylon) te).generatorTileEntity != null) {
                    TileEntityKC2EnderKineticGenerator generatorTileEntity = ((TileEntityKC2EnderKineticEnergyPylon) te).generatorTileEntity;
                    entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Feeding Kinetic Generator at x: " + generatorTileEntity.getPos().getX() + " y: " + generatorTileEntity.getPos().getY() + " z: " + generatorTileEntity.getPos().getZ()));
                } else {
                    entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "No Ender Kinetic Generator in range."));
                }

            }
        }
        return false;
    }
}
