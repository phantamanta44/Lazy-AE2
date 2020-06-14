package io.github.phantamanta44.threng.util;

import io.github.phantamanta44.libnine.util.render.TextureRegion;
import io.github.phantamanta44.libnine.util.render.TextureResource;

public class ButtonRender {

    private final TextureRegion normal, disabled, hovered;

    public ButtonRender(TextureResource sheet, int x, int y, int width, int height) {
        this.normal = sheet.getRegion(x, y, width, height);
        this.disabled = sheet.getRegion(x, y + height, width, height);
        this.hovered = sheet.getRegion(x, y + height * 2, width, height);
    }

    public TextureRegion getSprite(State state) {
        switch (state) {
            case NORMAL:
                return normal;
            case DISABLED:
                return disabled;
            case HOVERED:
                return hovered;
            default:
                throw new IllegalArgumentException("Bad button state: " + state);
        }
    }

    public enum State {
        NORMAL, DISABLED, HOVERED
    }

}
