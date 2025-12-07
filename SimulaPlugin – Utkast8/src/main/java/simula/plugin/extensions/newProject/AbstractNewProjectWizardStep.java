package simula.plugin.extensions.newProject;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.ide.wizard.NewProjectWizardStep;
import com.intellij.openapi.observable.properties.PropertyGraph;
import com.intellij.openapi.util.UserDataHolder;
import org.jetbrains.annotations.NotNull;

///  See: https://plugins.jetbrains.com/docs/intellij/new-project-wizard.html?from=DevkitUnregisteredNamedColorInspection#chaining-steps
public class AbstractNewProjectWizardStep implements NewProjectWizardStep {
    NewProjectWizardStep parentStep;

    public AbstractNewProjectWizardStep(NewProjectWizardStep parentStep) {
        this.parentStep=parentStep;
    }

    @Override
    public @NotNull WizardContext getContext() {
        return parentStep.getContext();
    }

    @Override
    public @NotNull PropertyGraph getPropertyGraph() {
        return parentStep.getPropertyGraph();
    }

    @Override
    public @NotNull Keywords getKeywords() {
        return parentStep.getKeywords();
    }

    @Override
    public @NotNull UserDataHolder getData() {
        return parentStep.getData();
    }
}
