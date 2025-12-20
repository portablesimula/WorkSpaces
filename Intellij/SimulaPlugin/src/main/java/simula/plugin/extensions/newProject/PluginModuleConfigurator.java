package simula.plugin.extensions.newProject;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.SourceFolder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.concurrency.annotations.RequiresWriteLock;
import org.jetbrains.annotations.NotNull;

public class PluginModuleConfigurator {

    @RequiresWriteLock
    public static void addSourceDirectory(@NotNull Module module, @NotNull VirtualFile srcDir) {
        // All modifications to project structure must be run inside a write action
        ApplicationManager.getApplication().runWriteAction(() -> {
            final ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
            final ModifiableRootModel modifiableModel = rootManager.getModifiableModel();

            if(true) throw new RuntimeException("HER MÅ DET RETTES !!!");
            try {
                // Find or add the content entry for the directory
                ContentEntry contentEntry = null;// findContentEntryFor(srcDir, modifiableModel); // RETT DETTE
                if (contentEntry == null) {
                    contentEntry = modifiableModel.addContentEntry(srcDir.getUrl());
                }

                // Add the directory as a source folder
                // The second argument 'false' indicates it is not a test source folder
                SourceFolder sourceFolder = contentEntry.addSourceFolder(srcDir.getUrl(), false);

                // You can also set a package prefix if needed
                // sourceFolder.setPackagePrefix("com.example.mypackage");

                // Commit the changes to the module model
                modifiableModel.commit();
            } catch (Exception e) {
                // In case of an error, dispose the model to avoid data corruption
                modifiableModel.dispose();
                // Handle the exception appropriately
                e.printStackTrace();
            }
        });
    }
}

