package io.github.phantamanta44.threng.inventory.base;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Objects;

public abstract class ContainerEnergized<T extends TileEntity> extends ContainerTile<T> {

    public ContainerEnergized(T tile, InventoryPlayer ipl) {
        super(tile, ipl);
    }

    public IEnergyStorage getEnergyStorage() {
        return Objects.requireNonNull(tile.getCapability(CapabilityEnergy.ENERGY, null));
    }

}
