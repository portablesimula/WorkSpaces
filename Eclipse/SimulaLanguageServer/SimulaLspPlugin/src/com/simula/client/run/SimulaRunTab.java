package com.simula.client.run;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class SimulaRunTab extends AbstractLaunchConfigurationTab {

    private Text fileText;
    private Button browseButton;
    
    // Unique keys to store the configuration settings
    public static final String ATTR_SIMULA_PROGRAM = "MY_SIMULA_PROGRAM_PATH";

    /**
     * 1. Create the UI controls for your tab.
     */
    @Override
    public void createControl(Composite parent) {
    	IO.println("SimulaRunTab.createControl: ");
        Composite comp = new Composite(parent, SWT.NONE);
        setControl(comp);
        comp.setLayout(new GridLayout(1, false));
        
        // Group for File Selection
        Group group = new Group(comp, SWT.NONE);
        group.setText("Simula Program");
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setLayout(new GridLayout(2, false));
        
        // Text field for file path
        fileText = new Text(group, SWT.BORDER);
        fileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Automatically dirty the tab when text changes so the "Apply" button enables
        fileText.addModifyListener(e -> updateLaunchConfigurationDialog());
        
        // Browse button
        browseButton = new Button(group, SWT.PUSH);
        browseButton.setText("Browse...");
        browseButton.addListener(SWT.Selection, event -> handleBrowse());
    }

    private void handleBrowse() {
    	IO.println("SimulaRunTab.handleBrowse: ");
        // Implement workspace file dialog or file system dialog here
        // String path = ...
        // fileText.setText(path);
        fileText.setText("BrowseButtonPressed ?  DETTE MÅ RETTES SEINERE");
    }

    /**
     * 2. Initialize the UI fields from an existing launch configuration.
     */
//    @Override
//    public void initializeFrom(ILaunchConfiguration configuration) {
//    	IO.println("SimulaRunTab.initializeFrom: ");
//        try {
//            // Read the saved attribute or default to empty string
//            String program = configuration.getAttribute(ATTR_SIMULA_PROGRAM, "");
//            fileText.setText(program);
//        } catch (CoreException e) {
//            fileText.setText("");
//        }
//    }
    @Override
    public void initializeFrom(ILaunchConfiguration configuration) {
        try {
            // Read the file path attribute stored by the shortcut
            String filePath = configuration.getAttribute("SIMULA_PROGRAM_FILE", "");
            
            // Update your UI text field
            fileText.setText(filePath);
            
            // Optional: Resolve the exact IFile object from the workspace if needed
            if (!filePath.isEmpty()) {
                IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filePath));
                if (file.exists()) {
                    // Do any additional processing with the IFile instance here
                }
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * 3. Save the UI values back into the launch configuration copy.
     */
    @Override
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
    	IO.println("SimulaRunTab.performApply: ");
        // Save the current text in the text field to the configuration copy
        configuration.setAttribute(ATTR_SIMULA_PROGRAM, fileText.getText().trim());
    }

    /**
     * 4. Set defaults when a user creates a NEW launch configuration.
     */
    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
    	IO.println("SimulaRunTab.setDefaults: ");
        configuration.setAttribute(ATTR_SIMULA_PROGRAM, "");
    }

    /**
     * 5. The name displayed on the Tab itself.
     */
    @Override
    public String getName() {
    	IO.println("SimulaRunTab.getName: Main");
        return "Main";
    }

}
