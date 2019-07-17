package io.github.phantamanta44.threng.recipe.component;

import io.github.phantamanta44.libnine.recipe.input.IRcpIn;
import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import io.github.phantamanta44.libnine.util.collection.ISieve;
import io.github.phantamanta44.libnine.util.tuple.ITriple;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TriItemInput implements IRcpIn<ITriple<ItemStack, ItemStack, ItemStack>> {

    protected final List<IDisplayableMatcher<ItemStack>> inputs;

    public TriItemInput(Collection<IDisplayableMatcher<ItemStack>> inputs) {
        this.inputs = new ArrayList<>(inputs);
    }

    public List<IDisplayableMatcher<ItemStack>> getInputs() {
        return inputs;
    }

    @Override
    public boolean matches(ITriple<ItemStack, ItemStack, ItemStack> input) {
        return recursiveMatch(0, ISieve.over(Arrays.asList(input.getA(), input.getB(), input.getC())));
    }

    private boolean recursiveMatch(int index, ISieve<ItemStack> available) {
        if (index == inputs.size()) {
            for (ItemStack stack : available) {
                if (!stack.isEmpty()) return false;
            }
            return true;
        }
        for (int i = 0; i < available.size(); i++) {
            if (inputs.get(index).test(available.get(i)) && recursiveMatch(index + 1, available.excluding(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ITriple<ItemStack, ItemStack, ItemStack> consume(ITriple<ItemStack, ItemStack, ItemStack> input) {
        ItemStack[] stacks = new ItemStack[] { input.getA(), input.getB(), input.getC() };
        for (int i = 0; i < stacks.length; i++) {
            if (!stacks[i].isEmpty()) {
                if (stacks[i].getCount() > 1) {
                    stacks[i].shrink(1);
                } else {
                    stacks[i] = ItemStack.EMPTY;
                }
            }
        }
        return ITriple.of(stacks[0], stacks[1], stacks[2]);
    }

}
