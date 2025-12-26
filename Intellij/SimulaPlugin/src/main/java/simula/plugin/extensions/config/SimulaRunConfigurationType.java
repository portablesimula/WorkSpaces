package simula.plugin.extensions.config;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import simula.plugin.util.Util;

final class SimulaRunConfigurationType extends ConfigurationTypeBase {

    SimulaRunConfigurationType() {
        super("Simula",
                "Simula",
                "A custom run configuration type for Simula",
                Util.getSimulaIcon()); // Use a relevant icon
        addFactory(new SimulaConfigurationFactory(this));
    }

}