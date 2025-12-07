package simula.plugin.extensions.newProject;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.ide.wizard.NewProjectWizardStep;
import com.intellij.openapi.observable.properties.PropertyGraph;
import com.intellij.openapi.util.UserDataHolder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SimulaCustomProjectStep extends ModuleWizardStep implements NewProjectWizardStep {
    private JPanel myPanel;

    public void MyCustomProjectStep() {
// Initialize your custom step UI here
        myPanel = new JPanel();
        myPanel.add(new JLabel("This is my custom step"));
    }

    @Override
    public JComponent getComponent() {
        return myPanel;
    }

    @Override
    public void updateDataModel() {
// Perform any necessary actions when the user moves to the next step
    }

    @Override
    public boolean validate() {
// Perform any necessary validation for the step
        return true; // or return false if validation fails
    }

    @Override
    public @NotNull WizardContext getContext() {
        return null;
    }

    @Override
    public @NotNull PropertyGraph getPropertyGraph() {
        return null;
    }

    @Override
    public @NotNull Keywords getKeywords() {
        return null;
    }

    @Override
    public @NotNull UserDataHolder getData() {
        return null;
    }
}
