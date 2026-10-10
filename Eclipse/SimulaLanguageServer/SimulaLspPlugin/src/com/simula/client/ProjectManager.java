package com.simula.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CopyFilesAndFoldersOperation;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.FileEditorInput;

import com.simula.client.newWizard.NewSimulaProjectWizard;

public class ProjectManager {
	
	public static IProject getProjectByName(String projectName) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);

		if (project.exists()) {
		    // The project exists, but ensure it's open before accessing its files
		    if (!project.isOpen()) {
		        try {
					project.open(null);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // passing null for IProgressMonitor
		    }
		}	
		return project;
	}

	
	public static IProject getSimulaProject() {
		printAllProjects();
//		IProject project = getActiveProject();
		// ...
		List<IProject> simulaProjects = getSimulaProjects();
		IO.println("SimulaStartupHandler.getSimulaProject: simulaProjects="+simulaProjects);
		if(simulaProjects.isEmpty()) {
			return createNewProject();		
		}
		if(simulaProjects.size() > 1) {
			return askUserToSelectProject(simulaProjects);
		}
		return simulaProjects.get(0);
	}

	public static boolean isSimulaProject(IProject project) {
		try {
			// Ensure the project is open before checking its nature
			return project != null && project.isOpen() && project.hasNature(DEF.SIMULA_NATURE_ID);
		} catch (CoreException e) {
			// Handle exceptions (e.g., project does not exist or is closed)
			return false;
		}
	}

	private static IProject askUserToSelectProject(List<IProject> simulaProjects) {
	    // Get the current active window shell
	    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	    
	    // WorkbenchLabelProvider naturally extracts the proper icons and text for IProject elements
	    ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, new WorkbenchLabelProvider());
	    
	    dialog.setTitle("Select Simula Project");
	    dialog.setMessage(
	    		  "Your workspace contains several Simula projects.\n"
	    		+ "In which project do you want to place the file?\n\n"
	    		+ "Choose a project from the list or leave unselected\n"
	    		+ "in which case the file is not added to any project:");
	    dialog.setElements(simulaProjects.toArray());
	    
	    // CRITICAL: Allows the user to click OK with zero elements highlighted
//	    dialog.setAllowEmptySelection(true); 
	    
	    // Optional: set to false if you only want them to pick at most ONE project
	    dialog.setMultipleSelection(false); 
	    
	    if (dialog.open() == Window.OK) {
	        Object[] result = dialog.getResult();
	        if (result != null && result.length > 0) {
	            return (IProject) result[0];
	        }
	    }
	    
	    // Returns null if the user canceled OR explicitly chose "none"
	    return null; 
	}

	public static List<IProject> getSimulaProjects() {
	    List<IProject> simulaProjects = new ArrayList<>();
	    
	    // 1. Hent roten til det gjeldende Eclipse-workspacet
	    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	    
	    // 2. Gå gjennom alle prosjekter i workspacet
	    for (IProject project : root.getProjects()) {
	    	if(isSimulaProject(project)) simulaProjects.add(project);
	    }
	    IO.println("ProjectManager.getSimulaProjects: returns: " + simulaProjects);
	    return simulaProjects;
	}

    public static void addSimulaNature(IProject project) throws CoreException {
        // 1. Hent den eksisterende prosjektbeskrivelsen
        IProjectDescription description = project.getDescription();
        String[] prevNatures = description.getNatureIds();
        
        // 2. Opprett en ny matrise med plass til den nye nature-ID-en
        String[] newNatures = new String[prevNatures.length + 1];
        System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
        
        // 3. Legg til din spesifikke Simula Nature ID (må matche plugin.xml)
//        newNatures[prevNatures.length] = "din.plugin.id.simulaNature"; 
        newNatures[prevNatures.length] = DEF.SIMULA_NATURE_ID; 
        
        // 4. Sett de oppdaterte natures på beskrivelsen og lagre
        description.setNatureIds(newNatures);
        project.setDescription(description, null);
    }

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
	
	public void refresh(IProject project) throws CoreException {
		// Call this after performing native Java file writes to update the UI
		project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
	}
	
    
    /**
     * Creates a file in the specified project and opens it in the Eclipse Generic Editor.
     * 
     * @param project The target IProject workspace resource
     * @param fileName The name of the file (e.g., "config.txt" or "script.js")
     * @param initialContent The starting string content of the file
     */
    public static void createAndOpenInGenericEditor(String fileName, String initialContent) {
        // 1. Get a handle on the file within the project
    	IProject project = getSimulaProject();
    	IFile file = project.getFile(fileName);

        // 2. Create the file resource with initial content if it doesn't exist
        if (!file.exists()) {
            try {
                InputStream source = new ByteArrayInputStream(initialContent.getBytes());
                // This automatically triggers resource change listeners to refresh Project Explorer
                file.create(source, true, null); 
            } catch (CoreException e) {
                e.printStackTrace();
                return;
            }
        }

        // 3. Open the file in the Generic Editor on the UI thread
        PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
            try {
                IWorkbenchPage page = PlatformUI.getWorkbench()
                                                .getActiveWorkbenchWindow()
                                                .getActivePage();
                
                // The explicit ID for the Eclipse Generic Editor
                String genericEditorId = "org.eclipse.ui.genericeditor.GenericEditor";
                
                // Open the editor
                page.openEditor(new FileEditorInput(file), genericEditorId, true);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

	/// create a completely new file inside an existing project,
	/// get a handle on the project, define the path, and invoke IFile.create()
	public void addFileToProject(String projectName, String filePath, String content) {
	    // 1. Get a reference to the project
	    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	    
	    if (project.exists()) {
	        // 2. Define the file path relative to the project
	        IFile file = project.getFile(new Path(filePath));
	        
	        // 3. Prepare your text/binary content
	        InputStream source = new ByteArrayInputStream(content.getBytes());
	        
	        try {
	            // Create the file. If parent folders don't exist, you'll need to create them first.
	            if (!file.exists()) {
	                file.create(source, IFile.FORCE, null);
	            } else {
	                // If it already exists, update the contents instead
	                file.setContents(source, IFile.FORCE, null);
	            }
	        } catch (CoreException e) {
	            e.printStackTrace();
	        }
	    }
	}

	
	private static void addFileToProject(IProject project, URI fileUri, String desiredFileName) {
	    IProgressMonitor monitor = new NullProgressMonitor();
	    
	    // 1. Get the file handle relative to your target project folder
	    IFile file = project.getFile(desiredFileName);
	    
	    try {
	        // 2. Link the project-relative handle to the absolute URI location
	        if (!file.exists()) {
	            file.createLink(fileUri, IResource.NONE, monitor);
	        } else {
	            // If it's already there, just refresh it to make it visible
	            file.refreshLocal(IResource.DEPTH_ZERO, monitor);
	        }
	    } catch (CoreException e) {
	        e.printStackTrace();
	    }
	}
	
//    public void addFileToSrcFolder(IProject project, String fileName, String fileContent, IProgressMonitor monitor) {
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
            } else {
                // If it already exists, overwrite its content
                newFile.setContents(source, IFile.FORCE, monitor);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle Eclipse core exceptions here
        }
    }

    public static void OLD_linkFileToSrcFolder(IProject project, URI fileUri) {
    	IO.println("ProjectManager.linkFileToSrcFolder: " + fileUri);
    	// Get the handle to the 'src' folder
    	IFolder srcFolder = project.getFolder("src");

    	// The external file you want to link
    	File externalFile = new File(fileUri);
    	IPath externalPath = new Path(externalFile.getAbsolutePath());

    	// Define the name the file will have inside the 'src' folder
    	IFile linkedFile = srcFolder.getFile(externalPath.lastSegment());

    	try {
    	    // Create the link. 
    	    // Using IResource.NONE or IResource.REPLACE depends on your fallback strategy
    	    linkedFile.createLink(externalPath, IResource.NONE, new NullProgressMonitor());

        	// Forces the resource tree to sync with the local file system structure
        	linkedFile.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
    	} catch (CoreException e) {
    	    e.printStackTrace();
    	    // Handle exception (e.g., file already exists, invalid path)
    	}
    	IO.println("ProjectManager.linkFileToSrcFolder: DONE: " + fileUri);    	
    }
    
    public static void linkFileToSrcFolder(IProject project, URI fileUri) {
    	IO.println("ProjectManager.linkFileToSrcFolder: " + fileUri);
	    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    	// Get the handle to the 'src' folder
    	IFolder srcFolder = project.getFolder("src");
    	CopyFilesAndFoldersOperation operation = new CopyFilesAndFoldersOperation(shell);

    	// 3. Define the absolute file paths (as Strings) of the files to import
    	String path = fileUri.getPath(); 
    	String[] filePaths = new String[] {
//    	    "C:\\path\\to\\external\\file1.txt",
//    	    "C:\\path\\to\\external\\file2.jpg"
    			path
    	};

    	// 4. Execute the operation. This automatically triggers the Eclipse dialog.
    	operation.copyFiles(filePaths, srcFolder);
    }
	

	public static void printAllProjects() {
		// Get the root of the workspace
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		// Retrieve all projects
		IProject[] projects = root.getProjects();

		for (IProject project : projects) {
			if (project.isOpen()) {
//				System.out.println("ProjectManager.printAllProjects: Project: " + project);
//				System.out.println("ProjectManager.printAllProjects: Project Name: " + project.getName());
//				System.out.println("ProjectManager.printAllProjects: Project Location: " + project.getLocation().toOSString());
//				System.out.println("ProjectManager.printAllProjects: Project.type: " + project.getType());
//				if (project.isOpen()) {
//					try {
//						IProjectDescription description = project.getDescription();
//						String[] natureIds = description.getNatureIds();
//
//						for (String natureId : natureIds) {
//							System.out.println("ProjectManager.printAllProjects: Project Nature ID: " + natureId);
//						}
//
////						System.out.println("ProjectManager.printAllProjects: Project.content type: " + project.getContentTypeMatcher());
//					} catch (CoreException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
				printProjectDescription(project);
			}
		}
	}
    
    public static void printProjectDescription(IProject project) {
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
