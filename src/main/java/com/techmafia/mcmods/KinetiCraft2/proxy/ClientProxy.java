package com.techmafia.mcmods.KinetiCraft2.proxy;

import com.techmafia.mcmods.KinetiCraft2.init.KinetiCraft2Blocks;
import com.techmafia.mcmods.KinetiCraft2.init.KinetiCraft2Items;
import com.techmafia.mcmods.KinetiCraft2.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Created by Meng on 7/31/2015.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void registerClientStuff() {
        //TileEntitySpecialRenderer kineticEnergyCubeSpecialRenderer = new TileEntityRendererKC2KineticEnergyCube();
        //TileEntitySpecialRenderer hardenedKineticEnergyCubeSpecialRenderer = new TileEntityRendererKC2HardenedKineticEnergyCube();

        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKC2KineticEnergyCube.class, kineticEnergyCubeSpecialRenderer);
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKC2HardenedKineticEnergyCube.class, hardenedKineticEnergyCubeSpecialRenderer);

        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(KinetiCraft2Blocks.kineticEnergyCube), new TileEntityItemRendererKC2Base(kineticEnergyCubeSpecialRenderer, new TileEntityKC2KineticEnergyCube()));
        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(KinetiCraft2Blocks.kineticEnergyCube), new TileEntityItemRendererKC2Base(hardenedKineticEnergyCubeSpecialRenderer, new TileEntityKC2HardenedKineticEnergyCube()));
    }

    @Override
    public void init() {
        final int DEFAULT_ITEM_SUBTYPE = 0;

        /**
         * Items
         */
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(KinetiCraft2Items.kineticDust, DEFAULT_ITEM_SUBTYPE, new ModelResourceLocation("kineticraft2:kineticDust", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(KinetiCraft2Items.kineticIngot, DEFAULT_ITEM_SUBTYPE, new ModelResourceLocation("kineticraft2:kineticIngot", "inventory"));

        /**
         * Blocks
         */
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(GameRegistry.findItem(Reference.MOD_ID, "kineticBlock"), DEFAULT_ITEM_SUBTYPE, new ModelResourceLocation("kineticraft2:kineticBlock", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(GameRegistry.findItem(Reference.MOD_ID, "kineticEnergyCube"), DEFAULT_ITEM_SUBTYPE, new ModelResourceLocation("kineticraft2:kineticEnergyCube", "inventory"));
    }
}
