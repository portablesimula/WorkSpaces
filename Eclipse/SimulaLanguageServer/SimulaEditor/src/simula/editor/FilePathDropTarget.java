package simula.editor;

import javax.swing.*;

import simula.text.TabbedTextHandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

/// @author Google AI
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public class FilePathDropTarget extends TransferHandler {
    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) return false;
        Transferable transferable = support.getTransferable();
        try {
        	@SuppressWarnings("unchecked")
        	List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
        	
        	// TODO: Make doOpenFile is thread-safe
        	LOOP:for (File file : files) {
        		IO.println(file.getAbsolutePath() + "\n");
        		SwingUtilities.invokeLater(() -> {
        			TabbedTextHandler.doOpenFile(file.getPath());
        		});
        		break LOOP;
        	}
        	
        	return true;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return false;
    }
}
