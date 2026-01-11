package simula.plugin.extensions.config;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import simula.plugin.extensions.exec.SimulaCompiler;
import simula.plugin.extensions.exec.SimulaRunJarfile;

import java.util.Map;

public class SimulaRunConfiguration extends RunConfigurationBase<SimulaSettings> {

    protected SimulaRunConfiguration(Project project,
                                     ConfigurationFactory factory,
                                     String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    protected SimulaSettings getOptions() {
        return (SimulaSettings) super.getOptions();
    }

    public Map<String, String> getOptionsMap() {
        return getOptions().getOptionsMap();
    }

    public void setOptionsMap(Map<String, String> newOptions) {
        getOptions().setOptionsMap(newOptions);
    }


    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new SimulaSettingsEditor();
    }

    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
        System.out.println("SimulaRunConfiguration.getState: executor.getActionName: " + executor.getActionName());
        System.out.println("SimulaRunConfiguration.getState: environment.getModulePath: " + environment.getModulePath());

        System.out.println("SimulaRunConfiguration.getState: Call the Simula Compiler to produce the .jar file");
        int exitCode = SimulaCompiler.call(environment, getOptions());

        if(exitCode == 0) {
            System.out.println("SimulaRunConfiguration.getState: Execute resulting .jar");
            return new SimulaRunJarfile(environment, getOptions());
        }
        return null;

    }

}