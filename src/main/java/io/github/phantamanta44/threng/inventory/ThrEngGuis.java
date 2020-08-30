package io.github.phantamanta44.threng.inventory;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.libnine.LibNine;
import io.github.phantamanta44.libnine.gui.GuiIdentity;
import io.github.phantamanta44.threng.client.gui.*;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ThrEngGuis {

    public static final GuiIdentity<ContainerAggregator, GuiAggregator> AGGREGATOR
            = new GuiIdentity<>(LangConst.GUI_AGGREGATOR, ContainerAggregator.class);
    public static final GuiIdentity<ContainerCentrifuge, GuiCentrifuge> CENTRIFUGE
            = new GuiIdentity<>(LangConst.GUI_CENTRIFUGE, ContainerCentrifuge.class);
    public static final GuiIdentity<ContainerEtcher, GuiEtcher> ETCHER
            = new GuiIdentity<>(LangConst.GUI_ETCHER, ContainerEtcher.class);
    public static final GuiIdentity<ContainerFastCraftingBus, GuiFastCraftingBus> FAST_CRAFTER
            = new GuiIdentity<>(LangConst.GUI_FAST_CRAFTER, ContainerFastCraftingBus.class);
    public static final GuiIdentity<ContainerLevelMaintainer, GuiLevelMaintainer> LEVEL_MAINTAINER
            = new GuiIdentity<>(LangConst.GUI_LEVEL_MAINTAINER, ContainerLevelMaintainer.class);
    public static final GuiIdentity<ContainerBigAssembler, GuiBigAssembler> BIG_ASSEMBLER
            = new GuiIdentity<>(LangConst.GUI_BIG_ASSEMBLER, ContainerBigAssembler.class);
    public static final GuiIdentity<ContainerEnergizer, GuiEnergizer> ENERGIZER
            = new GuiIdentity<>(LangConst.GUI_ENERGIZER, ContainerEnergizer.class);

    @InitMe(ThrEngConst.MOD_ID)
    public static void registerCommon() {
        LibNine.PROXY.getRegistrar().queueGuiServerReg(AGGREGATOR, ContainerAggregator::new);
        LibNine.PROXY.getRegistrar().queueGuiServerReg(CENTRIFUGE, ContainerCentrifuge::new);
        LibNine.PROXY.getRegistrar().queueGuiServerReg(ETCHER, ContainerEtcher::new);
        LibNine.PROXY.getRegistrar().queueGuiServerReg(FAST_CRAFTER, ContainerFastCraftingBus::new);
        LibNine.PROXY.getRegistrar().queueGuiServerReg(LEVEL_MAINTAINER, ContainerLevelMaintainer::new);
        LibNine.PROXY.getRegistrar().queueGuiServerReg(BIG_ASSEMBLER, ContainerBigAssembler::new);
        LibNine.PROXY.getRegistrar().queueGuiServerReg(ENERGIZER, ContainerEnergizer::new);
    }

    @SideOnly(Side.CLIENT)
    @InitMe(value = ThrEngConst.MOD_ID, sides = { Side.CLIENT })
    public static void registerClient() {
        LibNine.PROXY.getRegistrar().queueGuiClientReg(AGGREGATOR, (c, w, p, x, y, z) -> new GuiAggregator(c));
        LibNine.PROXY.getRegistrar().queueGuiClientReg(CENTRIFUGE, (c, w, p, x, y, z) -> new GuiCentrifuge(c));
        LibNine.PROXY.getRegistrar().queueGuiClientReg(ETCHER, (c, w, p, x, y, z) -> new GuiEtcher(c));
        LibNine.PROXY.getRegistrar().queueGuiClientReg(FAST_CRAFTER, (c, w, p, x, y, z) -> new GuiFastCraftingBus(c));
        LibNine.PROXY.getRegistrar().queueGuiClientReg(LEVEL_MAINTAINER, (c, w, p, x, y, z) -> new GuiLevelMaintainer(c));
        LibNine.PROXY.getRegistrar().queueGuiClientReg(BIG_ASSEMBLER, (c, w, p, x, y, z) -> new GuiBigAssembler(c));
        LibNine.PROXY.getRegistrar().queueGuiClientReg(ENERGIZER, (c, w, p, x, y, z) -> new GuiEnergizer(c));
    }

}
