package com.simula.client.ui.dnd;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import java.io.File;
import java.io.FileInputStream;

public class REMOVED_EditorDropTargetListener extends DropTargetAdapter {
    
    private final ITextEditor editor;

    public REMOVED_EditorDropTargetListener(ITextEditor editor) {
        this.editor = editor;
    }

    @Override
    public void dragOver(DropTargetEvent event) {
        // Enable visual feedback indicating a copy operation is allowed
        event.detail = DND.DROP_COPY;
    }

    @Override
    public void drop(DropTargetEvent event) {
        // Confirm the dropped data consists of OS file paths
        if (event.data instanceof String[]) {
            String[] filePaths = (String[]) event.data;
            IEditorInput input = editor.getEditorInput();
            
            // Derive the target project from the currently open editor file
            if (input instanceof IFileEditorInput) {
                IProject project = ((IFileEditorInput) input).getFile().getProject();
                
                for (String path : filePaths) {
                    addFileToProject(project, new File(path));
                }
            }
        }
    }

    private void addFileToProject(IProject project, File sourceFile) {
        try {
            // Define the file space inside the current project root
            IFile targetFile = project.getFile(new Path(sourceFile.getName()));
            
            if (!targetFile.exists()) {
                try (FileInputStream is = new FileInputStream(sourceFile)) {
                    targetFile.create(is, IResource.NONE, null);
                }
            } else {
                // Optional: Handle overwriting or merging existing resources
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
}
