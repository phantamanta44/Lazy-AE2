package io.github.phantamanta44.threng.inventory;

import io.github.phantamanta44.threng.inventory.base.ContainerSimpleProcessor;
import io.github.phantamanta44.threng.tile.TileEtcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class ContainerEtcher extends ContainerSimpleProcessor<TileEtcher> {

    ContainerEtcher(EntityPlayer player, World world, int x, int y, int z) {
        super(Objects.requireNonNull((TileEtcher)world.getTileEntity(new BlockPos(x, y, z))), player.inventory);
        addSlotToContainer(new SlotItemHandler(tile.getInputSlots(), 0, 37, 17));
        addSlotToContainer(new SlotItemHandler(tile.getInputSlots(), 1, 37, 53));
        addSlotToContainer(new SlotItemHandler(tile.getInputSlots(), 2, 60, 35));
        addSlotToContainer(new SlotItemHandler(tile.getOutputSlot(), 0, 120, 35));
    }

}
