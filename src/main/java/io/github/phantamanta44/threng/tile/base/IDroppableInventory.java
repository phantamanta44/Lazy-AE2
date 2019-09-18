package io.github.phantamanta44.threng.tile.base;

import io.github.phantamanta44.libnine.util.collection.Accrue;
import net.minecraft.item.ItemStack;

public interface IDroppableInventory {

    void collectDrops(Accrue<ItemStack> drops);

}
