package io.github.phantamanta44.threng;

import io.github.phantamanta44.libnine.Virtue;
import io.github.phantamanta44.libnine.util.L9CreativeTab;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import io.github.phantamanta44.threng.item.ThrEngItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ThrEngConst.MOD_ID, version = ThrEngConst.VERSION, useMetadata = true)
public class ThrEng extends Virtue {

    @SuppressWarnings("NullableProblems")
    @Mod.Instance(ThrEngConst.MOD_ID)
    public static ThrEng INSTANCE;

    @SuppressWarnings("NullableProblems")
    @SidedProxy(
            clientSide = "io.github.phantamanta44.threng.client.ClientProxy",
            serverSide = "io.github.phantamanta44.threng.CommonProxy")
    public static CommonProxy PROXY;

    @SuppressWarnings("NullableProblems")
    public static Logger LOGGER;

    public ThrEng() {
        super(ThrEngConst.MOD_ID, new L9CreativeTab(ThrEngConst.MOD_ID, () -> new ItemStack(ThrEngItems.MATERIAL)));
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        PROXY.onPreInit(event);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        PROXY.onInit(event);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        PROXY.onPostInit(event);
    }

}
