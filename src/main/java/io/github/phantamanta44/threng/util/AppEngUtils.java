package io.github.phantamanta44.threng.util;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.definitions.IItemDefinition;
import io.github.phantamanta44.libnine.util.helper.ItemUtils;
import net.minecraft.item.ItemStack;

import java.util.function.Function;
import java.util.function.Predicate;

public class AppEngUtils {

    public static final Predicate<ItemStack> IS_UPGRADE_ACCEL = genItemPredicate(defs -> defs.materials().cardSpeed());

    private static Predicate<ItemStack> genItemPredicate(Function<IDefinitions, IItemDefinition> item) {
        return item.apply(AEApi.instance().definitions()).maybeStack(1)
                .map(ItemUtils::matchesWithWildcard)
                .orElse(is -> false);
    }

}
