package simula.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import simula.plugin.util.Util;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreateNewFileAction extends AnAction {
    public CreateNewFileAction() {
        super("Simula File", "Creates a new Simula file", Util.getSimulaIcon());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Util.TRACE("CreateNewFileAction.actionPerformed: anActionEvent: " + anActionEvent.getClass().getSimpleName());
        VirtualFile directory = null;
        VirtualFile file = anActionEvent.getData(CommonDataKeys.VIRTUAL_FILE);

        if (file != null) {
            // If the selection is already a directory, use it directly
            // Otherwise, get the parent directory of the selected file
            directory = file.isDirectory() ? file : file.getParent();
            Util.TRACE("CreateNewFileAction.actionPerformed: directory: " + directory);

            if (directory != null) {
                String path = directory.getPath();
                // Perform action with the directory path
                Util.TRACE("CreateNewFileAction.actionPerformed: 2'directory: " + directory);
           }
        }
        if(directory == null) return;

        Project project = anActionEvent.getProject();
        Object answ = JOptionPane.showInputDialog(null,"Enter filename:","New Simula File",
                JOptionPane.QUESTION_MESSAGE, Util.getSimulaIcon("sim.png"),null, "Unnamed.sim");
        String newFileName = (answ == null)? null : answ.toString();

        Util.TRACE("CreateNewFileAction.actionPerformed: " + newFileName);
        if(newFileName == null) return;

        String content = "begin\n   outtext(\"Hello World\");\nend;\n";
        boolean didWrite =  tryWrite(project, directory, newFileName, content, false);
        if (! didWrite) {
            String msg = "File already exists: " + newFileName + "\n\nDo you want to overwrite it ?\n\n";
            int answer = Util.optionDialog(msg, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, "Yes", "No");
            if (answer == JOptionPane.OK_OPTION) tryWrite(project, directory, newFileName, content, true);
        }
    }

    private boolean tryWrite(@NotNull Project project, @NotNull VirtualFile sourceDir, @NotNull String newFileName, @NotNull String content, boolean doOverwrie) {
        Util.TRACE("CreateNewFileAction.tryWrite: " + newFileName + ", doOverwrite: " + doOverwrie);
        AtomicBoolean doWrite = new AtomicBoolean(true);

        WriteAction.run(() -> {
            try {
//                VirtualFile sourceDir = VfsUtil.createDirectoryIfMissing(Util.getBaseDir(project), "src");
                if(! doOverwrie) {
                    Util.TRACE("CreateNewFileAction.tryWrite: Check for overwrite: " + newFileName + ", doOverwrite: " + doOverwrie);
                    // Check for overwrite
                    VirtualFile oldFile = sourceDir.findChild(newFileName);
                    Util.TRACE("CreateNewFileAction.tryWrite: Check for overwrite: " + newFileName + ", oldFile: " + oldFile);
                    if (oldFile != null) {
                        doWrite.set(false);
                    }
                }
                if(doWrite.get()) {
                    Util.TRACE("CreateNewFileAction.tryWrite: doWrite: " + newFileName + ", content: " + content);
                    VirtualFile newFile = sourceDir.createChildData(this, newFileName);
                    newFile.setBinaryContent(content.getBytes(StandardCharsets.UTF_8));

                    // Hand over to the editor
                    ApplicationManager.getApplication().invokeLater(() -> {
                        FileEditorManager.getInstance(project).openFile(newFile, true);
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return doWrite.get();
    }

}
