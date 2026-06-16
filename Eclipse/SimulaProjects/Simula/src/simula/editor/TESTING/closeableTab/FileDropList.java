package simula.editor.TESTING.closeableTab;

import javax.swing.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.util.List;

public class FileDropList extends JFrame {
    private JList<String> fileList;
    private DefaultListModel<String> listModel;

    public FileDropList() {
        setTitle("File Drag and Drop Example");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);

        // 1. Enable the default drop target behavior
        fileList.setDragEnabled(true);

        // 2. Set the custom TransferHandler
        fileList.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                // Only accept file flavors (DataFlavor.javaFileListFlavor)
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }

                try {
                    // Fetch the data from the transferable object
                    Transferable transferable = support.getTransferable();
                    @SuppressWarnings("unchecked")
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                    // Process the files (add names to our list model)
                    for (File file : files) {
                        listModel.addElement(file.getAbsolutePath());
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        add(new JScrollPane(fileList));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileDropList().setVisible(true));
    }
}
