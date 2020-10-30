package io.github.phantamanta44.threng.tile.base;

import io.github.phantamanta44.libnine.LibNine;
import io.github.phantamanta44.libnine.capability.impl.L9AspectSlot;
import io.github.phantamanta44.libnine.recipe.IRcp;
import io.github.phantamanta44.libnine.recipe.input.IRcpIn;
import io.github.phantamanta44.libnine.recipe.output.IRcpOut;
import io.github.phantamanta44.libnine.util.TriBool;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.libnine.util.data.serialization.IDatum;
import io.github.phantamanta44.libnine.util.helper.InventoryUtils;
import io.github.phantamanta44.libnine.util.world.BlockSide;
import io.github.phantamanta44.libnine.util.world.IAllocableSides;
import io.github.phantamanta44.libnine.util.world.WorldUtils;
import io.github.phantamanta44.threng.util.AppEngUtils;
import io.github.phantamanta44.threng.util.SlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class TileSimpleProcessor<IT, OT, I extends IRcpIn<IT>, O extends IRcpOut<OT>, R extends IRcp<IT, I, O>>
        extends TileMachine implements IAllocableSides<SlotType.BasicIO>, IDroppableInventory, IAutoExporting {

    private final Class<R> recipeType;

    @AutoSerialize(sync = false)
    private final L9AspectSlot slotUpgrade = new TileSimpleProcessor.UpgradeSlot(this);
    @AutoSerialize
    private final IDatum.OfBool autoExporting = IDatum.ofBool(false);

    private TriBool canWork = TriBool.NONE;
    @Nullable
    private R activeRecipe = null;
    @Nullable
    private O activeRecipeOutput = null;
    private int maxWork = -1;
    private int energyCost = -1;

    public TileSimpleProcessor(Class<R> recipeType, int energyBuffer) {
        super(energyBuffer);
        this.recipeType = recipeType;
    }

    @Nullable
    public R getActiveRecipe() {
        return activeRecipe;
    }

    protected abstract IAllocableSides<SlotType.BasicIO> getSidedIo();

    @Override
    public void setFace(BlockSide face, SlotType.BasicIO state) {
        getSidedIo().setFace(face, state);
        setDirty();
    }

    @Override
    public SlotType.BasicIO getFace(BlockSide face) {
        return getSidedIo().getFace(face);
    }

    @Override
    public boolean isAutoExporting() {
        return autoExporting.isTrue();
    }

    @Override
    public void setAutoExporting(boolean exporting) {
        if (autoExporting.isTrue() != exporting) {
            autoExporting.setBool(exporting);
            setDirty();
        }
    }

    @Override
    protected void tick() {
        super.tick();
        if (!world.isRemote && autoExporting.isTrue() && world.getTotalWorldTime() % 16 == 0) {
            doAutoExporting();
        }
    }

    private void doAutoExporting() {
        boolean somethingChanged = false;
        IItemHandlerModifiable outputs = getOutputInventory();
        if (outputs.getSlots() <= 0) {
            return;
        }
        int slotIndex = 0;
        while (outputs.getStackInSlot(slotIndex).isEmpty()) {
            if (++slotIndex >= outputs.getSlots()) {
                return;
            }
        }
        EnumFacing front = getFrontFace();
        IAllocableSides<SlotType.BasicIO> sides = getSidedIo();
        for (BlockSide side : BlockSide.values()) {
            if (!sides.getFace(side).allowsOutput) {
                continue;
            }
            EnumFacing dir = side.getDirection(front);
            TileEntity adjTile = WorldUtils.getAdjacentTile(this, dir);
            if (adjTile == null || !adjTile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite())) {
                continue;
            }
            ItemStack toExport = outputs.getStackInSlot(slotIndex);
            IItemHandler adjInv = Objects.requireNonNull(
                    adjTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite()));
            ItemStack remaining = ItemHandlerHelper.insertItem(adjInv, toExport, false);
            if (toExport.getCount() != remaining.getCount()) {
                outputs.setStackInSlot(slotIndex, remaining);
                somethingChanged = true;
                if (remaining.isEmpty() && ++slotIndex >= outputs.getSlots()) {
                    break;
                }
            }
        }
        if (somethingChanged) {
            setDirty();
        }
    }

    @Override
    protected boolean canWork() {
        if (canWork == TriBool.NONE) {
            IT input = getInput();
            R newRecipe = LibNine.PROXY.getRecipeManager().getRecipeList(recipeType).findRecipe(input);
            if (newRecipe != null) {
                if (newRecipe != activeRecipe) {
                    activeRecipe = newRecipe;
                    resetWork();
                }
                activeRecipeOutput = activeRecipe.mapToOutput(input);
                canWork = TriBool.wrap(activeRecipeOutput.isAcceptable(getOutputEnvironment()));
            } else {
                activeRecipe = null;
                canWork = TriBool.FALSE;
            }
        }
        return canWork.value;
    }

    @Override
    protected int getDeltaWork() {
        return 1;
    }

    @Override
    protected int getMaxWork() {
        if (maxWork == -1) {
            maxWork = getBaseMaxWork() - getUpgradeReducedWork();
        }
        return maxWork;
    }

    @Override
    protected int getEnergyCost() {
        if (energyCost == -1) {
            energyCost = (getBaseEnergyPerOperation() + getUpgradeEnergyCost()) / getMaxWork();
        }
        return energyCost;
    }

    protected int getUpgradeCount() {
        ItemStack stack = slotUpgrade.getStackInSlot();
        return stack.isEmpty() ? 0 : stack.getCount();
    }

    @Override
    protected void onWorkFinished() {
        acceptOutput(
                Objects.requireNonNull(activeRecipe).input().consume(getInput()),
                Objects.requireNonNull(activeRecipeOutput));
    }

    protected void markWorkStateDirty() {
        canWork = TriBool.NONE;
    }

    protected abstract int getBaseEnergyPerOperation();

    protected abstract int getUpgradeEnergyCost();

    protected abstract int getBaseMaxWork();

    protected abstract int getUpgradeReducedWork();

    protected abstract IT getInput();

    protected abstract OT getOutputEnvironment();

    protected abstract void acceptOutput(IT newInputs, O output);

    protected abstract IItemHandlerModifiable getOutputInventory();

    public L9AspectSlot getUpgradeSlot() {
        return slotUpgrade;
    }

    @Override
    public void collectDrops(Accrue<ItemStack> drops) {
        InventoryUtils.accrue(drops, slotUpgrade);
    }

    private static class UpgradeSlot extends L9AspectSlot.Observable {

        UpgradeSlot(TileSimpleProcessor<?, ?, ?, ?, ?> tile) {
            super(AppEngUtils.IS_UPGRADE_ACCEL, (s, o, n) -> tile.maxWork = tile.energyCost = -1);
        }

        @Override
        public int getSlotLimit() {
            return 8;
        }

    }

}
