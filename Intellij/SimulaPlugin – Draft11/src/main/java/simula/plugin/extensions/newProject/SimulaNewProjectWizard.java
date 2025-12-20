package simula.plugin.extensions.newProject;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.ide.wizard.NewProjectWizardStep;
import com.intellij.ide.wizard.RootNewProjectWizardStep;
import com.intellij.ide.wizard.language.LanguageGeneratorNewProjectWizard;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.module.Module;
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
//        return null;
    }

//    @Override
//    public @NotNull NewProjectWizardStep createStep(@NotNull NewProjectWizardStep parent) {
//        // The parent step is provided by the framework, handling basic project settings
//        // You can add your own custom steps in a sequence after the parent.
//
//        // Example: Adding a custom configuration step
//        MyCustomProjectStep customStep = new MyCustomProjectStep(parent);
//
//        // Chain the steps using nextStep
//        return parent.nextStep(context -> customStep);
//
//        // For more complex sequences, refer to the JetBrains SDK documentation
//    }

}
