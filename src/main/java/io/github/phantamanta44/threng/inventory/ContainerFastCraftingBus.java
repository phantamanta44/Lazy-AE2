package io.github.phantamanta44.threng.inventory;

import io.github.phantamanta44.libnine.gui.L9Container;
import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.libnine.util.world.BlockSide;
import io.github.phantamanta44.libnine.util.world.IAllocableSides;
import io.github.phantamanta44.threng.tile.TileFastCraftingBus;
import io.github.phantamanta44.threng.util.SlotType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class ContainerFastCraftingBus extends L9Container implements IAllocableSides<SlotType.BasicIO> {

    private final TileFastCraftingBus tile;

    ContainerFastCraftingBus(EntityPlayer player, World world, int x, int y, int z) {
        super(player.inventory);
        this.tile = (TileFastCraftingBus)Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z)));

        // pattern slots
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new SlotItemHandler(tile.getPatternInventory(), i, 8 + 18 * i, 62));
        }

        // import buffer
        for (int yPos = 0; yPos < 3; yPos++) {
            for (int xPos = 0; xPos < 3; xPos++) {
                addSlotToContainer(new SlotItemHandler(
                        tile.getImportInventory(), yPos * 3 + xPos, 62 + xPos * 18, 8 + yPos * 18));
            }
        }

        // export buffer
        for (int yPos = 0; yPos < 3; yPos++) {
            for (int xPos = 0; xPos < 3; xPos++) {
                addSlotToContainer(new SlotItemHandler(
                        tile.getExportInventory(), yPos * 3 + xPos, 116 + xPos * 18, 8 + yPos * 18));
            }
        }
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
    public void onClientInteraction(ByteUtils.Reader data) {
        byte opcode = data.readByte();
        if (opcode == 0) {
            tile.setFace(BlockSide.values()[data.readByte()], SlotType.BasicIO.values()[data.readByte()]);
        } else {
            throw new IllegalStateException("Unknown opcode: " + opcode);
        }
    }

}
