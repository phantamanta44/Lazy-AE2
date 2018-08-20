package io.github.phantamanta44.threng;

import io.github.phantamanta44.threng.recipe.ThrEngRecipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void onPreInit(FMLPreInitializationEvent event) {
        ThrEngRecipes.registerRecipeTypes();
    }

    public void onInit(FMLInitializationEvent event) {
        ThrEngRecipes.addRecipes();
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        // NO-OP
    }

}
