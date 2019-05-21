package io.github.phantamanta44.threng.recipe;

import io.github.phantamanta44.libnine.recipe.IRcp;
import io.github.phantamanta44.libnine.recipe.input.ItemStackInput;
import io.github.phantamanta44.libnine.recipe.output.ItemStackOutput;
import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import net.minecraft.item.ItemStack;

public class PurifyRecipe implements IRcp<ItemStack, ItemStackInput, ItemStackOutput> {

    private final ItemStackInput input;
    private final ItemStackOutput output;

    public PurifyRecipe(IDisplayableMatcher<ItemStack> in, ItemStack output) {
        this.input = new ItemStackInput(in, 1);
        this.output = new ItemStackOutput(output);
    }

    public ItemStackOutput getOutput() {
        return output;
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
