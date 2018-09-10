package io.github.phantamanta44.threng.client.gui;

import io.github.phantamanta44.threng.client.gui.base.GuiEnergized;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.inventory.ContainerEtcher;
import net.minecraft.client.resources.I18n;

public class GuiEtcher extends GuiEnergized<ContainerEtcher> {

    public GuiEtcher(ContainerEtcher cont) {
        super(cont, ResConst.GUI_ETCHER.getTexture());
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.CONTAINER_ETCHER));
        if (cont.getWorkFraction() < 0.5F) {
            float progress = cont.getWorkFraction() * 2F;
            ResConst.GUI_ETCHER_PRESS_TOP.drawPartial(55, 22, 0F, 0F, 1F, progress);
            ResConst.GUI_ETCHER_PRESS_BOTTOM.drawPartial(55, 54, 0F, 1F - progress, 1F, 1F);
        } else {
            ResConst.GUI_ETCHER_PRESS_TOP.draw(55, 22);
            ResConst.GUI_ETCHER_PRESS_BOTTOM.draw(55, 54);
            ResConst.GUI_ETCHER_PROGRESS.drawPartial(84, 36, 0F, 0F, (cont.getWorkFraction() - 0.5F) * 2F, 1F);
        }
    }

}
