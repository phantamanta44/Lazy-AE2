package io.github.phantamanta44.threng.recipe;

import io.github.phantamanta44.libnine.recipe.IRcp;
import io.github.phantamanta44.libnine.recipe.output.ItemStackOutput;
import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import io.github.phantamanta44.libnine.util.tuple.IPair;
import io.github.phantamanta44.threng.recipe.component.ItemEnergyInput;
import net.minecraft.item.ItemStack;

public class EnergizeRecipe implements IRcp<IPair<ItemStack, Integer>, ItemEnergyInput, ItemStackOutput> {

    private final ItemEnergyInput input;
    private final ItemStackOutput output;

    public EnergizeRecipe(IDisplayableMatcher<ItemStack> itemInput, int energyCost, ItemStack output) {
        this.input = new ItemEnergyInput(itemInput, energyCost);
        this.output = new ItemStackOutput(output);
    }

    public ItemStackOutput getOutput() {
        return output;
    }

    @Override
    public ItemEnergyInput input() {
        return input;
    }

    @Override
    public ItemStackOutput mapToOutput(IPair<ItemStack, Integer> input) {
        return output;
    }

}
