package simula.plugin.extensions.newProject;

import com.intellij.ide.wizard.NewProjectWizardStep;
import com.intellij.ide.wizard.RootNewProjectWizardStep;
import com.intellij.ide.wizard.language.LanguageGeneratorNewProjectWizard;
import simula.plugin.util.Util;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

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
        return new RootNewProjectWizardStep(parent.getContext());
//        return new NewProjectWizardBaseStep(parent);
 //        throw new RuntimeException("SimulaNewProjectWizard.createStep: "+newProjectWizardStep);
 //       return null;
    }

//    public @NotNull NewProjectWizardStep createStep2(@NotNull NewProjectWizardStep parent) {
//        NewProjectWizardStep root = new RootNewProjectWizardStep(parent.getContext());
//        root.
//                .nextStep()
//
//                .nextStep(::FirstProjectWizardStep)
//    .nextStep(::SecondProjectWizardStep)
//    .nextStep(::ThirdProjectWizardStep)
//    }


//    override fun createStep(parent: NewProjectWizardStep): NewProjectWizardStep
//    = NewPythonProjectStep(parent, createPythonModuleStructure = true)

}
