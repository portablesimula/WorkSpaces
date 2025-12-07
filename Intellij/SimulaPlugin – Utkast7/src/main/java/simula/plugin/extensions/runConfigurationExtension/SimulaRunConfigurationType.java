package simula.plugin.extensions.runConfigurationExtension;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import simula.plugin.util.Util;
import org.jetbrains.annotations.NotNull;

public class SimulaRunConfigurationType extends ConfigurationTypeBase {
    private static MyConfigurationFactory myConfigurationFactory;
    private static SimulaRunConfigurationType INSTANCE;


    public SimulaRunConfigurationType() {
        // ID, Name, Description, Icon
        super("Simula",
                "Simula",
                "A custom run configuration type for Simula",
//                AllIcons.General.Information); // Use a relevant icon
                Util.getSimulaIcon()); // Use a relevant icon
//        addFactory(new MyConfigurationFactory(this));
        myConfigurationFactory = new MyConfigurationFactory(this);
        addFactory(myConfigurationFactory);
    }

    public static @NotNull SimulaRunConfigurationType getInstance() {
        if(SimulaRunConfigurationType.INSTANCE == null)
            SimulaRunConfigurationType.INSTANCE = new SimulaRunConfigurationType();
        return SimulaRunConfigurationType.INSTANCE;
    }

    public static ConfigurationFactory myFactory() {
        return SimulaConfigurationFactory.getInstance();
    }

    public static @NotNull ConfigurationFactory getFactory() {
//        if (true) throw new RuntimeException("MyRunConfigurationType_saved.getFactory: FactoryBuilderSupport");
        System.out.println("SimulaRunConfigurationType.getFactory: ");
//        ConfigurationFactory[] factories = INSTANCE.getFactories();
//        return factories[0];
        return myFactory();
    }

    // Inner class for the factory
    private static class MyConfigurationFactory extends ConfigurationFactory {
        protected MyConfigurationFactory(ConfigurationType type) {
            super(type);
//            if(true) throw new RuntimeException("NEW SimulaRunConfigurationType: ");
            System.out.println("NEW SimulaRunConfigurationType: ");
        }

        @Override
        public String getId(){
//            if(true) throw new RuntimeException("SimulaRunConfigurationType.getId: ");
            System.out.println("SimulaRunConfigurationType.getId: -> Simula");
//            Thread.dumpStack();
            return("Simula");
        }

        @NotNull
        @Override
        public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
            System.out.println("SimulaRunConfigurationType.createTemplateConfiguration: ");
//            if(true) throw new RuntimeException("SimulaRunConfigurationType.createTemplateConfiguration: ");
            return new SimulaRunConfiguration(project, this, "My Run Config");
        }
    }
}
