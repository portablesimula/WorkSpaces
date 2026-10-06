package com.simula.client.ui.dnd;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.IWizardDescriptor;

public class REMOVED_SimulaDropHandler {

    public static void register() {
        Display.getDefault().asyncExec(() -> {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        	IO.println("SimulaDropHandler.register: window=" + window);
            if (window == null) return;
            
            Shell shell = window.getShell();
        	IO.println("SimulaDropHandler.register: shell=" + shell);
            
            // Definer at vi godtar fil-overføringer (FileTransfer)
            int operations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT;
            DropTarget target = new DropTarget(shell, operations);
            target.setTransfer(new Transfer[] { FileTransfer.getInstance() });
            
            target.addDropListener(new DropTargetAdapter() {
                @Override
                public void dragEnter(DropTargetEvent event) {
                	IO.println("SimulaDropHandler'dragEnter: " + event);
                    if (event.detail == DND.DROP_DEFAULT) {
                        event.detail = DND.DROP_COPY;
                    }
                }

                @Override
                public void drop(DropTargetEvent event) {
                	IO.println("SimulaDropHandler'drop: " + event);
                	IO.println("SimulaDropHandler'drop: data=" + event.data);
                	IO.println("SimulaDropHandler'drop: currentDataType=" + event.currentDataType);
                    if (FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
                        String[] files = (String[]) event.data;
                        if (files != null && files.length > 0) {
                            // Håndter de droppede filene
                            handleDroppedFiles(files);
                        }
                    }
                }
            });
        });
    }
    
    private static void handleDroppedFiles(String[] filePaths) {
        IO.println("SimulaDropHandler.handleDroppedFiles: simulaProject=" + filePaths);
        IProject simulaProject = findActiveSimulaProject();
        IO.println("SimulaDropHandler.handleDroppedFiles: simulaProject=" + simulaProject);
        
        if (simulaProject == null) {
            // Ingen Simula-prosjekt funnet, åpne New Project Wizard
            simulaProject = openNewProjectWizardAndGetProject();
        }
        
        if (simulaProject != null && simulaProject.isOpen()) {
            for (String path : filePaths) {
                importFileToProject(simulaProject, path);
            }
        }
    }

    private static IProject findActiveSimulaProject() {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        for (IProject project : root.getProjects()) {
            try {
                // Bytt ut "my.simula.nature.id" med din faktiske Simula Project Nature ID
                if (project.isOpen() && project.hasNature("my.simula.nature.id")) {
                    return project; // Returnerer det første åpne Simula-prosjektet
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static IProject openNewProjectWizardAndGetProject() {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        
        // Finn din eksisterende New Project Wizard ved hjelp av dens ID
        // Bytt ut "my.simula.project.wizard.id" med din faktiske wizard-ID
        IWizardDescriptor wizardDesc = PlatformUI.getWorkbench()
                .getNewWizardRegistry().findWizard("my.simula.project.wizard.id");
        
        if (wizardDesc != null) {
            try {
                IWorkbenchWizard wizard = wizardDesc.createWizard();
                wizard.init(PlatformUI.getWorkbench(), null);
                
                WizardDialog dialog = new WizardDialog(shell, wizard);
                if (dialog.open() == WizardDialog.OK) {
                    // Etter at brukeren har fullført wizarden, søker vi etter det nyopprettede prosjektet
                    return findActiveSimulaProject();
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void importFileToProject(IProject project, String filePath) {
        File sourceFile = new File(filePath);
        IFile targetFile = project.getFile(new Path(sourceFile.getName()));
        
        try {
            // Kopier filen inn i prosjektet (hvis den ikke allerede eksisterer)
            if (!targetFile.exists()) {
                try (FileInputStream ins = new FileInputStream(sourceFile)) {
                    targetFile.create(ins, true, null);
                }
            }
            
            // Åpne filen i Generic Editor
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            if (page != null) {
                // ID til den moderne Generic Editor i Eclipse
                String genericEditorId = "org.eclipse.ui.genericeditor.GenericEditor";
                IDE.openEditor(page, targetFile, genericEditorId);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

}
