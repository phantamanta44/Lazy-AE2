package io.github.phantamanta44.threng.tile;

import appeng.api.AEApi;
import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.GridFlags;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.me.GridAccessException;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.me.helpers.AENetworkProxy;
import io.github.phantamanta44.libnine.capability.impl.L9AspectInventory;
import io.github.phantamanta44.libnine.capability.provider.CapabilityBrokerDirPredicated;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.libnine.util.world.BlockSide;
import io.github.phantamanta44.libnine.util.world.IAllocableSides;
import io.github.phantamanta44.libnine.util.world.SideAlloc;
import io.github.phantamanta44.libnine.util.world.WorldUtils;
import io.github.phantamanta44.threng.ThrEngConfig;
import io.github.phantamanta44.threng.block.BlockMachine;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import io.github.phantamanta44.threng.tile.base.IDroppableInventory;
import io.github.phantamanta44.threng.tile.base.TileNetworkDevice;
import io.github.phantamanta44.threng.util.AppEngUtils;
import io.github.phantamanta44.threng.util.ConjoinedItemHandler;
import io.github.phantamanta44.threng.util.InvUtils;
import io.github.phantamanta44.threng.util.SlotType;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@RegisterTile(ThrEngConst.MOD_ID)
public class TileFastCraftingBus extends TileNetworkDevice
        implements ICraftingProvider, IAllocableSides<SlotType.BasicIO>, IDroppableInventory {

    @AutoSerialize(sync = false)
    private final L9AspectInventory patternInventory = new L9AspectInventory.Observable(9, (i, o, n) -> {
        if (world != null && !world.isRemote) {
            aeGrid().ifPresent(grid -> grid.postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode())));
        }
        setDirty();
    });
    @AutoSerialize(sync = false)
    private final L9AspectInventory importInventory = new L9AspectInventory.Observable(9, (i, o, n) -> setDirty());
    @AutoSerialize(sync = false)
    private final L9AspectInventory exportInventory = new L9AspectInventory.Observable(9, (i, o, n) -> setDirty());
    @AutoSerialize
    private final SideAlloc<SlotType.BasicIO> sides = new SideAlloc<>(SlotType.BasicIO.OUTPUT, this::getFrontFace);

    private final List<ItemStack> cachedExportInvState = new ArrayList<>();

    public TileFastCraftingBus() {
        markRequiresSync();
        Predicate<ItemStack> patternPred = stack -> stack.getItem() instanceof ICraftingPatternItem;
        for (int i = 0; i < 9; i++) {
            patternInventory.withPredicate(i, patternPred);
        }
    }

    @Override
    protected ICapabilityProvider initCapabilities() {
        return new CapabilityBrokerDirPredicated()
                .with(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, importInventory,
                        sides.getPredicate(SlotType.BasicIO.INPUT));
    }

    @Nullable
    @Override
    protected ItemStack getNetworkRepresentation() {
        return BlockMachine.Type.FAST_CRAFTER.newStack(1);
    }

    @Override
    protected void initProxy(AENetworkProxy proxy) {
        if (ThrEngConfig.networkDevices.fastCrafterIdlePower > 0D) {
            proxy.setIdlePowerUsage(ThrEngConfig.networkDevices.fastCrafterIdlePower);
        }
        proxy.setFlags(GridFlags.REQUIRE_CHANNEL);
    }

    public IItemHandlerModifiable getPatternInventory() {
        return patternInventory;
    }

    public IItemHandlerModifiable getImportInventory() {
        return importInventory;
    }

    public IItemHandlerModifiable getExportInventory() {
        return exportInventory;
    }

    @Override
    public void collectDrops(Accrue<ItemStack> drops) {
        InvUtils.accrue(drops, importInventory, exportInventory, patternInventory);
    }

    @Override
    public void setFace(BlockSide face, SlotType.BasicIO state) {
        sides.setFace(face, state);
        setDirty();
    }

    @Override
    public SlotType.BasicIO getFace(BlockSide face) {
        return sides.getFace(face);
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            aeGrid().ifPresent(grid -> {
                IMEMonitor<IAEItemStack> storage = grid.<IStorageGrid>getCache(IStorageGrid.class)
                        .getInventory(AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class));
                IEnergyGrid energy = grid.getCache(IEnergyGrid.class);
                AppEngUtils.importItems(importInventory, storage, energy, actionSource);
            });

            if (isBusy()) { // check if items are in the export buffer
                IItemHandler conjItemHandler = computeAdjInvs();
                for (int i = 0; i < exportInventory.getSlots(); i++) {
                    ItemStack stack = exportInventory.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        exportInventory.setStackInSlot(i, ItemHandlerHelper.insertItem(conjItemHandler, stack, false));
                    }
                }
            }
        }
    }

    @Override
    public void provideCrafting(ICraftingProviderHelper helper) {
        for (int i = 0; i < patternInventory.getSlots(); i++) {
            ItemStack stack = patternInventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ICraftingPatternItem) {
                ICraftingPatternItem item = ((ICraftingPatternItem)stack.getItem());
                ICraftingPatternDetails pattern = item.getPatternForItem(stack, world);
                if (pattern != null) {
                    helper.addCraftingOption(this, pattern);
                }
            }
        }
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails pattern, InventoryCrafting inv) {
        IItemHandler conjItemHandler = computeAdjInvs();
        for (IAEItemStack input : pattern.getCondensedInputs()) {
            if (!ItemHandlerHelper.insertItem(conjItemHandler, input.getDefinition(), true).isEmpty()) {
                return false;
            }
        }
        int outBufIndex = 0;
        for (IAEItemStack input : pattern.getCondensedInputs()) {
            exportInventory.setStackInSlot(outBufIndex++, input.createItemStack());
        }

        try {
            cpuIter:
            for (ICraftingCPU cpu : getProxy().getCrafting().getCpus()) {
                if (cpu instanceof CraftingCPUCluster) {
                    for (AppEngUtils.CraftingTask task : AppEngUtils.getTasks((CraftingCPUCluster)cpu)) {
                        if (task.getPattern().equals(pattern)) {
                            long invocations = task.getInvocations() - 1;
                            while (invocations > 0) {
                                for (int i = 0; i < exportInventory.getSlots(); i++) {
                                    cachedExportInvState.add(exportInventory.getStackInSlot(i).copy());
                                }
                                for (IAEItemStack input : pattern.getCondensedInputs()) {
                                    if (!ItemHandlerHelper
                                            .insertItem(exportInventory, input.createItemStack(), false).isEmpty()) {
                                        for (int i = 0; i < cachedExportInvState.size(); i++) {
                                            exportInventory.setStackInSlot(i, cachedExportInvState.get(i));
                                        }
                                        cachedExportInvState.clear();
                                        break cpuIter;
                                    }
                                }
                                if (task.tryExtractItems(cpu.getActionSource())) {
                                    cachedExportInvState.clear();
                                    --invocations;
                                    task.decrement();
                                } else {
                                    for (int i = 0; i < cachedExportInvState.size(); i++) {
                                        exportInventory.setStackInSlot(i, cachedExportInvState.get(i));
                                    }
                                    cachedExportInvState.clear();
                                    continue cpuIter;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (GridAccessException e) {
            // NO-OP
        }
        return true;
    }

    @Override
    public boolean isBusy() {
        for (int i = 0; i < exportInventory.getSlots(); i++) {
            if (!exportInventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private IItemHandler computeAdjInvs() {
        List<IItemHandler> adjInvs = new ArrayList<>();
        for (BlockSide side : BlockSide.values()) {
            if (sides.getFace(side) == SlotType.BasicIO.OUTPUT) {
                EnumFacing dir = side.getDirection(getFrontFace());
                EnumFacing fromDir = dir.getOpposite();
                TileEntity adj = WorldUtils.getAdjacentTile(this, dir);
                if (adj != null && adj.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, fromDir)) {
                    adjInvs.add(adj.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, fromDir));
                }
            }
        }
        return new ConjoinedItemHandler(adjInvs);
    }

}
