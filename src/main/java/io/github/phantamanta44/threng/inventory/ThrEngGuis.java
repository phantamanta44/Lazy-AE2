package io.github.phantamanta44.threng.inventory;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.libnine.LibNine;
import io.github.phantamanta44.libnine.gui.GuiIdentity;
import io.github.phantamanta44.threng.client.gui.GuiAggregator;
import io.github.phantamanta44.threng.client.gui.GuiCentrifuge;
import io.github.phantamanta44.threng.client.gui.GuiEtcher;
import io.github.phantamanta44.threng.client.gui.GuiFastCraftingBus;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import net.minecraftforge.fml.relauncher.Side;

public class ThrEngGuis {

    public static final GuiIdentity<ContainerAggregator, GuiAggregator> AGGREGATOR
            = new GuiIdentity<>(LangConst.GUI_AGGREGATOR, ContainerAggregator.class);
    public static final GuiIdentity<ContainerCentrifuge, GuiCentrifuge> CENTRIFUGE
            = new GuiIdentity<>(LangConst.GUI_CENTRIFUGE, ContainerCentrifuge.class);
    public static final GuiIdentity<ContainerEtcher, GuiEtcher> ETCHER
            = new GuiIdentity<>(LangConst.GUI_ETCHER, ContainerEtcher.class);
    public static final GuiIdentity<ContainerFastCraftingBus, GuiFastCraftingBus> FAST_CRAFTER
            = new GuiIdentity<>(LangConst.GUI_FAST_CRAFTER, ContainerFastCraftingBus.class);

    @InitMe(ThrEngConst.MOD_ID)
    public static void registerCommon() {
        LibNine.PROXY.getRegistrar().queueGuiServerReg(AGGREGATOR, ContainerAggregator::new);
        LibNine.PROXY.getRegistrar().queueGuiServerReg(CENTRIFUGE, ContainerCentrifuge::new);
        LibNine.PROXY.getRegistrar().queueGuiServerReg(ETCHER, ContainerEtcher::new);
        LibNine.PROXY.getRegistrar().queueGuiServerReg(FAST_CRAFTER, ContainerFastCraftingBus::new);
    }

    @InitMe(value = ThrEngConst.MOD_ID, sides = { Side.CLIENT })
    public static void registerClient() {
        LibNine.PROXY.getRegistrar().queueGuiClientReg(AGGREGATOR, (c, w, p, x, y, z) -> new GuiAggregator(c));
        LibNine.PROXY.getRegistrar().queueGuiClientReg(CENTRIFUGE, (c, w, p, x, y, z) -> new GuiCentrifuge(c));
        LibNine.PROXY.getRegistrar().queueGuiClientReg(ETCHER, (c, w, p, x, y, z) -> new GuiEtcher(c));
        LibNine.PROXY.getRegistrar().queueGuiClientReg(FAST_CRAFTER, (c, w, p, x, y, z) -> new GuiFastCraftingBus(c));
    }

}
