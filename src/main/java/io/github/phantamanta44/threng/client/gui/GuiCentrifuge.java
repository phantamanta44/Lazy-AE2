package io.github.phantamanta44.threng.client.gui;

import io.github.phantamanta44.threng.client.gui.base.GuiSimpleProcessor;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.inventory.ContainerCentrifuge;
import net.minecraft.client.resources.I18n;

public class GuiCentrifuge extends GuiSimpleProcessor<ContainerCentrifuge> {

    public GuiCentrifuge(ContainerCentrifuge cont) {
        super(cont, ResConst.GUI_CENTRIFUGE.getTexture());
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.CONTAINER_CENTRIFUGE));
        ResConst.GUI_CENTRIFUGE_PROGRESS.drawPartial(80, 36, 0F, 0F, cont.getWorkFraction(), 1F);
    }

}
