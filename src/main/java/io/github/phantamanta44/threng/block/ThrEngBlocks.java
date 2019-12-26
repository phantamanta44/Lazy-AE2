package io.github.phantamanta44.threng.block;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.threng.constant.ThrEngConst;

@SuppressWarnings("NotNullFieldNotInitialized")
public class ThrEngBlocks {

    public static BlockMachine MACHINE;

    @InitMe(ThrEngConst.MOD_ID)
    public static void init() {
        MACHINE = new BlockMachine();
    }

}
