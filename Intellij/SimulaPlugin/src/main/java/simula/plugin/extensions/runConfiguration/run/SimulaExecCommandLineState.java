package simula.plugin.extensions.runConfiguration.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import simula.plugin.extensions.runConfiguration.DemoRunConfiguration;
import simula.plugin.extensions.runConfiguration.DemoRunConfigurationOptions;
import simula.plugin.util.Dialogs;
import simula.plugin.util.Global;
import simula.plugin.util.Util;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

// Called from: DemoRunConfiguration
public class SimulaExecCommandLineState extends CommandLineState {
    Map<String, String> optionMap;

    public SimulaExecCommandLineState(@NotNull ExecutionEnvironment environment, Map<String, String> optionMap) {
        super(environment);
        this.optionMap = optionMap;
    }

    @Override
    protected @NotNull ProcessHandler startProcess() throws ExecutionException {
        Util.TRACE("+++++++++++++++++++++++++++++++++++++++++++++++++++");
        Util.TRACE("+++   SimulaExecCommandLineState.startProcess   +++");
        Util.TRACE("+++           Execute Users Program             +++");
        Util.TRACE("+++++++++++++++++++++++++++++++++++++++++++++++++++");
        // 1. Create a GeneralCommandLine object
        Util.TRACE("SimulaCommandLineState.startProcess: 1. Create a GeneralCommandLine object");
        GeneralCommandLine commandLine = getCommandLine();

        // 2. Wrap it in a ProcessHandler
        // OSProcessHandler is commonly used for standard external processes
        ProcessHandler processHandler = new OSProcessHandler(commandLine);

        // 3. Optional: attach a console view (though CommandLineState usually handles this automatically)
        // The console view will display stdout/stderr
//        ConsoleView consoleView = getConsoleViewFromToolWindow(getEnvironment().getProject(), "Simula Trace Console");
//        if(consoleView != null) consoleView.attachToProcess(processHandler);
//        SEE: Code below ...

        // 4. Return the handler
        return processHandler;
    }

    /// Get CommandLine for Simula and user program execution in one step.
    private GeneralCommandLine getCommandLine() {
        Project project = getEnvironment().getProject();
        String userHomeDir = System.getProperty("user.home");
        loadSimulaProperties();
        String simulaHomeDir = simulaProperties.getProperty("simula.home");
        String workDirectory =  project.getBasePath();
        String simulaOutDir = workDirectory + "/bin";
        String sourceFile = getCurrentFilePath(project);
        Util.TRACE("SimulaExecCommandLineState.getCommandLine: sourceFile=" + sourceFile);
        Global.currentSourceFile = sourceFile;
        if(sourceFile == null) {
            Util.IERR("SimulaExecCommandLineState.getCommandLine: No Source file available");
            return null;
        }
        String userDir = workDirectory + "/ssf";


        // TODO: DETTE MÅ RETTES FØR ENDELIG VERSJON
//        String javaExePath = "java";
        String javaExePath = "C:\\Program Files\\Java\\jdk-25\\bin\\java.exe";

        // Set up for Simula Compiler
        GeneralCommandLine commandLine = new GeneralCommandLine()
                .withExePath(javaExePath)
                .withParameters("-jar"
                        , "C:/Users/omyhr/Simula/Simula-2.0/simula.jar"
                        , "-output", simulaOutDir
//                        , "-noexec"
//                       , "-verbose"
                )
//                .withWorkDirectory(workDirectory) // Set working directory
                .withWorkDirectory(userDir) // Set working directory
                .withCharset(Charset.forName("UTF-8"));

//        String userDir = workDirectory + "/ssf";
//       commandLine.addParameters("-userDir", userDir);

        Map<String, String> optionMap = getOptionsMap(getEnvironment());
        if(optionMap != null) {
            DemoRunConfigurationOptions.setDefaults(optionMap);
            Util.TRACE("SimulaExecCommandLineState.getCommandLine: optionMap=" + optionMap);

//            if(optionMap.get("simula.compiler.verbose").equals("true")) commandLine.addParameters("-verbose");
//            if(optionMap.get("simula.compiler.caseSensitive").equals("true")) commandLine.addParameters("-caseSensitive");
//            if(optionMap.get("simula.compiler.noExecution").equals("true")) commandLine.addParameters("-noExecution");
//            if(optionMap.get("simula.compiler.warnings").equals("true")) commandLine.addParameters("-warnings");
//            if(optionMap.get("simula.compiler.noextension").equals("true")) commandLine.addParameters("-noextension");

            if (optionMap.get("simula.runtime.verbose").equals("true")) commandLine.addParameters("-verbose");
            if (optionMap.get("simula.runtime.noPopup").equals("true")) commandLine.addParameters("-noPopup");
            //            if (optionMap.get("simula.compiler.noExecution").equals("true")) commandLine.addParameters("-noExecution");
            //            if (optionMap.get("simula.compiler.warnings").equals("true")) commandLine.addParameters("-warnings");
            //            if (optionMap.get("simula.compiler.extensions").equals("true")) commandLine.addParameters("-extensions");
        }

        commandLine.addParameters(sourceFile);
        Util.TRACE("SimulaExecCommandLineState.getCommandLine: commandLine="+commandLine);
        return commandLine;
    }

    private Map<String, String> getOptionsMap(ExecutionEnvironment environment) {
        RunProfile runProfile = getEnvironment().getRunProfile();
        if(runProfile instanceof DemoRunConfiguration myRunConfiguration) {
            DemoRunConfigurationOptions options = myRunConfiguration.getState();
            Map<String, String> optionMap = options.getOptionsMap();
            DemoRunConfigurationOptions.setDefaults(optionMap);
            Util.TRACE("SimulaCompiler.runCommandFromPlugin: optionMap=" + optionMap);
            return optionMap;
        }
        return null;
    }

    private static Properties simulaProperties;
    public static void loadSimulaProperties() {
        simulaProperties = new Properties();
        String USER_HOME = System.getProperty("user.home");
        File simulaPropertiesDir = new File(USER_HOME, ".simula");
        File simulaPropertiesFile = new File(simulaPropertiesDir, "simulaProperties.xml");
        try {
            simulaProperties.loadFromXML(new FileInputStream(simulaPropertiesFile));
        } catch (IOException e) {
            Messages.showMessageDialog("Can't load Simula Properties",
                    "ERROR", Util.getSimulaIcon("sim.png"));
            throw new RuntimeException(e);
        }
    }

    private static String getCurrentFilePath(Project project) {
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        Editor editor = fileEditorManager.getSelectedTextEditor();
        if (editor != null) {
            VirtualFile currentFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
            if (currentFile != null) {
                if(hasUnsavedChanges(currentFile)) {
                    String msg = "The file: \n"+currentFile+"\nHas unsaved changes - do you want to save it ?";
                    int res = Dialogs.optionDialog(msg,"Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,"Yes","No");
                    Util.TRACE("SimulaCompiler.getCurrentFilePath: res="+res);
                    if(res == JOptionPane.YES_OPTION) updateVirtualFileContent(project, currentFile, editor.getDocument().getText());
                }
                return currentFile.getPath();
            }
        }
        return null;
    }

    private static boolean hasUnsavedChanges(VirtualFile file) {
        FileDocumentManager instance = FileDocumentManager.getInstance();
        Document document = instance.getDocument(file);
        if (document != null) {
            return instance.isDocumentUnsaved(document);
        }
        return false; // Or handle the case where the file has no associated document (e.g., image file)
    }

    private static void updateVirtualFileContent(Project project, VirtualFile file, String newContent) {
        // 1. Get the Document
        Document document = FileDocumentManager.getInstance().getDocument(file);

        if (document != null) {
            // 2. Ensure file is writable
            ReadonlyStatusHandler.getInstance(project).ensureFilesWritable(file);

            // 3. Perform write action
            WriteCommandAction.runWriteCommandAction(project, () -> {
                // All text strings must use only \n as line separators
                document.setText(newContent.replace("\r\n", "\n").replace("\r", "\n"));
            });
        }
    }

//    private static ConsoleView getConsoleViewFromToolWindow(@NotNull Project project, @NotNull String toolWindowId) {
//        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
//        ToolWindow toolWindow = toolWindowManager.getToolWindow(toolWindowId);
//        if (toolWindow == null) return null;
//        ContentManager contentManager = toolWindow.getContentManager();
//        Content[] contents = contentManager.getContents();
//        for (Content content : contents) {
//            Component component = content.getComponent();
//            // The ConsoleView itself is a Swing component. If you added the
//            // ConsoleView component directly to the Content, you can check its type.
//            if (component instanceof ConsoleView) {
//                return (ConsoleView) component;
//            }
//            // If the ConsoleView is nested inside another JPanel (e.g., in a complex UI),
//            // you might need a helper method to find it recursively within the component hierarchy.
//            else {
//                ConsoleView nestedView = findConsoleViewRecursively(component);
//                if (nestedView != null) {
//                    return nestedView;
//                }
//            }
//        }
//        return null;
//    }
//
//    // Helper method to find a ConsoleView within a container hierarchy
//    private static ConsoleView findConsoleViewRecursively(Component component) {
//        if (component instanceof ConsoleView) {
//            return (ConsoleView) component;
//        }
//        if (component instanceof Container) {
//            for (Component child : ((Container) component).getComponents()) {
//                ConsoleView found = findConsoleViewRecursively(child);
//                if (found != null) {
//                    return found;
//                }
//            }
//        }
//        return null;
//    }

}
