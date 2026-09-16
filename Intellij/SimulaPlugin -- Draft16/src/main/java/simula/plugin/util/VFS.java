package simula.plugin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


public class VFS {

    public static VirtualFile getFolder(Project project, String name) {
        VirtualFile baseDir = Util.getBaseDir(project);
        AtomicReference<VirtualFile> newDir = new AtomicReference<>();
        ApplicationManager.getApplication().runWriteAction(() -> {
            try { newDir.set(VfsUtil.createDirectoryIfMissing(Util.getBaseDir(project), name));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
        return newDir.get();
    }


}
