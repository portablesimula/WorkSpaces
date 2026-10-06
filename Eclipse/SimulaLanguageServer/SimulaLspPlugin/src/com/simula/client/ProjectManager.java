package com.simula.client;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
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
	
	
	public static IProject showAndSelectProjectInExplorer() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		try {
		    // 1. Get the active workbench page
		    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		    
		    // 2. Show the Project Explorer view (ID: org.eclipse.ui.navigator.ProjectExplorer)
		    // This will open the view if it's closed, or bring it to focus if it's open.
		    IViewPart projectExplorer = page.showView("org.eclipse.ui.navigator.ProjectExplorer");
		    
		    // 3. Get the selection provider for the view
		    ISelectionProvider selectionProvider = projectExplorer.getSite().getSelectionProvider();
		    
		    if (selectionProvider != null) {
		        // Target a specific IProject instance (e.g., your newly created or accessed project)
		        IProject targetProject = root.getProject("MyProjectName");
		        
		        // 4. Set the selection to focus on your project
		        selectionProvider.setSelection(new StructuredSelection(targetProject));
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}	}
}
