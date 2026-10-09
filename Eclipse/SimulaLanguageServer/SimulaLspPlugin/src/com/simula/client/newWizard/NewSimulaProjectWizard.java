package com.simula.client.newWizard;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.osgi.framework.Bundle;

import com.simula.client.DEF;
import com.simula.client.ProjectManager;
import com.simula.client.ui.SimulaPerspectiveListener;


public class NewSimulaProjectWizard extends Wizard implements INewWizard {

    private SimulaProjectCreationPage page;
    private IProject project;
    
    @SuppressWarnings("unused")
	private IWorkbench workbench;

    public NewSimulaProjectWizard() {
        super();
        setNeedsProgressMonitor(true);
        setWindowTitle("New Simula Project");
    }

	public IProject getCreatedProject() {
		// TODO Auto-generated method stub
		return project;
	}

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
    }

    @Override
    public void addPages() {
        page = new SimulaProjectCreationPage("NewSimulaProjectPage");
        page.setTitle("Simula Project");
        page.setDescription("Create a new Simula Project.");
        addPage(page);
    }
    
    @Override
    public boolean performFinish() {
        // Get the project handle from the wizard page
//      final IProject project = page.getProjectHandle();
        project = page.getProjectHandle();
//        final URI location = page.useDefaults() ? null : page.getLocationURI();
        final URI location = page.getLocationURI();
        final boolean includeSamples = page.isIncludeSamplesSelected();

        // Run the workspace modification within a WorkspaceModifyOperation to keep the UI responsive
        try {
            getContainer().run(true, true, monitor -> {
                try {
					createProject(project, location, monitor, includeSamples);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        ProjectManager.printAllProjects();
        return true;
    }
//    @Override
//    public boolean performFinish() {
//        final String projectName = page.getProjectName();
//        final org.eclipse.core.runtime.IPath projectLocation = page.getLocationPath();
//
//        // Kjør som en IWorkspaceRunnable for å sikre konsistens og ytelse
//        IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
//            @Override
//            public void run(IProgressMonitor monitor) throws CoreException {
//                createNewProject(projectName, projectLocation, monitor);
//            }
//        };
//
//        try {
//            ResourcesPlugin.getWorkspace().run(runnable, null);
//        } catch (CoreException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
    
    private void createNewProject(String name, org.eclipse.core.runtime.IPath location, IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Oppretter prosjekt", 3);

        // 1. Hent prosjekthåndtaket
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);

        // 2. Opprett en konsistent IProjectDescription
        IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(name);
        if (!ResourcesPlugin.getWorkspace().getRoot().getLocation().equals(location)) {
            description.setLocation(location);
        }

        // SIKRE KONSISTENS: Legg til dine egne prosjektnaturer (Natures) her
        // Dette sørger for at Eclipse gjenkjenner prosjektet riktig hver gang
        String[] defaultNatures = description.getNatureIds();
        String[] newNatures = new String[defaultNatures.length + 1];
        System.arraycopy(defaultNatures, 0, newNatures, 0, defaultNatures.length);
        newNatures[defaultNatures.length] = DEF.SIMULA_NATURE_ID;
        description.setNatureIds(newNatures);

        // 3. Opprett og åpne prosjektet med beskrivelsen
        project.create(description, monitor);
        monitor.worked(1);
        
        project.open(monitor);
        monitor.worked(1);

        // (Valgfritt) Legg til standardmapper eller filer her
        // ...
        
        monitor.done();
    }
    
    private void createProject(IProject project, URI location, IProgressMonitor monitor, boolean includeSamples) throws Exception {
    	SubMonitor subMonitor = SubMonitor.convert(monitor, "Creating Simula Project", 3);

    	// 1. Create and open the base Eclipse Project
    	if (!project.exists()) {
//    		IProjectDescription description = project.getWorkspace().newProjectDescription(project.getName());
            IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(project.getName());
            IO.println("NewSimulaProject.createProject: location="+location);
            if (location != null) {
    			description.setLocationURI(location);
    	        String[] defaultNatures = description.getNatureIds();
    	        String[] newNatures = new String[defaultNatures.length + 1];
    	        System.arraycopy(defaultNatures, 0, newNatures, 0, defaultNatures.length);
    	        newNatures[defaultNatures.length] = DEF.SIMULA_NATURE_ID;
    	        description.setNatureIds(newNatures);
    		}
    		project.create(description, subMonitor.split(1));
    	}
    	if (!project.isOpen()) {
    		project.open(subMonitor.split(1));
    	}
    	
    	

//    	ProjectManager.addSimulaNature(project);
        SimulaPerspectiveListener.switchToSimulaPerspective();
    	
    	// 2. Create the 'src' directory
    	IFolder srcFolder = project.getFolder("src");
    	if (!srcFolder.exists()) {
    		srcFolder.create(IResource.FORCE, true, subMonitor.split(1));
    	}

        if(includeSamples) {
	        // 3. Create the 'samples' directory
	        IFolder samplesFolder = project.getFolder("samples");
	        if (!samplesFolder.exists()) {
	            // force = true, local = true
	            samplesFolder.create(true, true, monitor);
	        }
	        // 3. Copy files from plugin's platform resource path '/samples' into 'samples'
	        try {
	            copySamplesToSource(samplesFolder, monitor);
	        } catch (Exception e) {
	            throw new CoreException(new org.eclipse.core.runtime.Status(
	                org.eclipse.core.runtime.IStatus.ERROR, DEF.SIMULA_PLUGIN_ID, "Failed to copy sample files", e));
	        }
        }
        monitor.worked(1);
        monitor.done();
    }

    private void copySamplesToSource(IFolder targetFolder, IProgressMonitor monitor) throws Exception {
        String pluginId = "com.simula.lsp.client"; 
        Bundle bundle = Platform.getBundle(pluginId);
        if (bundle == null) return;

        // Locate the /samples directory inside your plugin bundle
        IPath samplesPath = new Path("/samples");
        
        // Find entries matching everything under /samples. Use your specific file extensions if needed (e.g., "*.sim")
        java.util.Enumeration<URL> entries = bundle.findEntries(samplesPath.toString(), "*", true);
        
        if (entries != null) {
            while (entries.hasMoreElements()) {
                URL fileUrl = entries.nextElement();
                
                // Determine the relative path from the /samples root folder
                IPath pathInsideSamples = new Path(fileUrl.getPath()).makeRelativeTo(samplesPath);
                
                // If it points to a directory entry itself, skip or create subfolder
                if (fileUrl.getPath().endsWith("/")) {
                    if (!pathInsideSamples.isEmpty()) {
                        IFolder subFolder = targetFolder.getFolder(pathInsideSamples);
                        if (!subFolder.exists()) {
                            subFolder.create(true, true, monitor);
                        }
                    }
                    continue;
                }

                // Target destination for the file within the 'src' workspace folder
                IFile targetFile = targetFolder.getFile(pathInsideSamples);
                
                // Open stream and write directly to the workspace IFile abstraction
                try (InputStream is = fileUrl.openStream()) {
                    if (targetFile.exists()) {
                        targetFile.setContents(is, true, true, monitor);
                    } else {
                        // Ensure parent folders are built if nested
                        if (!targetFile.getParent().exists() && targetFile.getParent() instanceof IFolder) {
                            ((IFolder) targetFile.getParent()).create(true, true, monitor);
                        }
                        targetFile.create(is, true, monitor);
                    }
                }
            }
        }
    }
    
}
