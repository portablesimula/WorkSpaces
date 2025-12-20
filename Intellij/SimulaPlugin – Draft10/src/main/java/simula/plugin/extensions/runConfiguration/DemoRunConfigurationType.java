package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.NotNullLazyValue;
import simula.plugin.util.Util;

final class DemoRunConfigurationType extends ConfigurationTypeBase {

    static final String ID = "DemoRunConfiguration";

    DemoRunConfigurationType() {
//        super(ID, "Demo", "Demo run configuration type", NotNullLazyValue.createValue(() -> AllIcons.Nodes.Console));
        super(ID, "Demo", "Demo run configuration type", Util.getSimulaIcon());
        addFactory(new DemoConfigurationFactory(this));
    }

}