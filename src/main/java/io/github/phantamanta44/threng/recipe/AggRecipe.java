package io.github.phantamanta44.threng.recipe;

import io.github.phantamanta44.libnine.recipe.IRcp;
import io.github.phantamanta44.libnine.recipe.input.IRcpIn;
import io.github.phantamanta44.libnine.recipe.output.ItemStackOutput;
import io.github.phantamanta44.libnine.util.collection.ISieve;
import io.github.phantamanta44.libnine.util.tuple.ITriple;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class AggRecipe implements IRcp<ITriple<ItemStack, ItemStack, ItemStack>, AggRecipe.Input, ItemStackOutput> {

    private final Input input;
    private final ItemStackOutput output;

    public AggRecipe(Collection<Predicate<ItemStack>> input, ItemStack output) {
        this.input = new Input(input);
        this.output = new ItemStackOutput(output);
    }

    @Override
    public Input input() {
        return input;
    }

    @Override
    public ItemStackOutput mapToOutput(ITriple<ItemStack, ItemStack, ItemStack> input) {
        return output;
    }

    public static class Input implements IRcpIn<ITriple<ItemStack, ItemStack, ItemStack>> {

        private final List<Predicate<ItemStack>> inputs;

        public Input(Collection<Predicate<ItemStack>> inputs) {
            this.inputs = new ArrayList<>(inputs);
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

}
