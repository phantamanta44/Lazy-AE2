package io.github.phantamanta44.threng.inventory;

import io.github.phantamanta44.threng.inventory.base.ContainerSimpleProcessor;
import io.github.phantamanta44.threng.tile.TileCentrifuge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class ContainerCentrifuge extends ContainerSimpleProcessor<TileCentrifuge> {

    ContainerCentrifuge(EntityPlayer player, World world, int x, int y, int z) {
        super(Objects.requireNonNull((TileCentrifuge)world.getTileEntity(new BlockPos(x, y, z))), player.inventory);
        addSlotToContainer(new SlotItemHandler(tile.getInputSlot(), 0, 56, 35));
        addSlotToContainer(new SlotItemHandler(tile.getOutputSlot(), 0, 116, 35));
    }

}
