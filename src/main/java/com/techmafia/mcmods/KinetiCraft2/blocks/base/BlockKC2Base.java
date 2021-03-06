package com.techmafia.mcmods.KinetiCraft2.blocks.base;

import com.techmafia.mcmods.KinetiCraft2.creativetab.CreativeTabKC2;
import com.techmafia.mcmods.KinetiCraft2.reference.Reference;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Meng on 7/27/2015.
 */
public class BlockKC2Base extends BlockContainer {
    public BlockKC2Base(Material material) {
        super(material);
        this.setHardness(0.1f);
        this.setCreativeTab(CreativeTabKC2.KC2_TAB);
    }

    public BlockKC2Base(String blockName, Material material) {
        super(material);
        this.setUnlocalizedName(blockName);
        this.setHardness(0.2f);
        this.setCreativeTab(CreativeTabKC2.KC2_TAB);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(Reference.MOD_NAME + ":" + this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(":") + 1));
    }
    */

    /**
     * Required override
     **/
    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return null;
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    @Override
    public int getRenderType() {
        return super.getRenderType();
    }
}
