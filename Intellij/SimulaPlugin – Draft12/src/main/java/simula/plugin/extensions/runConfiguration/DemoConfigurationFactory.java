package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DemoConfigurationFactory extends ConfigurationFactory {

//    private static DemoConfigurationFactory INSTANCE;
//
//    protected DemoConfigurationFactory() {
//        super(DemoRunConfigurationType.getInstance());
//    }
//
//    public static @NotNull DemoConfigurationFactory getInstance() {
//        if(DemoConfigurationFactory.INSTANCE == null) DemoConfigurationFactory.INSTANCE = new DemoConfigurationFactory();
//        return DemoConfigurationFactory.INSTANCE;
//    }

    protected DemoConfigurationFactory(ConfigurationType type) {
        super(type);
    }

//    @Override
//    public @NotNull String getId() {
//        return DemoRunConfigurationType.ID;
//    }
    @Override
    public String getId() { return("Simula"); }

    /// Creates a new template run configuration within the context of the specified project.
    /// @param project the project in which the run configuration will be used
    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new DemoRunConfiguration(project, this, "Demo run config");
    }

    @Nullable
    @Override
    public Class<? extends BaseState> getOptionsClass() {
        return DemoRunConfigurationOptions.class;
    }

}