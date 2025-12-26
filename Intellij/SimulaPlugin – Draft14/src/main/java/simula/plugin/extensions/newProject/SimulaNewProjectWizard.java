package simula.plugin.extensions.newProject;

import com.intellij.ide.wizard.AbstractNewProjectWizardStep;
import com.intellij.ide.wizard.NewProjectWizardStep;
import com.intellij.ide.wizard.language.LanguageGeneratorNewProjectWizard;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.dsl.builder.Panel;
import org.jetbrains.annotations.Nullable;
import simula.plugin.extensions.start_close.SimulaStartupActivity;
import simula.plugin.util.Util;
import org.jetbrains.annotations.NotNull;
import simula.plugin.util.VFS;

import javax.swing.*;
import java.io.IOException;

public class SimulaNewProjectWizard implements LanguageGeneratorNewProjectWizard {
    @Override
    public @NotNull String getName() {
        return "Simula";
    }

    @Override
    public @NotNull Icon getIcon() {
        return Util.getSimulaIcon();
    }

    /// See: https://plugins.jetbrains.com/docs/intellij/new-project-wizard.html#wizard-steps
    @Override
    public @NotNull NewProjectWizardStep createStep(@NotNull NewProjectWizardStep parent) {
    //    return new RootNewProjectWizardStep(parent.getContext());
    return new SimulaProjectWizardStep(parent);
    }

    class SimulaProjectWizardStep extends AbstractNewProjectWizardStep {
        private final JCheckBox myCheckbox = new JCheckBox("Add Simula samples", true);

        public SimulaProjectWizardStep(@NotNull NewProjectWizardStep parent) {
            super(parent);
        }

        @Override
        public void setupUI(Panel builder) {
            // 2. Add the checkbox to the builder panel
            builder.row("", r -> {
                r.cell(myCheckbox);
                return null;
            });
        }

        @Override
        public void setupProject(@NotNull Project project) {
            // Handle logic once the 'Create' button is pressed
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++       Simula new Project Wizard         +++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("SimulaProjectWizardStep.setupProject: " + project.getName());
            SimulaStartupActivity.unregisterIdeActions();
            VirtualFile baseDir = Util.getBaseDir(project);

//            ApplicationManager.getApplication().runWriteAction(() -> {
//                 try { VirtualFile srcDir = VfsUtil.createDirectoryIfMissing(baseDir, "src");
//                    System.out.println("SimulaProjectWizardStep.setupProject: srcDir: " + srcDir.getPath());
//               } catch (IOException e) { throw new RuntimeException(e); }
//            });
//
//            ApplicationManager.getApplication().runWriteAction(() -> {
//                try { VirtualFile binDir = VfsUtil.createDirectoryIfMissing(baseDir, "bin");
//                    System.out.println("SimulaProjectWizardStep.setupProject: binDir: " + binDir.getPath());
//                } catch (IOException e) { throw new RuntimeException(e); }
//            });

            if(myCheckbox.isSelected()) {
                ApplicationManager.getApplication().runWriteAction(() -> {
                    try {
                        VirtualFile ssfDir = VfsUtil.createDirectoryIfMissing(baseDir, "ssf");
                        System.out.println("SimulaProjectWizardStep.setupProject: ssfDir: " + ssfDir.getPath());
                        VirtualFile simDir = Util.getSimulaSamplesDir();
                        VfsUtil.copyDirectory(this, simDir, ssfDir, null);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

//            VfsUtil.markDirtyAndRefresh(true, true, true, baseDir);

//            ApplicationManager.getApplication().runWriteAction(() -> {
//                try {
//                    VirtualFile srcDir = VfsUtil.createDirectoryIfMissing(baseDir, "src");
//                    System.out.println("SimulaProjectWizardStep.setupProject: srcDir: " + srcDir.getPath());
//                    VirtualFile binDir = VfsUtil.createDirectoryIfMissing(baseDir, "bin");
//                    System.out.println("SimulaProjectWizardStep.setupProject: binDir: " + binDir.getPath());
//                    if(myCheckbox.isSelected()) {
//                        System.out.println("SimulaProjectWizardStep.setupProject: SELECTED !");
//                        VirtualFile ssfDir = VfsUtil.createDirectoryIfMissing(baseDir, "ssf");
//                        System.out.println("SimulaProjectWizardStep.setupProject: ssfDir: " + ssfDir.getPath());
//                        VirtualFile simDir = Util.getSimulaSamplesDir();
//                        VfsUtil.copyDirectory(this, simDir, ssfDir, null);
//                    }
//                    VfsUtil.markDirtyAndRefresh(true, true, true, baseDir);
//
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });

        }
    }

}
