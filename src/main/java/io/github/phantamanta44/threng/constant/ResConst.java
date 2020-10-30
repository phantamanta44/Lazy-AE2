package io.github.phantamanta44.threng.constant;

import io.github.phantamanta44.libnine.util.render.TextureRegion;
import io.github.phantamanta44.libnine.util.render.TextureResource;
import io.github.phantamanta44.threng.ThrEng;
import io.github.phantamanta44.threng.util.ButtonRender;

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

    public static final TextureResource GUI_LEVEL_MAINTAINER = getTextureGui("level_maintainer", 256, 256);

    public static final TextureResource GUI_BIG_ASSEMBLER = getTextureGui("big_assembler", 256, 256);
    public static final TextureRegion GUI_BIG_ASSEMBLER_QUEUE = GUI_BIG_ASSEMBLER.getRegion(176, 0, 3, 25);
    public static final TextureRegion GUI_BIG_ASSEMBLER_CPUS = GUI_BIG_ASSEMBLER.getRegion(179, 0, 3, 25);
    public static final TextureRegion GUI_BIG_ASSEMBLER_PROGRESS = GUI_BIG_ASSEMBLER.getRegion(182, 0, 16, 2);

    public static final TextureResource GUI_ENERGIZER = getTextureGui("energizer", 256, 256);
    public static final TextureRegion GUI_ENERGIZER_PROGRESS = GUI_ENERGIZER.getRegion(176, 0, 22, 29);

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
    public static final TextureRegion GUI_COMP_SIDE_IO_OMNI = GUI_COMP_SIDE_IO.getRegion(17, 10, 5, 5);

    private static final TextureResource GUI_COMP_SUBMIT = getTextureGuiComp("submit", 39, 13);
    public static final TextureRegion GUI_COMP_SUBMIT_NORMAL = GUI_COMP_SUBMIT.getRegion(0, 0, 13, 13);
    public static final TextureRegion GUI_COMP_SUBMIT_DISABLED = GUI_COMP_SUBMIT.getRegion(13, 0, 13, 13);
    public static final TextureRegion GUI_COMP_SUBMIT_HOVERED = GUI_COMP_SUBMIT.getRegion(26, 0, 13, 13);

    private static final TextureResource GUI_COMP_NEXT_PREV = getTextureGuiComp("next_prev", 58, 33);
    public static final ButtonRender GUI_COMP_NEXT_PREV_PREV = new ButtonRender(GUI_COMP_NEXT_PREV, 0, 0, 18, 11);
    public static final ButtonRender GUI_COMP_NEXT_PREV_NEXT = new ButtonRender(GUI_COMP_NEXT_PREV, 18, 0, 18, 11);
    public static final ButtonRender GUI_COMP_NEXT_PREV_FIRST = new ButtonRender(GUI_COMP_NEXT_PREV, 36, 0, 11, 11);
    public static final ButtonRender GUI_COMP_NEXT_PREV_LAST = new ButtonRender(GUI_COMP_NEXT_PREV, 47, 0, 11, 11);

    private static final TextureResource GUI_COMP_AUTO_EXPORT = getTextureGuiComp("auto_export", 34, 17);
    public static final TextureRegion GUI_COMP_AUTO_EXPORT_OFF = GUI_COMP_AUTO_EXPORT.getRegion(0, 0, 17, 17);
    public static final TextureRegion GUI_COMP_AUTO_EXPORT_ON = GUI_COMP_AUTO_EXPORT.getRegion(17, 0, 17, 17);

    private static TextureResource getTextureGuiComp(String path, int width, int height) {
        return getTextureGui("component/" + path, width, height);
    }

    private static final TextureResource INT_JEI_CAT_AGG = getTextureJeiInt("aggregator", 256, 256);
    public static final TextureRegion INT_JEI_CAT_AGG_BG = INT_JEI_CAT_AGG.getRegion(0, 0, 130, 34);

    private static final TextureResource INT_JEI_CAT_PURIFY = getTextureJeiInt("centrifuge", 256, 256);
    public static final TextureRegion INT_JEI_CAT_PURIFY_BG = INT_JEI_CAT_PURIFY.getRegion(0, 0, 90, 34);

    private static final TextureResource INT_JEI_CAT_ETCH = getTextureJeiInt("etcher", 256, 256);
    public static final TextureRegion INT_JEI_CAT_ETCH_BG = INT_JEI_CAT_ETCH.getRegion(0, 0, 113, 62);

    private static final TextureResource INT_JEI_CAT_ENERGIZE = getTextureJeiInt("energizer", 256, 256);
    public static final TextureRegion INT_JEI_CAT_ENERGIZE_BG = INT_JEI_CAT_ENERGIZE.getRegion(0, 0, 90, 47);

    private static TextureResource getTextureJeiInt(String path, int width, int height) {
        return getTexture("jei/" + path, width, height);
    }

}
