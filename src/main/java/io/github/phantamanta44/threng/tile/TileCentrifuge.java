package io.github.phantamanta44.threng.tile;

import io.github.phantamanta44.libnine.capability.impl.L9AspectSlot;
import io.github.phantamanta44.libnine.capability.provider.CapabilityBrokerDirPredicated;
import io.github.phantamanta44.libnine.recipe.input.ItemStackInput;
import io.github.phantamanta44.libnine.recipe.output.ItemStackOutput;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.libnine.util.helper.InventoryUtils;
import io.github.phantamanta44.libnine.util.world.IAllocableSides;
import io.github.phantamanta44.libnine.util.world.SideAlloc;
import io.github.phantamanta44.threng.ThrEngConfig;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import io.github.phantamanta44.threng.recipe.PurifyRecipe;
import io.github.phantamanta44.threng.tile.base.TileSimpleProcessor;
import io.github.phantamanta44.threng.util.ConjoinedItemHandler;
import io.github.phantamanta44.threng.util.InsertOnlyItemHandler;
import io.github.phantamanta44.threng.util.SlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

@RegisterTile(ThrEngConst.MOD_ID)
public class TileCentrifuge extends TileSimpleProcessor<ItemStack, ItemStack, ItemStackInput, ItemStackOutput, PurifyRecipe> {

    @AutoSerialize(sync = false)
    private final L9AspectSlot slotInput = new L9AspectSlot.Observable((s, o, n) -> markWorkStateDirty());
    @AutoSerialize(sync = false)
    private final L9AspectSlot slotOutput = new L9AspectSlot.Observable(is -> false, (s, o, n) -> markWorkStateDirty());
    @AutoSerialize
    private final SideAlloc<SlotType.BasicIO> sides = new SideAlloc<>(SlotType.BasicIO.NONE, this::getFrontFace);

    public TileCentrifuge() {
        super(PurifyRecipe.class, ThrEngConfig.processing.centrifugeEnergyBuffer);
        setInitialized();
    }

    @Override
    protected CapabilityBrokerDirPredicated initCapabilities() {
        InsertOnlyItemHandler inputs = new InsertOnlyItemHandler(slotInput);
        return super.initCapabilities()
                .with(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inputs, sides.getPredicate(SlotType.BasicIO.INPUT))
                .with(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, slotOutput, sides.getPredicate(SlotType.BasicIO.OUTPUT))
                .with(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                        new ConjoinedItemHandler(inputs, slotOutput), sides.getPredicate(SlotType.BasicIO.OMNI));
    }

    @Override
    public IAllocableSides<SlotType.BasicIO> getSidedIo() {
        return sides;
    }

    @Override
    protected int getBaseEnergyPerOperation() {
        return ThrEngConfig.processing.centrifugeEnergyCostBase;
    }

    @Override
    protected int getUpgradeEnergyCost() {
        return ThrEngConfig.processing.centrifugeEnergyCostUpgrade * getUpgradeCount();
    }

    @Override
    protected int getBaseMaxWork() {
        return ThrEngConfig.processing.centrifugeWorkTicksBase;
    }

    @Override
    protected int getUpgradeReducedWork() {
        return ThrEngConfig.processing.centrifugeWorkTicksUpgrade * getUpgradeCount();
    }

    @Override
    protected ItemStack getInput() {
        return slotInput.getStackInSlot();
    }

    @Override
    protected ItemStack getOutputEnvironment() {
        return slotOutput.getStackInSlot();
    }

    @Override
    protected void acceptOutput(ItemStack newInputs, ItemStackOutput output) {
        slotInput.setStackInSlot(newInputs);
        if (slotOutput.getStackInSlot().isEmpty()) {
            slotOutput.setStackInSlot(output.getOutput().copy());
        } else {
            slotOutput.getStackInSlot().grow(output.getOutput().getCount());
        }
    }

    public IItemHandler getInputSlot() {
        return slotInput;
    }

    public IItemHandler getOutputSlot() {
        return slotOutput;
    }

    @Override
    protected IItemHandlerModifiable getOutputInventory() {
        return slotOutput;
    }

    @Override
    public void collectDrops(Accrue<ItemStack> drops) {
        super.collectDrops(drops);
        InventoryUtils.accrue(drops, slotInput, slotOutput);
    }

}
