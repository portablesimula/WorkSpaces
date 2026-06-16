package simula.editor.TESTING.dragAndDrop;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class FileContentDropTarget extends TransferHandler {
    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        Transferable transferable = support.getTransferable();
        try {
            List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
            JTextArea textArea = (JTextArea) support.getComponent();
            
            // Leser kun den første filen som slippes
            if (!files.isEmpty()) {
                File file = files.get(0);
                String content = Files.readString(file.toPath());
                textArea.setText(content);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
