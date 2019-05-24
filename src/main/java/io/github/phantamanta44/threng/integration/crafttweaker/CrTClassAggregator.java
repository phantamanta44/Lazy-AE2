package io.github.phantamanta44.threng.integration.crafttweaker;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import io.github.phantamanta44.libnine.LibNine;
import io.github.phantamanta44.threng.ThrEng;
import io.github.phantamanta44.threng.recipe.AggRecipe;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ModOnly("crafttweaker")
@ZenRegister
@ZenClass("mods.threng.Aggregator")
public class CrTClassAggregator {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient in1, IIngredient in2, @Nullable @Optional IIngredient in3) {
        ThrEng.PROXY.registerPostInitTask(
                () -> LibNine.PROXY.getRecipeManager().getRecipeList(AggRecipe.class).add(
                        new AggRecipe(Stream.of(in1, in2, in3)
                                .map(CrTUtils::matchInputItemStack)
                                .collect(Collectors.toList()),
                                (ItemStack)output.getInternal())));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        ThrEng.PROXY.registerPostInitTask(() -> {
            if (!LibNine.PROXY.getRecipeManager().getRecipeList(AggRecipe.class).recipes()
                    .removeIf(r -> output.matches(CraftTweakerMC.getIItemStack(r.getOutput().getOutput())))) {
                ThrEng.LOGGER.warn("No recipes were removed for CraftTweaker Aggregator.removeRecipe call: {}", output.toString());
            }
        });
    }

}
