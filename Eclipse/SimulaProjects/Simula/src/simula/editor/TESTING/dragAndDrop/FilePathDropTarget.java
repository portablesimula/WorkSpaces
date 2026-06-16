package simula.editor.TESTING.dragAndDrop;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

public class FilePathDropTarget extends TransferHandler {
    @Override
    public boolean canImport(TransferSupport support) {
        // Vi aksepterer kun sletting av filer
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        Transferable transferable = support.getTransferable();
        try {
            // Hent ut listen over filer som ble sluppet
            List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
            JTextArea textArea = (JTextArea) support.getComponent();
            
            // Legg til filbanene i tekstområdet
            for (File file : files) {
                textArea.append(file.getAbsolutePath() + "\n");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
