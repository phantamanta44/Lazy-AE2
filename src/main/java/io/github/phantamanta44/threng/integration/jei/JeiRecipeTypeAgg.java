package io.github.phantamanta44.threng.integration.jei;

import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.integration.jei.base.ThrEngJeiCategory;
import io.github.phantamanta44.threng.recipe.AggRecipe;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.stream.Collectors;

class JeiRecipeTypeAgg extends ThrEngJeiCategory<AggRecipe, JeiRecipeTypeAgg.Recipe> {

    JeiRecipeTypeAgg() {
        super(AggRecipe.class, ThrEngJei.CAT_AGG, LangConst.INT_JEI_CAT_AGG, ResConst.INT_JEI_CAT_AGG_BG);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, Recipe recipe, IIngredients ingredients) {
        for (int i = 0; i < 3; i++) {
            layout.getItemStacks().init(i, true, 20 + 20 * i, 27);
        }
        layout.getItemStacks().init(3, false, 120, 27);
        layout.getItemStacks().set(ingredients);
    }

    @Override
    public Recipe wrap(AggRecipe recipe) {
        return new Recipe(recipe);
    }

    static class Recipe implements IRecipeWrapper {

        private final AggRecipe recipe;

        Recipe(AggRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInputLists(ItemStack.class, recipe.input().getInputs().stream()
                    .map(IDisplayableMatcher::getVisuals).collect(Collectors.toList()));
            ingredients.setOutput(ItemStack.class, recipe.getOutput().getOutput());
        }

    }

}
