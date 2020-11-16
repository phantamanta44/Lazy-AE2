package io.github.phantamanta44.threng.inventory.base;

import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.libnine.util.world.BlockSide;
import io.github.phantamanta44.libnine.util.world.IAllocableSides;
import io.github.phantamanta44.threng.tile.base.IAutoExporting;
import io.github.phantamanta44.threng.tile.base.TileSimpleProcessor;
import io.github.phantamanta44.threng.util.SlotType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;

public abstract class ContainerSimpleProcessor<T extends TileSimpleProcessor<?, ?, ?, ?, ?>> extends ContainerEnergized<T>
        implements IAllocableSides<SlotType.BasicIO>, IAutoExporting {

    public ContainerSimpleProcessor(T tile, InventoryPlayer ipl) {
        super(tile, ipl);
        addSlotToContainer(new SlotItemHandler(tile.getUpgradeSlot(), 0, 146, 62));
    }

    public float getWorkFraction() {
        return tile.getWorkFraction();
    }

    @Override
    public void setFace(BlockSide face, SlotType.BasicIO state) {
        sendInteraction(new byte[] { 0, (byte)face.ordinal(), (byte)state.ordinal() });
    }

    @Override
    public SlotType.BasicIO getFace(BlockSide face) {
        return tile.getFace(face);
    }

    @Override
    public boolean isAutoExporting() {
        return tile.isAutoExporting();
    }

    @Override
    public void setAutoExporting(boolean exporting) {
        sendInteraction(new byte[] { 1, exporting ? (byte)1 : (byte)0 });
    }

    @Override
    public void handleClientInteraction(ByteUtils.Reader data) {
        byte opcode = data.readByte();
        switch (opcode) {
            case 0: // setFace
                tile.setFace(BlockSide.values()[data.readByte()], SlotType.BasicIO.get(data.readByte()));
                break;
            case 1: // setAutoExporting
                tile.setAutoExporting(data.readByte() != 0);
                break;
            default:
                throw new IllegalStateException("Unknown opcode: " + opcode);
        }
    }

}
