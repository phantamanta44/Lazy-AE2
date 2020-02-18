package io.github.phantamanta44.threng.integration.oc;

import io.github.phantamanta44.threng.integration.IntegrationModule;
import li.cil.oc.api.Driver;

@IntegrationModule.Register("opencomputers")
public class ThrEngOc implements IntegrationModule {

    @Override
    public void init() {
        Driver.add(new OcDriverLevelMaintainer());
        Driver.add(new OcDriverLevelMaintainer.EnvProvider());
    }

}
