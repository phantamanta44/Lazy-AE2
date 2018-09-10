package io.github.phantamanta44.threng.inventory.base;

import io.github.phantamanta44.threng.tile.base.TileSimpleProcessor;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSimpleProcessor<T extends TileSimpleProcessor<?, ?, ?, ?, ?>> extends ContainerEnergized<T> {

    public ContainerSimpleProcessor(T tile, InventoryPlayer ipl) {
        super(tile, ipl);
        addSlotToContainer(new SlotItemHandler(tile.getUpgradeSlot(), 0, 146, 62));
    }

    public float getWorkFraction() {
        return tile.getWorkFraction();
    }

}
