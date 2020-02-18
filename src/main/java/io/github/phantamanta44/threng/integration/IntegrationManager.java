package io.github.phantamanta44.threng.integration;

import io.github.phantamanta44.threng.ThrEng;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.util.ArrayList;
import java.util.List;

public class IntegrationManager {

    private final List<IntegrationModule> integrations = new ArrayList<>();

    public void load(ASMDataTable asmTable) {
        for (ASMDataTable.ASMData asmData : asmTable.getAll(IntegrationModule.Register.class.getName())) {
            String modId = (String)asmData.getAnnotationInfo().get("value");
            if (Loader.isModLoaded(modId)) {
                ThrEng.LOGGER.info("Loading mod integration: {}", modId);
                try {
                    integrations.add((IntegrationModule)Class.forName(asmData.getClassName()).newInstance());
                } catch (Exception e) {
                    ThrEng.LOGGER.error("Failed to load integration!", e);
                }
            } else {
                ThrEng.LOGGER.info("Skipping missing mod for integration: {}", modId);
            }
        }
    }

    public void init() {
        for (IntegrationModule module : integrations) {
            try {
                ThrEng.LOGGER.info("Initializing mod integration: {}", module.getClass().getSimpleName());
                module.init();
            } catch (Exception e) {
                ThrEng.LOGGER.error("Failed to initialize integration!", e);
            }
        }
    }

}
