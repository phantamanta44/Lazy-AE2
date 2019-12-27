package io.github.phantamanta44.threng.client.gui.component;

import io.github.phantamanta44.libnine.client.gui.component.GuiComponent;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.util.IPaginated;

public class GuiComponentPageNav extends GuiComponent {

    private final IPaginated pageHost;

    public GuiComponentPageNav(int x, int y, IPaginated pageHost) {
        super(x, y, 37, 11);
        this.pageHost = pageHost;
    }

    @Override
    public void render(float partialTicks, int mX, int mY, boolean mouseOver) {
        int pageNum = pageHost.getCurrentPage();
        if (pageNum == 0) {
            ResConst.GUI_COMP_NEXT_PREV_PREV_DISABLED.draw(x, y);
        } else if (mouseOver && mX < x + 18) {
            ResConst.GUI_COMP_NEXT_PREV_PREV_HOVERED.draw(x, y);
        } else {
            ResConst.GUI_COMP_NEXT_PREV_PREV_NORMAL.draw(x, y);
        }
        if (pageNum == pageHost.getPageCount() - 1) {
            ResConst.GUI_COMP_NEXT_PREV_NEXT_DISABLED.draw(x + 19, y);
        } else if (mouseOver && mX > x + 19) {
            ResConst.GUI_COMP_NEXT_PREV_NEXT_HOVERED.draw(x + 19, y);
        } else {
            ResConst.GUI_COMP_NEXT_PREV_NEXT_NORMAL.draw(x + 19, y);
        }
    }

    @Override
    public boolean onClick(int mX, int mY, int button, boolean mouseOver) {
        if (mouseOver) {
            if (mX < x + 18) {
                int pageNum = pageHost.getCurrentPage();
                if (pageNum > 0) {
                    pageHost.setPage(pageNum - 1);
                    playClickSound();
                    return true;
                }
            } else if (mX > x + 19) {
                int pageNum = pageHost.getCurrentPage();
                if (pageNum < pageHost.getPageCount() - 1) {
                    pageHost.setPage(pageNum + 1);
                    playClickSound();
                    return true;
                }
            }
        }
        return false;
    }

}
