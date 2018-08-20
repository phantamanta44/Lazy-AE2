package io.github.phantamanta44.threng.block;

import io.github.phantamanta44.libnine.block.L9BlockStated;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.tile.base.IActivable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockMachine extends L9BlockStated {

    public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

    public BlockMachine() {
        super(LangConst.BLOCK_MACHINE, Material.IRON);
        setHardness(6.0F);
        setTileFactory((w, m) -> Type.fromMeta(m).createTile());
    }

    @Override
    protected void accrueProperties(Accrue<IProperty<?>> props) {
        props.accept(TYPE);
    }

    @Override
    protected void accrueVolatileProperties(Accrue<IProperty<?>> props) {
        props.accept(ThrEngProps.ACTIVE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof IActivable
                ? state.withProperty(ThrEngProps.ACTIVE, ((IActivable)tile).isActive())
                : state;
    }

    public enum Type implements IStringSerializable {

        AGGREGATOR,
        CENTRIFUGE,
        ETCHER;

        public TileEntity createTile() {

        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }

        public static Type fromMeta(int meta) {
            return values()[meta];
        }

    }

}
