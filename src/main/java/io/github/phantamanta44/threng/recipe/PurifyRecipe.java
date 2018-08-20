package io.github.phantamanta44.threng.recipe;

import io.github.phantamanta44.libnine.recipe.IRcp;
import io.github.phantamanta44.libnine.recipe.input.ItemStackInput;
import io.github.phantamanta44.libnine.recipe.output.ItemStackOutput;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class PurifyRecipe implements IRcp<ItemStack, ItemStackInput, ItemStackOutput> {

    private final ItemStackInput input;
    private final ItemStackOutput output;

    public PurifyRecipe(Predicate<ItemStack> in, ItemStack output) {
        this.input = new ItemStackInput(in, 1);
        this.output = new ItemStackOutput(output);
    }

    @Override
    public ItemStackInput input() {
        return input;
    }

    @Override
    public ItemStackOutput mapToOutput(ItemStack input) {
        return output;
    }

}
