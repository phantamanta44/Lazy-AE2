package io.github.phantamanta44.threng.item;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("NullableProblems")
public class ThrEngItems {

    public static ItemMaterial MATERIAL;

    @InitMe(ThrEngConst.MOD_ID)
    public static void init() {
        MATERIAL = new ItemMaterial();
        OreDictionary.registerOre("ingotFluixSteel", ItemMaterial.Type.FLUIX_STEEL.newStack(1));
        OreDictionary.registerOre("dustCoal", ItemMaterial.Type.COAL_DUST.newStack(1));
    }

}
