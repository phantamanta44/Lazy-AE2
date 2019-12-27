package io.github.phantamanta44.threng.tile;

import io.github.phantamanta44.libnine.component.multiblock.MultiBlockComponent;
import io.github.phantamanta44.libnine.component.multiblock.MultiBlockConnectable;
import io.github.phantamanta44.libnine.component.multiblock.MultiBlockType;
import io.github.phantamanta44.libnine.tile.L9TileEntity;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import io.github.phantamanta44.libnine.util.TriBool;
import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import io.github.phantamanta44.threng.multiblock.ThrEngMultiBlocks;
import io.github.phantamanta44.threng.tile.base.IBigAssemblerUnit;

@RegisterTile(ThrEngConst.MOD_ID)
public class TileBigAssemblerPart extends L9TileEntity implements IBigAssemblerUnit {

    @AutoSerialize
    protected final MultiBlockComponent<IBigAssemblerUnit> multiBlock
            = new MultiBlockComponent<>(this, ThrEngMultiBlocks.BIG_ASSEMBLER);

    private TriBool clientCachedCxnStatus = TriBool.NONE;

    public TileBigAssemblerPart() {
        markRequiresSync();
        multiBlock.onConnectionStatusChange(this::setDirty);
    }

    @Override
    public MultiBlockType<IBigAssemblerUnit> getMultiBlockType() {
        return ThrEngMultiBlocks.BIG_ASSEMBLER;
    }

    @Override
    public MultiBlockConnectable<IBigAssemblerUnit> getMultiBlockConnection() {
        return multiBlock;
    }

    @Override
    public boolean isActive() {
        return multiBlock.isConnected();
    }

    @Override
    public void deserBytes(ByteUtils.Reader data) {
        super.deserBytes(data);
        TriBool cxnStatus = TriBool.wrap(multiBlock.isConnected());
        if (clientCachedCxnStatus != cxnStatus) {
            clientCachedCxnStatus = cxnStatus;
            world.markBlockRangeForRenderUpdate(pos, pos);
        }
    }

}
