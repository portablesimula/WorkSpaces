package com.simula.client.newWizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class SimulaProjectCreationPage extends WizardNewProjectCreationPage {

    private Button includeSamplesCheckbox;
    private boolean isIncludeSamplesSelected = true;

    public SimulaProjectCreationPage(String pageName) {
        super(pageName);
        setTitle("Simula Project");
        setDescription("Create a new Simula Project.");
    }

    @Override
    public void createControl(Composite parent) {
        // 1. Create the standard Eclipse project creation controls (Name, Location)
        super.createControl(parent);
        
        // 2. Get the top-level composite created by the superclass
        Composite composite = (Composite) getControl();

        // 3. Create your custom checkbox button
        includeSamplesCheckbox = new Button(composite, SWT.CHECK);
        includeSamplesCheckbox.setText("Add Simula Samples");
        includeSamplesCheckbox.setSelection(true);
        
        // 4. Configure layout data so it aligns nicely with standard components
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3; // Standard page uses a 3-column layout
        includeSamplesCheckbox.setLayoutData(gd);

        // 5. Track the state selection when clicked
        includeSamplesCheckbox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                isIncludeSamplesSelected = includeSamplesCheckbox.getSelection();
            }
        });
    }

    /**
     * Helper method to expose the checkbox state to your Wizard class.
     */
    public boolean isIncludeSamplesSelected() {
        return isIncludeSamplesSelected;
    }
}
