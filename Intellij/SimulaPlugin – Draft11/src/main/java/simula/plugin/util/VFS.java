package simula.plugin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class VFS {

    ///  Internal TEST
    public static void test(Project project) {
        VirtualFile baseDir = project.getBaseDir();
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
        VirtualFile baseDir = project.getBaseDir();
        if(project != null) {
            VirtualFile src = getChild(baseDir, "src");
            return src;
        }
        return null;
    }

    public static VirtualFile createDirectory(VirtualFile baseDir, String newDirName) {
        System.out.println("VFS.createDirectory: " + newDirName);
        VirtualFile prev = getChild(baseDir, newDirName);
        if(prev != null) {
            System.out.println("VFS.createDirectory: ALREADY : prev=" + prev);
            return prev;
        }

        final VirtualFile[] newDirectory = new VirtualFile[1];
        final boolean[] done = {false};
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                try {
                    newDirectory[0] = baseDir.createChildDirectory(new Object(), newDirName);
                    // You can now use newDirectory
                    System.out.println("VFS.createDirectory: GOT IT: child=" + newDirectory[0].getPath());
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

}
