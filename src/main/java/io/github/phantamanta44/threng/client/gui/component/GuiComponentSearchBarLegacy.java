package io.github.phantamanta44.threng.client.gui.component;

import io.github.phantamanta44.libnine.client.gui.IScreenDrawable;
import io.github.phantamanta44.libnine.client.gui.component.GuiComponent;
import io.github.phantamanta44.libnine.util.helper.InputUtils;
import io.github.phantamanta44.libnine.util.render.GuiUtils;
import io.github.phantamanta44.threng.util.ISearchHost;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

/*public class GuiComponentSearchBarLegacy extends GuiComponent {

    private final ISearchHost searchHost;
    private final int textOrigin;

    private String value = "";
    private boolean focused = true;

    public GuiComponentSearchBarLegacy(int x, int y, int width, int height, ISearchHost searchHost) {
        super(x, y, width, height);
        this.searchHost = searchHost;
        this.textOrigin = (height - GuiUtils.getFontHeight()) / 2;
    }

    @Override
    public void render(float partialTicks, int mX, int mY, boolean mouseOver) {
        drawString(value, x + textOrigin, y + textOrigin + 1, IScreenDrawable.DEF_TEXT_COL);
        if (focused && (System.currentTimeMillis() % 1000) < 500) {
            GuiUtils.drawRect(
                    x + GuiUtils.getStringWidth(value) + textOrigin, y + textOrigin,
                    1, GuiUtils.getFontHeight(), IScreenDrawable.DEF_TEXT_COL);
        }
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    @Override
    public boolean onClick(int mX, int mY, int button, boolean mouseOver) {
        if (mouseOver) {
            if (button == 1) {
                value = "";
                searchHost.setSearchQuery(null);
            }
            focused = true;
            return true;
        } else {
            focused = false;
            return false;
        }
    }

    @Override
    public boolean onKeyPress(int keyCode, char typed) {
        if (focused) {
            int currentLength = value.length();
            if (currentLength < textLength && typed >= 32 && typed < 127) {
                value += typed;
                updateValidity();
            } else if (keyCode == Keyboard.KEY_BACK && !value.isEmpty()) {
                if (InputUtils.checkModsNonExclusive(InputUtils.ModKey.CTRL)) {
                    int endIndex = 0;
                    if (Character.isLetterOrDigit(value.charAt(currentLength - 1))) {
                        for (int i = currentLength - 2; i >= 0; i--) {
                            if (!Character.isLetterOrDigit(value.charAt(i))) {
                                endIndex = i;
                                break;
                            }
                        }
                    } else {
                        for (int i = currentLength - 2; i >= 0; i--) {
                            if (Character.isLetterOrDigit(value.charAt(i))) {
                                endIndex = i;
                                break;
                            }
                        }
                    }
                    value = value.substring(0, endIndex + 1);
                } else {
                    value = value.substring(0, currentLength - 1);
                }
                updateValidity();
            } else if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
                updateValidity();
                if (valid) callback.accept(value);
            } else if (keyCode == Keyboard.KEY_ESCAPE) {
                focused = false;
            }
            return true;
        } else if (keyCode == Keyboard.KEY_TAB) {
            focused = true;
            return true;
        }
        return false;
    }

}*/
