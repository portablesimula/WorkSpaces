package com.simula.client.newWizard;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.simula.client.DEF;

public class NewSimulaFileWizard extends Wizard implements INewWizard {
    private SimulaFileWizardPage page;
    
    @SuppressWarnings("unused")
	private IStructuredSelection selection;
    @SuppressWarnings("unused")
	private IWorkbench workbench;

    public NewSimulaFileWizard() {
        super();
        setNeedsProgressMonitor(true);
        setWindowTitle("New Simula File");
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
        page = new SimulaFileWizardPage();
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

        IPath path = new Path(outputFileName);
        String extension = path.getFileExtension();
        if(extension != "sim") {
        	outputFileName = outputFileName + ".sim";
//        	page.setMessage("File extension .sim was added to file: " + outputFileName);
        	IStatus warningStatus = new Status(
        		    IStatus.WARNING, DEF.SIMULA_PLUGIN_ID, "File extension .sim was added to file: " + outputFileName);
        	ILog.get().log(warningStatus);
        	try {
        	    IWorkbenchPage page = PlatformUI.getWorkbench()
        	                                    .getActiveWorkbenchWindow()
        	                                    .getActivePage();
        	    if (page != null) {
        	        // Open the view or bring it to the front if already open
        	        page.showView("org.eclipse.pde.runtime.LogView");
        	    }
        	} catch (PartInitException e) {
        	    // Handle exception if the view fails to open
        	    e.printStackTrace();
        	}
        }

        
        IO.println("NewSimulaFileWizard.performFinish: Creating item: " + outputFileName);
        // Implement resource manipulation, file system generation, or file generation logic here
        
        return true;
    }
}
