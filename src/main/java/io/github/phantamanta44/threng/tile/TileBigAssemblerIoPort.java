package io.github.phantamanta44.threng.tile;

import io.github.phantamanta44.libnine.capability.provider.CapabilityBroker;
import io.github.phantamanta44.libnine.component.multiblock.MultiBlockCore;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import io.github.phantamanta44.threng.tile.base.IBigAssemblerUnit;
import io.github.phantamanta44.threng.util.ConjoinedItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

@RegisterTile(ThrEngConst.MOD_ID)
public class TileBigAssemblerIoPort extends TileBigAssemblerPart {

    private final BigAssemblerIoHandler ioHandler = new BigAssemblerIoHandler();

    public TileBigAssemblerIoPort() {
        multiBlock.onConnectionStatusChange(ioHandler::invalidateCache);
    }

    @Override
    protected ICapabilityProvider initCapabilities() {
        return new CapabilityBroker().with(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, ioHandler);
    }

    private class BigAssemblerIoHandler implements IItemHandler {

        @Nullable
        private IItemHandler cachedItemHandler = null;

        void invalidateCache() {
            cachedItemHandler = null;
        }

        private IItemHandler getItemHandler() {
            if (cachedItemHandler == null) {
                MultiBlockCore<IBigAssemblerUnit> core = multiBlock.getCore();
                if (core != null) {
                    cachedItemHandler = new ConjoinedItemHandler(
                            ((TileBigAssemblerCore)core.getUnit()).getPatternStores().stream()
                                    .map(TileBigAssemblerPatternStore::getPatternInventory)
                                    .collect(Collectors.toList()));
                } else {
                    cachedItemHandler = new EmptyHandler();
                }
            }
            return cachedItemHandler;
        }

        @Override
        public int getSlots() {
            return getItemHandler().getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return getItemHandler().getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return getItemHandler().insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return getItemHandler().extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return getItemHandler().getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return getItemHandler().isItemValid(slot, stack);
        }

    }

}
