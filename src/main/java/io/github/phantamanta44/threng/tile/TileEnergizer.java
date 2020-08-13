package io.github.phantamanta44.threng.tile;

import io.github.phantamanta44.libnine.capability.impl.L9AspectSlot;
import io.github.phantamanta44.libnine.capability.provider.CapabilityBrokerDirPredicated;
import io.github.phantamanta44.libnine.recipe.output.ItemStackOutput;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.libnine.util.tuple.IPair;
import io.github.phantamanta44.libnine.util.world.IAllocableSides;
import io.github.phantamanta44.libnine.util.world.SideAlloc;
import io.github.phantamanta44.threng.ThrEngConfig;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import io.github.phantamanta44.threng.recipe.EnergizeRecipe;
import io.github.phantamanta44.threng.recipe.component.ItemEnergyInput;
import io.github.phantamanta44.threng.tile.base.TileSimpleProcessor;
import io.github.phantamanta44.threng.util.InvUtils;
import io.github.phantamanta44.threng.util.SlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@RegisterTile(ThrEngConst.MOD_ID)
public class TileEnergizer
        extends TileSimpleProcessor<IPair<ItemStack, Integer>, ItemStack, ItemEnergyInput, ItemStackOutput, EnergizeRecipe> {

    @AutoSerialize(sync = false)
    private final L9AspectSlot slotInput = new L9AspectSlot.Observable((s, o, n) -> markWorkStateDirty());
    @AutoSerialize(sync = false)
    private final L9AspectSlot slotOutput = new L9AspectSlot.Observable(is -> false, (s, o, n) -> markWorkStateDirty());
    @AutoSerialize
    private final SideAlloc<SlotType.BasicIO> sides = new SideAlloc<>(SlotType.BasicIO.NONE, this::getFrontFace);

    public TileEnergizer() {
        super(EnergizeRecipe.class, ThrEngConfig.processing.energizerEnergyBuffer);
        setInitialized();
    }

    @Override
    protected CapabilityBrokerDirPredicated initCapabilities() {
        return super.initCapabilities()
                .with(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, slotInput, sides.getPredicate(SlotType.BasicIO.INPUT))
                .with(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, slotOutput, sides.getPredicate(SlotType.BasicIO.OUTPUT));
    }

    @Override
    public IAllocableSides<SlotType.BasicIO> getSidedIo() {
        return sides;
    }

    @Override
    protected int getBaseEnergyPerOperation() {
        EnergizeRecipe recipe = getActiveRecipe();
        return recipe != null ? recipe.input().getEnergy() : 8100;
    }

    @Override
    protected int getUpgradeEnergyCost() {
        return ThrEngConfig.processing.energizerEnergyCostUpgrade * getUpgradeCount();
    }

    @Override
    protected int getBaseMaxWork() {
        return ThrEngConfig.processing.energizerWorkTicksBase;
    }

    @Override
    protected int getUpgradeReducedWork() {
        return ThrEngConfig.processing.energizerWorkTicksUpgrade * getUpgradeCount();
    }

    @Override
    protected IPair<ItemStack, Integer> getInput() {
        return IPair.of(slotInput.getStackInSlot(), energy.getQuantity());
    }

    @Override
    protected ItemStack getOutputEnvironment() {
        return slotOutput.getStackInSlot();
    }

    @Override
    protected void acceptOutput(IPair<ItemStack, Integer> newInputs, ItemStackOutput output) {
        slotInput.setStackInSlot(newInputs.getA()); // ignore the energy; it was already consumed to do the work
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
    public void collectDrops(Accrue<ItemStack> drops) {
        super.collectDrops(drops);
        InvUtils.accrue(drops, slotInput, slotOutput);
    }

}
