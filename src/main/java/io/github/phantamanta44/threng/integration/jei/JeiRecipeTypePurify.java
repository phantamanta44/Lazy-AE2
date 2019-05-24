package io.github.phantamanta44.threng.integration.jei;

import crafttweaker.annotations.ModOnly;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.integration.jei.base.ThrEngJeiCategory;
import io.github.phantamanta44.threng.recipe.PurifyRecipe;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

@ModOnly("jei")
class JeiRecipeTypePurify extends ThrEngJeiCategory<PurifyRecipe, JeiRecipeTypePurify.Recipe> {

    JeiRecipeTypePurify() {
        super(PurifyRecipe.class, ThrEngJei.CAT_PURIFY, LangConst.INT_JEI_CAT_PURIFY, ResConst.INT_JEI_CAT_PURIFY_BG);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, Recipe recipe, IIngredients ingredients) {
        layout.getItemStacks().init(0, true, 40, 27);
        layout.getItemStacks().init(1, false, 100, 27);
        layout.getItemStacks().set(ingredients);
    }

    @Override
    public Recipe wrap(PurifyRecipe recipe) {
        return new Recipe(recipe);
    }

    static class Recipe implements IRecipeWrapper {

        private final PurifyRecipe recipe;

        Recipe(PurifyRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInputs(ItemStack.class, recipe.input().getMatcher().getVisuals());
            ingredients.setOutput(ItemStack.class, recipe.getOutput().getOutput());
        }

    }

}
