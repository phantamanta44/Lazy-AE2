package io.github.phantamanta44.threng.tile.base;

import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.libnine.util.data.serialization.IDatum;
import net.minecraft.util.EnumFacing;

public abstract class TileNetworkDevice extends TileAENetworked implements IDirectionable, IActivable {

    @AutoSerialize
    private final IDatum<EnumFacing> frontFace = IDatum.of(EnumFacing.NORTH);
    @AutoSerialize
    private final IDatum.OfBool active = IDatum.ofBool(false);

    private EnumFacing clientFace = EnumFacing.NORTH;
    private boolean clientActive = false;

    @Override
    public boolean isActive() {
        return active.isTrue();
    }

    @Override
    public EnumFacing getFrontFace() {
        return frontFace.get();
    }

    @Override
    public void setFrontFace(EnumFacing face) {
        frontFace.set(face);
        setDirty();
    }

    @MENetworkEventSubscribe
    public void onPowerStatusChange(final MENetworkPowerStatusChange event) {
        boolean nowActive = getProxy().isActive();
        if (active.isTrue() != nowActive) {
            active.setBool(nowActive);
            setDirty();
        }
    }

    @Override
    public void deserBytes(ByteUtils.Reader data) {
        super.deserBytes(data);
        EnumFacing front = frontFace.get();
        if (clientFace != front) {
            world.markBlockRangeForRenderUpdate(pos, pos);
            clientFace = front;
        }
        boolean isActive = active.isTrue();
        if (clientActive != isActive) {
            world.markBlockRangeForRenderUpdate(pos, pos);
            clientActive = isActive;
        }
    }

}
