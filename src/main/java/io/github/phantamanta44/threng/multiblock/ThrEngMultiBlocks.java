package io.github.phantamanta44.threng.multiblock;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.libnine.component.multiblock.MultiBlockType;
import io.github.phantamanta44.libnine.util.world.CuboidIterator;
import io.github.phantamanta44.libnine.util.world.structmatcher.StructureMatcherBlockState;
import io.github.phantamanta44.libnine.util.world.structmatcher.StructureMatcherCuboid;
import io.github.phantamanta44.threng.ThrEng;
import io.github.phantamanta44.threng.block.BlockBigAssembler;
import io.github.phantamanta44.threng.block.ThrEngBlocks;
import io.github.phantamanta44.threng.tile.base.IBigAssemblerUnit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

public class ThrEngMultiBlocks {

    public static final MultiBlockType<IBigAssemblerUnit> BIG_ASSEMBLER
            = ThrEng.INSTANCE.newMultiBlockType("big_assembler", 64, IBigAssemblerUnit.class);

    @InitMe
    public static void init() {
        StructureMatcherCuboid mBigAssembler = new StructureMatcherCuboid(
                StructureMatcherCuboid.CorePosition.IN_FACE,
                new StructureMatcherBlockState(s -> {
                    switch (s.getValue(BlockBigAssembler.TYPE)) {
                        case VENT:
                        case CONTROLLER:
                        case IO_PORT:
                            return true;
                        default:
                            return false;
                    }
                }),
                new StructureMatcherBlockState(s -> {
                    switch (s.getValue(BlockBigAssembler.TYPE)) {
                        case MODULE_PATTERN:
                        case MODULE_CPU:
                            return true;
                        default:
                            return false;
                    }
                }));
        mBigAssembler.setMinVolume(1);
        mBigAssembler.setMaxVolume(512);
        mBigAssembler.setWallEdgeMatcher(
                new StructureMatcherBlockState(s -> s.getValue(BlockBigAssembler.TYPE) == BlockBigAssembler.Type.FRAME));
        mBigAssembler.setPostTest((w, min, max) -> {
            Iterator<BlockPos> iter = new CuboidIterator(min.add(1, 1, 1), max.add(-1, -1, -1));
            boolean seenPatternStore = false;
            while (iter.hasNext()) {
                BlockPos pos = iter.next();
                if (!w.isAirBlock(pos)) {
                    IBlockState state = w.getBlockState(pos);
                    if (state.getBlock() != ThrEngBlocks.BIG_ASSEMBLER) {
                        return false;
                    } else if (state.getValue(BlockBigAssembler.TYPE) == BlockBigAssembler.Type.MODULE_PATTERN) {
                        seenPatternStore = true;
                    }
                }
            }
            return seenPatternStore;
        });
        BIG_ASSEMBLER.setStructureMatcher(mBigAssembler);
    }

}
