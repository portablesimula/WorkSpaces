package simula.plugin.extensions.runConfiguration.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import simula.plugin.extensions.runConfiguration.DemoRunConfigurationOptions;
import simula.plugin.util.Global;
import simula.plugin.util.Util;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

public class SimulaCompiler {
    private static Project project;
    private static Map<String, String> options;
    private static String sourceFile;

    private static String userHomeDir;
    private static String workDirectory;
    private static Properties simulaProperties;
    private static String simulaHomeDir;
    private static String simulaOutDir;

    public static int call(ExecutionEnvironment environment, Map<String, String> optionMap) {
        project = environment.getProject();
        options = optionMap;
        Util.printProject("SimulaCompiler.call: ", project);

        userHomeDir = System.getProperty("user.home");

        loadSimulaProperties();
        simulaHomeDir = simulaProperties.getProperty("simula.home");
//        Util.TRACE("simulaHomeDir: "+simulaHomeDir);
        workDirectory =  project.getBasePath();
        simulaOutDir = workDirectory + "/bin";
//        Util.TRACE("simulaOutDir: "+simulaOutDir);

        sourceFile = getCurrentFilePath(project);
        Global.currentSourceFile = sourceFile;
        if(sourceFile == null) {
            Util.TRACE("SimulaCompiler.call: ERROR: No Source file available");
            return -1;
        } else {
//            Util.TRACE("SimulaCompiler.call: sourceFile=" + sourceFile);
            askRunSimula();
//
//            RunManager runManager = RunManager.getInstance(project);
//            List<RunnerAndConfigurationSettings> allConfigurations = runManager.getAllSettings();
//            // Alternatively, you can use getConfigurationsList()
//            // List<RunConfiguration> configurationsList = runManager.getConfigurationsList();
        }
        return 0;
    }


    public static int call(@NotNull Project prj, @NotNull DemoRunConfigurationOptions optn) {
        project = prj;
//        options = optn;
        if(options == null) Util.IERR("SimulaCompiler.call: ERROR: No Options available");
        System.out.println("SimulaCompiler.call: Project=" + project);
        Util.printProject("SimulaCompiler.call: ", project);
        userHomeDir = System.getProperty("user.home");
        loadSimulaProperties();
        simulaHomeDir = simulaProperties.getProperty("simula.home");
        workDirectory =  project.getBasePath();
        simulaOutDir = workDirectory + "/bin";
        sourceFile = getCurrentFilePath(project);
        Global.currentSourceFile = sourceFile;
        if(sourceFile == null) {
            Util.IERR("SimulaCompiler.call: No Source file available");
            return -1;
        }
//       Util.TRACE("SimulaCompiler.call: sourceFile=" + sourceFile);
        askRunSimula();

        return 0;
    }

    // ***************************************************************
    // *** askRunSimula
    // ***************************************************************
    private static void askRunSimula() {
        String title = "TITLE:askRunSimula: ";
        String msg = "Source File: " + sourceFile;
        msg +="\nUser dir: " + userHomeDir;
        msg +="\nWorkDirectory: " + workDirectory;
        msg +="\n\nDo you want to start Simula Compiling now ?\n\n";
        int answer = Util.optionDialog(msg,title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, "Start Simula", "Cancel");
        if(answer == JOptionPane.OK_OPTION) {
            System.out.println("SimulaCompiler.askRunSimula: DO RUN SIMULA");
            runCommandFromPlugin(project);
        }
    }

    private static void runCommandFromPlugin(Project project) {
        if (project == null) {
            Util.IERR("SimulaCompiler.runCommandFromPlugin: project == null");
            return;
        }

        // TODO: DETTE MÅ RETTES FØR ENDELIG VERSJON
//        String javaExePath = "java";
        String javaExePath = "C:\\Program Files\\Java\\jdk-25\\bin\\java.exe";

        GeneralCommandLine commandLine = new GeneralCommandLine()
            .withExePath(javaExePath)
            .withParameters("-jar"
                , "C:/Users/omyhr/Simula/Simula-2.0/simula.jar"
                , "-output", simulaOutDir
                , "-noexec"
                , "-verbose"
             )
            .withWorkDirectory(workDirectory) // Set working directory
            .withCharset(Charset.forName("UTF-8"));

        if(options == null) Util.IERR("SimulaCompiler.runCommandFromPlugin: ERROR: No Options available");
//        Map<String, String> options = options.getOptionsMap();
        DemoRunConfigurationOptions.setDefaults(options);
       Util.TRACE("SimulaCompiler.runCommandFromPlugin: options=" + options);
        if(options.get("simula.compiler.verbose").equals("true")) commandLine.addParameters("-verbose");
        if(options.get("simula.compiler.caseSensitive").equals("true")) commandLine.addParameters("-caseSensitive");
        if(options.get("simula.compiler.noExecution").equals("true")) commandLine.addParameters("-noExecution");
        if(options.get("simula.compiler.warnings").equals("true")) commandLine.addParameters("-warnings");
        if(options.get("simula.compiler.noextension").equals("true")) commandLine.addParameters("-noextension");

        commandLine.addParameters(sourceFile);


        Util.TRACE("SimulaCompiler.runCommandFromPlugin: commandLine="+commandLine);

        try {
            RunExternalProcess.exec(project, commandLine);
        } catch (ExecutionException e) {
            Messages.showErrorDialog(project,
                    "Could not start the process: " + e.getMessage(), "Error");
            throw new RuntimeException(e);
        }
    }

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
            if (currentFile != null) return currentFile.getPath();
        }
        return null;
    }

}
