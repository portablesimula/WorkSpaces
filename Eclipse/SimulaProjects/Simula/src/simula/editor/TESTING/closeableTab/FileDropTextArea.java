package simula.editor.TESTING.closeableTab;

import javax.swing.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.util.List;

public class FileDropTextArea extends JTextArea {

    public FileDropTextArea() {
        // Enable the default drop target behavior
        setDragEnabled(true);
        
        // Set the custom transfer handler
        setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                // Only accept file drops
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }

                try {
                    // Fetch the dropped items as a List of Files
                    Transferable transferable = support.getTransferable();
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                    // Process each file (Scenario A: Append names, Scenario B: Read content)
                    StringBuilder sb = new StringBuilder();
                    for (File file : files) {
                        sb.append(file.getAbsolutePath()).append("\n");
                    }
                    
                    // Append or set the text in the text area
                    append(sb.toString());
                    return true;
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
    }
}
