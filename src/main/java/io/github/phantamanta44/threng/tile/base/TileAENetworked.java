package io.github.phantamanta44.threng.tile.base;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.me.helpers.MachineSource;
import io.github.phantamanta44.libnine.tile.L9TileEntityTicking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public abstract class TileAENetworked extends L9TileEntityTicking implements IActionHost, IGridProxyable {

    protected final IActionSource actionSource = new MachineSource(this);
    private final AENetworkProxy aeProxy = new AENetworkProxy(this, "aeproxy", getNetworkRepresentation(), true);
    private boolean proxyReadied = false;

    public TileAENetworked() {
        setInitialized();
    }

    @Nullable
    protected abstract ItemStack getNetworkRepresentation();

    protected void initProxy(AENetworkProxy proxy) {
        // NO-OP
    }

    @Override
    public AENetworkProxy getProxy() {
        return aeProxy;
    }

    @Nullable
    @Override
    public IGridNode getGridNode(@Nonnull AEPartLocation aePartLocation) {
        return getProxy().getNode();
    }

    public Optional<IGrid> aeGrid() {
        return Optional.ofNullable(getProxy().getNode()).map(IGridNode::getGrid);
    }

    @Nonnull
    @Override
    public AECableType getCableConnectionType(@Nonnull AEPartLocation aePartLocation) {
        return AECableType.SMART;
    }

    @Nonnull
    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    protected void tick() {
        if (!proxyReadied) {
            proxyReadied = true;
            getProxy().onReady();
            initProxy(getProxy());
        }
    }

    @Override
    public void gridChanged() {
        // NO-OP
    }

    @Override
    public void securityBreak() {
        world.destroyBlock(pos, true);
    }

    @Override
    public IGridNode getActionableNode() {
        return getProxy().getNode();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        getProxy().onChunkUnload();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        getProxy().invalidate();
    }

    @Override
    public void validate() {
        super.validate();
        getProxy().validate();
    }

    @Override
    public void deserNBT(NBTTagCompound nbt) {
        super.deserNBT(nbt);
        getProxy().readFromNBT(nbt);
    }

    @Override
    public void serNBT(NBTTagCompound tag) {
        super.serNBT(tag);
        getProxy().writeToNBT(tag);
    }

}
