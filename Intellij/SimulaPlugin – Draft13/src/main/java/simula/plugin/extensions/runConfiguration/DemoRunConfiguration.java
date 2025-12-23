package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import simula.plugin.extensions.runConfiguration.run.SimulaCompilerCommandLineState;
import simula.plugin.extensions.runConfiguration.run.SimulaCompiler;
import simula.plugin.extensions.runConfiguration.run.SimulaExecCommandLineState;
import simula.plugin.util.Util;

import java.util.Map;

public class DemoRunConfiguration extends RunConfigurationBase<DemoRunConfigurationOptions> {
//    DemoRunConfigurationOptions demoRunConfigurationOptions;

    protected DemoRunConfiguration(Project project,
                                   ConfigurationFactory factory,
                                   String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    protected DemoRunConfigurationOptions getOptions() {
//        return (DemoRunConfigurationOptions) super.getOptions();
        RunConfigurationOptions options = super.getOptions();
//        System.out.println("DemoRunConfiguration.getOptions: " + options.getClass().getSimpleName());
        return (DemoRunConfigurationOptions) options;
    }

    public String getJarFileDirName() {
        return getOptions().getJarFileDirName();
    }

    public void setJarFileDirName(String dirName) {
        getOptions().setJarFileDirName(dirName);
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
        return new DemoSettingsEditor();
    }

//    @Nullable
//    @Override
//    public RunProfileState getState(@NotNull Executor executor,
//                                    @NotNull ExecutionEnvironment environment) {
//        return new CommandLineState(environment) {
//            @NotNull
//            @Override
//            protected ProcessHandler startProcess() throws ExecutionException {
//                GeneralCommandLine commandLine =
//                        new GeneralCommandLine(getOptions().getScriptName());
//                OSProcessHandler processHandler = ProcessHandlerFactory.getInstance()
//                        .createColoredProcessHandler(commandLine);
//                ProcessTerminatedListener.attach(processHandler);
//                return processHandler;
//            }
//        };
//    }

    public static final boolean SINGLE_STEP_EXEC = false;//true;

    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
        Map<String, String> optionMap = getOptions().getOptionsMap();
        if(SINGLE_STEP_EXEC) {
            return new SimulaCompilerCommandLineState(environment, optionMap);
        } else {
            System.out.println("MyRunConfiguration.getState: ");
            System.out.println("MyRunConfiguration.getState: executor.getActionName: " + executor.getActionName());

            System.out.println("MyRunConfiguration.getState: environment.getModulePath: " + environment.getModulePath());

            // Call the Simula Compiler to produce the .jar file
//            int exitCode = SimulaCompiler.call(environment.getProject(), simOptions);
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
        if(runProfile instanceof DemoRunConfiguration myRunConfiguration) {
            DemoRunConfigurationOptions options = myRunConfiguration.getState();
            Map<String, String> optionMap = options.getOptionsMap();
            DemoRunConfigurationOptions.setDefaults(optionMap);
            Util.TRACE("SimulaCompiler.runCommandFromPlugin: optionMap=" + optionMap);
            return optionMap;
        }
        return null;
    }

}