package io.github.phantamanta44.threng.item.block;

import io.github.phantamanta44.libnine.block.L9BlockStated;
import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemBlockStated;
import io.github.phantamanta44.threng.block.BlockMachine;
import io.github.phantamanta44.threng.tile.base.TileAENetworked;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockMachine extends L9ItemBlockStated implements ParameterizedItemModel.IParamaterized {

    public ItemBlockMachine(L9BlockStated block) {
        super(block);
    }

    @Override
    public void getModelMutations(ItemStack stack, ParameterizedItemModel.Mutation m) {
        m.mutate("type", BlockMachine.Type.values()[stack.getMetadata()].getName());
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileAENetworked) {
                ((TileAENetworked)tile).getProxy().setOwner(player);
            }
            return true;
        }
        return false;
    }

}
