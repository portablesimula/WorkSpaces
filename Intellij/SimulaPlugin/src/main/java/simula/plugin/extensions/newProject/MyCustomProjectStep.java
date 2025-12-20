package simula.plugin.extensions.newProject;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.ide.wizard.NewProjectWizardStep;
import com.intellij.openapi.observable.properties.PropertyGraph;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.UserDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MyCustomProjectStep implements NewProjectWizardStep {
    private final NewProjectWizardStep parent;
    private JPanel myPanel;
    // Add your UI components here

    public MyCustomProjectStep(NewProjectWizardStep parent) {
        this.parent = parent;
        myPanel = new JPanel();
        // Initialize custom UI components
        myPanel.add(new JLabel("This is my custom step"));
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

    // You can override other methods like _init_ (for setup) or
    // setupProject (for final project setup actions) as needed.
}