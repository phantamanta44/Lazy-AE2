package io.github.phantamanta44.threng.inventory.base;

import io.github.phantamanta44.libnine.gui.L9Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Objects;

public abstract class ContainerEnergized<T extends TileEntity> extends L9Container {

    protected final T tile;

    public ContainerEnergized(T tile, InventoryPlayer ipl) {
        super(ipl);
        this.tile = tile;
    }

    public IEnergyStorage getEnergyStorage() {
        return Objects.requireNonNull(tile.getCapability(CapabilityEnergy.ENERGY, null));
    }

}
