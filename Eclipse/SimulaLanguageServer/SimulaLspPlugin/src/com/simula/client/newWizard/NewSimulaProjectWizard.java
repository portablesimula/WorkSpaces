package com.simula.client.newWizard;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class NewSimulaProjectWizard extends Wizard implements INewWizard {

//    private MyProjectWizardPage page;
    private SimulaProjectCreationPage page;
    private IWorkbench workbench;

    public NewSimulaProjectWizard() {
        super();
        setNeedsProgressMonitor(true);
        setWindowTitle("New Custom Project Wizard");
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
    }

    @Override
    public void addPages() {
        // Instantiate and anchor the UI creation page
//        page = new MyProjectWizardPage("CustomProjectPage");
        page = new SimulaProjectCreationPage("CustomProjectPage");
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        // Retrieve values securely entered on the UI layout
        final String projectName = page.getProjectName();
        final org.eclipse.core.runtime.IPath location = page.getLocationPath();

        // Safely wrap workspace modifications into a background progress thread
        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
            @Override
            protected void execute(IProgressMonitor monitor) throws CoreException, 
                    InvocationTargetException, InterruptedException {
                createProject(projectName, location, monitor);
            }
        };

        try {
            getContainer().run(true, booleanValue(), op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            realException.printStackTrace();
            return false;
        }

        return true;
    }

    private void createProject(String name, org.eclipse.core.runtime.IPath location, IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Creating " + name, 2);
        
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
        IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(project.getName());
        
        if (!Platform.getLocation().equals(location)) {
            description.setLocation(location);
        }

        // Establish the core resource baseline
        if (!project.exists()) {
            project.create(description, monitor);
        }
        
        if (!project.isOpen()) {
            project.open(monitor);
        }
        
        monitor.worked(1);
        
        // Contextual Logic: Add a standard nature (e.g., Java Nature) or inject custom boilerplate files here
        // Example: Add Java Project Natures if needed
        
        monitor.worked(1);
        monitor.done();
    }
    
    // Tiny helper to bypass anonymous thread scope constraint flags
    private boolean booleanValue() { return true; }
}
