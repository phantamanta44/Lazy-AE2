package io.github.phantamanta44.threng.client.gui;

import io.github.phantamanta44.threng.client.gui.base.GuiSimpleProcessor;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.inventory.ContainerEnergizer;
import net.minecraft.client.resources.I18n;

public class GuiEnergizer extends GuiSimpleProcessor<ContainerEnergizer> {

    public GuiEnergizer(ContainerEnergizer cont) {
        super(cont, ResConst.GUI_ENERGIZER.getTexture());
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.CONTAINER_ENERGIZER));
        drawProgressBar(81, 29, cont.getWorkFraction());
    }

    public static void drawProgressBar(int x, int y, float frac) {
        ResConst.GUI_ENERGIZER_PROGRESS.drawPartial(x, y, 0F, 0F, frac, 1F);
    }

}
