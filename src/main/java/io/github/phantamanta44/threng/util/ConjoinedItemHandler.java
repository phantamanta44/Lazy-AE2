package io.github.phantamanta44.threng.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.Arrays;
import java.util.Collection;

// faster lookup but higher memory overhead than forge's CombinedInvWrapper
public class ConjoinedItemHandler implements IItemHandlerModifiable {

    private final IItemHandler[] delegates;
    private final int[] baseIndices;

    public ConjoinedItemHandler(Collection<IItemHandler> delegates) {
        int i = 0;
        for (IItemHandler delegate : delegates) {
            i += delegate.getSlots();
        }
        this.delegates = new IItemHandler[i];
        this.baseIndices = new int[i];
        i = 0;
        for (IItemHandler delegate : delegates) {
            int slotCount = delegate.getSlots();
            for (int j = 0; j < slotCount; j++) {
                this.delegates[i + j] = delegate;
                this.baseIndices[i + j] = i;
            }
            i += slotCount;
        }
    }

    public ConjoinedItemHandler(IItemHandler... delegates) {
        this(Arrays.asList(delegates));
    }

    @Override
    public int getSlots() {
        return delegates.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return delegates[slot].getStackInSlot(slot - baseIndices[slot]);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        IItemHandler inv = delegates[slot];
        if (inv instanceof IItemHandlerModifiable) {
            ((IItemHandlerModifiable)inv).setStackInSlot(slot - baseIndices[slot], stack);
        }
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return delegates[slot].insertItem(slot - baseIndices[slot], stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return delegates[slot].extractItem(slot - baseIndices[slot], amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return delegates[slot].getSlotLimit(slot - baseIndices[slot]);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return delegates[slot].isItemValid(slot - baseIndices[slot], stack);
    }

}
