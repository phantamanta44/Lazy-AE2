package io.github.phantamanta44.threng.tile;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.crafting.ICraftingGrid;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.networking.crafting.ICraftingRequester;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStackWatcher;
import appeng.api.networking.storage.IStackWatcherHost;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import appeng.me.helpers.AENetworkProxy;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import com.google.common.collect.ImmutableSet;
import io.github.phantamanta44.libnine.capability.impl.L9AspectInventory;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.libnine.util.data.ISerializable;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.libnine.util.math.MathUtils;
import io.github.phantamanta44.threng.ThrEngConfig;
import io.github.phantamanta44.threng.block.BlockMachine;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import io.github.phantamanta44.threng.tile.base.TileNetworkDevice;
import io.github.phantamanta44.threng.util.ThrEngCraftingTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

@RegisterTile(ThrEngConst.MOD_ID)
public class TileLevelMaintainer extends TileNetworkDevice implements IStackWatcherHost, ICraftingRequester {

    public static final int REQ_COUNT = 5;

    @AutoSerialize
    private final InventoryRequest requests = new InventoryRequest(this);
    @AutoSerialize
    private final L9AspectInventory results = new L9AspectInventory.Observable(REQ_COUNT, (i, o, n) -> setDirty());
    @AutoSerialize
    private final ThrEngCraftingTracker crafter = new ThrEngCraftingTracker(this, REQ_COUNT);

    @Nullable
    private IStackWatcher watcher;
    private final long[] knownCounts = new long[REQ_COUNT];

    private int sleepTicks = 0;
    private int sleepIncrement = ThrEngConfig.networkDevices.levelMaintainerSleepMax;

    public TileLevelMaintainer() {
        markRequiresSync();
        Arrays.fill(knownCounts, -1);
    }

    public InventoryRequest getRequestInventory() {
        return requests;
    }

    @Nullable
    @Override
    protected ItemStack getNetworkRepresentation() {
        return BlockMachine.Type.LEVEL_MAINTAINER.newStack(1);
    }

    @Override
    protected void initProxy(AENetworkProxy proxy) {
        if (ThrEngConfig.networkDevices.levelMaintainerIdlePower > 0D) {
            proxy.setIdlePowerUsage(ThrEngConfig.networkDevices.levelMaintainerIdlePower);
        }
        proxy.setFlags(GridFlags.REQUIRE_CHANNEL);
    }

    @Override
    protected void tick() {
        if (!world.isRemote) {
            aeGrid().ifPresent(grid -> {
                if (sleepTicks <= 0) {
                    IEnergyGrid energyGrid = grid.getCache(IEnergyGrid.class);
                    IMEMonitor<IAEItemStack> storageGrid = grid.<IStorageGrid>getCache(IStorageGrid.class)
                            .getInventory(AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class));
                    boolean workDone = false;

                    // try pushing crafting results
                    for (int i = 0; i < REQ_COUNT; i++) {
                        ItemStack stack = results.getStackInSlot(i);
                        if (stack.isEmpty()) {
                            continue;
                        }
                        IAEItemStack aeStack = Objects.requireNonNull(AEItemStack.fromItemStack(stack));
                        IAEItemStack rem = Platform.poweredInsert(energyGrid, storageGrid, aeStack, actionSource, Actionable.MODULATE);
                        if (rem == null || rem.getStackSize() < aeStack.getStackSize()) {
                            workDone = true;
                            results.setStackInSlot(i, rem != null ? rem.createItemStack() : ItemStack.EMPTY);
                        }
                    }

                    // try doing crafting work
                    ICraftingGrid crafting = grid.getCache(ICraftingGrid.class);
                    for (int i = 0; i < REQ_COUNT; i++) {
                        if (requests.isRequesting(i)) {
                            if (knownCounts[i] == -1) {
                                IAEItemStack stack = storageGrid.getStorageList()
                                        .findPrecise(AEItemStack.fromItemStack(requests.getStackInSlot(i)));
                                knownCounts[i] = stack == null ? 0 : stack.getStackSize();
                                workDone = true;
                            }
                            if (crafter.isSlotOpen(i)) {
                                long toCraft = requests.computeDelta(i, knownCounts[i]);
                                if (toCraft > 0 && crafter.requestCrafting(
                                        i, requests.request(i, toCraft), getWorld(), grid, crafting, actionSource)) {
                                    workDone = true;
                                }
                            }
                        }
                    }

                    // did we get anything done?
                    if (workDone) {
                        setDirty();
                        if (sleepIncrement > ThrEngConfig.networkDevices.levelMaintainerSleepMin) {
                            sleepIncrement = Math.max(sleepIncrement - 20, ThrEngConfig.networkDevices.levelMaintainerSleepMin);
                        }
                    } else if (sleepIncrement < ThrEngConfig.networkDevices.levelMaintainerSleepMax) {
                        sleepIncrement = Math.min(sleepIncrement + 30, ThrEngConfig.networkDevices.levelMaintainerSleepMax);
                    }
                    sleepTicks = sleepIncrement;
                } else {
                    --sleepTicks;
                }
            });
        }
    }

    @Override
    public void updateWatcher(IStackWatcher watcher) {
        this.watcher = watcher;
        resetWatcher();
    }

    private void resetWatcher() {
        if (watcher != null) {
            watcher.reset();
            requests.populateWatcher(watcher);
        }
    }

    private void clearIndex(int index) {
        knownCounts[index] = -1;
        resetWatcher();
    }

    @Override
    public void onStackChange(IItemList<?> items, IAEStack<?> stack, IAEStack<?> delta,
                              IActionSource actionSource, IStorageChannel<?> channel) {
        for (int i = 0; i < REQ_COUNT; i++) {
            if (requests.matches(i, (IAEItemStack)delta)) {
                knownCounts[i] = stack == null ? 0 : stack.getStackSize();
            }
        }
    }

    @Override
    public ImmutableSet<ICraftingLink> getRequestedJobs() {
        return crafter.getRequestedJobs();
    }

    @Nullable
    @Override
    public IAEItemStack injectCraftedItems(ICraftingLink link, @Nullable IAEItemStack stack, Actionable mode) {
        if (stack == null) {
            return null;
        }
        int slot = crafter.getSlotForJob(link);
        if (slot == -1) {
            return stack;
        }
        if (mode == Actionable.SIMULATE) {
            return AEItemStack.fromItemStack(results.insertItem(slot, stack.createItemStack(), true));
        } else {
            IAEItemStack rem = AEItemStack.fromItemStack(results.insertItem(slot, stack.createItemStack(), false));
            if (rem == null || rem.getStackSize() < stack.getStackSize()) {
                sleepIncrement = ThrEngConfig.networkDevices.levelMaintainerSleepMin; // urgent!
                sleepTicks = 0;
            }
            return rem;
        }
    }

    @Override
    public void jobStateChange(ICraftingLink link) {
        if (crafter.onJobStateChange(link)) {
            setDirty();
        }
    }

    public static class InventoryRequest implements ISerializable, IItemHandlerModifiable {

        private final TileLevelMaintainer owner;
        private final ItemStack[] requestStacks = new ItemStack[REQ_COUNT];
        private final long[] requestQtys = new long[REQ_COUNT];
        private final long[] requestBatches = new long[REQ_COUNT];

        InventoryRequest(TileLevelMaintainer owner) {
            this.owner = owner;
            Arrays.fill(requestStacks, ItemStack.EMPTY);
            Arrays.fill(requestBatches, 1);
        }

        public long getQuantity(int index) {
            return requestQtys[index];
        }

        public void updateQuantity(int index, long quantity) {
            if (index >= 0 && index < REQ_COUNT) {
                if (quantity > 0) {
                    if (!requestStacks[index].isEmpty()) {
                        requestQtys[index] = quantity;
                        owner.setDirty();
                    }
                } else if (!requestStacks[index].isEmpty()) {
                    requestStacks[index] = ItemStack.EMPTY;
                    requestQtys[index] = 0;
                    owner.clearIndex(index);
                    owner.setDirty();
                }
            }
        }

        public long getBatchSize(int index) {
            return requestBatches[index];
        }

        public void updateBatchSize(int index, long quantity) {
            if (index >= 0 && index < REQ_COUNT && quantity >= 0) {
                requestBatches[index] = quantity;
                owner.setDirty();
            }
        }

        public boolean isRequesting(int index) {
            return !requestStacks[index].isEmpty();
        }

        boolean matches(int index, IAEItemStack stack) {
            return stack.isSameType(requestStacks[index]);
        }

        long computeDelta(int index, long existing) {
            return requestStacks[index].isEmpty() ? 0
                    : MathUtils.clamp(requestQtys[index] - existing, 0, requestBatches[index]);
        }

        IAEItemStack request(int index, long count) {
            AEItemStack stack = Objects.requireNonNull(AEItemStack.fromItemStack(requestStacks[index]));
            stack.setStackSize(count);
            return stack;
        }

        void populateWatcher(IStackWatcher watcher) {
            for (int i = 0; i < REQ_COUNT; i++) {
                if (!requestStacks[i].isEmpty()) {
                    watcher.add(AEItemStack.fromItemStack(requestStacks[i]));
                }
            }
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            if (!owner.getWorld().isRemote) {
                requestStacks[slot] = stack;
                if (stack.isEmpty()) {
                    requestQtys[slot] = 0;
                } else {
                    requestQtys[slot] = stack.getCount();
                    stack.setCount(1);
                }
                owner.clearIndex(slot);
                owner.setDirty();
            }
        }

        @Override
        public int getSlots() {
            return REQ_COUNT;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return requestStacks[slot];
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            throw new UnsupportedOperationException();
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public void serBytes(ByteUtils.Writer data) {
            for (int i = 0; i < REQ_COUNT; i++) {
                data.writeItemStack(requestStacks[i]);
                if (!requestStacks[i].isEmpty()) {
                    data.writeLong(requestQtys[i]);
                }
                data.writeLong(requestBatches[i]);
            }
        }

        @Override
        public void deserBytes(ByteUtils.Reader data) {
            for (int i = 0; i < REQ_COUNT; i++) {
                requestStacks[i] = data.readItemStack();
                requestQtys[i] = requestStacks[i].isEmpty() ? 0 : data.readLong();
                requestBatches[i] = data.readLong();
            }
        }

        @Override
        public void serNBT(NBTTagCompound tag) {
            NBTTagList dtoList = new NBTTagList();
            for (int i = 0; i < REQ_COUNT; i++) {
                NBTTagCompound dto = new NBTTagCompound();
                if (!requestStacks[i].isEmpty()) {
                    dto.setTag("Stack", requestStacks[i].serializeNBT());
                    dto.setLong("Count", requestQtys[i]);
                }
                dto.setLong("Batch", requestBatches[i]);
                dtoList.appendTag(dto);
            }
            tag.setTag("Requests", dtoList);
        }

        @Override
        public void deserNBT(NBTTagCompound tag) {
            NBTTagList dtoList = tag.getTagList("Requests", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < REQ_COUNT; i++) {
                NBTTagCompound dto = dtoList.getCompoundTagAt(i);
                if (dto.hasKey("Stack")) {
                    requestStacks[i] = new ItemStack(dto.getCompoundTag("Stack"));
                    requestQtys[i] = dto.getLong("Count");
                } else {
                    requestStacks[i] = ItemStack.EMPTY;
                    requestQtys[i] = 0;
                }
                requestBatches[i] = dto.getLong("Batch");
            }
        }

    }

}
