package com.resourcefulbees.resourcefulbees.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.resourcefulbees.resourcefulbees.ResourcefulBees;
import com.resourcefulbees.resourcefulbees.api.IBeeRegistry;
import com.resourcefulbees.resourcefulbees.compat.jei.ingredients.EntityIngredient;
import com.resourcefulbees.resourcefulbees.lib.BeeConstants;
import com.resourcefulbees.resourcefulbees.lib.MutationTypes;
import com.resourcefulbees.resourcefulbees.registry.BeeRegistry;
import com.resourcefulbees.resourcefulbees.registry.ModItems;
import com.resourcefulbees.resourcefulbees.utils.BeeInfoUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class BlockToFluid implements IRecipeCategory<BlockToFluid.Recipe> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(ResourcefulBees.MOD_ID, "textures/gui/jei/beemutation.png");
    public static final ResourceLocation ICONS = new ResourceLocation(ResourcefulBees.MOD_ID, "textures/gui/jei/icons.png");
    public static final ResourceLocation ID = new ResourceLocation(ResourcefulBees.MOD_ID, "block_to_fluid_mutation");
    private static final IBeeRegistry BEE_REGISTRY = BeeRegistry.getRegistry();
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable info;
    private final IDrawable beeHive;
    private final String localizedName;

    public static final Logger LOGGER = LogManager.getLogger();

    public BlockToFluid(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GUI_BACK, -12, 0, 99, 75).addPadding(0, 0, 0, 0).build();
        this.icon = guiHelper.createDrawable(ICONS, 0, 0, 16, 16);
        this.info = guiHelper.createDrawable(ICONS, 16, 0, 9, 9);
        this.beeHive = guiHelper.createDrawableIngredient(new ItemStack(ModItems.T1_BEEHIVE_ITEM.get()));
        this.localizedName = I18n.format("gui.resourcefulbees.jei.category.block_to_fluid_mutation");
    }

    public static List<BlockToFluid.Recipe> getMutationRecipes(IIngredientManager ingredientManager) {
        List<BlockToFluid.Recipe> recipes = new ArrayList<>();

        BEE_REGISTRY.getBees().forEach(((s, beeData) -> {
            if (beeData.getMutationData().hasMutation()) {
                beeData.getMutationData().iBlockTagMutations.forEach((t, m) -> {
                    if (m.type == MutationTypes.BLOCK_TO_FLUID) {
                        ITag<Item> tag = BeeInfoUtils.getItemTag(m.mutationData.inputID.toLowerCase().replace(BeeConstants.TAG_PREFIX, ""));
                        Fluid output = BeeInfoUtils.getFluid(m.mutationData.outputID);
                        if (tag != null && output != null) {
                            recipes.add(new Recipe(tag, new FluidStack(output, 1000), beeData.getName(), m.type, m.chance, true));
                        } else if (tag != null && output == null) {
                            LOGGER.warn(String.format("Fluid output: [%s] is not valid.", m.mutationData.outputID));
                        } else {
                            LOGGER.warn(String.format("Block Tag: [%s] does not have Item Tag equivalent", m.mutationData.inputID));
                        }
                    }
                });
                beeData.getMutationData().iBlockMutations.forEach((b, m) -> {
                    if (m.type == MutationTypes.BLOCK_TO_FLUID) {
                        Item input = BeeInfoUtils.getItem(m.mutationData.inputID);
                        Fluid output = BeeInfoUtils.getFluid(m.mutationData.outputID);
                        if (input == null || output == null) {
                            LOGGER.warn(String.format("One or both of the following values are not valid: [%s, %s]", m.mutationData.inputID, m.mutationData.outputID));
                        } else {
                            recipes.add(new Recipe(new ItemStack(input), new FluidStack(output, 1000), beeData.getName(), m.type, m.chance, false));
                        }
                    }
                });
            }
        }));

        return recipes;
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends BlockToFluid.Recipe> getRecipeClass() {
        return BlockToFluid.Recipe.class;
    }

    @Override
    public String getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(BlockToFluid.Recipe recipe, IIngredients ingredients) {
        if (recipe.isAcceptsAny()) {
            if (MutationTypes.BLOCK_TO_FLUID.equals(recipe.mutationType)) {
                List<Ingredient> list = new ArrayList<>();
                Ingredient ing = Ingredient.fromTag(recipe.tag);
                list.add(ing);
                ingredients.setInputIngredients(list);
                ingredients.setOutput(VanillaTypes.FLUID, recipe.fluidOut);
            }
        } else {
            if (MutationTypes.BLOCK_TO_FLUID.equals(recipe.mutationType)) {
                ingredients.setInput(VanillaTypes.ITEM, recipe.itemIn);
                ingredients.setOutput(VanillaTypes.FLUID, recipe.fluidOut);
            }
        }
        ingredients.setInput(JEICompat.ENTITY_INGREDIENT, new EntityIngredient(recipe.beeType, -45.0f));
    }

    @Override
    public List<ITextComponent> getTooltipStrings(Recipe recipe, double mouseX, double mouseY) {
        double infoX = 63D;
        double infoY = 8D;
        if (mouseX >= infoX && mouseX <= infoX + 9D && mouseY >= infoY && mouseY <= infoY + 9D) {
            return Collections.singletonList(new StringTextComponent(I18n.format("gui." + ResourcefulBees.MOD_ID + ".jei.category.mutation.info")));
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, BlockToFluid.Recipe recipe, IIngredients ingredients) {
        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();
        itemStacks.init(0, true, 15, 57);
        itemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

        IGuiFluidStackGroup fluidStacks = iRecipeLayout.getFluidStacks();
        fluidStacks.init(0, false, 66, 49);
        fluidStacks.set(0, ingredients.getOutputs(VanillaTypes.FLUID).get(0));

        IGuiIngredientGroup<EntityIngredient> ingredientStacks = iRecipeLayout.getIngredientsGroup(JEICompat.ENTITY_INGREDIENT);
        ingredientStacks.init(0, true, 16, 10);
        ingredientStacks.set(0, ingredients.getInputs(JEICompat.ENTITY_INGREDIENT).get(0));
    }

    @Override
    public void draw(BlockToFluid.Recipe recipe, MatrixStack stack, double mouseX, double mouseY) {
        this.beeHive.draw(stack, 65, 10);
        this.info.draw(stack, 63, 8);
    }

    public static class Recipe {
        private final FluidStack fluidOut;
        private final ItemStack itemIn;
        private final String beeType;

        private final boolean acceptsAny;
        private final ITag<Item> tag;
        private final float chance;

        private final MutationTypes mutationType;

        public Recipe(ItemStack baseBlock, FluidStack mutationBlock, String beeType, MutationTypes type, float chance, boolean acceptsAny) {
            this.fluidOut = mutationBlock;
            this.itemIn = baseBlock;
            this.beeType = beeType;
            this.mutationType = type;
            this.acceptsAny = acceptsAny;
            this.chance = chance;
            this.tag = null;
        }

        //TAGS!!!
        public Recipe(ITag<Item> baseBlock, FluidStack mutationBlock, String beeType, MutationTypes type, float chance, boolean acceptsAny) {
            this.fluidOut = mutationBlock;
            this.itemIn = null;
            this.beeType = beeType;
            this.mutationType = type;
            this.acceptsAny = acceptsAny;
            this.chance = chance;
            this.tag = baseBlock;
        }


        public boolean isAcceptsAny() {
            return acceptsAny;
        }

        public ITag<?> getTag() {
            return tag;
        }

        public String getBeeType() {
            return this.beeType;
        }
    }
}
