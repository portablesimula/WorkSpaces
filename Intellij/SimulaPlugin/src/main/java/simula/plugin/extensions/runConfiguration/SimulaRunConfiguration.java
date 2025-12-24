package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import simula.plugin.extensions.runConfiguration.run.SimulaCompileAndExecCommandLineState;
import simula.plugin.extensions.runConfiguration.run.SimulaCompiler;
import simula.plugin.extensions.runConfiguration.run.SimulaExecCommandLineState;
import simula.plugin.util.Util;

import java.util.Map;

public class SimulaRunConfiguration extends RunConfigurationBase<SimulaRunConfigurationOptions> {

    protected SimulaRunConfiguration(Project project,
                                     ConfigurationFactory factory,
                                     String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    protected SimulaRunConfigurationOptions getOptions() {
        return (SimulaRunConfigurationOptions) super.getOptions();
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

    
//    public static final boolean SIMULA_COMPILE_AND_EXEC_IN_ONE_STEP = true;
    public static final boolean SIMULA_COMPILE_AND_EXEC_IN_ONE_STEP = false;

    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
        Map<String, String> optionMap = getOptions().getOptionsMap();
        if(SIMULA_COMPILE_AND_EXEC_IN_ONE_STEP) {
            return new SimulaCompileAndExecCommandLineState(environment, optionMap);
        } else {
            System.out.println("SimulaRunConfiguration.getState: executor.getActionName: " + executor.getActionName());
            System.out.println("SimulaRunConfiguration.getState: environment.getModulePath: " + environment.getModulePath());

            // Call the Simula Compiler to produce the .jar file
            int exitCode = SimulaCompiler.call(environment, optionMap);

            if(exitCode == 0) {
                System.out.println("SimulaRunConfiguration.getState: Execute resulting .jar");
                return new SimulaExecCommandLineState(environment, optionMap);
            }
            return null;
        }
    }

    private Map<String, String> getOptionsMap(ExecutionEnvironment environment) {
        RunProfile runProfile = environment.getRunProfile();
        if(runProfile instanceof SimulaRunConfiguration myRunConfiguration) {
            SimulaRunConfigurationOptions options = myRunConfiguration.getState();
            Map<String, String> optionMap = options.getOptionsMap();
            SimulaRunConfigurationOptions.setDefaults(optionMap);
            Util.TRACE("SimulaRunConfiguration.getOptionsMap: " + optionMap);
            return optionMap;
        }
        return null;
    }

}