package io.github.phantamanta44.threng.integration.crafttweaker;

import crafttweaker.annotations.ModOnly;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.item.MCItemStack;
import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import io.github.phantamanta44.libnine.util.helper.ItemUtils;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@ModOnly("crafttweaker")
class CrTUtils {

    static IDisplayableMatcher<ItemStack> matchInputItemStack(@Nullable IIngredient input) {
        if (input == null) {
            return ItemUtils.matchesWithWildcard(ItemStack.EMPTY);
        }
        List<ItemStack> display = input.getItems().stream()
                .filter(i -> i instanceof MCItemStack)
                .map(i -> (ItemStack)i.getInternal())
                .collect(Collectors.toList());
        if (display.isEmpty()) {
            throw new IllegalArgumentException("Bad CraftTweaker item ingredient: " + input.toString());
        }
        return IDisplayableMatcher.ofMany(() -> display, is -> input.matches(CraftTweakerMC.getIItemStack(is)));
    }

}
