package io.github.phantamanta44.threng.item;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.threng.constant.ThrEngConst;

@SuppressWarnings("NotNullFieldNotInitialized")
public class ThrEngItems {

    public static ItemMaterial MATERIAL;

    @InitMe(ThrEngConst.MOD_ID)
    public static void init() {
        MATERIAL = new ItemMaterial();
    }

}
