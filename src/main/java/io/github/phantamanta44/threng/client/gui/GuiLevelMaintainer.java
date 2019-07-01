package io.github.phantamanta44.threng.client.gui;

import appeng.container.slot.SlotFake;
import appeng.util.ReadableNumberConverter;
import io.github.phantamanta44.libnine.client.gui.L9GuiContainer;
import io.github.phantamanta44.libnine.client.gui.component.impl.GuiComponentTextInput;
import io.github.phantamanta44.libnine.util.render.GuiUtils;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.inventory.ContainerLevelMaintainer;
import io.github.phantamanta44.threng.tile.TileLevelMaintainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;

import java.util.function.LongConsumer;

public class GuiLevelMaintainer extends L9GuiContainer {

    public static final int GUI_HEIGHT = 214;

    private final ContainerLevelMaintainer cont;
    private final GuiComponentTextInput[] qtyInputs = new GuiComponentTextInput[TileLevelMaintainer.REQ_COUNT];
    private final GuiComponentTextInput[] batchInputs = new GuiComponentTextInput[TileLevelMaintainer.REQ_COUNT];

    public GuiLevelMaintainer(ContainerLevelMaintainer cont) {
        super(cont, ResConst.GUI_LEVEL_MAINTAINER.getTexture(), 176, GUI_HEIGHT);
        this.cont = cont;
        for (int i = 0; i < TileLevelMaintainer.REQ_COUNT; i++) {
            int index = i; // necessary for lambda binding
            qtyInputs[i] = createTextBox(38, 21 + 20 * i, cont.getRequestQuantity(i), LangConst.TT_REQ_QTY,
                    q -> cont.updateRequestQuantity(index, q));
            batchInputs[i] = createTextBox(101, 21 + 20 * i, cont.getBatchSize(i), LangConst.TT_BATCH_SIZE,
                    q -> cont.updateBatchSize(index, q));
        }
    }

    private GuiComponentTextInput createTextBox(int x, int y, long initial, String tooltipKey, LongConsumer callback) {
        GuiComponentTextInput comp = new GuiComponentTextInput(x, y, 41, 6,
                ResConst.GUI_COMP_SUBMIT_NORMAL, ResConst.GUI_COMP_SUBMIT_HOVERED, ResConst.GUI_COMP_SUBMIT_DISABLED,
                0xFFFFFF, 0xDD1515,
                s -> {
                    try {
                        return Long.parseLong(s) >= 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                s -> callback.accept(Long.parseLong(s)), Long.toString(initial), tooltipKey);
        addComponent(comp);
        return comp;
    }

    public void updateTextBoxes(TileLevelMaintainer.InventoryRequest requests) {
        for (int i = 0; i < TileLevelMaintainer.REQ_COUNT; i++) {
            if (!qtyInputs[i].isFocused()) {
                qtyInputs[i].setValue(Long.toString(requests.getQuantity(i)));
            }
            if (!batchInputs[i].isFocused()) {
                batchInputs[i].setValue(Long.toString(requests.getBatchSize(i)));
            }
        }
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.CONTAINER_LEVEL_MAINTAINER));
    }

    @Override
    public void drawSlot(Slot slot) {
        super.drawSlot(slot);
        if (slot instanceof SlotFake) {
            long count = cont.getRequestQuantity(slot.getSlotIndex());
            if (count > 0) {
                String countStr = ReadableNumberConverter.INSTANCE.toSlimReadableForm(count);
                GlStateManager.pushMatrix();
                GlStateManager.disableDepth();
                GlStateManager.scale(0.75F, 0.75F, 0.75F);
                drawString(countStr,
                        (int)Math.floor((slot.xPos + 16 - GuiUtils.getStringWidth(countStr) * 0.375F) * 1.333F),
                        (int)Math.floor((slot.yPos + 16 - GuiUtils.getFontHeight() * 0.375F) * 1.333F),
                        0xFFFFFF, true);
                GlStateManager.color(1F, 1F, 1F, 1F);
                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
            }
        }
    }

}
