package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.NotNullLazyValue;
import org.jetbrains.annotations.NotNull;
import simula.plugin.util.Util;

final class DemoRunConfigurationType extends ConfigurationTypeBase {

//    private static DemoConfigurationFactory myConfigurationFactory;
//    private static DemoRunConfigurationType INSTANCE;

    DemoRunConfigurationType() {
        super("Simula",
                "Simula",
                "A custom run configuration type for Simula",
                Util.getSimulaIcon()); // Use a relevant icon
//        myConfigurationFactory = new DemoConfigurationFactory(this);
//        addFactory(myConfigurationFactory);
        addFactory(new DemoConfigurationFactory(this));
    }

//    public static @NotNull DemoRunConfigurationType getInstance() {
//        if(DemoRunConfigurationType.INSTANCE == null)
//            DemoRunConfigurationType.INSTANCE = new DemoRunConfigurationType();
//        return DemoRunConfigurationType.INSTANCE;
//    }
//
//    public static ConfigurationFactory myFactory() {
//        return DemoConfigurationFactory.getInstance();
//    }

//    public static @NotNull ConfigurationFactory getFactory() {
//        System.out.println("DemoRunConfigurationType.getFactory: ");
//        return myFactory();
//    }

}