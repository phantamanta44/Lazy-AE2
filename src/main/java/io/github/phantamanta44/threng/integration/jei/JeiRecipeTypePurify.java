package io.github.phantamanta44.threng.integration.jei;

import crafttweaker.annotations.ModOnly;
import io.github.phantamanta44.threng.client.gui.GuiCentrifuge;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.integration.jei.base.ThrEngJeiCategory;
import io.github.phantamanta44.threng.recipe.PurifyRecipe;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.TickTimer;
import net.minecraft.client.Minecraft;

@ModOnly("jei")
class JeiRecipeTypePurify extends ThrEngJeiCategory<PurifyRecipe, JeiRecipeTypePurify.Recipe> {

    private final ITickTimer animator = new TickTimer(32, 32, false);

    JeiRecipeTypePurify() {
        super(PurifyRecipe.class, ThrEngJei.CAT_PURIFY, LangConst.INT_JEI_CAT_PURIFY, ResConst.INT_JEI_CAT_PURIFY_BG);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, Recipe recipe, IIngredients ingredients) {
        layout.getItemStacks().init(0, true, 4, 8);
        layout.getItemStacks().init(1, false, 64, 8);
        layout.getItemStacks().set(ingredients);
    }

    @Override
    public Recipe wrap(PurifyRecipe recipe) {
        return new Recipe(recipe);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        GuiCentrifuge.drawProgressBar(29, 10, animator.getValue() / (float)animator.getMaxValue());
    }

    static class Recipe implements IRecipeWrapper {

        private final PurifyRecipe recipe;

        Recipe(PurifyRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInputs(VanillaTypes.ITEM, recipe.input().getMatcher().getVisuals());
            ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput().getOutput());
        }

    }

}
