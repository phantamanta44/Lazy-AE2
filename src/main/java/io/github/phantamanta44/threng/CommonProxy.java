package io.github.phantamanta44.threng;

import appeng.api.config.Upgrades;
import io.github.phantamanta44.threng.block.BlockMachine;
import io.github.phantamanta44.threng.integration.IntegrationManager;
import io.github.phantamanta44.threng.item.ItemMaterial;
import io.github.phantamanta44.threng.recipe.ThrEngRecipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Deque;
import java.util.LinkedList;

public class CommonProxy {

    private final IntegrationManager intManager = new IntegrationManager();
    private final Deque<Runnable> postInitTasks = new LinkedList<>();
    private boolean postPostInit = false;

    public void onPreInit(FMLPreInitializationEvent event) {
        ThrEngRecipes.registerRecipeTypes();
        intManager.load(event.getAsmData());
    }

    public void onInit(FMLInitializationEvent event) {
        OreDictionary.registerOre("ingotFluixSteel", ItemMaterial.Type.FLUIX_STEEL.newStack(1));
        OreDictionary.registerOre("dustCoal", ItemMaterial.Type.COAL_DUST.newStack(1));
        ThrEngRecipes.addRecipes();

        Upgrades.SPEED.registerItem(BlockMachine.Type.AGGREGATOR.newStack(1), 8);
        Upgrades.SPEED.registerItem(BlockMachine.Type.CENTRIFUGE.newStack(1), 8);
        Upgrades.SPEED.registerItem(BlockMachine.Type.ETCHER.newStack(1), 8);

        intManager.init();
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        postPostInit = true;
        while (!postInitTasks.isEmpty()) {
            postInitTasks.pop().run();
        }
    }

    public void registerPostInitTask(Runnable task) {
        if (postPostInit) {
            task.run();
        } else {
            postInitTasks.add(task);
        }
    }

}
