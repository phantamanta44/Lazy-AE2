package io.github.phantamanta44.threng.recipe;

import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import io.github.phantamanta44.threng.recipe.base.component.TriItemRecipe;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public class AggRecipe extends TriItemRecipe {

    public AggRecipe(Collection<IDisplayableMatcher<ItemStack>> inputs, ItemStack output) {
        super(inputs, output);
    }

}
