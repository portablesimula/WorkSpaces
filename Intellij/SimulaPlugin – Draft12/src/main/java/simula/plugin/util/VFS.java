package simula.plugin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import java.nio.file.Files;


public class VFS {

    ///  Internal TEST
    public static void test(Project project) {
        VirtualFile baseDir = Util.getBaseDir(project);
        System.out.println("VFStest: Project Root Directory baseDir: " + baseDir);
        if (baseDir != null) {
            System.out.println("VFStest: Project Root Directory VirtualFile: " + baseDir.getPath());
            // You can convert a VirtualFile to a java.io.File
            // java.io.File file = new java.io.File(baseDir.getPath());
            VirtualFile[] children = baseDir.getChildren();
            for (VirtualFile child : children) {
                System.out.println("VFStest: child=" + child.getPath());
            }
            createDirectory(baseDir, "src");
            createDirectory(baseDir, "bin");
            VirtualFile src = getChild(baseDir, "src");
            VirtualFile bin = getChild(baseDir, "bin");
//            if(src != null) {
//                createDirectory(baseDir, "newDirName");
//            }
        }
    }

    public static VirtualFile getSourceDir(Project project) {
        VirtualFile baseDir = Util.getBaseDir(project);
        if(project != null) {
            VirtualFile src = getChild(baseDir, "src");
            return src;
        }
        return null;
    }

//    public static VirtualFile zz_createDirectory(VirtualFile baseDir, String newDirName) {
//        // Assuming you have a reference to the parent VirtualFile
//        ApplicationManager.getApplication().runWriteAction(() -> {
//            try {
//                // 'this' is the requestor object (usually your plugin instance)
//                VirtualFile childDir = baseDir.createChildDirectory(this, "newFolderName");
//            } catch (IOException e) {
//                // Handle I/O errors
//            }
//        });
//    }

    public static VirtualFile createDirectory(VirtualFile baseDir, String newDirName) {
        VirtualFile prev = getChild(baseDir, newDirName);
        if(prev != null) return prev;

        final VirtualFile[] newDirectory = new VirtualFile[1];
        final boolean[] done = {false};
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                try {
                    newDirectory[0] = baseDir.createChildDirectory(new Object(), newDirName);
                    System.out.println("VFS.createDirectory: " + newDirectory[0].getPath());
                } catch (IOException e) {
                    // Handle exception
                    e.printStackTrace();
                }
                done[0] = true;
            }
        });
        while(! done[0]) { Thread.yield(); }
        return newDirectory[0];
    }

    public static VirtualFile getChild(VirtualFile parent, String childName) {
        VirtualFile[] children = parent.getChildren();
        for (VirtualFile child : children) {
           if (child.getName().equals(childName)) {
                System.out.println("VFStest: GOT IT: child=" + child.getPath());
                return child;
            }
        }
        return null;
    }

    public static void copyFile(@NotNull Project project, @NotNull VirtualFile vFile, @NotNull VirtualFile targetDir) {
        ApplicationManager.getApplication().invokeLater(() -> {
            // Copy file within a WriteAction
            WriteAction.run(() -> {
                try {
                    // Copy the file to the target directory
                    Util.TRACE("MyCustomFileDropHandler.copyFile: Copy " + vFile + " ===> " + targetDir);
                    VirtualFile copiedFile = VfsUtil.copyFile(null, vFile, targetDir);
                    Util.TRACE("MyCustomFileDropHandler.copyFile: copiedFile " + copiedFile);
                    Util.TRACE("MyCustomFileDropHandler.copyFile: copiedFile.parent " + copiedFile.getParent());

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
