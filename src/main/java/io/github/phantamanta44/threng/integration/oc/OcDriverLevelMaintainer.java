package io.github.phantamanta44.threng.integration.oc;

import io.github.phantamanta44.threng.block.BlockMachine;
import io.github.phantamanta44.threng.block.ThrEngBlocks;
import io.github.phantamanta44.threng.tile.TileLevelMaintainer;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.DriverBlock;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.internal.Database;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

public class OcDriverLevelMaintainer implements DriverBlock {

    @Override
    public boolean worksWith(World world, BlockPos pos, EnumFacing face) {
        return world.getTileEntity(pos) instanceof TileLevelMaintainer;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing face) {
        return new EnvLevelMaintainer((TileLevelMaintainer)Objects.requireNonNull(world.getTileEntity(pos)));
    }

    public static class EnvLevelMaintainer extends AbstractManagedEnvironment implements NamedBlock {

        private static final String NAME = "me_level_maintainer";

        private final TileLevelMaintainer tile;

        public EnvLevelMaintainer(TileLevelMaintainer tile) {
            this.tile = tile;
            setNode(Network.newNode(this, Visibility.Network).withComponent(NAME).create());
        }

        @Override
        public String preferredName() {
            return NAME;
        }

        @Override
        public int priority() {
            return 5;
        }

        @Callback(doc = "function(index:number):boolean -- Checks whether a request is valid or not.")
        public Object[] isRequestValid(Context ctx, Arguments args) {
            return new Object[] { tile.getRequestInventory().isRequesting(checkJobIndex(args)) };
        }

        @Callback(doc = "function([index:number]) -- Clears a request, or clears all requests if no index is specified.")
        public Object[] clearRequest(Context ctx, Arguments args) {
            if (args.count() > 0) {
                tile.getRequestInventory().updateQuantity(checkJobIndex(args), 0);
            } else {
                for (int i = 0; i < TileLevelMaintainer.REQ_COUNT; i++) {
                    tile.getRequestInventory().updateQuantity(i, 0);
                }
            }
            return new Object[0];
        }

        @Callback(doc = "function(index:number):table -- Gets the item requested at the given index.")
        public Object[] getRequestItem(Context ctx, Arguments args) {
            return new Object[] { tile.getRequestInventory().getStackInSlot(checkJobIndex(args)) };
        }

        @Callback(doc = "function(index:number, dbAddress:string[, slot:number[, requestCount:number[, batchSize:number]]]) -- Sets a new request item from the database.")
        public Object[] setRequestItem(Context ctx, Arguments args) {
            int index = checkJobIndex(args);
            Database db = OcDatabase.getDatabase(node(), args.checkString(1));
            ItemStack stack = db.getStackInSlot(args.optInteger(2, 0));
            tile.getRequestInventory().setStackInSlot(index, stack.copy());
            if (args.count() > 3) {
                tile.getRequestInventory().updateQuantity(index, Math.round(args.checkDouble(3)));
                if (args.count() > 4) {
                    tile.getRequestInventory().updateBatchSize(index, Math.round(args.checkDouble(4)));
                }
            }
            return new Object[0];
        }

        @Callback(doc = "function(index:number):number -- Gets the quantity to maintain at the given index.")
        public Object[] getRequestQuantity(Context ctx, Arguments args) {
            return new Object[] { tile.getRequestInventory().getQuantity(checkJobIndex(args)) };
        }

        @Callback(doc = "function(index:number, requestCount:number) -- Sets the quantity of a requested item to maintain.")
        public Object[] setRequestQuantity(Context ctx, Arguments args) {
            int index = checkJobIndex(args);
            if (tile.getRequestInventory().isRequesting(index)) {
                tile.getRequestInventory().updateQuantity(index, Math.round(args.checkDouble(1)));
            }
            return new Object[0];
        }

        @Callback(doc = "function(index:number):number -- Gets the request batch size at the given index.")
        public Object[] getRequestBatchSize(Context ctx, Arguments args) {
            return new Object[] { tile.getRequestInventory().getBatchSize(checkJobIndex(args)) };
        }

        @Callback(doc = "function(index:number, batchSize:number) -- Sets the batch size of a requested item.")
        public Object[] setRequestBatchSize(Context ctx, Arguments args) {
            int index = checkJobIndex(args);
            if (tile.getRequestInventory().isRequesting(index)) {
                tile.getRequestInventory().updateBatchSize(index, Math.round(args.checkDouble(1)));
            }
            return new Object[0];
        }

        private int checkJobIndex(Arguments args) {
            int index = args.checkInteger(0);
            if (index < 0 || index >= TileLevelMaintainer.REQ_COUNT) {
                throw new IllegalArgumentException("request index " + index + " out of bounds");
            }
            return index;
        }

    }

    public static class EnvProvider implements EnvironmentProvider {

        @Nullable
        @Override
        public Class<?> getEnvironment(ItemStack stack) {
            if (stack.getItem() == ThrEngBlocks.MACHINE.getItemBlock()
                    && BlockMachine.Type.fromMeta(stack.getMetadata()) == BlockMachine.Type.LEVEL_MAINTAINER) {
                return EnvLevelMaintainer.class;
            }
            return null;
        }

    }

}
