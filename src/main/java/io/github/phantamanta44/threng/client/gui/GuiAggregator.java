package io.github.phantamanta44.threng.client.gui;

import io.github.phantamanta44.threng.client.gui.base.GuiEnergized;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.inventory.ContainerAggregator;
import net.minecraft.client.resources.I18n;

public class GuiAggregator extends GuiEnergized<ContainerAggregator> {

    public GuiAggregator(ContainerAggregator cont) {
        super(cont, ResConst.GUI_AGGREGATOR.getTexture());
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.CONTAINER_AGGREGATOR));
        ResConst.GUI_AGGREGATOR_PROGRESS.drawPartial(92, 36, 0F, 0F, cont.getWorkFraction(), 1F);
    }

}
