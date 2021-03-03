package com.resourcefulbees.resourcefulbees.client.gui.screen.beepedia.pages;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.resourcefulbees.resourcefulbees.ResourcefulBees;
import com.resourcefulbees.resourcefulbees.api.beedata.CustomBeeData;
import com.resourcefulbees.resourcefulbees.client.gui.screen.beepedia.BeepediaScreen;
import com.resourcefulbees.resourcefulbees.client.gui.screen.beepedia.pages.mutations.BlockMutationPage;
import com.resourcefulbees.resourcefulbees.client.gui.screen.beepedia.pages.mutations.EntityMutationPage;
import com.resourcefulbees.resourcefulbees.client.gui.screen.beepedia.pages.mutations.ItemMutationPage;
import com.resourcefulbees.resourcefulbees.client.gui.screen.beepedia.pages.mutations.MutationsPage;
import com.resourcefulbees.resourcefulbees.lib.MutationTypes;
import com.resourcefulbees.resourcefulbees.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class MutationListPage extends BeeDataPage {

    List<Pair<MutationTypes, List<MutationsPage>>> mutations = new ArrayList<>();

    private List<MutationsPage> activeList = null;
    int tab;
    private int page;
    private MutationsPage activePage = null;

    public ResourceLocation mutationImage = new ResourceLocation(ResourcefulBees.MOD_ID, "textures/gui/beepedia/mutate.png");

    Button prevTab;
    Button nextTab;
    Button leftArrow;
    Button rightArrow;
    private ResourceLocation mutationChanceImage = new ResourceLocation(ResourcefulBees.MOD_ID, "textures/gui/beepedia/mutation_sparkles.png");

    public MutationListPage(BeepediaScreen beepedia, CustomBeeData beeData, int xPos, int yPos, BeePage parent) {
        super(beepedia, beeData, xPos, yPos, parent);
        prevTab = new ImageButton(xPos + (SUB_PAGE_WIDTH / 2) - 48, yPos + 6, 8, 11, 0, 0, 11, arrowImage, 16, 33, button -> prevTab());
        nextTab = new ImageButton(xPos + (SUB_PAGE_WIDTH / 2) + 40, yPos + 6, 8, 11, 8, 0, 11, arrowImage, 16, 33, button -> nextTab());
        leftArrow = new ImageButton(xPos + (SUB_PAGE_WIDTH / 2) - 28, yPos + SUB_PAGE_HEIGHT - 16, 8, 11, 0, 0, 11, arrowImage, 16, 33, button -> prevPage());
        rightArrow = new ImageButton(xPos + (SUB_PAGE_WIDTH / 2) + 20, yPos + SUB_PAGE_HEIGHT - 16, 8, 11, 8, 0, 11, arrowImage, 16, 33, button -> nextPage());
        beepedia.addButton(prevTab);
        beepedia.addButton(nextTab);
        beepedia.addButton(leftArrow);
        beepedia.addButton(rightArrow);
        prevTab.visible = false;
        nextTab.visible = false;
        leftArrow.visible = false;
        rightArrow.visible = false;
        List<MutationsPage> blockMutations = new ArrayList<>();
        List<MutationsPage> itemMutations = new ArrayList<>();
        List<MutationsPage> entityMutations = new ArrayList<>();
        if (beeData.getMutationData().hasBlockMutations()) {
            beeData.getMutationData().getJeiBlockMutations().forEach((b, m) -> blockMutations.add(new BlockMutationPage(b, m, MutationTypes.BLOCK, beeData, beepedia)));
            beeData.getMutationData().getJeiBlockTagMutations().forEach((b, m) -> blockMutations.add(new BlockMutationPage(b, m, MutationTypes.BLOCK, beeData, beepedia)));
        }
        if (beeData.getMutationData().hasItemMutations()) {
            beeData.getMutationData().getJeiItemMutations().forEach((b, m) -> itemMutations.add(new ItemMutationPage(b, m, MutationTypes.ITEM, beeData, beepedia)));
            beeData.getMutationData().getJeiBlockTagItemMutations().forEach((b, m) -> itemMutations.add(new ItemMutationPage(b, m, MutationTypes.ITEM, beeData, beepedia)));
        }
        if (beeData.getMutationData().hasEntityMutations()) {
            beeData.getMutationData().getEntityMutations().forEach((b, m) -> entityMutations.add(new EntityMutationPage(b, m, MutationTypes.ITEM, beeData, beepedia)));
        }
        if (!blockMutations.isEmpty()) {
            mutations.add(Pair.of(MutationTypes.BLOCK, blockMutations));
        }
        if (!itemMutations.isEmpty()) {
            mutations.add(Pair.of(MutationTypes.ITEM, itemMutations));
        }
        if (!entityMutations.isEmpty()) {
            mutations.add(Pair.of(MutationTypes.ENTITY, entityMutations));
        }
    }

    private void nextPage() {
        if (activeList == null) return;
        page++;
        if (page >= activeList.size()) page = 0;
        activePage = activeList.get(page);
        BeepediaScreen.currScreenState.setMutationsPage(page);
    }

    private void prevPage() {
        if (activeList == null) return;
        page--;
        if (page < 0) page = activeList.size() - 1;
        activePage = activeList.get(page);
        BeepediaScreen.currScreenState.setMutationsPage(page);
    }

    private void nextTab() {
        tab++;
        if (tab >= mutations.size()) tab = 0;
        BeepediaScreen.currScreenState.setCurrentMutationTab(tab);
        activeList = mutations.get(tab).getRight();
        selectPage(activeList);
    }

    private void prevTab() {
        tab--;
        if (tab < 0) tab = mutations.size() - 1;
        BeepediaScreen.currScreenState.setCurrentMutationTab(tab);
        activeList = mutations.get(tab).getRight();
        selectPage(activeList);
    }

    @Override
    public void renderBackground(MatrixStack matrix, float partialTick, int mouseX, int mouseY) {
        FontRenderer font = Minecraft.getInstance().fontRenderer;
        TranslationTextComponent title;
        switch (mutations.get(BeepediaScreen.currScreenState.getCurrentMutationTab()).getLeft()) {
            case BLOCK:
                title = new TranslationTextComponent("gui.resourcefulbees.beepedia.bee_subtab.mutations.block");
                break;
            case ENTITY:
                title = new TranslationTextComponent("gui.resourcefulbees.beepedia.bee_subtab.mutations.entity");
                break;
            case ITEM:
                title = new TranslationTextComponent("gui.resourcefulbees.beepedia.bee_subtab.mutations.item");
                break;
            default:
                throw new UnsupportedOperationException(String.format("found a legacy mutation. %s", BeepediaScreen.currScreenState.getCurrentMutationTab()));
        }

        if (activePage != null) {
            int padding = font.getWidth(title) / 2;
            font.draw(matrix, title, (float) xPos + ((float) SUB_PAGE_WIDTH / 2) - padding, (float) yPos + 8, TextFormatting.WHITE.getColor());
            StringTextComponent mutationCount = new StringTextComponent("x " + beeData.getMutationData().getMutationCount());
            font.draw(matrix, mutationCount, (float) xPos + 20, (float) yPos + 26, TextFormatting.GRAY.getColor());
            Minecraft.getInstance().getTextureManager().bindTexture(mutationImage);
            AbstractGui.drawTexture(matrix, xPos, yPos + 22, 0, 0, 169, 84, 169, 84);
            Minecraft.getInstance().getTextureManager().bindTexture(mutationChanceImage);
            AbstractGui.drawTexture(matrix, xPos, yPos + 22, 0, 0, 16, 16, 16, 16);
            activePage.draw(matrix, xPos, yPos + 22);
            RenderUtils.renderEntity(matrix, parent.bee, beepedia.getMinecraft().world, xPos + SUB_PAGE_WIDTH / 2 - 15, yPos + 28, 45, 1.25f);
            if (activeList.size() > 1) {
                StringTextComponent page = new StringTextComponent(String.format("%d / %d", this.page + 1, activeList.size()));
                padding = font.getWidth(page) / 2;
                font.draw(matrix, page, (float) xPos + ((float) SUB_PAGE_WIDTH / 2) - padding, (float) yPos + SUB_PAGE_HEIGHT - 14, TextFormatting.WHITE.getColor());
            }
        }
    }

    @Override
    public void openPage() {
        super.openPage();
        // get current Tab
        tab = BeepediaScreen.currScreenState.getCurrentMutationTab();
        if (tab >= mutations.size()) tab = 0;
        BeepediaScreen.currScreenState.setCurrentMutationTab(tab);
        activeList = mutations.get(tab).getRight();

        // get current page
        selectPage(activeList);
        prevTab.visible = mutations.size() > 1;
        nextTab.visible = mutations.size() > 1;
    }

    private void selectPage(List<MutationsPage> activeList) {
        if (activeList != null) {
            page = BeepediaScreen.currScreenState.getMutationsPage();
            if (page >= activeList.size()) page = 0;
            if (activeList.isEmpty()) activePage = null;
            else activePage = activeList.get(page);
            BeepediaScreen.currScreenState.setMutationsPage(page);
            leftArrow.visible = activeList.size() > 1;
            rightArrow.visible = activeList.size() > 1;
        }
    }

    @Override
    public void closePage() {
        super.closePage();
        prevTab.visible = false;
        nextTab.visible = false;
        leftArrow.visible = false;
        rightArrow.visible = false;
    }

    @Override
    public void tick(int ticksActive) {
        if (activePage != null) activePage.tick(ticksActive);
    }

    @Override
    public void drawTooltips(MatrixStack matrix, int mouseX, int mouseY) {
        if (activePage != null) activePage.drawTooltips(matrix, xPos, yPos + 22, mouseX, mouseY);
        if (BeepediaScreen.mouseHovering(xPos + SUB_PAGE_WIDTH / 2 - 20, yPos + 28, 30, 30, mouseX, mouseY)) {
            List<ITextComponent> tooltip = new ArrayList<>();
            IFormattableTextComponent name = parent.bee.getName().copy();
            IFormattableTextComponent id = new StringTextComponent(parent.bee.getEntityString()).formatted(TextFormatting.DARK_GRAY);
            tooltip.add(name);
            tooltip.add(id);
            beepedia.renderTooltip(matrix, tooltip, mouseX, mouseY);
        }
        if (BeepediaScreen.mouseHovering(xPos, yPos + 22, 16, 16, mouseX, mouseY)) {
            TranslationTextComponent text = new TranslationTextComponent("gui.resourcefulbees.beepedia.bee_subtab.mutations.mutation_count.tooltip");
            beepedia.renderTooltip(matrix, text, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (activePage != null) return activePage.mouseClick(xPos, yPos + 22, (int) mouseX, (int) mouseY);
        return false;
    }
}