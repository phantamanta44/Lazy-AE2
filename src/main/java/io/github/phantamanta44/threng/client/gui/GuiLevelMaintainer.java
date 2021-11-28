package io.github.phantamanta44.threng.client.gui;

import io.github.phantamanta44.libnine.client.gui.L9GuiContainer;
import io.github.phantamanta44.libnine.client.gui.component.GuiComponent;
import io.github.phantamanta44.libnine.client.gui.component.impl.GuiComponentTextInput;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.inventory.ContainerLevelMaintainer;
import io.github.phantamanta44.threng.tile.TileLevelMaintainer;
import net.minecraft.client.resources.I18n;

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

        for (int i = 0; i < TileLevelMaintainer.REQ_COUNT; i++) {
            boolean lastRow = i + 1 == TileLevelMaintainer.REQ_COUNT;
            GuiComponent nextQty = !lastRow ? qtyInputs[i+1] : null;
            GuiComponent nextBatch = !lastRow ? batchInputs[i+1] : null;
            qtyInputs[i].setNextComponentOnEnter(nextQty);
            qtyInputs[i].setNextComponentOnTab(batchInputs[i]);
            batchInputs[i].setNextComponentOnEnter(nextBatch);
            batchInputs[i].setNextComponentOnTab(nextQty);
        }
    }

    private GuiComponentTextInput createTextBox(int x, int y, long initial, String tooltipKey, LongConsumer callback) {
        GuiComponentTextInput comp = new GuiComponentTextInput(x, y, 55, 8,
                null, null, null,
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

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.CONTAINER_LEVEL_MAINTAINER));
    }
}
