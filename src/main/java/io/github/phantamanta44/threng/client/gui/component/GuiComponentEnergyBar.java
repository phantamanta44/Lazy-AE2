package io.github.phantamanta44.threng.client.gui.component;

import io.github.phantamanta44.libnine.client.gui.component.impl.GuiComponentVerticalBar;
import io.github.phantamanta44.libnine.util.format.FormatUtils;
import io.github.phantamanta44.threng.constant.ResConst;
import net.minecraftforge.energy.IEnergyStorage;

public class GuiComponentEnergyBar extends GuiComponentVerticalBar {

    public GuiComponentEnergyBar(IEnergyStorage energy) {
        super(165, 7, ResConst.GUI_COMP_ENERGY_BG, ResConst.GUI_COMP_ENERGY_FG, 1, 1,
                () -> (float)energy.getEnergyStored() / energy.getMaxEnergyStored(),
                () -> String.format("%s / %s",
                        FormatUtils.formatSI(energy.getEnergyStored(), "FE"),
                        FormatUtils.formatSI(energy.getMaxEnergyStored(), "FE")));
    }

}
