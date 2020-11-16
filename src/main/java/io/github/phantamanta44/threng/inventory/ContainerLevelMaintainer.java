package io.github.phantamanta44.threng.inventory;

import appeng.container.slot.SlotFake;
import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.threng.client.gui.GuiLevelMaintainer;
import io.github.phantamanta44.threng.inventory.base.ContainerTile;
import io.github.phantamanta44.threng.tile.TileLevelMaintainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class ContainerLevelMaintainer extends ContainerTile<TileLevelMaintainer> {

    ContainerLevelMaintainer(EntityPlayer player, World world, int x, int y, int z) {
        super((TileLevelMaintainer)Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))),
                player.inventory, GuiLevelMaintainer.GUI_HEIGHT);
        TileLevelMaintainer.InventoryRequest reqInv = tile.getRequestInventory();
        for (int i = 0; i < TileLevelMaintainer.REQ_COUNT; i++) {
            addSlotToContainer(new SlotFake(reqInv, i, 17, 19 + 20 * i));
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (slotId >= 0 && slotId < inventorySlots.size()) {
            Slot slot = getSlot(slotId);
            if (slot instanceof SlotFake) {
                ItemStack hand = player.inventory.getItemStack();
                switch (clickType) {
                    case PICKUP:
                        slot.putStack(hand.isEmpty() ? ItemStack.EMPTY : hand.copy());
                        break;
                    case QUICK_MOVE:
                        slot.putStack(ItemStack.EMPTY);
                        break;
                }
                return ItemStack.EMPTY;
            }
        }
        return super.slotClick(slotId, dragType, clickType, player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        return ItemStack.EMPTY;
    }

    public long getRequestQuantity(int index) {
        return tile.getRequestInventory().getQuantity(index);
    }

    public void updateRequestQuantity(int index, long quantity) {
        sendInteraction(ByteUtils.writer().writeByte((byte)0).writeInt(index).writeLong(quantity).toArray());
    }

    public long getBatchSize(int index) {
        return tile.getRequestInventory().getBatchSize(index);
    }

    public void updateBatchSize(int index, long quantity) {
        sendInteraction(ByteUtils.writer().writeByte((byte)1).writeInt(index).writeLong(quantity).toArray());
    }

    @Override
    public void handleClientInteraction(ByteUtils.Reader data) {
        switch (data.readByte()) {
            case 0:
                tile.getRequestInventory().updateQuantity(data.readInt(), data.readLong());
                break;
            case 1:
                tile.getRequestInventory().updateBatchSize(data.readInt(), data.readLong());
                break;
        }
    }

}
