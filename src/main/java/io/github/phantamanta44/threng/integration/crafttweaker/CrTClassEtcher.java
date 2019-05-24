package io.github.phantamanta44.threng.integration.crafttweaker;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import io.github.phantamanta44.libnine.LibNine;
import io.github.phantamanta44.threng.ThrEng;
import io.github.phantamanta44.threng.recipe.EtchRecipe;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("crafttweaker")
@ZenRegister
@ZenClass("mods.threng.Etcher")
public class CrTClassEtcher {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input) {
        ThrEng.PROXY.registerPostInitTask(
                () -> LibNine.PROXY.getRecipeManager().getRecipeList(EtchRecipe.class).add(
                        new EtchRecipe(CrTUtils.matchInputItemStack(input), (ItemStack)output.getInternal())));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        ThrEng.PROXY.registerPostInitTask(() -> {
            if (!LibNine.PROXY.getRecipeManager().getRecipeList(EtchRecipe.class).recipes()
                    .removeIf(r -> output.matches(CraftTweakerMC.getIItemStack(r.getOutput().getOutput())))) {
                ThrEng.LOGGER.warn("No recipes were removed for CraftTweaker Etcher.removeRecipe call: {}", output.toString());
            }
        });
    }

}
