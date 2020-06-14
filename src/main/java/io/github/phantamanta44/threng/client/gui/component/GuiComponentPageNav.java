package io.github.phantamanta44.threng.client.gui.component;

import io.github.phantamanta44.libnine.client.gui.component.GuiComponent;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.util.ButtonRender;
import io.github.phantamanta44.threng.util.IPaginated;

public class GuiComponentPageNav extends GuiComponent {

    private final IPaginated pageHost;

    public GuiComponentPageNav(int x, int y, IPaginated pageHost) {
        super(x, y, 61, 11);
        this.pageHost = pageHost;
    }

    @Override
    public void render(float partialTicks, int mX, int mY, boolean mouseOver) {
        int pageNum = pageHost.getCurrentPage();
        ButtonRender.State sFirst = ButtonRender.State.NORMAL, sPrev = ButtonRender.State.NORMAL;
        ButtonRender.State sNext = ButtonRender.State.NORMAL, sLast = ButtonRender.State.NORMAL;
        if (pageNum == 0) {
            sFirst = sPrev = ButtonRender.State.DISABLED;
        } else if (mouseOver) {
            if (mX < x + 11) {
                sFirst = ButtonRender.State.HOVERED;
            } else if (mX > x + 12 && mX < x + 30) {
                sPrev = ButtonRender.State.HOVERED;
            }
        }
        if (pageNum == pageHost.getPageCount() - 1) {
            sNext = sLast = ButtonRender.State.DISABLED;
        } else if (mouseOver) {
            if (mX > x + 50) {
                sLast = ButtonRender.State.HOVERED;
            } else if (mX > x + 31 && mX < x + 49) {
                sNext = ButtonRender.State.HOVERED;
            }
        }
        ResConst.GUI_COMP_NEXT_PREV_FIRST.getSprite(sFirst).draw(x, y);
        ResConst.GUI_COMP_NEXT_PREV_PREV.getSprite(sPrev).draw(x + 12, y);
        ResConst.GUI_COMP_NEXT_PREV_NEXT.getSprite(sNext).draw(x + 31, y);
        ResConst.GUI_COMP_NEXT_PREV_LAST.getSprite(sLast).draw(x + 50, y);
    }

    @Override
    public boolean onClick(int mX, int mY, int button, boolean mouseOver) {
        if (mouseOver) {
            if (mX < x + 11) {
                if (pageHost.getCurrentPage() > 0) {
                    pageHost.setPage(0);
                    playClickSound();
                    return true;
                }
            } else if (mX > x + 12 && mX < x + 30) {
                int pageNum = pageHost.getCurrentPage();
                if (pageNum > 0) {
                    pageHost.setPage(pageNum - 1);
                    playClickSound();
                    return true;
                }
            } else if (mX > x + 31 && mX < x + 49) {
                int pageNum = pageHost.getCurrentPage();
                if (pageNum < pageHost.getPageCount() - 1) {
                    pageHost.setPage(pageNum + 1);
                    playClickSound();
                    return true;
                }
            } else if (mX > x + 50) {
                int maxPage = pageHost.getPageCount() - 1;
                if (pageHost.getCurrentPage() < maxPage) {
                    pageHost.setPage(maxPage);
                    playClickSound();
                    return true;
                }
            }
        }
        return false;
    }

}
