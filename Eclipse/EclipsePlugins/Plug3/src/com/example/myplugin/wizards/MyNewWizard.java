package com.example.myplugin.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class MyNewWizard extends Wizard implements INewWizard {
    
    private IWorkbench workbench;
    private IStructuredSelection selection;
    private MyWizardPage mainPage;

    public MyNewWizard() {
        super();
        setNeedsProgressMonitor(true);
        setWindowTitle("New Custom File Wizard");
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
    }

    @Override
    public void addPages() {
        // Instantiate and add your UI page(s)
        mainPage = new MyWizardPage(selection);
        addPage(mainPage);
    }

    @Override
    public boolean performFinish() {
        // Execute logic when the user clicks 'Finish'
        // (e.g., creating a file, running a background job)
        return mainPage.createNewResource(); 
    }
}
