package com.simula.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ProjectManager {

	public static void printAllProjects() {
		// Get the root of the workspace
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		// Retrieve all projects
		IProject[] projects = root.getProjects();

		for (IProject project : projects) {
			if (project.isOpen()) {
				System.out.println("Project Name: " + project.getName());
				System.out.println("Project Location: " + project.getLocation().toOSString());
			}
		}

	}
	
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
	
	public static IProject getActiveProject() {
	    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    if (window == null) {
	        return null;
	    }

	    // 1. Try getting the project from the current view selection (e.g., Package Explorer)
	    ISelection selection = window.getSelectionService().getSelection();
	    if (selection instanceof IStructuredSelection) {
	        Object firstElement = ((IStructuredSelection) selection).getFirstElement();
	        if (firstElement instanceof IAdaptable) {
	            IProject project = ((IAdaptable) firstElement).getAdapter(IProject.class);
	            if (project != null) {
	                return project;
	            }
	        }
	    }

	    // 2. Fallback: Try getting the project from the active editor
	    IWorkbenchPage activePage = window.getActivePage();
	    if (activePage != null) {
	        IEditorPart activeEditor = activePage.getActiveEditor();
	        if (activeEditor != null) {
	            IEditorInput input = activeEditor.getEditorInput();
	            IProject project = input.getAdapter(IProject.class);
	            if (project != null) {
	                return project;
	            }
	            
	            // Second fallback fallback: Adapt to resource first, then get project
	            IResource resource = input.getAdapter(IResource.class);
	            if (resource != null) {
	                return resource.getProject();
	            }
	        }
	    }

	    return null;
	}

	
	public void refresh(IProject project) throws CoreException {
		// Call this after performing native Java file writes to update the UI
		project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
	}
	
	/// create a completely new file inside an existing project,
	/// get a handle on the project, define the path, and invoke IFile.create()
	public void addFileToProject(String projectName, String filePath, String content) {
	    // 1. Get the workspace root
	    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	    
	    // 2. Get the target project
	    IProject project = root.getProject(projectName);
	    
	    if (project.isOpen()) {
	        // 3. Get the file handle (relative to the project)
	        IFile file = project.getFile(new Path(filePath));
	        
	        // 4. Set up file content stream
	        InputStream source = new ByteArrayInputStream(content.getBytes());
	        
	        try {
	            // 5. Create the file in the workspace
	            if (!file.exists()) {
	                file.create(source, IResource.NONE, new NullProgressMonitor());
	            } else {
	                // Update file content if it already exists
	                file.setContents(source, IResource.FORCE, new NullProgressMonitor());
	            }
	        } catch (CoreException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static void addFileToProject(IProject project, URI fileUri, String desiredFileName) {
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
	
	/// Linking an Existing External File.
	/// If the file already exists somewhere else on the local file system and you want it
	/// to appear in the Project Explorer without physically moving it, create it as a linked resource:
	public void linkExternalFile(IProject project, String targetFileName, IPath externalFilePath) {
	    IFile file = project.getFile(new Path(targetFileName));
	    try {
	        if (!file.exists()) {
	            // Link the workspace handle to the absolute file system path
	            file.createLink(externalFilePath, IResource.NONE, new NullProgressMonitor());
	        }
	    } catch (CoreException e) {
	        e.printStackTrace();
	    }
	}

}
