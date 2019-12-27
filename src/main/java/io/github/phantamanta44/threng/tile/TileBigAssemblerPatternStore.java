package io.github.phantamanta44.threng.tile;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingMedium;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import io.github.phantamanta44.libnine.capability.impl.L9AspectInventory;
import io.github.phantamanta44.libnine.component.multiblock.MultiBlockCore;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import io.github.phantamanta44.threng.tile.base.IBigAssemblerUnit;
import io.github.phantamanta44.threng.tile.base.IDroppableInventory;
import io.github.phantamanta44.threng.util.InvUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

@RegisterTile(ThrEngConst.MOD_ID)
public class TileBigAssemblerPatternStore extends TileBigAssemblerPart implements IDroppableInventory {

    @AutoSerialize
    private final L9AspectInventory patternInv = new L9AspectInventory.Observable(36, (i, o, n) -> {
        if (world != null) {
            if (!world.isRemote) {
                MultiBlockCore<IBigAssemblerUnit> core = multiBlock.getCore();
                if (core != null) {
                    ((TileBigAssemblerCore)core.getUnit()).notifyPatternUpdate();
                }
            }
        }
        setDirty();
    });

    public IItemHandlerModifiable getPatternInventory() {
        return patternInv;
    }

    public void providePatterns(ICraftingMedium core, ICraftingProviderHelper helper) {
        for (int i = 0; i < patternInv.getSlots(); i++) {
            ItemStack stack = patternInv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ICraftingPatternItem) {
                ICraftingPatternDetails pattern = ((ICraftingPatternItem)stack.getItem()).getPatternForItem(stack, world);
                if (pattern.isCraftable()) {
                    helper.addCraftingOption(core, pattern);
                }
            }
        }
    }

    @Override
    public void collectDrops(Accrue<ItemStack> drops) {
        InvUtils.accrue(drops, patternInv);
    }

}
