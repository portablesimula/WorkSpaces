package simula.plugin.extensions.runConfigurationExtension;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.LocatableConfigurationBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.Executor;
import com.intellij.execution.impl.CheckableRunConfigurationEditor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.options.SettingsEditorGroup;
import com.intellij.openapi.project.Project;
import simula.plugin.extensions.runConfigurationExtension.run.SimulaCommandLineState;
import simula.plugin.extensions.runConfigurationExtension.run.simulaCompiler.SimulaCompiler;
import simula.plugin.util.CTOption;
import simula.plugin.util.RTOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimulaRunConfiguration extends LocatableConfigurationBase<MyRunConfigurationOptions> {
    public CTOption ctOption;
    public RTOption rtOption;

    protected SimulaRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
        System.out.println("NEW MyRunConfiguration: ");
//        if(true) throw new RuntimeException("NEW MyRunConfiguration: ");

        ctOption = new CTOption();
        rtOption = new RTOption();
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
        System.out.println("MyRunConfiguration.getState: ");
        System.out.println("MyRunConfiguration.getState: executor.getActionName: " + executor.getActionName());

        System.out.println("MyRunConfiguration.getState: environment.getModulePath: " + environment.getModulePath());

        // Call the Simula Compiler to produce the .jar file
        SimulaCompiler.call(environment.getProject(), ctOption);

        System.out.println("MyRunConfiguration.getState: ");
//        if(true) throw new RuntimeException("MyRunConfiguration.getState: ");

        // Return the execution logic to Intellij which will execute it
        return new SimulaCommandLineState(environment);
    }

    /**
     * Returns the UI control for editing the run configuration settings.
     * <p>
     * If additional control over validation is required, the object
     * returned from this method may also implement {@link CheckableRunConfigurationEditor}.
     * <p>
     * If the settings it provides need to be displayed in multiple tabs,
     * returned editor should extend {@link SettingsEditorGroup}.
     *
     * @return the settings editor component.
     */
    @Override
    public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        System.out.println("MyRunConfiguration.getConfigurationEditor: ");
//        if(true) throw new RuntimeException("MyRunConfiguration.getConfigurationEditor: ");
        // Return your custom SettingsEditor implementation (see step 4)
        return new SimulaSettingsEditor(this);
    }

    // You also need methods to read/write settings if not using the default options class
}
