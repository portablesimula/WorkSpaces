package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SimulaConfigurationFactory extends ConfigurationFactory {
    private static SimulaConfigurationFactory INSTANCE;

    protected SimulaConfigurationFactory(SimulaRunConfigurationType type) {
        super(type);
    }

   protected SimulaConfigurationFactory() {
        super(SimulaRunConfigurationType.getInstance());
    }

    public static @NotNull SimulaConfigurationFactory getInstance() {
        if(SimulaConfigurationFactory.INSTANCE == null) SimulaConfigurationFactory.INSTANCE = new SimulaConfigurationFactory();
        return SimulaConfigurationFactory.INSTANCE;
    }

    /**
     * Creates a new template run configuration within the context of the specified project.
     *
     * @param project the project in which the run configuration will be used
     */
    @Override
    public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        System.out.println("MyConfigurationFactory.createTemplateConfiguration: ");
//        if(true) throw new RuntimeException("MyConfigurationFactory.createTemplateConfiguration: ");
        // Instantiate your custom RunConfiguration with default settings
        // The name "Template" is a common convention for the template instance
        return new SimulaRunConfiguration(project, this, "Template");
    }

    @Override
    public @NotNull String getId() {
        // Return a unique, stable, and non-localized identifier for this factory
//        if(true) throw new RuntimeException("MyConfigurationFactory.getId: ");
        return "Simula"; // Example ID
    }
}