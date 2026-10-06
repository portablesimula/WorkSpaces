package com.simula.client.newWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewSimulaFileWizardPage extends WizardPage {
    private Text fileText;

    public NewSimulaFileWizardPage() {
        super("wizardPage");
        setTitle("Custom File Configuration");
        setDescription("Enter a name for your custom artifact.");
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        new Label(container, SWT.NONE).setText("File Name:");
        fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
        
        // Essential: Assign the composite container back to the page
        setControl(container);
    }

    public String getFileName() {
        return fileText.getText();
    }
}
