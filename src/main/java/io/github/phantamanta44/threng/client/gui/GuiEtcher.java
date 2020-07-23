package io.github.phantamanta44.threng.client.gui;

import io.github.phantamanta44.threng.client.gui.base.GuiSimpleProcessor;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.inventory.ContainerEtcher;
import net.minecraft.client.resources.I18n;

public class GuiEtcher extends GuiSimpleProcessor<ContainerEtcher> {

    public GuiEtcher(ContainerEtcher cont) {
        super(cont, ResConst.GUI_ETCHER.getTexture());
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.CONTAINER_ETCHER));
        drawProgressBar(55, 22, cont.getWorkFraction());
    }

    public static void drawProgressBar(int x, int y, float frac) {
        if (frac < 0.5F) {
            float progress = frac * 2F;
            ResConst.GUI_ETCHER_PRESS_TOP.drawPartial(x, y, 0F, 0F, 1F, progress);
            ResConst.GUI_ETCHER_PRESS_BOTTOM.drawPartial(x, y + 32, 0F, 1F - progress, 1F, 1F);
        } else {
            ResConst.GUI_ETCHER_PRESS_TOP.draw(x, y);
            ResConst.GUI_ETCHER_PRESS_BOTTOM.draw(x, y + 32);
            ResConst.GUI_ETCHER_PROGRESS.drawPartial(x + 29, y + 14, 0F, 0F, (frac - 0.5F) * 2F, 1F);
        }
    }

}
