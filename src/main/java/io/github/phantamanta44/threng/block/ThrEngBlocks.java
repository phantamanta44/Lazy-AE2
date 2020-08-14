package io.github.phantamanta44.threng.block;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ThrEngConst;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ThrEngBlocks {

    @GameRegistry.ObjectHolder(ThrEngConst.MOD_ID + ":" + LangConst.BLOCK_MACHINE)
    public static BlockMachine MACHINE;
    @GameRegistry.ObjectHolder(ThrEngConst.MOD_ID + ":" + LangConst.BLOCK_BIG_ASSEMBLER)
    public static BlockBigAssembler BIG_ASSEMBLER;

    @InitMe(ThrEngConst.MOD_ID)
    public static void init() {
        new BlockMachine();
        new BlockBigAssembler();
    }

}
