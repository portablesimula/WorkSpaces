package simula.plugin.extensions.exec;

import com.intellij.execution.ExecutionResult;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import org.jetbrains.annotations.NotNull;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.GenericProgramRunner;
import com.intellij.execution.ui.RunContentDescriptor;
import org.jetbrains.annotations.Nullable;

public class SimulaProgramRunner extends GenericProgramRunner<RunnerSettings> {

    @NotNull
    @Override
    public String getRunnerId() {
        return "SimulaRunner";
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        // Return true if this runner supports the specific executor and profile type
        return DefaultRunExecutor.EXECUTOR_ID.equals(executorId); // && profile instanceof SimulaRunConfigurationType;
    }
    @Nullable
    @Override
    protected RunContentDescriptor doExecute(
            @NotNull RunProfileState runProfileState,
            @NotNull ExecutionEnvironment env) throws ExecutionException {
        System.out.println("SimulaProgramRunner.doExecute: ");

        // 1. Save all documents
        FileDocumentManager.getInstance().saveAllDocuments();
        System.out.println("SimulaProgramRunner.doExecute: Step1 Done - runProfileState="+runProfileState);

        // 2. The state is responsible for setting up the command line and creating the process handler
        // ExecutionResult holds the ProcessHandler and the ConsoleView
        final ExecutionResult executionResult = runProfileState.execute(env.getExecutor(), this);
        if (executionResult == null) {
            return null; // Execution failed or was cancelled
        }

        // 3. Create the RunContentDescriptor
        //    When you override doExecute in your GenericProgramRunner,
        //    you must ensure the console you attach to the RunContentDescriptor is interactive.
        //    The standard TextConsoleBuilder creates a console that supports input by default if attached to a process.
//        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
//        ConsoleView consoleView = consoleBuilder.getConsole();
//        TextConsoleBuilderFactory factory = TextConsoleBuilderFactory.getInstance();
//        TextConsoleBuilder builder = factory.createBuilder(project);


        final RunContentDescriptor descriptor = new RunContentDescriptor(
                executionResult.getExecutionConsole(),
                executionResult.getProcessHandler(),
                executionResult.getExecutionConsole().getComponent(),
                env.getRunProfile().getName()
        );

        // Optional: Set the reuse of the run content descriptor if needed
        env.setContentToReuse(descriptor);

        return descriptor;

    }

}