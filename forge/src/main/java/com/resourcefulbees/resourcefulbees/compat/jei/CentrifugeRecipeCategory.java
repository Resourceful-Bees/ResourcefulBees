package com.resourcefulbees.resourcefulbees.compat.jei;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.resourcefulbees.resourcefulbees.ResourcefulBees;
import com.resourcefulbees.resourcefulbees.recipe.CentrifugeRecipe;
import com.resourcefulbees.resourcefulbees.registry.ModItems;
import com.resourcefulbees.resourcefulbees.tileentity.CentrifugeTileEntity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CentrifugeRecipeCategory extends BaseCategory<CentrifugeRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(ResourcefulBees.MOD_ID, "centrifuge");
    protected final IDrawableAnimated arrow;
    private final IDrawable fluidHider;
    private final IDrawable multiblock;

    private static final ResourceLocation BACKGROUND_IMAGE = new ResourceLocation(ResourcefulBees.MOD_ID, "textures/gui/jei/centrifuge.png");

    public CentrifugeRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, ID,
                I18n.get("gui.resourcefulbees.jei.category.centrifuge"),
                guiHelper.createDrawable(BACKGROUND_IMAGE, 0, 0, 133, 65),
                guiHelper.createDrawableIngredient(new ItemStack(ModItems.CENTRIFUGE_ITEM.get())),
                CentrifugeRecipe.class);

        this.fluidHider = guiHelper.createDrawable(BACKGROUND_IMAGE, 9, 41, 18, 18);
        this.arrow = guiHelper.drawableBuilder(new ResourceLocation(ResourcefulBees.MOD_ID, "textures/gui/jei/centrifuge.png"), 0, 66, 73, 30)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.multiblock = guiHelper.createDrawable(new ResourceLocation(ResourcefulBees.MOD_ID, "textures/gui/jei/icons.png"), 25, 0, 16, 16);
    }

    @Override
    public void setIngredients(CentrifugeRecipe recipe, @NotNull IIngredients iIngredients) {
        List<Pair<ItemStack, Float>> outputs = recipe.itemOutputs;
        List<Pair<FluidStack, Float>> fluidOutput = recipe.fluidOutput;
        List<ItemStack> stacks = new ArrayList<>();
        List<FluidStack> fluids = new ArrayList<>();


        fluids.add(fluidOutput.get(0).getLeft().copy());

        if (outputs.get(0).getLeft().copy().isEmpty()) {
            stacks.add(new ItemStack(Items.STONE));
        } else {
            stacks.add(outputs.get(0).getLeft().copy());
        }
        stacks.add(outputs.get(1).getLeft().copy());
        if (recipe.noBottleInput) {
            iIngredients.setInputIngredients(Lists.newArrayList(recipe.ingredient));
            fluids.add(fluidOutput.get(1).getLeft().copy());
        } else {
            stacks.add(outputs.get(2).getLeft().copy());
            ItemStack bottleStack = new ItemStack(Items.GLASS_BOTTLE, outputs.get(2).getLeft().getCount());
            iIngredients.setInputIngredients(Lists.newArrayList(recipe.ingredient, Ingredient.of(bottleStack)));
        }

        iIngredients.setOutputs(VanillaTypes.ITEM, stacks);
        iIngredients.setOutputs(VanillaTypes.FLUID, fluids);
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, CentrifugeRecipe centrifugeRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = iRecipeLayout.getFluidStacks();

        guiItemStacks.init(CentrifugeTileEntity.HONEYCOMB_SLOT, true, 9, 5);
        guiItemStacks.set(CentrifugeTileEntity.HONEYCOMB_SLOT, iIngredients.getInputs(VanillaTypes.ITEM).get(0));


        if (centrifugeRecipe.hasFluidOutput) {
            if (centrifugeRecipe.itemOutputs.get(0).getLeft().isEmpty()) {
                guiFluidStacks.init(CentrifugeTileEntity.OUTPUT1, false, 109, 6, 16, 16, iIngredients.getOutputs(VanillaTypes.FLUID).get(0).get(0).getAmount(), true, null);
                guiFluidStacks.set(CentrifugeTileEntity.OUTPUT1, iIngredients.getOutputs(VanillaTypes.FLUID).get(0));
            } else {
                guiItemStacks.init(CentrifugeTileEntity.OUTPUT1, false, 108, 5);
                guiItemStacks.set(CentrifugeTileEntity.OUTPUT1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
                guiFluidStacks.init(5, false, 109, 42, 16, 16, iIngredients.getOutputs(VanillaTypes.FLUID).get(0).get(0).getAmount(), true, null);
                guiFluidStacks.set(5, iIngredients.getOutputs(VanillaTypes.FLUID).get(0));
            }
        } else {
            guiItemStacks.init(CentrifugeTileEntity.OUTPUT1, false, 108, 5);
            guiItemStacks.init(CentrifugeTileEntity.HONEY_BOTTLE, false, 59, 44);
            guiItemStacks.set(CentrifugeTileEntity.OUTPUT1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
        }
        if (centrifugeRecipe.noBottleInput) {
            guiFluidStacks.init(CentrifugeTileEntity.HONEY_BOTTLE, false, 60, 45, 16, 16, iIngredients.getOutputs(VanillaTypes.FLUID).get(1).get(0).getAmount(), true, null);
            guiFluidStacks.set(CentrifugeTileEntity.HONEY_BOTTLE, iIngredients.getOutputs(VanillaTypes.FLUID).get(1));
        } else {
            guiItemStacks.init(CentrifugeTileEntity.BOTTLE_SLOT, true, 9, 23);
            guiItemStacks.set(CentrifugeTileEntity.BOTTLE_SLOT, iIngredients.getInputs(VanillaTypes.ITEM).get(1));
            guiItemStacks.init(CentrifugeTileEntity.HONEY_BOTTLE, false, 59, 44);
            guiItemStacks.set(CentrifugeTileEntity.HONEY_BOTTLE, iIngredients.getOutputs(VanillaTypes.ITEM).get(2));
        }
        guiItemStacks.init(CentrifugeTileEntity.OUTPUT2, false, 108, 23);
        guiItemStacks.set(CentrifugeTileEntity.OUTPUT2, iIngredients.getOutputs(VanillaTypes.ITEM).get(1));

    }

    @Override
    public void draw(CentrifugeRecipe recipe, @NotNull PoseStack matrix, double mouseX, double mouseY) {
        this.arrow.draw(matrix, 31, 14);

        final float beeOutput = recipe.itemOutputs.get(0).getRight();
        final float beeswax = recipe.itemOutputs.get(1).getRight();
        final float honeyBottle = recipe.itemOutputs.size() < 3 ? recipe.fluidOutput.get(1).getRight() : recipe.itemOutputs.get(2).getRight();
        final float fluid = recipe.fluidOutput.get(0).getRight();

        DecimalFormat decimalFormat = new DecimalFormat("##%");

        String honeyBottleString = decimalFormat.format(honeyBottle);
        String beeOutputString = decimalFormat.format(beeOutput);
        String beeswaxString = decimalFormat.format(beeswax);
        String fluidString = decimalFormat.format(fluid);

        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        int honeyBottleOffset = fontRenderer.width(honeyBottleString) / 2;
        int beeOutputOffset = fontRenderer.width(beeOutputString) / 2;
        int beeswaxOffset = fontRenderer.width(beeswaxString) / 2;
        int fluidOffset = fontRenderer.width(fluidString) / 2;

        if (beeOutput < 1.0)
            fontRenderer.draw(matrix, beeOutputString, (float) 95 - beeOutputOffset, (float) 10, 0xff808080);
        if (beeswax < 1.0) fontRenderer.draw(matrix, beeswaxString, (float) 95 - beeswaxOffset, (float) 30, 0xff808080);
        if (fluid < 1.0 && !(recipe.hasFluidOutput && recipe.itemOutputs.get(0).getLeft().isEmpty()))
            fontRenderer.draw(matrix, fluidString, (float) 95 - fluidOffset, (float) 46, 0xff808080);
        if (honeyBottle < 1.0)
            fontRenderer.draw(matrix, honeyBottleString, (float) 45 - honeyBottleOffset, (float) 49, 0xff808080);
        if (recipe.multiblock) {
            multiblock.draw(matrix, 10, 45);
        }
        if (!recipe.hasFluidOutput || (recipe.hasFluidOutput && recipe.itemOutputs.get(0).getLeft().isEmpty())) {
            this.fluidHider.draw(matrix, 108, 41);
        }
    }

    @Override
    public @NotNull List<Component> getTooltipStrings(@NotNull CentrifugeRecipe recipe, double mouseX, double mouseY) {
        if (mouseX >= 10 && mouseX <= 26 && mouseY >= 45 && mouseY <= 61) {
            return Collections.singletonList(new TextComponent("Multiblock only recipe."));
        }
        return super.getTooltipStrings(recipe, mouseX, mouseY);
    }
}