package io.github.phantamanta44.threng.recipe;

import io.github.phantamanta44.libnine.recipe.IRcp;
import io.github.phantamanta44.libnine.recipe.input.IRcpIn;
import io.github.phantamanta44.libnine.recipe.output.ItemStackOutput;
import io.github.phantamanta44.libnine.util.helper.OreDictUtils;
import io.github.phantamanta44.libnine.util.tuple.ITriple;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class EtchRecipe implements IRcp<ITriple<ItemStack, ItemStack, ItemStack>, EtchRecipe.Input, ItemStackOutput> {

    private final Input input;
    private final ItemStackOutput output;

    public EtchRecipe(Predicate<ItemStack> input, ItemStack output) {
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

        private final Predicate<ItemStack> inputPredicate;

        public Input(Predicate<ItemStack> input) {
            this.inputPredicate = input;
        }

        @Override
        public boolean matches(ITriple<ItemStack, ItemStack, ItemStack> input) {
            return OreDictUtils.matchesOredict(input.getA(), "dustRedstone")
                    && OreDictUtils.matchesOredict(input.getB(), "itemSilicon")
                    && inputPredicate.test(input.getC());
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
