package io.github.phantamanta44.threng.integration.jei;

import crafttweaker.annotations.ModOnly;
import io.github.phantamanta44.threng.client.gui.GuiEnergizer;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.integration.jei.base.ThrEngJeiCategory;
import io.github.phantamanta44.threng.recipe.EnergizeRecipe;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.TickTimer;
import net.minecraft.client.Minecraft;

@ModOnly("jei")
class JeiRecipeTypeEnergize extends ThrEngJeiCategory<EnergizeRecipe, JeiRecipeTypeEnergize.Recipe> {

    private final ITickTimer animator = new TickTimer(32, 32, false);

    JeiRecipeTypeEnergize() {
        super(EnergizeRecipe.class, ThrEngJei.CAT_ENERGIZE, LangConst.INT_JEI_CAT_ENERGIZE, ResConst.INT_JEI_CAT_ENERGIZE_BG);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, Recipe recipe, IIngredients ingredients) {
        layout.getItemStacks().init(0, true, 4, 8);
        layout.getItemStacks().init(1, false, 64, 8);
        layout.getItemStacks().set(ingredients);
    }

    @Override
    public Recipe wrap(EnergizeRecipe recipe) {
        return new Recipe(recipe);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        GuiEnergizer.drawProgressBar(30, 3, animator.getValue() / (float)animator.getMaxValue());
    }

    static class Recipe implements IRecipeWrapper {

        private final EnergizeRecipe recipe;

        Recipe(EnergizeRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInputs(VanillaTypes.ITEM, recipe.input().getMatcher().getVisuals());
            ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput().getOutput());
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            minecraft.fontRenderer.drawStringWithShadow(String.format("%,d FE", recipe.input().getEnergy()), 3, 36, 0xFFFFFFFF);
        }

    }

}
