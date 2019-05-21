package io.github.phantamanta44.threng.client.gui;

import io.github.phantamanta44.libnine.client.gui.L9GuiContainer;
import io.github.phantamanta44.threng.client.gui.component.GuiComponentSidedIo;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.inventory.ContainerFastCraftingBus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiFastCraftingBus extends L9GuiContainer {

    public GuiFastCraftingBus(ContainerFastCraftingBus cont) {
        super(cont, ResConst.GUI_FAST_CRAFTER.getTexture());
        addComponent(new GuiComponentSidedIo(cont, 7, 11 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT));
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        drawContainerName(I18n.format(LangConst.CONTAINER_FAST_CRAFTER));
    }

}
