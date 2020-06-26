package com.dungeonderps.resourcefulbees.loot.function;

import com.dungeonderps.resourcefulbees.lib.BeeConst;
import com.dungeonderps.resourcefulbees.registry.RegistryHandler;
import com.dungeonderps.resourcefulbees.tileentity.HoneycombTileEntity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

import javax.annotation.Nonnull;

public class BlockItemFunction extends LootFunction {

    protected BlockItemFunction(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected ItemStack doApply(@Nonnull ItemStack stack, LootContext context) {
        TileEntity tile = context.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof HoneycombTileEntity){
            HoneycombTileEntity blockEntity = (HoneycombTileEntity)tile;
            ItemStack blockItem = new ItemStack(RegistryHandler.HONEYCOMB_BLOCK_ITEM.get());
            blockItem.getOrCreateChildTag(BeeConst.NBT_ROOT).putString(BeeConst.NBT_BEE_TYPE, blockEntity.beeType);
            blockItem.getOrCreateChildTag(BeeConst.NBT_ROOT).putString(BeeConst.NBT_COLOR, blockEntity.blockColor);
            if (blockEntity.beeType.equals("") && blockEntity.blockColor.equals(""))
                return ItemStack.EMPTY;
            else
                return blockItem;
        }
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<BlockItemFunction> {

        public Serializer() {
            super(new ResourceLocation("resourcefulbees:blockitem"), BlockItemFunction.class);
        }

        @Nonnull
        @Override
        public BlockItemFunction deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull ILootCondition[] conditionsIn) {
            return new BlockItemFunction(conditionsIn);
        }
    }
}
