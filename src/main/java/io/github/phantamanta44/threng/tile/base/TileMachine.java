package io.github.phantamanta44.threng.tile.base;

import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.libnine.util.data.serialization.IDatum;
import net.minecraft.util.EnumFacing;

public abstract class TileMachine extends TilePowered implements IActivable, IDirectionable {

    @AutoSerialize
    private final IDatum<EnumFacing> frontFace = IDatum.of(EnumFacing.NORTH);
    @AutoSerialize
    private final IDatum.OfInt work = IDatum.ofInt(0);

    private boolean couldWorkLastTick = false;
    private boolean working = false;
    private EnumFacing clientFace = EnumFacing.NORTH;

    public TileMachine(int energyBuffer) {
        super(energyBuffer);
    }

    @Override
    protected void tick() {
        boolean wasWorking = working;
        if (canWork()) {
            int energyCost = getEnergyCost();
            if (energy.getQuantity() > energyCost) {
                if (!world.isRemote) {
                    energy.draw(energyCost, true);
                    if (work.preincrement(getDeltaWork()) > getMaxWork()) {
                        work.setInt(0);
                        onWorkFinished();
                    }
                }
                working = true;
                setDirty();
            } else {
                working = false;
            }
            couldWorkLastTick = true;
        } else if (couldWorkLastTick) {
            couldWorkLastTick = false;
            if (!world.isRemote) work.setInt(0);
            working = false;
            setDirty();
        }
        if (working != wasWorking) {
            world.markBlockRangeForRenderUpdate(pos, pos);
        }
    }

    protected abstract int getEnergyCost();

    protected abstract boolean canWork();

    protected abstract int getDeltaWork();

    protected abstract int getMaxWork();

    protected abstract void onWorkFinished();

    public int getWork() {
        return work.getInt();
    }

    public float getWorkFraction() {
        return Math.min((float)getWork() / getMaxWork(), 1F);
    }

    @Override
    public boolean isActive() {
        return working;
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

    @Override
    public void deserBytes(ByteUtils.Reader data) {
        super.deserBytes(data);
        EnumFacing front = frontFace.get();
        if (clientFace != front) {
            world.markBlockRangeForRenderUpdate(pos, pos);
            clientFace = front;
        }
    }

}
