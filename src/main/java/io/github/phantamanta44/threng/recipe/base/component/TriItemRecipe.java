package io.github.phantamanta44.threng.recipe.base.component;

import io.github.phantamanta44.libnine.recipe.IRcp;
import io.github.phantamanta44.libnine.recipe.output.ItemStackOutput;
import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import io.github.phantamanta44.libnine.util.tuple.ITriple;
import io.github.phantamanta44.threng.recipe.component.TriItemInput;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public abstract class TriItemRecipe implements IRcp<ITriple<ItemStack, ItemStack, ItemStack>, TriItemInput, ItemStackOutput> {

    private final TriItemInput input;
    private final ItemStackOutput output;

    public TriItemRecipe(Collection<IDisplayableMatcher<ItemStack>> input, ItemStack output) {
        this.input = createInput(input);
        this.output = new ItemStackOutput(output);
    }

    protected TriItemInput createInput(Collection<IDisplayableMatcher<ItemStack>> input) {
        return new TriItemInput(input);
    }

    public ItemStackOutput getOutput() {
        return output;
    }

    @Override
    public TriItemInput input() {
        return input;
    }

    @Override
    public ItemStackOutput mapToOutput(ITriple<ItemStack, ItemStack, ItemStack> input) {
        return output;
    }

}
