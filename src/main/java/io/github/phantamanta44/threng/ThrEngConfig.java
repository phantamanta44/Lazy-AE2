package io.github.phantamanta44.threng;

import io.github.phantamanta44.threng.constant.ThrEngConst;
import net.minecraftforge.common.config.Config;

@Config(modid = ThrEngConst.MOD_ID, name = "lazy_ae2")
public class ThrEngConfig {

    public static final ProcessingConfig processing = new ProcessingConfig();

    public static class ProcessingConfig {

        @Config.Comment("The size of the fluix aggregator's energy buffer.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int aggregatorEnergyBuffer = 100000;

        @Config.Comment("The base energy cost for each fluix aggregation operation performed.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int aggregatorEnergyCostBase = 8100;

        @Config.Comment("The additional energy cost for fluix aggregation incurred by each acceleration card.")
        @Config.RangeInt(min = 0)
        @Config.RequiresWorldRestart
        public int aggregatorEnergyCostUpgrade = 863;

        @Config.Comment("The base number of ticks needed to complete one fluix aggregation operation.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int aggregatorWorkTicksBase = 150;

        @Config.Comment("The number of ticks by which each acceleration card hastens a fluix aggregation operation.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int aggregatorWorkTicksUpgrade = 18;

        @Config.Comment("The size of the pulse centrifuge's energy buffer.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int centrifugeEnergyBuffer = 100000;

        @Config.Comment("The base energy cost for each centrifuging operation performed.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int centrifugeEnergyCostBase = 8100;

        @Config.Comment("The additional energy cost for centrifuging incurred by each acceleration card.")
        @Config.RangeInt(min = 0)
        @Config.RequiresWorldRestart
        public int centrifugeEnergyCostUpgrade = 863;

        @Config.Comment("The base number of ticks needed to complete one centrifuging operation.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int centrifugeWorkTicksBase = 150;

        @Config.Comment("The number of ticks by which each acceleration card hastens a centrifuging operation.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int centrifugeWorkTicksUpgrade = 18;

        @Config.Comment("The size of the circuit etcher's energy buffer.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int etcherEnergyBuffer = 100000;

        @Config.Comment("The base energy cost for each circuit etching operation performed.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int etcherEnergyCostBase = 8100;

        @Config.Comment("The additional energy cost for circuit etching incurred by each acceleration card.")
        @Config.RangeInt(min = 0)
        @Config.RequiresWorldRestart
        public int etcherEnergyCostUpgrade = 863;

        @Config.Comment("The base number of ticks needed to complete one circuit etching operation.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int etcherWorkTicksBase = 150;

        @Config.Comment("The number of ticks by which each acceleration card hastens a circuit etching operation.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int etcherWorkTicksUpgrade = 18;

        @Config.Comment("The size of the crystal energizer's energy buffer.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int energizerEnergyBuffer = 100000;

        @Config.Comment("The additional energy cost for crystal energization incurred by each acceleration card.")
        @Config.RangeInt(min = 0)
        @Config.RequiresWorldRestart
        public int energizerEnergyCostUpgrade = 1625;

        @Config.Comment("The base number of ticks needed to complete one crystal energization operation.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int energizerWorkTicksBase = 150;

        @Config.Comment("The number of ticks by which each acceleration card hastens a crystal energization operation.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int energizerWorkTicksUpgrade = 18;
        
    }

    public static final NetworkDeviceConfig networkDevices = new NetworkDeviceConfig();

    public static class NetworkDeviceConfig {

        @Config.Comment("The idle power consumption of the preemptive assembly unit.")
        @Config.RangeDouble(min = 0D)
        @Config.RequiresWorldRestart
        public double fastCrafterIdlePower = 6D;

        @Config.Comment("The idle power consumption of the level maintainer.")
        @Config.RangeDouble(min = 0D)
        @Config.RequiresWorldRestart
        public double levelMaintainerIdlePower = 3D;

        @Config.Comment({
                "The interval between work ticks for the level maintainer while active.",
                "Setting this too low may cause lag!"
        })
        @Config.RangeInt(min = 0)
        @Config.RequiresWorldRestart
        public int levelMaintainerSleepActive = 16;

        @Config.Comment({
                "The interval between work ticks for the level maintainer while not active.",
                "Setting this too low may cause lag!"
        })
        @Config.RangeInt(min = 0)
        @Config.RequiresWorldRestart
        public int levelMaintainerSleepPassive = 64;

    }

    public static final MassAssemblerConfig massAssembler = new MassAssemblerConfig();

    public static class MassAssemblerConfig {

        @Config.Comment("The idle power consumption of the mass assembly chamber.")
        @Config.RangeDouble(min = 0D)
        @Config.RequiresWorldRestart
        public double idlePower = 3D;

        @Config.Comment({
                "The size of the mass assembler's crafting job queue.",
                "Some crafting job data may be lost if this is decreased!"
        })
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int jobQueueSize = 64;

        @Config.Comment("The amount of work needed to complete one crafting job.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int workPerJob = 16;

        @Config.Comment("The base amount of energy consumed to perform one unit of work.")
        @Config.RangeDouble(min = 0D)
        @Config.RequiresWorldRestart
        public double energyPerWorkBase = 16D;

        @Config.Comment("The additional energy consumed per unit of work for each installed coprocessor.")
        @Config.RangeDouble(min = 0D)
        @Config.RequiresWorldRestart
        public double energyPerWorkUpgrade = 1D;

        @Config.Comment({
                "The base amount of work performed per tick.",
                "If set to zero, the mass assembler will not do any work without a coprocessor installed."
        })
        @Config.RangeInt(min = 0)
        @Config.RequiresWorldRestart
        public int workPerTickBase = 1;

        @Config.Comment("The additional work performed per tick for each installed coprocessor.")
        @Config.RangeInt(min = 1)
        @Config.RequiresWorldRestart
        public int workPerTickUpgrade = 3;

    }

}
