package com.techmafia.mcmods.KinetiCraft2.blocks.itemblocks;

import com.techmafia.mcmods.KinetiCraft2.blocks.itemblocks.base.ItemBlockKC2Powered;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * Created by myang on 11/5/15.
 */
public class ItemBlockKC2ThermalKineticGenerator extends ItemBlockKC2Powered {
    int capacity = 1000000;

    public ItemBlockKC2ThermalKineticGenerator(Block block) {
        super(block);
    }

    @Override
    public int getMaxEnergyStored(ItemStack itemStack) {
        return capacity;
    }
}
