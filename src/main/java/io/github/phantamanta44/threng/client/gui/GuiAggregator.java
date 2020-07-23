package io.github.phantamanta44.threng.client.gui;

import io.github.phantamanta44.threng.client.gui.base.GuiSimpleProcessor;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.inventory.ContainerAggregator;
import net.minecraft.client.resources.I18n;

public class GuiAggregator extends GuiSimpleProcessor<ContainerAggregator> {

    public GuiAggregator(ContainerAggregator cont) {
        super(cont, ResConst.GUI_AGGREGATOR.getTexture());
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.CONTAINER_AGGREGATOR));
        drawProgressBar(92, 36, cont.getWorkFraction());
    }

    public static void drawProgressBar(int x, int y, float frac) {
        ResConst.GUI_AGGREGATOR_PROGRESS.drawPartial(x, y, 0F, 0F, frac, 1F);
    }

}
