package simula.plugin.extensions.exec;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import simula.plugin.extensions.config.SimulaRunConfiguration;
import simula.plugin.extensions.config.SimulaSettings;
import simula.plugin.util.Dialogs;
import simula.plugin.util.Util;

import javax.swing.*;
import java.nio.charset.Charset;
import java.util.Map;

// Called from: SimulaRunConfiguration
public class SimulaRunJarfile extends CommandLineState {
    private final SimulaSettings options;

    public SimulaRunJarfile(@NotNull ExecutionEnvironment environment, SimulaSettings options) {
        super(environment);
        this.options = options;
    }

    @Override
    protected @NotNull ProcessHandler startProcess() throws ExecutionException {
        Util.TRACE("+++++++++++++++++++++++++++++++++++++++++++++++++++");
        Util.TRACE("+++        SimulaRunJarfile.startProcess        +++");
        Util.TRACE("+++           Execute Users Program             +++");
        Util.TRACE("+++++++++++++++++++++++++++++++++++++++++++++++++++");
        // 1. Create a GeneralCommandLine object
        Util.TRACE("SimulaRunJarfile.startProcess: 1. Create a GeneralCommandLine object");
        GeneralCommandLine commandLine = getCommandLine();

        // 2. Wrap it in a ProcessHandler
        // OSProcessHandler is commonly used for standard external processes
        // Use ColoredProcessHandler if your output includes ANSI colors
//       ProcessHandler processHandler = new ColoredProcessHandler(commandLine);
        ProcessHandler processHandler = new OSProcessHandler(commandLine);

        // 3. Optional: attach a console view (though CommandLineState usually handles this automatically)
        // The console view will display stdout/stderr

        // 4. Add a listener to show "Process finished with exit code X"
        ProcessTerminatedListener.attach(processHandler);

        // 5. Return the handler
        return processHandler;
    }

    @Override
    @NotNull
    public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        ExecutionResult result = super.execute(executor, runner);
        ExecutionConsole console = result.getExecutionConsole();
        if (console instanceof ConsoleView consoleView) {
            ProcessHandler processHandler = result.getProcessHandler();
            consoleView.attachToProcess(processHandler);
            processHandler.startNotify();

            consoleView.print("SimulaRunJarfile.execute: ConsoleView attachToProcess...\n", ConsoleViewContentType.NORMAL_OUTPUT);

        }
        return result;
    }

    /// Get CommandLine for user program execution.
    private GeneralCommandLine getCommandLine() {
        Project project = getEnvironment().getProject();
        if (project == null) { Util.IERR("SimulaRunJarfile.call: project == null"); return null; }

        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        Editor editor = fileEditorManager.getSelectedTextEditor();
        if (editor == null) { Util.IERR("SimulaRunJarfile.call: editor == null"); return null; }
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
//        String workDirectory = currentFile.getParent().getParent().getPath();
        String workDirectory = currentFile.getParent().getPath();
        String name = currentFile.getNameWithoutExtension();
        String jarFile = workDirectory + "/bin/" + name + ".jar";


        // askRunJarFile
        String title = "AskRunJarFile: ";
        String msg = "Jar File: " + jarFile;
        msg +="\nWorkDirectory: " + workDirectory;
        msg +="\n\nDo you want to run JarFile now ?\n\n";
        int answer = Dialogs.optionDialog(msg,title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, "Execute", "Cancel");
        if(answer != JOptionPane.OK_OPTION) return null;
        System.out.println("SimulaRunJarfile.call: DO RUN JARFILE");

        // TODO: DETTE MÅ RETTES FØR ENDELIG VERSJON
        // HUSK Å RETTE Java 21 ==> 25 OVERALT OGSÅ JAVA_HOME i Miljøvariablene
//        String javaExePath = "java";
        String javaExePath = "C:\\Program Files\\Java\\jdk-25\\bin\\java.exe";

        GeneralCommandLine commandLine = new GeneralCommandLine()
                .withExePath(javaExePath)
                .withParameters("-jar", jarFile)
                .withWorkDirectory(workDirectory) // Set working directory
                .withCharset(Charset.forName("UTF-8"));
        options.addRuntimeOptions(commandLine);
        Util.TRACE("SimulaRunJarfile.getCommandLine: commandLine="+commandLine);
        return commandLine;
    }

}
