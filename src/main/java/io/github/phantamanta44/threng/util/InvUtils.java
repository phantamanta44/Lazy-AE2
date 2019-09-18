package io.github.phantamanta44.threng.util;

import io.github.phantamanta44.libnine.util.collection.Accrue;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InvUtils {

    public static Stream<ItemStack> stream(IItemHandler handler) {
        return IntStream.range(0, handler.getSlots()).mapToObj(handler::getStackInSlot);
    }

    public static void accrue(Accrue<ItemStack> stacks, IItemHandler... invs) {
        for (IItemHandler inv : invs) {
            stream(inv).forEach(stacks);
        }
    }

}
