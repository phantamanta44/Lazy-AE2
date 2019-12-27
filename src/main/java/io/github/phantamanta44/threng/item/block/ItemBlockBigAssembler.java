package io.github.phantamanta44.threng.item.block;

import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemBlockStated;
import io.github.phantamanta44.threng.block.BlockBigAssembler;
import net.minecraft.item.ItemStack;

public class ItemBlockBigAssembler extends L9ItemBlockStated implements ParameterizedItemModel.IParamaterized {

    public ItemBlockBigAssembler(BlockBigAssembler block) {
        super(block);
    }

    @Override
    public void getModelMutations(ItemStack stack, ParameterizedItemModel.Mutation m) {
        m.mutate("type", BlockBigAssembler.Type.getForMeta(stack.getMetadata()).getName());
    }

}
