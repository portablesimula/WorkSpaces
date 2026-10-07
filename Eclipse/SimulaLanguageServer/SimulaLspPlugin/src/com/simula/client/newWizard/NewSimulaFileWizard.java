package com.simula.client.newWizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewSimulaFileWizard extends Wizard implements INewWizard {
    private NewSimulaFileWizardPage page;
    
    @SuppressWarnings("unused")
	private IStructuredSelection selection;
    @SuppressWarnings("unused")
	private IWorkbench workbench;

    public NewSimulaFileWizard() {
        super();
        setNeedsProgressMonitor(true);
        setWindowTitle("New Custom Artifact Wizard");
    }

    // Required by INewWizard - captures current environment state
    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
    }

    // Add wizard steps/pages sequentially
    @Override
    public void addPages() {
        page = new NewSimulaFileWizardPage();
        addPage(page); // Registers page via JFace workflow
    }

    // Executes final processing when user hits 'Finish'
    @Override
    public boolean performFinish() {
        String outputFileName = page.getFileName();
        
        if (outputFileName.isEmpty()) {
            page.setErrorMessage("File name cannot be empty.");
            return false;
        }

        System.out.println("Creating item: " + outputFileName);
        // Implement resource manipulation, file system generation, or file generation logic here
        
        return true;
    }
}
