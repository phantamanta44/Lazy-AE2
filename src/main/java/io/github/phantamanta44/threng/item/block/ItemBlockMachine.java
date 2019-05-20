package io.github.phantamanta44.threng.item.block;

import io.github.phantamanta44.libnine.block.L9BlockStated;
import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemBlockStated;
import io.github.phantamanta44.threng.block.BlockMachine;
import net.minecraft.item.ItemStack;

public class ItemBlockMachine extends L9ItemBlockStated implements ParameterizedItemModel.IParamaterized {

    public ItemBlockMachine(L9BlockStated block) {
        super(block);
    }

    @Override
    public void getModelMutations(ItemStack stack, ParameterizedItemModel.Mutation m) {
        m.mutate("type", BlockMachine.Type.values()[stack.getMetadata()].getName());
    }

}
