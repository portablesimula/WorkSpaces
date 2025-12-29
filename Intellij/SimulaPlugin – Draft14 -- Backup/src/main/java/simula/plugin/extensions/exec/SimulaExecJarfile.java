package simula.plugin.extensions.exec;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import simula.plugin.extensions.config.SimulaSettings;
import simula.plugin.util.Dialogs;
import simula.plugin.util.Util;

import javax.swing.*;
import java.nio.charset.Charset;

public class SimulaExecJarfile {

    public static int call(ExecutionEnvironment environment, SimulaSettings options) {
        Util.TRACE("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        Util.TRACE("+++            SimulaExecJarfile.call               +++");
        Util.TRACE("+++          Call Resulting User's .jar             +++");
        Util.TRACE("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        Project project = environment.getProject();
        Util.printProject("SimulaExecJarfile.call: ", project);
        if (project == null) { Util.IERR("SimulaExecJarfile.call: project == null"); return -1; }

        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        Editor editor = fileEditorManager.getSelectedTextEditor();
        if (editor == null) { Util.IERR("SimulaExecJarfile.call: editor == null"); return -1; }
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        String workDirectory = currentFile.getParent().getPath();
        String name = currentFile.getNameWithoutExtension();
        String jarFile = workDirectory + "/bin/" + name + ".jar";

        // askRunJarFile
        String title = "AskRunJarFile: ";
        String msg = "Jar File: " + jarFile;
        msg +="\nWorkDirectory: " + workDirectory;
        msg +="\n\nDo you want to run JarFile now ?\n\n";
        int answer = Dialogs.optionDialog(msg,title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, "Execute", "Cancel");
        if(answer != JOptionPane.OK_OPTION) return 0;
        System.out.println("SimulaExecJarfile.call: DO RUN JARFILE");

        // TODO: DETTE MÅ RETTES FØR ENDELIG VERSJON
//        String javaExePath = "java";
        String javaExePath = "C:\\Program Files\\Java\\jdk-25\\bin\\java.exe";

        GeneralCommandLine commandLine = new GeneralCommandLine()
                .withExePath(javaExePath)
                .withParameters("-jar", jarFile)
                .withWorkDirectory(workDirectory) // Set working directory
                .withCharset(Charset.forName("UTF-8"));
        options.addRuntimeOptions(commandLine);
        commandLine.addParameters(jarFile);
        Util.TRACE("SimulaExecJarfile.call: commandLine="+commandLine);

        try {
            RunExternalProcess.exec(project, commandLine);
        } catch (ExecutionException e) {
            Messages.showErrorDialog(project,
                    "Could not start the process: " + e.getMessage(), "Error");
            return -1;
        }
        return 0;
    }

}
