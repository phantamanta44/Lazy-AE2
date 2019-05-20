package io.github.phantamanta44.threng.constant;

import io.github.phantamanta44.libnine.util.world.BlockSide;
import io.github.phantamanta44.threng.util.SlotType;
import net.minecraft.client.resources.I18n;

public class LangConst {

    public static final String ITEM_MATERIAL = "material";

    public static final String BLOCK_MACHINE = "machine";

    public static final String GUI_AGGREGATOR = "aggregator";
    public static final String GUI_CENTRIFUGE = "centrifuge";
    public static final String GUI_ETCHER = "etcher";

    public static final String CONTAINER_KEY = ThrEngConst.MOD_ID + ".container.";
    public static final String CONTAINER_AGGREGATOR = CONTAINER_KEY + GUI_AGGREGATOR;
    public static final String CONTAINER_CENTRIFUGE = CONTAINER_KEY + GUI_CENTRIFUGE;
    public static final String CONTAINER_ETCHER = CONTAINER_KEY + GUI_ETCHER;

    public static final String MISC_KEY = ThrEngConst.MOD_ID + ".misc.";

    public static String getSideName(BlockSide side) {
        return I18n.format(MISC_KEY + "side." + side.name().toLowerCase());
    }

    public static String getIoName(SlotType.BasicIO io) {
        return I18n.format(MISC_KEY + "io." + io.name().toLowerCase());
    }

}
