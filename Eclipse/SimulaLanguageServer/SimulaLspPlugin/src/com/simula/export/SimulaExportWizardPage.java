package com.simula.export;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class SimulaExportWizardPage extends WizardPage {
    private Combo projectCombo;
    private Text textDestination;

    protected SimulaExportWizardPage() {
        super("SimulaExportWizardPage");
        setTitle("Simula Runnable JAR Export");
        setDescription("Select the Simula project and target destination path.");
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(3, false));

        // 1. Project Selection
        new Label(container, SWT.NONE).setText("Select Project:");
        projectCombo = new Combo(container, SWT.READ_ONLY);
        projectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
            if (project.isOpen()) {
                projectCombo.add(project.getName());
            }
        }
        if (projectCombo.getItemCount() > 0) projectCombo.select(0);

        // 2. Destination Selection
        new Label(container, SWT.NONE).setText("Destination JAR:");
        textDestination = new Text(container, SWT.BORDER);
        textDestination.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        Button browseButton = new Button(container, SWT.PUSH);
        browseButton.setText("Browse...");
        browseButton.addListener(SWT.Selection, e -> {
            FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
            dialog.setFilterExtensions(new String[] { "*.jar" });
            String path = dialog.open();
            if (path != null) {
                textDestination.setText(path);
            }
        });

        textDestination.setText("DETTE MÅ SKRIVES FERDIG SEINERE: ");

        setControl(container);
    }

    public String getSelectedProjectName() {
        return projectCombo.getText();
    }

    public String getDestinationPath() {
        return textDestination.getText();
    }
}
