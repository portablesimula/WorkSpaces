package com.example.myplugin.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MyWizardPage extends WizardPage {
    private Text fileNameText;
    private IStructuredSelection selection;

    protected MyWizardPage(IStructuredSelection selection) {
        super("MyWizardPage");
        setTitle("Custom Artifact");
        setDescription("Enter a name for your custom configuration file.");
        this.selection = selection;
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, false));

        new Label(container, SWT.NONE).setText("File Name:");
        fileNameText = new Text(container, SWT.BORDER);
        
        // Required: Inform the wizard structure of the page's root control
        setControl(container);
    }

    public boolean createNewResource() {
        String fileName = fileNameText.getText();
        if (fileName.isEmpty()) {
            setErrorMessage("File name cannot be empty");
            return false;
        }
        // TODO: Insert your actual file/resource generation logic here
        return true;
    }
}
