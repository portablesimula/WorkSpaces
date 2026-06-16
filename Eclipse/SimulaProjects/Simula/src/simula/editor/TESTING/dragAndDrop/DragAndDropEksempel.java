package simula.editor.TESTING.dragAndDrop;

import javax.swing.*;
import java.awt.*;

public class DragAndDropEksempel {
    public static void main(String[] args) {
        // Sikrer at Swing kjører på riktig tråd
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Drag and Drop Eksempel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLayout(new BorderLayout());

            // Opprett tekstområde
            JTextArea textArea = new JTextArea();
            textArea.setText("Dra filer fra skrivebordet/mappen og slipp dem her...\n\n");
            
            // VIKTIG: Koble TransferHandler til ditt JTextArea
//            textArea.setTransferHandler(new FilePathDropTarget());
            textArea.setTransferHandler(new FileContentDropTarget());

            // Legg til scrollbar og plasser i vinduet
            JScrollPane scrollPane = new JScrollPane(textArea);
            frame.add(scrollPane, BorderLayout.CENTER);

            // Sentrer og vis vinduet
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
