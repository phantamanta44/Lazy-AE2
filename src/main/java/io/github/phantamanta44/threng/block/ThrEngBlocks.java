package io.github.phantamanta44.threng.block;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.libnine.LibNine;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ThrEngConst;

@SuppressWarnings("NullableProblems")
public class ThrEngBlocks {

    public static BlockMachine MACHINE;

    @InitMe(ThrEngConst.MOD_ID)
    public static void init() {
        MACHINE = new BlockMachine();

        BlockMachine.Type[] machineTypes = BlockMachine.Type.values();
        for (int i = 0; i < machineTypes.length; i++) {
            LibNine.PROXY.getRegistrar()
                    .queueItemBlockModelReg(MACHINE, i, LangConst.BLOCK_MACHINE, "inv_" + machineTypes[i].getName());
        }
    }

}
