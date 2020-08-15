package io.github.phantamanta44.threng.recipe.component;

import io.github.phantamanta44.libnine.recipe.input.IRcpIn;
import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import io.github.phantamanta44.libnine.util.tuple.IPair;
import net.minecraft.item.ItemStack;

public class ItemEnergyInput implements IRcpIn<IPair<ItemStack, Integer>> {

    protected final IDisplayableMatcher<ItemStack> itemMatcher;
    protected final int energy;

    public ItemEnergyInput(IDisplayableMatcher<ItemStack> itemMatcher, int energy) {
        this.itemMatcher = itemMatcher;
        this.energy = energy;
    }

    public IDisplayableMatcher<ItemStack> getMatcher() {
        return itemMatcher;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public boolean matches(IPair<ItemStack, Integer> input) {
        return itemMatcher.test(input.getA()); // don't match on energy; it's reflected in consumption
    }

    @Override
    public IPair<ItemStack, Integer> consume(IPair<ItemStack, Integer> input) {
        ItemStack stack = input.getA().copy();
        if (stack.getCount() == 1) {
            return IPair.of(ItemStack.EMPTY, input.getB() - energy);
        }
        stack.shrink(1);
        return IPair.of(stack, input.getB() - energy);
    }

}
