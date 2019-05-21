package io.github.phantamanta44.threng.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class ConjoinedItemHandler implements IItemHandler {

    private final List<IItemHandler> delegates;
    private final int[] thresholds;

    public ConjoinedItemHandler(List<IItemHandler> delegates) {
        this.delegates = delegates;
        this.thresholds = new int[delegates.size()];
        if (!delegates.isEmpty()) {
            thresholds[0] = delegates.get(0).getSlots();
            for (int i = 1; i < thresholds.length; i++) {
                thresholds[i] = thresholds[i - 1] + delegates.get(i).getSlots();
            }
        }
    }

    @Override
    public int getSlots() {
        return delegates.stream().mapToInt(IItemHandler::getSlots).sum();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        int ihIndex = getItemHandler(slot);
        return delegates.get(ihIndex).getStackInSlot(slot - getBaseSlot(ihIndex));
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        int ihIndex = getItemHandler(slot);
        return delegates.get(ihIndex).insertItem(slot - getBaseSlot(ihIndex), stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        int ihIndex = getItemHandler(slot);
        return delegates.get(ihIndex).extractItem(slot - getBaseSlot(ihIndex), amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        int ihIndex = getItemHandler(slot);
        return delegates.get(ihIndex).getSlotLimit(slot - getBaseSlot(ihIndex));
    }

    private int getItemHandler(int index) {
        for (int i = 0; i < thresholds.length; i++) {
            if (index < thresholds[i]) {
                return i;
            }
        }
        int slotCount = getSlots();
        if (slotCount == 0) {
            throw new IndexOutOfBoundsException(String.format("%d is not a slot in an empty inventory!", index));
        }
        throw new IndexOutOfBoundsException(String.format("%d is not in slot indices [0, %d)!", index, slotCount));
    }

    private int getBaseSlot(int ihIndex) {
        return ihIndex == 0 ? 0 : thresholds[ihIndex - 1];
    }

}
