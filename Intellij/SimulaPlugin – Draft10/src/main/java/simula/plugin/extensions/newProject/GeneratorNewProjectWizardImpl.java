package simula.plugin.extensions.newProject;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.ide.wizard.GeneratorNewProjectWizard;
import com.intellij.ide.wizard.NewProjectWizardStep;
import simula.plugin.util.Util;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GeneratorNewProjectWizardImpl implements GeneratorNewProjectWizard {
    @Override
    public @NotNull String getId() {
        return "SimulaX";
    }

    @Override
    public @NotNull String getName() {
        return "SimulaX";
    }

    @Override
    public @NotNull Icon getIcon() {
        return Util.getSimulaIcon();
    }

    @Override
    public @NotNull NewProjectWizardStep createStep(@NotNull WizardContext wizardContext) {
        throw new RuntimeException("GeneratorNewProjectWizardImpl.createStep: "+wizardContext);
//        return null;
    }
}
