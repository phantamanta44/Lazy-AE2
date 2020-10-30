package io.github.phantamanta44.threng.client.gui.component;

import io.github.phantamanta44.libnine.client.gui.component.GuiComponent;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.tile.base.IAutoExporting;
import net.minecraft.client.resources.I18n;

public class GuiComponentAutoExport extends GuiComponent {

    private final IAutoExporting machine;

    public GuiComponentAutoExport(IAutoExporting machine, int x, int y) {
        super(x, y, 17, 17);
        this.machine = machine;
    }

    public GuiComponentAutoExport(IAutoExporting machine) {
        this(machine, 127, 7);
    }

    @Override
    public void render(float partialTicks, int mX, int mY, boolean mouseOver) {
        (machine.isAutoExporting() ? ResConst.GUI_COMP_AUTO_EXPORT_ON : ResConst.GUI_COMP_AUTO_EXPORT_OFF).draw(x, y);
    }

    @Override
    public void renderTooltip(float partialTicks, int mX, int mY) {
        drawTooltip(I18n.format(machine.isAutoExporting() ? LangConst.AUTO_EXPORT_ON : LangConst.AUTO_EXPORT_OFF), mX, mY);
    }

    @Override
    public boolean onClick(int mX, int mY, int button, boolean mouseOver) {
        if (mouseOver) {
            playClickSound();
            machine.setAutoExporting(!machine.isAutoExporting());
            return true;
        }
        return false;
    }

}
