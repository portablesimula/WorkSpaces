package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simula.plugin.extensions.runConfiguration.run.SimulaCommandLineState;
import simula.plugin.extensions.runConfiguration.run.simulaCompiler.SimulaCompiler;

import java.util.Map;

public class DemoRunConfiguration extends RunConfigurationBase<DemoRunConfigurationOptions> {
    DemoRunConfigurationOptions demoRunConfigurationOptions;

    protected DemoRunConfiguration(Project project,
                                   ConfigurationFactory factory,
                                   String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    protected DemoRunConfigurationOptions getOptions() {
        return (DemoRunConfigurationOptions) super.getOptions();
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

    @Nullable
    @Override
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
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
        // Call the Simula Compiler to produce the .jar file
        int exitCode = SimulaCompiler.call(environment.getProject(), demoRunConfigurationOptions);

        if(exitCode == 0) {
            System.out.println("SimulaRunConfiguration.getState: Execute resulting .jar");

            // Return the execution logic to Intellij which will execute it
            return new SimulaCommandLineState(environment);
        }
        return null;
    }

}