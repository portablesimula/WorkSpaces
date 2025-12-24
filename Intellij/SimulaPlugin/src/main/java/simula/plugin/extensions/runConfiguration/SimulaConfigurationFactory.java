package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimulaConfigurationFactory extends ConfigurationFactory {

    protected SimulaConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @Override
    public String getId() { return("Simula"); }

    /// Creates a new template run configuration within the context of the specified project.
    /// @param project the project in which the run configuration will be used
    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new SimulaRunConfiguration(project, this, "Demo run config");
    }

    @Nullable
    @Override
    public Class<? extends BaseState> getOptionsClass() {
        return SimulaRunConfigurationOptions.class;
    }

}