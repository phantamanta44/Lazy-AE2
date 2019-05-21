package io.github.phantamanta44.threng.constant;

import io.github.phantamanta44.libnine.util.render.TextureRegion;
import io.github.phantamanta44.libnine.util.render.TextureResource;
import io.github.phantamanta44.threng.ThrEng;

public class ResConst {

    private static TextureResource getTexture(String path, int width, int height) {
        return ThrEng.INSTANCE.newTextureResource("textures/" + path + ".png", width, height);
    }

    public static final TextureResource GUI_AGGREGATOR = getTextureGui("aggregator", 256, 256);
    public static final TextureRegion GUI_AGGREGATOR_PROGRESS = GUI_AGGREGATOR.getRegion(176, 0, 24, 14);

    public static final TextureResource GUI_CENTRIFUGE = getTextureGui("centrifuge", 256, 256);
    public static final TextureRegion GUI_CENTRIFUGE_PROGRESS = GUI_CENTRIFUGE.getRegion(176, 0, 22, 14);

    public static final TextureResource GUI_ETCHER = getTextureGui("etcher", 256, 256);
    public static final TextureRegion GUI_ETCHER_PROGRESS = GUI_ETCHER.getRegion(176, 0, 22, 14);
    public static final TextureRegion GUI_ETCHER_PRESS_TOP = GUI_ETCHER.getRegion(198, 0, 22, 11);
    public static final TextureRegion GUI_ETCHER_PRESS_BOTTOM = GUI_ETCHER.getRegion(220, 0, 22, 11);

    public static final TextureResource GUI_FAST_CRAFTER = getTextureGui("fast_crafter", 256, 256);

    private static TextureResource getTextureGui(String path, int width, int height) {
        return getTexture("gui/" + path, width, height);
    }

    private static final TextureResource GUI_COMP_ENERGY = getTextureGuiComp("energy", 6, 72);
    public static final TextureRegion GUI_COMP_ENERGY_BG = GUI_COMP_ENERGY.getRegion(0, 0, 4, 72);
    public static final TextureRegion GUI_COMP_ENERGY_FG = GUI_COMP_ENERGY.getRegion(4, 0, 2, 70);

    private static final TextureResource GUI_COMP_SIDE_IO = getTextureGuiComp("side_io", 22, 17);
    public static final TextureRegion GUI_COMP_SIDE_IO_BG = GUI_COMP_SIDE_IO.getRegion(0, 0, 17, 17);
    public static final TextureRegion GUI_COMP_SIDE_IO_IN = GUI_COMP_SIDE_IO.getRegion(17, 0, 5, 5);
    public static final TextureRegion GUI_COMP_SIDE_IO_OUT = GUI_COMP_SIDE_IO.getRegion(17, 5, 5, 5);

    private static TextureResource getTextureGuiComp(String path, int width, int height) {
        return getTextureGui("component/" + path, width, height);
    }

}
