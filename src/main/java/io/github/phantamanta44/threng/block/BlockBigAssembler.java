package io.github.phantamanta44.threng.block;

import appeng.util.Platform;
import io.github.phantamanta44.libnine.block.L9BlockStated;
import io.github.phantamanta44.libnine.component.multiblock.MultiBlockCore;
import io.github.phantamanta44.libnine.item.L9ItemBlock;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.libnine.util.world.WorldUtils;
import io.github.phantamanta44.threng.ThrEng;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.inventory.ThrEngGuis;
import io.github.phantamanta44.threng.item.block.ItemBlockBigAssembler;
import io.github.phantamanta44.threng.tile.TileBigAssemblerCore;
import io.github.phantamanta44.threng.tile.TileBigAssemblerPart;
import io.github.phantamanta44.threng.tile.TileBigAssemblerPatternStore;
import io.github.phantamanta44.threng.tile.base.IActivable;
import io.github.phantamanta44.threng.tile.base.IBigAssemblerUnit;
import io.github.phantamanta44.threng.tile.base.IDroppableInventory;
import io.github.phantamanta44.threng.tile.base.TileAENetworked;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

public class BlockBigAssembler extends L9BlockStated {

    public static final IProperty<Type> TYPE = PropertyEnum.create("type", Type.class);

    public BlockBigAssembler() {
        super(LangConst.BLOCK_BIG_ASSEMBLER, Material.IRON);
        setHardness(6F);
        setTileFactory((w, m) -> Type.getForMeta(m).createTileEntity());
    }

    @Override
    protected void accrueProperties(Accrue<IProperty<?>> props) {
        props.accept(TYPE);
    }

    @Override
    protected void accrueVolatileProperties(Accrue<IProperty<?>> props) {
        props.accept(ThrEngProps.ACTIVE);
    }

    @Override
    protected L9ItemBlock initItemBlock() {
        return new ItemBlockBigAssembler(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(ThrEngProps.ACTIVE, Objects.<IActivable>requireNonNull(getTileEntity(world, pos)).isActive());
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue(IBlockState state) {
        switch (state.getValue(TYPE)) {
            case MODULE_CPU:
            case MODULE_PATTERN:
                return 11;
            default:
                return 0;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = Objects.requireNonNull(getTileEntity(world, pos));
        if (tile instanceof TileAENetworked && placer instanceof EntityPlayer) {
            ((TileAENetworked)tile).getProxy().setOwner((EntityPlayer)placer);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack held = player.getHeldItem(hand);
        if (Platform.isWrench(player, held, pos)) {
            if (player.isSneaking()) {
                if (!world.isRemote) {
                    dropBlockAsItem(world, pos, state, 0);
                    world.setBlockToAir(pos);
                }
                return true;
            }
        }
        IBigAssemblerUnit tile = getTileEntity(world, pos);
        if (tile != null) {
            MultiBlockCore<IBigAssemblerUnit> core = tile.getMultiBlockConnection().getCore();
            if (core != null) {
                if (core.getUnit().isActive()) {
                    if (!world.isRemote) {
                        ThrEng.INSTANCE.getGuiHandler().openGui(player, ThrEngGuis.BIG_ASSEMBLER, core.getUnit().getWorldPos());
                    }
                    return true;
                } else if (tile instanceof TileBigAssemblerCore) {
                    if (!world.isRemote) {
                        ((TileBigAssemblerCore)tile).tryAssemble(player);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = getTileEntity(world, pos);
        if (tile != null) {
            ((IBigAssemblerUnit)tile).getMultiBlockConnection().disconnect();
            if (tile instanceof IDroppableInventory) {
                ArrayList<ItemStack> drops = new ArrayList<>();
                ((IDroppableInventory)tile).collectDrops(new Accrue<>(drops));
                drops.forEach(stack -> WorldUtils.dropItem(world, pos, stack));
            }
        }
        super.breakBlock(world, pos, state);
    }

    public enum Type implements IStringSerializable {

        FRAME(TileBigAssemblerPart::new),
        VENT(TileBigAssemblerPart::new),
        CONTROLLER(TileBigAssemblerCore::new),
        MODULE_PATTERN(TileBigAssemblerPatternStore::new),
        MODULE_CPU(TileBigAssemblerPart::new);

        private static final Type[] VALUES = values();

        public static Type getForMeta(int meta) {
            return VALUES[meta];
        }

        private final Supplier<TileEntity> tileFactory;

        Type(Supplier<TileEntity> tileFactory) {
            this.tileFactory = tileFactory;
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }

        public int getMeta() {
            return ordinal();
        }

        public ItemStack newStack(int count) {
            return new ItemStack(ThrEngBlocks.BIG_ASSEMBLER, count, getMeta());
        }

        TileEntity createTileEntity() {
            return tileFactory.get();
        }

    }

}
