package com.dungeonderps.resourcefulbees.registry;

import com.dungeonderps.resourcefulbees.block.HoneycombBlock;
import com.dungeonderps.resourcefulbees.item.BeeJar;
import com.dungeonderps.resourcefulbees.item.ResourcefulHoneycomb;
import com.dungeonderps.resourcefulbees.lib.BeeConstants;
import com.dungeonderps.resourcefulbees.utils.Color;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.event.ColorHandlerEvent;

import static com.dungeonderps.resourcefulbees.ResourcefulBees.LOGGER;

public final class ColorHandler {
    private ColorHandler() {}

    public static void onItemColors(ColorHandlerEvent.Item event) {
        ItemColors colors = event.getItemColors();
        registerItems(colors, ResourcefulHoneycomb::getColor, RegistryHandler.RESOURCEFUL_HONEYCOMB.get());
        registerItems(colors, HoneycombBlock::getItemColor, RegistryHandler.HONEYCOMB_BLOCK_ITEM.get());
        registerItems(colors, BeeJar::getColor, RegistryHandler.BEE_JAR.get());
    }

    public static void onBlockColors(ColorHandlerEvent.Block event){
        BlockColors colors = event.getBlockColors();
        registerBlocks(colors, HoneycombBlock::getBlockColor, RegistryHandler.HONEYCOMB_BLOCK.get());
    }

    private static void registerItems(ItemColors handler, IItemColor itemColor, IItemProvider... items) {
        try {
            handler.register(itemColor, items);
        } catch (NullPointerException ex) {
            LOGGER.error("ItemColor Registration Failed", ex);
        }
    }

    private static void registerBlocks(BlockColors handler, IBlockColor blockColor, Block... blocks) {
        try{
            handler.register(blockColor, blocks);
        } catch (NullPointerException ex) {
            LOGGER.error("BlockColor Registration Failed", ex);
        }
    }

    public static int getItemColor(ItemStack stack, int tintIndex) {
        CompoundNBT honeycombNBT = stack.getChildTag(BeeConstants.NBT_ROOT);
        if (honeycombNBT != null && honeycombNBT.contains(BeeConstants.NBT_COLOR)
                && !honeycombNBT.getString(BeeConstants.NBT_COLOR).isEmpty()
                && !honeycombNBT.getString(BeeConstants.NBT_COLOR).equals(BeeConstants.STRING_DEFAULT_ITEM_COLOR)) {
            return Color.parseInt(honeycombNBT.getString(BeeConstants.NBT_COLOR));
        }
        else {
            return BeeConstants.DEFAULT_ITEM_COLOR;
        }
    }
}
