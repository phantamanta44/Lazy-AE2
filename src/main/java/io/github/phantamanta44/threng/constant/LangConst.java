package io.github.phantamanta44.threng.constant;

import io.github.phantamanta44.libnine.util.world.BlockSide;
import io.github.phantamanta44.threng.util.SlotType;
import net.minecraft.client.resources.I18n;

public class LangConst {

    public static final String ITEM_MATERIAL = "material";

    public static final String BLOCK_MACHINE = "machine";
    public static final String BLOCK_BIG_ASSEMBLER = "big_assembler";

    public static final String GUI_AGGREGATOR = "aggregator";
    public static final String GUI_CENTRIFUGE = "centrifuge";
    public static final String GUI_ETCHER = "etcher";
    public static final String GUI_FAST_CRAFTER = "fast_crafter";
    public static final String GUI_LEVEL_MAINTAINER = "level_maintainer";
    public static final String GUI_BIG_ASSEMBLER = "big_assembler";

    public static final String CONTAINER_KEY = ThrEngConst.MOD_ID + ".container.";
    public static final String CONTAINER_AGGREGATOR = CONTAINER_KEY + GUI_AGGREGATOR;
    public static final String CONTAINER_CENTRIFUGE = CONTAINER_KEY + GUI_CENTRIFUGE;
    public static final String CONTAINER_ETCHER = CONTAINER_KEY + GUI_ETCHER;
    public static final String CONTAINER_FAST_CRAFTER = CONTAINER_KEY + GUI_FAST_CRAFTER;
    public static final String CONTAINER_LEVEL_MAINTAINER = CONTAINER_KEY + GUI_LEVEL_MAINTAINER;
    public static final String CONTAINER_BIG_ASSEMBLER = CONTAINER_KEY + GUI_BIG_ASSEMBLER;

    public static final String MISC_KEY = ThrEngConst.MOD_ID + ".misc.";

    public static final String TT_KEY = MISC_KEY + "tooltip.";
    public static final String TT_REQ_QTY = TT_KEY + "request_size";
    public static final String TT_BATCH_SIZE = TT_KEY + "batch_size";
    public static final String TT_WORK_FRAC = TT_KEY + "work_fraction";
    public static final String TT_JOB_COUNT = TT_KEY + "job_count";
    public static final String TT_CPU_COUNT = TT_KEY + "cpu_count";
    public static final String TT_WORK_RATE = TT_KEY + "work_rate";

    public static final String NOTIF_KEY = MISC_KEY + "notif.";
    public static final String NOTIF_MULTIBLOCK_FORMED = NOTIF_KEY + "multiblock_formed";
    public static final String NOTIF_MULTIBLOCK_FAILED = NOTIF_KEY + "multiblock_failed";
    public static final String NOTIF_MULTIBLOCK_UNFORMED = NOTIF_KEY + "multiblock_unformed";

    public static String getSideName(BlockSide side) {
        return I18n.format(MISC_KEY + "side." + side.name().toLowerCase());
    }

    public static String getIoName(SlotType.BasicIO io) {
        return I18n.format(MISC_KEY + "io." + io.name().toLowerCase());
    }

    public static final String INT_KEY = ThrEngConst.MOD_ID + ".int.";

    public static final String INT_JEI_KEY = INT_KEY + "jei.";
    public static final String INT_JEI_CAT_KEY = INT_JEI_KEY + "category.";
    public static final String INT_JEI_CAT_AGG = INT_JEI_CAT_KEY + "aggregator";
    public static final String INT_JEI_CAT_PURIFY = INT_JEI_CAT_KEY + "centrifuge";
    public static final String INT_JEI_CAT_ETCH = INT_JEI_CAT_KEY + "etcher";

}
