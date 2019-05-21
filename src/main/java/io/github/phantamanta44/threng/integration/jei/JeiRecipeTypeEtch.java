package io.github.phantamanta44.threng.integration.jei;

import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.integration.jei.base.ThrEngJeiCategory;
import io.github.phantamanta44.threng.recipe.EtchRecipe;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;

class JeiRecipeTypeEtch extends ThrEngJeiCategory<EtchRecipe, JeiRecipeTypeEtch.Recipe> {

    JeiRecipeTypeEtch() {
        super(EtchRecipe.class, ThrEngJei.CAT_ETCH, LangConst.INT_JEI_CAT_ETCH, ResConst.INT_JEI_CAT_ETCH_BG);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, Recipe recipe, IIngredients ingredients) {
        layout.getItemStacks().init(0, true, 29, 9);
        layout.getItemStacks().init(1, true, 29, 45);
        layout.getItemStacks().init(2, true, 52, 27);
        layout.getItemStacks().init(3, false, 112, 27);
        layout.getItemStacks().set(ingredients);
    }

    @Override
    public Recipe wrap(EtchRecipe recipe) {
        return new Recipe(recipe);
    }

    static class Recipe implements IRecipeWrapper {

        private final EtchRecipe recipe;

        Recipe(EtchRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInputLists(ItemStack.class, Arrays.asList(
                    OreDictionary.getOres("dustRedstone"),
                    OreDictionary.getOres("itemSilicon"),
                    recipe.input().getEtchingMatcher().getVisuals()
            ));
            ingredients.setOutput(ItemStack.class, recipe.getOutput().getOutput());
        }

    }

}
