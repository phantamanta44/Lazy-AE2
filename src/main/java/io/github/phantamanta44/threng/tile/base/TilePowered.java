package io.github.phantamanta44.threng.tile.base;

import io.github.phantamanta44.libnine.capability.impl.L9AspectEnergy;
import io.github.phantamanta44.libnine.capability.provider.CapabilityBrokerDirPredicated;
import io.github.phantamanta44.libnine.component.reservoir.IIntReservoir;
import io.github.phantamanta44.libnine.component.reservoir.RatedIntReservoir;
import io.github.phantamanta44.libnine.component.reservoir.SimpleIntReservoir;
import io.github.phantamanta44.libnine.tile.L9TileEntityTicking;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

public abstract class TilePowered extends L9TileEntityTicking {

    @AutoSerialize
    protected final IIntReservoir energy;

    public TilePowered(int energyBuffer) {
        this.energy = new SimpleIntReservoir(energyBuffer);
    }

    @Override
    protected ICapabilityProvider initCapabilities() {
        return new CapabilityBrokerDirPredicated()
                .with(CapabilityEnergy.ENERGY, new L9AspectEnergy(new RatedIntReservoir(energy, -1, 0)));
    }

}
