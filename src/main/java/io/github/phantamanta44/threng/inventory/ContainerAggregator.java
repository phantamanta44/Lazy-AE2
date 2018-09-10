package io.github.phantamanta44.threng.inventory;

import io.github.phantamanta44.threng.inventory.base.ContainerSimpleProcessor;
import io.github.phantamanta44.threng.tile.TileAggregator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class ContainerAggregator extends ContainerSimpleProcessor<TileAggregator> {

    ContainerAggregator(EntityPlayer player, World world, int x, int y, int z) {
        super(Objects.requireNonNull((TileAggregator)world.getTileEntity(new BlockPos(x, y, z))), player.inventory);
        for (int i = 0; i < 3; i++) {
            addSlotToContainer(new SlotItemHandler(tile.getInputSlots(), i, 28 + i * 20, 35));
        }
        addSlotToContainer(new SlotItemHandler(tile.getOutputSlot(), 0, 128, 35));
    }

}
