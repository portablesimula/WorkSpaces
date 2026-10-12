package com.simula.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import com.simula.client.newWizard.NewSimulaProjectWizard;

public class ProjectManager {

    public static IProject createNewProject() {
    	// 1. Instansier din egen wizard-klasse
    	NewSimulaProjectWizard wizard = new NewSimulaProjectWizard();

    	// 2. Initialiser den (viktig for prosjekt-wizards for å sette opp workbench og utvalg)
    	wizard.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY);

    	// 3. Opprett en WizardDialog som ramme rundt wizarden
    	WizardDialog dialog = new WizardDialog(
    	    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
    	    wizard
    	);

    	// 4. Åpne dialogen og fang opp returkoden (blokkerer tråden til vinduet lukkes)
    	int result = dialog.open();

    	if (result == Window.OK) {
    	    IO.println("ProjectManager.createNewProject: OK");
    	    IProject newProject = wizard.getCreatedProject(); 
    	    return newProject;
    	} else {
    	    IO.println("ProjectManager.createNewProject: CANCEL");
    	    return null;
    	}
    }
	
	private void refresh(IProject project) throws CoreException {
		// Call this after performing native Java file writes to update the UI
		project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
	}

    public void copyFileToSrcFolder(IProject project, String fileName, URI fileUri) {
	    IProgressMonitor monitor = new NullProgressMonitor();
        try {
            String content = Files.readString(Paths.get(fileUri));//, StandardCharsets.UTF_8);
            
            	// 1. Get a reference to the 'src' folder
            IFolder srcFolder = project.getFolder("src");
            
            // Optional: Create the src folder if it doesn't exist yet
            if (!srcFolder.exists()) {
                srcFolder.create(true, true, monitor);
            }

            // 2. Get a reference to the file handle inside the src folder
            IFile newFile = srcFolder.getFile(fileName);

            // 3. Convert your file string content into an InputStream
            InputStream source = new ByteArrayInputStream(content.getBytes());

            // 4. Create the file in the workspace
            if (!newFile.exists()) {
                newFile.create(source, IFile.FORCE, monitor);
                IO.println("ProjectManager.copyFileToSrcFolder: " + fileName);
            } else {
                // If it already exists, overwrite its content
                newFile.setContents(source, IFile.FORCE, monitor);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle Eclipse core exceptions here
        }
    }


	public static void printAllProjects() {
		// Get the root of the workspace
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		// Retrieve all projects
		IProject[] projects = root.getProjects();

		for (IProject project : projects) {
			if (project.isOpen()) {
				printProjectDescription(project);
			}
		}
	}
    
    private static void printProjectDescription(IProject project) {
        // 1. Ensure the project is not null and is open before inspecting it
        if (project == null) {
            System.out.println("ProjectManager.printProjectDescription: Project reference is null.");
            return;
        }
        
        if (!project.isOpen()) {
            System.out.println("ProjectManager.printProjectDescription: Project '" + project.getName() + "' is closed.");
            return;
        }

        try {
            // 2. Retrieve the underlying IProjectDescription
            IProjectDescription description = project.getDescription();

            System.out.println("ProjectManager.printProjectDescription:  ========================================");
            System.out.println("Project Name: " + project.getName());
            System.out.println("Project Location: " + project.getLocation().toOSString());
            System.out.println("Description Name: " + description.getName());
            System.out.println("Description Location URI: " + description.getLocationURI());
            System.out.println("Description Comment:      " + description.getComment());

            // 3. Print associated Project Natures (e.g., Java, Plugin, etc.)
            String[] natures = description.getNatureIds();
            System.out.println("\n--- Project Natures ---");
            if (natures.length == 0) {
                System.out.println("None");
            } else {
                for (String nature : natures) {
                    System.out.println(" Nature ID: " + nature);
                }
            }

            // 4. Print Build Commands / Builders configured for this project
            ICommand[] buildSpec = description.getBuildSpec();
            System.out.println("\n--- Build Spec (Builders) ---");
            if (buildSpec.length == 0) {
                System.out.println("None");
            } else {
                for (ICommand command : buildSpec) {
                    System.out.println(" Builder Name: " + command.getBuilderName());
                }
            }

            // 5. Print Referenced Projects
            IProject[] referencedProjects = description.getReferencedProjects();
            System.out.println("\n--- Referenced Projects ---");
            if (referencedProjects.length == 0) {
                System.out.println("None");
            } else {
                for (IProject refProject : referencedProjects) {
                    System.out.println(" References: " + refProject.getName());
                }
            }
            System.out.println("========================================");

        } catch (CoreException e) {
            System.err.println("Failed to read project description for: " + project.getName());
            e.printStackTrace();
        }
    }

}
