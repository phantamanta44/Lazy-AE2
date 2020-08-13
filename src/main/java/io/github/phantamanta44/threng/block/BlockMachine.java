package io.github.phantamanta44.threng.block;

import appeng.util.Platform;
import io.github.phantamanta44.libnine.block.L9BlockStated;
import io.github.phantamanta44.libnine.gui.GuiIdentity;
import io.github.phantamanta44.libnine.item.L9ItemBlock;
import io.github.phantamanta44.libnine.tile.L9TileEntity;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.libnine.util.world.WorldBlockPos;
import io.github.phantamanta44.libnine.util.world.WorldUtils;
import io.github.phantamanta44.threng.ThrEng;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.inventory.ThrEngGuis;
import io.github.phantamanta44.threng.item.block.ItemBlockMachine;
import io.github.phantamanta44.threng.tile.*;
import io.github.phantamanta44.threng.tile.base.*;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

public class BlockMachine extends L9BlockStated {

    public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

    public BlockMachine() {
        super(LangConst.BLOCK_MACHINE, Material.IRON);
        setHardness(6.0F);
        setTileFactory((w, m) -> Type.fromMeta(m).createTile());
    }

    @Override
    protected L9ItemBlock initItemBlock() {
        return new ItemBlockMachine(this);
    }

    @Override
    protected void accrueProperties(Accrue<IProperty<?>> props) {
        props.accept(TYPE);
    }

    @Override
    protected void accrueVolatileProperties(Accrue<IProperty<?>> props) {
        props.acceptAll(ThrEngProps.ACTIVE, ThrEngProps.ROTATION);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = Objects.requireNonNull(world.getTileEntity(pos));
        return state
                .withProperty(ThrEngProps.ACTIVE, ((IActivable)tile).isActive())
                .withProperty(ThrEngProps.ROTATION, ((IDirectionable)tile).getFrontFace());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = Objects.requireNonNull(getTileEntity(world, pos));
        if (tile instanceof IDirectionable) {
            ((IDirectionable)tile).setFrontFace(placer.getHorizontalFacing().getOpposite());
        }
        if (tile instanceof TileAENetworked && placer instanceof EntityPlayer) {
            ((TileAENetworked)tile).getProxy().setOwner((EntityPlayer)placer);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if (player.isSneaking()) {
                ItemStack held = player.getHeldItem(hand);
                if (Platform.isWrench(player, held, pos)) {
                    dropBlockAsItem(world, pos, state, 0);
                    world.setBlockToAir(pos);
                    return true;
                }
            }
            ThrEng.INSTANCE.getGuiHandler().openGui(player, state.getValue(TYPE).gui, new WorldBlockPos(world, pos));
        }
        return true;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        TileMachine tile = Objects.requireNonNull(getTileEntity(world, pos));
        if (axis == EnumFacing.UP) {
            tile.setFrontFace(tile.getFrontFace().rotateY());
        } else if (axis == EnumFacing.DOWN) {
            tile.setFrontFace(tile.getFrontFace().rotateYCCW());
        } else {
            EnumFacing current = tile.getFrontFace();
            tile.setFrontFace(axis == current ? current.getOpposite() : axis);
        }
        return true;
    }

    @Nullable
    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return EnumFacing.HORIZONTALS;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = getTileEntity(world, pos);
        if (tile instanceof IDroppableInventory) {
            ArrayList<ItemStack> drops = new ArrayList<>();
            ((IDroppableInventory)tile).collectDrops(new Accrue<>(drops));
            drops.forEach(stack -> WorldUtils.dropItem(world, pos, stack));
        }
        super.breakBlock(world, pos, state);
    }

    public enum Type implements IStringSerializable {

        AGGREGATOR(TileAggregator::new, ThrEngGuis.AGGREGATOR),
        CENTRIFUGE(TileCentrifuge::new, ThrEngGuis.CENTRIFUGE),
        ETCHER(TileEtcher::new, ThrEngGuis.ETCHER),
        FAST_CRAFTER(TileFastCraftingBus::new, ThrEngGuis.FAST_CRAFTER),
        LEVEL_MAINTAINER(TileLevelMaintainer::new, ThrEngGuis.LEVEL_MAINTAINER),
        ENERGIZER(TileEnergizer::new, ThrEngGuis.ENERGIZER);

        public final GuiIdentity<?, ?> gui;

        private final Supplier<? extends L9TileEntity> factory;

        Type(Supplier<? extends L9TileEntity> factory, GuiIdentity<?, ?> gui) {
            this.factory = factory;
            this.gui = gui;
        }

        public TileEntity createTile() {
            return factory.get();
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }

        public static Type fromMeta(int meta) {
            return values()[meta];
        }

        public ItemStack newStack(int count) {
            return new ItemStack(ThrEngBlocks.MACHINE, count, ordinal());
        }

    }

}
