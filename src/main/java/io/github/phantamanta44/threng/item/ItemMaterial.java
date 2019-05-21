package io.github.phantamanta44.threng.item;

import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemSubs;
import io.github.phantamanta44.threng.constant.LangConst;
import net.minecraft.item.ItemStack;

public class ItemMaterial extends L9ItemSubs implements ParameterizedItemModel.IParamaterized {

    public ItemMaterial() {
        super(LangConst.ITEM_MATERIAL, Type.values().length);
    }

    @Override
    public void getModelMutations(ItemStack stack, ParameterizedItemModel.Mutation m) {
        m.mutate("type", Type.fromStack(stack).getMutation());
    }

    public enum Type {

        FLUIX_STEEL, STEEL_PROCESS_DUST, STEEL_PROCESS_INGOT, COAL_DUST, MACHINE_CORE, SPACE_GEM, PARALLEL_PROCESSOR,
        SPEC_CORE, SPEC_CORE_2, SPEC_CORE_4, SPEC_CORE_8, SPEC_CORE_16, SPEC_CORE_32, SPEC_CORE_64, SPEC_PROCESSOR;

        public static Type fromStack(ItemStack stack) {
            return fromMeta(stack.getMetadata());
        }

        public static Type fromMeta(int meta) {
            return values()[meta];
        }

        public String getMutation() {
            return name().toLowerCase();
        }

        public ItemStack newStack(int count) {
            return new ItemStack(ThrEngItems.MATERIAL, count, ordinal());
        }

    }

}
