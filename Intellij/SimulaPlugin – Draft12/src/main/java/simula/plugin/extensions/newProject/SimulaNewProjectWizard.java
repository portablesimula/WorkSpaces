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
            System.out.println("+++         Simula Project Created          +++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("MyCustomWizardStep.setupProject: " + project.getName());
            VirtualFile baseDir = Util.getBaseDir(project);
            ApplicationManager.getApplication().runWriteAction(() -> {
                try {
                    VirtualFile srcDir = VfsUtil.createDirectoryIfMissing(baseDir, "src");
                    VirtualFile binDir = VfsUtil.createDirectoryIfMissing(baseDir, "bin");
                    if(myCheckbox.isSelected()) {
                        System.out.println("MyCustomWizardStep.setupProject: SELECTED !");
                        VirtualFile ssfDir = VfsUtil.createDirectoryIfMissing(baseDir, "ssf");
                        VirtualFile simDir = Util.getSimulaSamplesDir();
                        VfsUtil.copyDirectory(this, simDir, ssfDir, null);
                    }
                    VfsUtil.markDirtyAndRefresh(true, true, true, baseDir);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }

}
