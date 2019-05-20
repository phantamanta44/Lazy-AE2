package io.github.phantamanta44.threng;

import io.github.phantamanta44.threng.item.ItemMaterial;
import io.github.phantamanta44.threng.recipe.ThrEngRecipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {

    public void onPreInit(FMLPreInitializationEvent event) {
        ThrEngRecipes.registerRecipeTypes();
    }

    public void onInit(FMLInitializationEvent event) {
        OreDictionary.registerOre("ingotFluixSteel", ItemMaterial.Type.FLUIX_STEEL.newStack(1));
        OreDictionary.registerOre("dustCoal", ItemMaterial.Type.COAL_DUST.newStack(1));
        ThrEngRecipes.addRecipes();
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        // NO-OP
    }

}
