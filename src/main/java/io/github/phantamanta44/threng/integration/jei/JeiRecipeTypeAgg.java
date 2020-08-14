package io.github.phantamanta44.threng.integration.jei;

import crafttweaker.annotations.ModOnly;
import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import io.github.phantamanta44.threng.client.gui.GuiAggregator;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.integration.jei.base.ThrEngJeiCategory;
import io.github.phantamanta44.threng.recipe.AggRecipe;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.TickTimer;
import net.minecraft.client.Minecraft;

import java.util.stream.Collectors;

@ModOnly("jei")
class JeiRecipeTypeAgg extends ThrEngJeiCategory<AggRecipe, JeiRecipeTypeAgg.Recipe> {

    private final ITickTimer animator = new TickTimer(32, 32, false);

    JeiRecipeTypeAgg() {
        super(AggRecipe.class, ThrEngJei.CAT_AGG, LangConst.INT_JEI_CAT_AGG, ResConst.INT_JEI_CAT_AGG_BG);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, Recipe recipe, IIngredients ingredients) {
        for (int i = 0; i < 3; i++) {
            layout.getItemStacks().init(i, true, 4 + 20 * i, 8);
        }
        layout.getItemStacks().init(3, false, 104, 8);
        layout.getItemStacks().set(ingredients);
    }

    @Override
    public Recipe wrap(AggRecipe recipe) {
        return new Recipe(recipe);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        GuiAggregator.drawProgressBar(69, 10, animator.getValue() / (float)animator.getMaxValue());
    }

    static class Recipe implements IRecipeWrapper {

        private final AggRecipe recipe;

        Recipe(AggRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInputLists(VanillaTypes.ITEM, recipe.input().getInputs().stream()
                    .map(IDisplayableMatcher::getVisuals).collect(Collectors.toList()));
            ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput().getOutput());
        }

    }

}
