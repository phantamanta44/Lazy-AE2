package io.github.phantamanta44.threng.tile;

import io.github.phantamanta44.threng.tile.base.TilePowered;

public class TileAggregator extends TilePowered {

    private static final int ENERGY_MAX = 80000;

    public TileAggregator() {
        super(ENERGY_MAX);
        setInitialized();
    }

    @Override
    protected void tick() {

    }

}
