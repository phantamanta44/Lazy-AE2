package io.github.phantamanta44.threng.inventory;

import io.github.phantamanta44.threng.inventory.base.ContainerSimpleProcessor;
import io.github.phantamanta44.threng.tile.TileEnergizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class ContainerEnergizer extends ContainerSimpleProcessor<TileEnergizer> {

    ContainerEnergizer(EntityPlayer player, World world, int x, int y, int z) {
        super(Objects.requireNonNull((TileEnergizer)world.getTileEntity(new BlockPos(x, y, z))), player.inventory);
        addSlotToContainer(new SlotItemHandler(tile.getInputSlot(), 0, 56, 35));
        addSlotToContainer(new SlotItemHandler(tile.getOutputSlot(), 0, 116, 35));
    }

}
