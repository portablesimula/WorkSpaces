package com.simula.client.ui.dnd;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dnd.IDragAndDropService;
import org.eclipse.ui.texteditor.ITextEditor;

public class REMOVED_DropServiceAttacher {

    public static void hookDropListener(ITextEditor editor) {
        // Adapt the text editor to retrieve the underlying Control structure
        Control control = editor.getAdapter(Control.class);
        if (control instanceof StyledText) {
            StyledText styledText = (StyledText) control;
            
            // Retrieve the Eclipse DnD Service
            IDragAndDropService dndService = editor.getSite().getService(IDragAndDropService.class);
            
            int operations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT;
            Transfer[] transfers = new Transfer[] { FileTransfer.getInstance() };
            
            // Merge your handler into the editor's primary text viewer area
            dndService.addMergedDropTarget(
                styledText, 
                operations, 
                transfers, 
                new REMOVED_EditorDropTargetListener(editor)
            );
        }
    }
}
