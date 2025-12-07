package simula.plugin.extensions.runConfiguration.run.simulaCompiler;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import simula.plugin.extensions.runConfiguration.SimOption;
import simula.plugin.util.Global;
import simula.plugin.util.Util;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class SimulaCompiler {
    private static Project project;
    private static SimOption ctOption;
    private static String sourceFile;

    private static String userHomeDir;
    private static String workDirectory;
    private static Properties simulaProperties;
    private static String simulaHomeDir;
    private static String simulaOutDir;

    public static int call(Project prj, SimOption optn) {
        project = prj;
        ctOption = optn;
        System.out.println("SimulaCompiler.call: Project=" + project);
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
            Util.TRACE("SimulaCompiler.call: sourceFile=" + sourceFile);
            askRunSimula();
//
//            RunManager runManager = RunManager.getInstance(project);
//            List<RunnerAndConfigurationSettings> allConfigurations = runManager.getAllSettings();
//            // Alternatively, you can use getConfigurationsList()
//            // List<RunConfiguration> configurationsList = runManager.getConfigurationsList();
        }
        return 0;
    }

    // ***************************************************************
    // *** askRunSimula
    // ***************************************************************
    public static void askRunSimula() {
        String title = "TITLE";
        String msg = "Source File: " + sourceFile;
        msg +="\nUser dir: " + userHomeDir;
        msg +="\nWorkDirectory: " + workDirectory;
        msg +="\n\nDo you want to start Simula Compiling now ?\n\n";
        int answer = Util.optionDialog(msg,title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, "Start Simula", "Exit");

//        if(DEBUG)
//            IO.println("SimulaExtractor.extract: answer="+answer); // TODO: MYH
        if(answer==0) {
            System.out.println("SimulaCompiler.askRunSimula: DO RUN SIMULA");
            runCommandFromPlugin(project);
        }
    }

    public static void runCommandFromPlugin(Project project) {

//        Util.TRACE("SimulaCompiler.runCommandFromPlugin: "+project);
        if (project == null) return;
        GeneralCommandLine commandLine = new GeneralCommandLine();

        // TODO: DETTE MÅ RETTES FØR ENDELIG VERSJON
//        String javaExePath = "java";
        String javaExePath = "C:\\Program Files\\Java\\jdk-25\\bin\\java.exe";

        Util.TRACE("SimulaCompiler.runCommandFromPlugin: " + project);
        Util.TRACE("SimulaCompiler.runCommandFromPlugin: simulaOutDir=" + simulaOutDir);
        Util.TRACE("SimulaCompiler.runCommandFromPlugin: sourceFile=" + sourceFile);

        commandLine.setExePath(javaExePath);
//        commandLine.addParameters("-version");
        commandLine.addParameters("-jar"
                , "C:/Users/omyhr/Simula/Simula-2.0/simula.jar"
                , "-output", simulaOutDir
                , "-noexec"
                , "-verbose"
                , sourceFile
        );

        commandLine.setWorkDirectory(project.getBasePath());

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
