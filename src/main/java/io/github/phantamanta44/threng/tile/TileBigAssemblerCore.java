package io.github.phantamanta44.threng.tile;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.GridFlags;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.me.helpers.AENetworkProxy;
import appeng.util.Platform;
import io.github.phantamanta44.libnine.capability.impl.L9AspectInventory;
import io.github.phantamanta44.libnine.component.multiblock.MultiBlockConnectable;
import io.github.phantamanta44.libnine.component.multiblock.MultiBlockCore;
import io.github.phantamanta44.libnine.component.multiblock.MultiBlockType;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import io.github.phantamanta44.libnine.util.TriBool;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.libnine.util.data.ISerializable;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.libnine.util.data.serialization.IDatum;
import io.github.phantamanta44.libnine.util.nbt.ChainingTagCompound;
import io.github.phantamanta44.threng.block.BlockBigAssembler;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import io.github.phantamanta44.threng.multiblock.ThrEngMultiBlocks;
import io.github.phantamanta44.threng.tile.base.IBigAssemblerUnit;
import io.github.phantamanta44.threng.tile.base.IDroppableInventory;
import io.github.phantamanta44.threng.tile.base.TileAENetworked;
import io.github.phantamanta44.threng.util.AppEngUtils;
import io.github.phantamanta44.threng.util.InvUtils;
import io.github.phantamanta44.threng.util.ThrEngTextStyles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RegisterTile(ThrEngConst.MOD_ID)
public class TileBigAssemblerCore extends TileAENetworked implements IBigAssemblerUnit, ICraftingProvider, IDroppableInventory {

    public static final int MAX_JOB_QUEUE = 64;
    private static final double ENERGY_PER_WORK = 16D;
    public static final int WORK_PER_JOB = 16;
    public static final int MAX_EFFECTIVE_CPUS = (MAX_JOB_QUEUE * WORK_PER_JOB - 1) / 3;

    @AutoSerialize
    private final MultiBlockCore<IBigAssemblerUnit> multiBlock = new MultiBlockCore<>(this, ThrEngMultiBlocks.BIG_ASSEMBLER);
    @AutoSerialize
    private final IDatum.OfInt cpuCount = IDatum.ofInt(0);
    @AutoSerialize
    private final L9AspectInventory craftingBuffer = new L9AspectInventory.Observable(MAX_JOB_QUEUE * 9, (i, o, n) -> setDirty());
    @AutoSerialize(sync = false)
    private final L9AspectInventory outputBuffer = new L9AspectInventory.Observable(MAX_JOB_QUEUE * 10, (i, o, n) -> setDirty());
    @AutoSerialize
    private final JobQueue jobQueue = new JobQueue();
    @AutoSerialize
    private final IDatum.OfInt work = IDatum.ofInt(0);

    private TriBool clientCachedFormStatus = TriBool.NONE;
    private boolean patternsDirty = false;
    @Nullable
    private List<TileBigAssemblerPatternStore> patternStoreCache = null;

    public TileBigAssemblerCore() {
        markRequiresSync();
        multiBlock.onFormationStatusChange(() -> {
            world.markBlockRangeForRenderUpdate(pos, pos);
            if (multiBlock.isFormed()) {
                int cpus = 0;
                for (MultiBlockConnectable<IBigAssemblerUnit> comp : multiBlock) {
                    if (comp.getUnit().getWorldPos().getBlockState()
                            .getValue(BlockBigAssembler.TYPE) == BlockBigAssembler.Type.MODULE_CPU) {
                        ++cpus;
                    }
                }
                cpuCount.setInt(cpus);
            } else {
                cpuCount.setInt(0);
            }
            patternStoreCache = null;
            notifyPatternUpdate();
            setDirty();
        });
    }

    @Override
    protected void initProxy(AENetworkProxy proxy) {
        proxy.setIdlePowerUsage(3D);
        proxy.setFlags(GridFlags.REQUIRE_CHANNEL);
    }

    public int getCpuCount() {
        return cpuCount.getInt();
    }

    public int getJobCount() {
        return jobQueue.getOutstandingJobCount();
    }

    public int getWork() {
        return work.getInt();
    }

    public double getEnergyCost() {
        return ENERGY_PER_WORK + cpuCount.getInt();
    }

    public int getWorkRate() {
        return cpuCount.getInt() * 3 + 1;
    }

    @Nullable
    public IAssemblyJob getActiveJob() {
        return jobQueue.peek();
    }

    public IItemHandler getCraftingBuffer() {
        return craftingBuffer;
    }

    public List<TileBigAssemblerPatternStore> getPatternStores() {
        if (patternStoreCache == null) {
            patternStoreCache = new ArrayList<>();
            if (multiBlock.isFormed()) {
                for (MultiBlockConnectable<IBigAssemblerUnit> comp : multiBlock) {
                    IBigAssemblerUnit unit = comp.getUnit();
                    if (unit instanceof TileBigAssemblerPatternStore) {
                        patternStoreCache.add((TileBigAssemblerPatternStore)unit);
                    }
                }
            }
        }
        return patternStoreCache;
    }

    @Override
    public MultiBlockType<IBigAssemblerUnit> getMultiBlockType() {
        return ThrEngMultiBlocks.BIG_ASSEMBLER;
    }

    @Override
    public MultiBlockConnectable<IBigAssemblerUnit> getMultiBlockConnection() {
        return multiBlock;
    }

    @Nullable
    @Override
    protected ItemStack getNetworkRepresentation() {
        return BlockBigAssembler.Type.CONTROLLER.newStack(1);
    }

    @Override
    public boolean isActive() {
        return multiBlock.isFormed();
    }

    @MENetworkEventSubscribe
    public void onPowerStatusChange(final MENetworkPowerStatusChange event) {
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @MENetworkEventSubscribe
    public void onNetworkChannelsChange(final MENetworkChannelsChanged event) {
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    public void tryAssemble(EntityPlayer player) {
        ITextComponent msg;
        if (multiBlock.isFormed()) {
            multiBlock.disconnect();
            msg = new TextComponentTranslation(LangConst.NOTIF_MULTIBLOCK_UNFORMED);
            msg.setStyle(ThrEngTextStyles.SUCCESS);
        } else {
            if (multiBlock.tryForm()) {
                msg = new TextComponentTranslation(LangConst.NOTIF_MULTIBLOCK_FORMED);
                msg.setStyle(ThrEngTextStyles.SUCCESS);
            } else {
                msg = new TextComponentTranslation(LangConst.NOTIF_MULTIBLOCK_FAILED);
                msg.setStyle(ThrEngTextStyles.FAILURE);
            }
        }
        player.sendStatusMessage(msg, true);
    }

    @Override
    public void collectDrops(Accrue<ItemStack> drops) {
        InvUtils.accrue(drops, craftingBuffer);
    }

    @Override
    protected void tick() {
        super.tick();
        if (!world.isRemote) {
            aeGrid().ifPresent(grid -> {
                IEnergyGrid energy = grid.getCache(IEnergyGrid.class);
                IMEMonitor<IAEItemStack> storage = grid.<IStorageGrid>getCache(IStorageGrid.class)
                        .getInventory(AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class));

                AppEngUtils.importItems(outputBuffer, storage, energy, actionSource);

                if (patternsDirty) {
                    grid.postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
                    patternsDirty = false;
                }

                if (multiBlock.isFormed()) {
                    int jobs = jobQueue.getOutstandingJobCount();
                    if (jobs > 0) {
                        int currentWork = work.getInt();
                        double energyUnitCost = getEnergyCost();
                        double extracted = energy.extractAEPower(
                                energyUnitCost * Math.min(getWorkRate(), jobs * WORK_PER_JOB - currentWork),
                                Actionable.MODULATE, PowerMultiplier.CONFIG);
                        int workDone = (int)Math.ceil(extracted / energyUnitCost);
                        if (workDone > 0) {
                            currentWork += workDone;
                            while (currentWork >= WORK_PER_JOB) {
                                currentWork -= WORK_PER_JOB;
                                jobQueue.dispatchJob();
                            }
                            work.setInt(currentWork);
                            setDirty();
                        }
                    } else if (work.getInt() > 0) {
                        work.setInt(0);
                        setDirty();
                    }
                }
            });
        }
    }

    public void notifyPatternUpdate() {
        patternsDirty = true;
    }

    @Override
    public void provideCrafting(ICraftingProviderHelper helper) {
        for (TileBigAssemblerPatternStore patternStore : getPatternStores()) {
            patternStore.providePatterns(this, helper);
        }
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails pattern, InventoryCrafting inv) {
        if (pattern.isCraftable() && !jobQueue.isFull()) {
            ItemStack output = pattern.getOutput(inv, getWorld());
            if (!output.isEmpty()) {
                ItemStack[] remaining = new ItemStack[9];
                int remMax = Math.min(inv.getSizeInventory(), 9); // only care about at most 9 remaining items
                for (int i = 0; i < remMax; i++) {
                    remaining[i] = Platform.getContainerItem(inv.getStackInSlot(i));
                }
                jobQueue.pushJob(inv, remaining, output);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isBusy() {
        return jobQueue.isFull();
    }

    @Override
    public void deserBytes(ByteUtils.Reader data) {
        super.deserBytes(data);
        TriBool formStatus = TriBool.wrap(multiBlock.isFormed());
        if (clientCachedFormStatus != formStatus) {
            clientCachedFormStatus = formStatus;
            world.markBlockRangeForRenderUpdate(pos, pos);
            patternStoreCache = null;
        }
    }

    private class JobQueue implements ISerializable {

        private BitSet jobSlots = new BitSet(MAX_JOB_QUEUE);
        private final Deque<CraftingJob> queue = new ArrayDeque<>();

        int getOutstandingJobCount() {
            return queue.size();
        }

        boolean isFull() {
            return queue.size() == MAX_JOB_QUEUE;
        }

        @Nullable
        CraftingJob peek() {
            return queue.peek();
        }

        void pushJob(InventoryCrafting input, ItemStack[] remaining, ItemStack output) {
            CraftingJob job = new CraftingJob(jobSlots.nextClearBit(0), remaining, output);
            queue.offer(job);
            jobSlots.set(job.index);
            for (int i = 0; i < 9; i++) {
                craftingBuffer.setStackInSlot(job.index * 9 + i, input.getStackInSlot(i));
            }
        }

        void dispatchJob() {
            CraftingJob job = queue.peek();
            if (job != null) {
                for (int i = 0; i < 10; i++) {
                    if (!outputBuffer.getStackInSlot(job.index * 10 + i).isEmpty()) {
                        return;
                    }
                }
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = job.remaining[i];
                    if (!stack.isEmpty()) {
                        outputBuffer.setStackInSlot(job.index * 10 + i, stack);
                    }
                    craftingBuffer.setStackInSlot(job.index * 9 + i, ItemStack.EMPTY);
                }
                outputBuffer.setStackInSlot(job.index * 10 + 9, job.result);
                queue.pop();
                jobSlots.clear(job.index);
            }
        }

        @Override
        public void serBytes(ByteUtils.Writer data) {
            byte[] jobSlotData = jobSlots.toByteArray();
            data.writeInt(jobSlotData.length).writeBytes(jobSlotData).writeInt(queue.size());
            for (CraftingJob job : queue) {
                data.writeInt(job.index);
                for (ItemStack remStack : job.remaining) {
                    data.writeItemStack(remStack);
                }
                data.writeItemStack(job.result);
            }
        }

        @Override
        public void deserBytes(ByteUtils.Reader data) {
            jobSlots = BitSet.valueOf(data.readBytes(data.readInt()));
            queue.clear();
            int jobCount = data.readInt();
            while (jobCount > 0) {
                queue.offer(new CraftingJob(
                        data.readInt(),
                        IntStream.range(0, 9).mapToObj(i -> data.readItemStack()).toArray(ItemStack[]::new),
                        data.readItemStack()));
                --jobCount;
            }
        }

        @Override
        public void serNBT(NBTTagCompound tag) {
            tag.setByteArray("JobSlots", jobSlots.toByteArray());
            NBTTagList queueTag = new NBTTagList();
            for (CraftingJob job : queue) {
                queueTag.appendTag(new ChainingTagCompound()
                        .withInt("Index", job.index)
                        .withList("Remaining", Arrays.stream(job.remaining)
                                .map(ItemStack::serializeNBT)
                                .collect(Collectors.toList()))
                        .withItemStack("Result", job.result));
            }
            tag.setTag("Queue", queueTag);
        }

        @Override
        public void deserNBT(NBTTagCompound tag) {
            jobSlots = BitSet.valueOf(tag.getByteArray("JobSlots"));
            queue.clear();
            for (NBTBase jobTag0 : tag.getTagList("Queue", Constants.NBT.TAG_COMPOUND)) {
                NBTTagCompound jobTag = (NBTTagCompound)jobTag0;
                NBTTagList remTag = jobTag.getTagList("Remaining", Constants.NBT.TAG_COMPOUND);
                ItemStack[] rem = new ItemStack[9];
                for (int i = 0; i < remTag.tagCount(); i++) {
                    rem[i] = new ItemStack(remTag.getCompoundTagAt(i));
                }
                queue.offer(new CraftingJob(jobTag.getInteger("Index"), rem, new ItemStack(jobTag.getCompoundTag("Result"))));
            }
        }

        private class CraftingJob implements IAssemblyJob {

            final int index;
            final ItemStack[] remaining;
            final ItemStack result;

            CraftingJob(int index, ItemStack[] remaining, ItemStack result) {
                this.index = index;
                this.remaining = remaining;
                this.result = result;
            }

            @Override
            public int getJobIndex() {
                return index;
            }

            @Override
            public ItemStack getResult() {
                return result;
            }

        }

    }

    public interface IAssemblyJob {

        int getJobIndex();

        ItemStack getResult();

    }

}
