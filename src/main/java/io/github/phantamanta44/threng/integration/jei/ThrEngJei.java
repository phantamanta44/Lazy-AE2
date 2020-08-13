package io.github.phantamanta44.threng.integration.jei;

import io.github.phantamanta44.libnine.LibNine;
import io.github.phantamanta44.libnine.recipe.IRecipeManager;
import io.github.phantamanta44.libnine.util.render.TextureRegion;
import io.github.phantamanta44.threng.block.BlockMachine;
import io.github.phantamanta44.threng.client.gui.GuiAggregator;
import io.github.phantamanta44.threng.client.gui.GuiCentrifuge;
import io.github.phantamanta44.threng.client.gui.GuiEnergizer;
import io.github.phantamanta44.threng.client.gui.GuiEtcher;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import io.github.phantamanta44.threng.integration.jei.base.ThrEngJeiCategory;
import io.github.phantamanta44.threng.inventory.ContainerAggregator;
import io.github.phantamanta44.threng.inventory.ContainerCentrifuge;
import io.github.phantamanta44.threng.inventory.ContainerEnergizer;
import io.github.phantamanta44.threng.inventory.ContainerEtcher;
import io.github.phantamanta44.threng.recipe.AggRecipe;
import io.github.phantamanta44.threng.recipe.EnergizeRecipe;
import io.github.phantamanta44.threng.recipe.EtchRecipe;
import io.github.phantamanta44.threng.recipe.PurifyRecipe;
import mezz.jei.Internal;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class ThrEngJei implements IModPlugin {

    private static final String KEY_CAT = ThrEngConst.MOD_ID + ".";
    public static final String CAT_AGG = KEY_CAT + "agg";
    public static final String CAT_PURIFY = KEY_CAT + "purify";
    public static final String CAT_ETCH = KEY_CAT + "etch";
    public static final String CAT_ENERGIZE = KEY_CAT + "energize";

    private final ThrEngJeiCategory<?, ?>[] categories = {
            new JeiRecipeTypeAgg(),
            new JeiRecipeTypePurify(),
            new JeiRecipeTypeEtch(),
            new JeiRecipeTypeEnergize()
    };

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(categories);
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(BlockMachine.Type.AGGREGATOR.newStack(1), CAT_AGG);
        registry.addRecipeCatalyst(BlockMachine.Type.CENTRIFUGE.newStack(1), CAT_PURIFY);
        registry.addRecipeCatalyst(BlockMachine.Type.ETCHER.newStack(1), CAT_ETCH);
        registry.addRecipeCatalyst(BlockMachine.Type.ENERGIZER.newStack(1), CAT_ENERGIZE);

        for (ThrEngJeiCategory<?, ?> category : categories) {
            category.registerHandler(registry);
        }

        IRecipeManager recipeManager = LibNine.PROXY.getRecipeManager();
        registry.addRecipes(recipeManager.getRecipeList(AggRecipe.class).recipes(), CAT_AGG);
        registry.addRecipes(recipeManager.getRecipeList(PurifyRecipe.class).recipes(), CAT_PURIFY);
        registry.addRecipes(recipeManager.getRecipeList(EtchRecipe.class).recipes(), CAT_ETCH);
        registry.addRecipes(recipeManager.getRecipeList(EnergizeRecipe.class).recipes(), CAT_ENERGIZE);

        registry.addRecipeClickArea(GuiAggregator.class, 92, 36, 24, 14, CAT_AGG);
        registry.addRecipeClickArea(GuiCentrifuge.class, 80, 36, 22, 14, CAT_PURIFY);
        registry.addRecipeClickArea(GuiEtcher.class, 84, 36, 22, 14, CAT_ETCH);
        registry.addRecipeClickArea(GuiEnergizer.class, 81, 29, 22, 29, CAT_ENERGIZE);

        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerAggregator.class, CAT_AGG, 37, 3, 0, 36);
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerCentrifuge.class, CAT_PURIFY, 37, 1, 0, 36);
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerEtcher.class, CAT_ETCH, 37, 3, 0, 36);
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerEnergizer.class, CAT_ENERGIZE, 37, 1, 0, 36);
    }

    public static IDrawable wrapDrawable(TextureRegion tex) {
        return Internal.getHelpers().getGuiHelper().createDrawable(
                tex.getTexture().getTexture(), tex.getX(), tex.getY(), tex.getWidth(), tex.getHeight());
    }

}
