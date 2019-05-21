package io.github.phantamanta44.threng.client.gui.component;

import io.github.phantamanta44.libnine.client.gui.component.GuiComponent;
import io.github.phantamanta44.libnine.util.render.GuiUtils;
import io.github.phantamanta44.libnine.util.world.BlockSide;
import io.github.phantamanta44.libnine.util.world.IAllocableSides;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.util.SlotType;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;

public class GuiComponentSidedIo extends GuiComponent {

    private static final BlockSide[] FACE_SIDES = {
            BlockSide.UP, BlockSide.LEFT, BlockSide.FRONT, BlockSide.RIGHT, BlockSide.DOWN, BlockSide.BACK
    };
    private static final int[] FACE_X_COORDS = { 6, 1, 6, 11, 6, 11 };
    private static final int[] FACE_Y_COORDS = { 1, 6, 6, 6, 11, 11 };

    private final IAllocableSides<SlotType.BasicIO> sides;

    public GuiComponentSidedIo(IAllocableSides<SlotType.BasicIO> sides, int x, int y) {
        super(x, y, 17, 17);
        this.sides = sides;
    }

    public GuiComponentSidedIo(IAllocableSides<SlotType.BasicIO> sides) {
        this(sides, 146, 7);
    }

    @Override
    public void render(float partialTicks, int mX, int mY, boolean mouseOver) {
        ResConst.GUI_COMP_SIDE_IO_BG.draw(x, y);
        for (int i = 0; i < FACE_SIDES.length; i++) {
            switch (sides.getFace(FACE_SIDES[i])) {
                case INPUT:
                    ResConst.GUI_COMP_SIDE_IO_IN.draw(x + FACE_X_COORDS[i], y + FACE_Y_COORDS[i]);
                    break;
                case OUTPUT:
                    ResConst.GUI_COMP_SIDE_IO_OUT.draw(x + FACE_X_COORDS[i], y + FACE_Y_COORDS[i]);
                    break;
            }
        }
    }

    @Override
    public void renderTooltip(float partialTicks, int mX, int mY) {
        for (int i = 0; i < FACE_SIDES.length; i++) {
            if (GuiUtils.isMouseOver(x + FACE_X_COORDS[i], y + FACE_Y_COORDS[i], 5, 5, mX, mY)) {
                drawTooltip(Arrays.asList(
                        TextFormatting.YELLOW + LangConst.getSideName(FACE_SIDES[i]),
                        LangConst.getIoName(sides.getFace(FACE_SIDES[i])
                        )), mX, mY);
                break;
            }
        }
    }

    @Override
    public boolean onClick(int mX, int mY, int button, boolean mouseOver) {
        if (button <= 1) {
            for (int i = 0; i < FACE_SIDES.length; i++) {
                if (GuiUtils.isMouseOver(x + FACE_X_COORDS[i], y + FACE_Y_COORDS[i], 5, 5, mX, mY)) {
                    if (button == 0) {
                        SlotType.BasicIO[] ioTypes = SlotType.BasicIO.values();
                        sides.setFace(FACE_SIDES[i], ioTypes[(sides.getFace(FACE_SIDES[i]).ordinal() + 1) % ioTypes.length]);
                    } else if (button == 1) {
                        SlotType.BasicIO[] ioTypes = SlotType.BasicIO.values();
                        int newOrdinal = sides.getFace(FACE_SIDES[i]).ordinal() - 1;
                        if (newOrdinal == -1) {
                            newOrdinal = ioTypes.length - 1;
                        }
                        sides.setFace(FACE_SIDES[i], ioTypes[newOrdinal]);
                    }
                    playClickSound();
                    return true;
                }
            }
        }
        return false;
    }

}
