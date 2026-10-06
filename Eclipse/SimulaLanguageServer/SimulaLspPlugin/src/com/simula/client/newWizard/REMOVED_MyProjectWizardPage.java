package com.simula.client.newWizard;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.swt.widgets.Composite;

public class REMOVED_MyProjectWizardPage extends WizardNewProjectCreationPage {
    
    public REMOVED_MyProjectWizardPage(String pageName) {
        super(pageName);
        setTitle("Custom Project Details");
        setDescription("Specify the names and location settings for the new project.");
    }

    @Override
    public void createControl(Composite parent) {
        // Inherits standard project name and location controls from parent
        super.createControl(parent);
        
        // Optional: Composite control = (Composite) getControl();
        // UI extensions (like adding custom checkboxes or text fields) go here
    }
}
