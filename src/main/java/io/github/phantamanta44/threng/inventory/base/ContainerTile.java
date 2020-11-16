package io.github.phantamanta44.threng.inventory.base;

import io.github.phantamanta44.libnine.gui.L9Container;
import io.github.phantamanta44.libnine.util.data.ByteUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public abstract class ContainerTile<T extends TileEntity> extends L9Container {

    protected final T tile;

    public ContainerTile(T tile, InventoryPlayer ipl, int height) {
        super(ipl, height);
        this.tile = tile;
    }

    public ContainerTile(T tile, InventoryPlayer ipl) {
        this(tile, ipl, 166);
    }

    protected boolean isContainerValid() {
        return tile.hasWorld() && tile.getWorld().getTileEntity(tile.getPos()) == tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return isContainerValid();
    }

    @Override
    protected void sendInteraction(byte[] data) {
        if (isContainerValid()) {
            super.sendInteraction(data);
        }
    }

    @Override
    public final void onClientInteraction(ByteUtils.Reader data) { // implementors should use handleClientInteraction
        if (isContainerValid()) {
            handleClientInteraction(data);
        }
    }

    protected void handleClientInteraction(ByteUtils.Reader data) {
        super.onClientInteraction(data);
    }

}
