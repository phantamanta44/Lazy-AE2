package io.github.phantamanta44.threng.item;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ThrEngItems {

    @GameRegistry.ObjectHolder(ThrEngConst.MOD_ID + ":" + LangConst.ITEM_MATERIAL)
    public static ItemMaterial MATERIAL;

    @InitMe(ThrEngConst.MOD_ID)
    public static void init() {
        new ItemMaterial();
    }

}
