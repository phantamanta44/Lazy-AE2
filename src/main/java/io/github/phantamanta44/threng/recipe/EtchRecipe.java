package io.github.phantamanta44.threng.recipe;

import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import io.github.phantamanta44.libnine.util.helper.OreDictUtils;
import io.github.phantamanta44.libnine.util.tuple.ITriple;
import io.github.phantamanta44.threng.recipe.base.component.TriItemRecipe;
import io.github.phantamanta44.threng.recipe.component.TriItemInput;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collection;

public class EtchRecipe extends TriItemRecipe {

    private static final IDisplayableMatcher<ItemStack> MATCH_REDSTONE = OreDictUtils.matchesOredict("dustRedstone");
    private static final IDisplayableMatcher<ItemStack> MATCH_SILICON = OreDictUtils.matchesOredict("itemSilicon");

    public EtchRecipe(IDisplayableMatcher<ItemStack> top, IDisplayableMatcher<ItemStack> bottom,
                      IDisplayableMatcher<ItemStack> middle, ItemStack output) {
        super(Arrays.asList(top, bottom, middle), output);
    }

    public EtchRecipe(IDisplayableMatcher<ItemStack> middle, ItemStack output) {
        this(MATCH_REDSTONE, MATCH_SILICON, middle, output);
    }

    @Override
    protected TriItemInput createInput(Collection<IDisplayableMatcher<ItemStack>> input) {
        return new Input(input);
    }

    public static class Input extends TriItemInput {

        public Input(Collection<IDisplayableMatcher<ItemStack>> inputs) {
            super(inputs);
        }

        @Override
        public boolean matches(ITriple<ItemStack, ItemStack, ItemStack> input) {
            return inputs.get(0).test(input.getA())
                    && inputs.get(1).test(input.getB())
                    && inputs.get(2).test(input.getC());
        }

    }

}
