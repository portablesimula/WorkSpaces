package com.simula.client;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.simula.client.ui.SimulaPerspectiveListener;

public class SimulaStartupHandler implements IStartup {

    @Override
    public void earlyStartup() {
        // Your code runs automatically here after the Workbench starts.
        // This execution occurs in a background thread.
        System.out.println("Eclipse Plugin successfully initialized on startup!");
        
//		REMOVED_SimulaDropHandler.register();

//		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//		page.addPartListener(new EditorDropInitializer());
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] workbenchWindows = workbench.getWorkbenchWindows();
		IO.print("SimulaStartupHandler.earlyStartup: workbenchWindows: "+workbenchWindows);
		
		for(IWorkbenchWindow win:workbenchWindows) {
			IWorkbenchPage activePage = win.getActivePage();
			IO.print("SimulaStartupHandler.earlyStartup: activePage: "+activePage);
		}
		
		registerMyPartListener();
		
        Display.getDefault().asyncExec(() -> {
        	PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(new SimulaPerspectiveListener());
        });
    }
		
		public void registerMyPartListener() {
	        // Switch to the UI Thread safely
	        Display.getDefault().asyncExec(() -> {
	            final IWorkbench workbench = PlatformUI.getWorkbench();
	            
	            // 1. Hook listeners to all currently open windows
	            for (IWorkbenchWindow window : workbench.getWorkbenchWindows()) {
	                hookPartListenerToWindow(window);
	            }
	            
	            // 2. Listen for any future windows that get opened
	            workbench.addWindowListener(new IWindowListener() {
	                @Override
	                public void windowOpened(IWorkbenchWindow window) {
	                    hookPartListenerToWindow(window);
	                }

	                @Override public void windowClosed(IWorkbenchWindow window) {}
	                @Override public void windowActivated(IWorkbenchWindow window) {}
	                @Override public void windowDeactivated(IWorkbenchWindow window) {}
	            });
	        });
		}
		
	    private void hookPartListenerToWindow(IWorkbenchWindow window) {
	        if (window == null) return;
	        
	        // Listen to pages already present or opened in this window
	        IWorkbenchPage activePage = window.getActivePage();
	        if (activePage != null) {
//	            activePage.addPartListener(new CustomPartListener());
	            activePage.addPartListener(partListener);
	        }
	    }

		// Implement your listener
		private final IPartListener2 partListener = new IPartListener2() {
		    @Override public void partOpened(IWorkbenchPartReference partRef) {
		        if (isGenericEditor(partRef)) {
		            // Your logic when a Generic Editor is opened
		        	URI uri = getFileUriFromReference(partRef);
			        System.out.println("SimulaStartupHandler'IPartListener2.partOpened: Part opened with ID: " + partRef.getId());
			        System.out.println("SimulaStartupHandler'IPartListener2.partOpened: URI: " + uri);
			        switchToSimulaPerspective();
			        
			        IProject project = ProjectManager.getSimulaProject();
			        addFileToProjectExplorer(project, uri);
		        }
		    }

		    @Override public void partActivated(IWorkbenchPartReference partRef) {
		        if (isGenericEditor(partRef)) {
		            // Your logic when Generic Editor takes focus
			        System.out.println("SimulaStartupHandler'IPartListener2.partOpened: Part activated with ID: " + partRef.getId());
		        }
		    }

//		    @Override public void partClosed(IWorkbenchPartReference partRef) { }
//		    @Override public void partActivated(IWorkbenchPartReference partRef) { }
//		    @Override public void partDeactivated(IWorkbenchPartReference partRef) { }
//		    @Override public void partVisible(IWorkbenchPartReference partRef) { }
//		    @Override public void partHidden(IWorkbenchPartReference partRef) { }
//		    @Override public void partBroughtToTop(IWorkbenchPartReference partRef) { }
//		    @Override public void partInputChanged(IWorkbenchPartReference partRef) { }
		};
		
		private void addFileToProjectExplorer(IProject project, URI fileUri) {
			ProjectManager.addFileToProject(project, fileUri, ""+fileUri);
		}
		
		private void switchToSimulaPerspective() {
		    // ID-en du har definert for perspektivet ditt i plugin.xml

		    // Kjør på UI-tråden for å unngå ugyldige tråd-tilganger
		    PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		        @Override
		        public void run() {
		            try {
		                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		                if (window != null) {
		                    PlatformUI.getWorkbench().showPerspective(DEF.SIMULA_PERSPECTIVE_ID, window);
		                }
		            } catch (WorkbenchException e) {
		                // Håndter eventuell feil hvis perspektiv-ID-en ikke finnes
		                e.printStackTrace();
		            }
		        }
		    });
		}
		

	    private static final String GENERIC_EDITOR_ID = "org.eclipse.ui.genericeditor.GenericEditor";
	    private boolean isGenericEditor(IWorkbenchPartReference partRef) {
	        return GENERIC_EDITOR_ID.equals(partRef.getId());
	    }

		public static URI getFileUriFromReference(IWorkbenchPartReference partRef) {
		    // 1. Check if the part reference points to an editor
		    if (partRef instanceof IEditorReference) {
		        IEditorReference editorRef = (IEditorReference) partRef;
		        try {
		            // 2. Extract the editor input (safely resolves without forcing part activation)
		            IEditorInput input = editorRef.getEditorInput();
		            if (input == null) return null;

		            // 3. Try to adapt or cast to IURIEditorInput (covers generic/remote/local file inputs)
		            IURIEditorInput uriInput = input.getAdapter(IURIEditorInput.class);
		            if (uriInput != null) {
		                return uriInput.getURI();
		            }
		            
		            // Fallback for direct instanceof check if the adapter mechanism isn't fully implemented
		            if (input instanceof IURIEditorInput) {
		                return ((IURIEditorInput) input).getURI();
		            }

		            // 4. Try to adapt or cast to IFileEditorInput (covers typical workspace files)
		            IFileEditorInput fileInput = input.getAdapter(IFileEditorInput.class);
		            if (fileInput == null && input instanceof IFileEditorInput) {
		                fileInput = (IFileEditorInput) input;
		            }
		            
		            if (fileInput != null) {
		                IFile file = fileInput.getFile();
		                if (file != null) {
		                    return file.getLocationURI(); // Returns the absolute file system URI
		                }
		            }
		        } catch (Exception e) {
		            // Handle or log potential PartInitException from getEditorInput()
		            e.printStackTrace();
		        }
		    }
		    return null;
		}
		
    }

