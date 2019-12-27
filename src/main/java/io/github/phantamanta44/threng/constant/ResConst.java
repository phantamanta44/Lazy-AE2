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

    public static final TextureResource GUI_LEVEL_MAINTAINER = getTextureGui("level_maintainer", 256, 256);

    public static final TextureResource GUI_BIG_ASSEMBLER = getTextureGui("big_assembler", 256, 256);
    public static final TextureRegion GUI_BIG_ASSEMBLER_QUEUE = GUI_BIG_ASSEMBLER.getRegion(176, 0, 3, 25);
    public static final TextureRegion GUI_BIG_ASSEMBLER_CPUS = GUI_BIG_ASSEMBLER.getRegion(179, 0, 3, 25);
    public static final TextureRegion GUI_BIG_ASSEMBLER_PROGRESS = GUI_BIG_ASSEMBLER.getRegion(182, 0, 16, 2);

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

    private static final TextureResource GUI_COMP_SUBMIT = getTextureGuiComp("submit", 39, 13);
    public static final TextureRegion GUI_COMP_SUBMIT_NORMAL = GUI_COMP_SUBMIT.getRegion(0, 0, 13, 13);
    public static final TextureRegion GUI_COMP_SUBMIT_DISABLED = GUI_COMP_SUBMIT.getRegion(13, 0, 13, 13);
    public static final TextureRegion GUI_COMP_SUBMIT_HOVERED = GUI_COMP_SUBMIT.getRegion(26, 0, 13, 13);

    private static final TextureResource GUI_COMP_NEXT_PREV = getTextureGuiComp("next_prev", 36, 33);
    public static final TextureRegion GUI_COMP_NEXT_PREV_PREV_NORMAL = GUI_COMP_NEXT_PREV.getRegion(0, 0, 18, 11);
    public static final TextureRegion GUI_COMP_NEXT_PREV_PREV_DISABLED = GUI_COMP_NEXT_PREV.getRegion(0, 11, 18, 11);
    public static final TextureRegion GUI_COMP_NEXT_PREV_PREV_HOVERED = GUI_COMP_NEXT_PREV.getRegion(0, 22, 18, 11);
    public static final TextureRegion GUI_COMP_NEXT_PREV_NEXT_NORMAL = GUI_COMP_NEXT_PREV.getRegion(18, 0, 18, 11);
    public static final TextureRegion GUI_COMP_NEXT_PREV_NEXT_DISABLED = GUI_COMP_NEXT_PREV.getRegion(18, 11, 18, 11);
    public static final TextureRegion GUI_COMP_NEXT_PREV_NEXT_HOVERED = GUI_COMP_NEXT_PREV.getRegion(18, 22, 18, 11);

    private static TextureResource getTextureGuiComp(String path, int width, int height) {
        return getTextureGui("component/" + path, width, height);
    }

    private static final TextureResource INT_JEI_CAT_AGG = getTextureJeiInt("aggregator", 256, 256);
    public static final TextureRegion INT_JEI_CAT_AGG_BG = INT_JEI_CAT_AGG.getRegion(0, 0, 162, 72);

    private static final TextureResource INT_JEI_CAT_PURIFY = getTextureJeiInt("centrifuge", 256, 256);
    public static final TextureRegion INT_JEI_CAT_PURIFY_BG = INT_JEI_CAT_PURIFY.getRegion(0, 0, 162, 72);

    private static final TextureResource INT_JEI_CAT_ETCH = getTextureJeiInt("etcher", 256, 256);
    public static final TextureRegion INT_JEI_CAT_ETCH_BG = INT_JEI_CAT_ETCH.getRegion(0, 0, 162, 72);

    private static TextureResource getTextureJeiInt(String path, int width, int height) {
        return getTexture("jei/" + path, width, height);
    }

}
