package simula.plugin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FileDropEvent;
import com.intellij.openapi.editor.FileDropHandler;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class MyFileDropHandler implements FileDropHandler {

    /// @return true, if the event is handled and must not be propagated further to the rest of handlers
    @Override
    public @Nullable Object handleDrop(@NotNull FileDropEvent fileDropEvent, @NotNull Continuation<? super Boolean> continuation) {
//        Editor editor = fileDropEvent.getEditor();
//        if (editor == null) return false;

        Collection<File> files = fileDropEvent.getFiles();
        Project project = fileDropEvent.getProject();
        try {
//        VirtualFile sourceDir = VFS.getSourceDir(project);
//            VirtualFile sourceDir = VfsUtil.createDirectoryIfMissing(Util.getBaseDir(project), "src");
//            if (sourceDir == null) {
//                Util.ALERT("Can't find source directory");
//                return false;
//            }

            VirtualFile sourceDir = VFS.getFolder(project, "src");

            LOOP: for (File file : files) {
                System.out.println("MyCustomFileDropHandler.handleDrop: File=" + file);
                VirtualFile vFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
                if (vFile != null && sourceDir != null) {
                    // Check for overwrite
                    VirtualFile oldFile = sourceDir.findChild(vFile.getName());
                    if (oldFile != null) {
                        String msg = "File already exists: " + oldFile.getName() + "\n\nDo you want to overwrite it ?\n\n";
                        int answer = Dialogs.optionDialog(msg,"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, "Yes", "No");
                        if(answer != JOptionPane.OK_OPTION) continue LOOP;
                    }
                    copyFile(project, vFile, sourceDir);
                }
            }
            return true; // Successfully handled
        } catch (Exception e) {
            Util.TRACE("MyCustomFileDropHandler.handleDrop: FAILED: " + e);
            return false;
        }
    }

    private static void copyFile(@NotNull Project project, @NotNull VirtualFile vFile, @NotNull VirtualFile targetDir) {
        ApplicationManager.getApplication().invokeLater(() -> {
            WriteAction.run(() -> {
                try {
                    Util.TRACE("MyCustomFileDropHandler.copyFile: Copy " + vFile + " ===> " + targetDir);
                    VirtualFile copiedFile = VfsUtil.copyFile(null, vFile, targetDir);
                    Util.TRACE("MyCustomFileDropHandler.copyFile: copiedFile " + copiedFile);
                    VfsUtil.markDirtyAndRefresh(true, true, true, project.getProjectFile());

                    // Hand over to the editor
                    ApplicationManager.getApplication().invokeLater(() -> {
                        FileEditorManager.getInstance(project).openFile(copiedFile, true);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

}