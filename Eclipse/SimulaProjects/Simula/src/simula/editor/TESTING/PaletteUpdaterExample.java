package simula.editor.TESTING;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaletteUpdaterExample extends JFrame {

    // Definerer paletten som et array av paneler og farger
    private final JPanel[] palettePanels = new JPanel[4];
    private final Color[] paletteColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    private final String[] paletteNames = { "KeyWord", "String", "Comment", "Constant"};

    public PaletteUpdaterExample() {
        setTitle("PaletteUpdaterExample: ");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel for å holde fargepaletten
        JPanel paletteContainer = new JPanel(new GridLayout(1, 4, 10, 10));
        paletteContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Opprett knappene (panelene) i paletten
        for (int i = 0; i < palettePanels.length; i++) {
            final int index = i; // Må være final for å brukes i anonym klasse
            
            palettePanels[i] = new JPanel();
            palettePanels[i].setBackground(paletteColors[i]);
            palettePanels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            palettePanels[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Legg til en mus-klikk-lytter for å åpne JColorChooser
            palettePanels[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    openColorChooser(index);
                }
            });

            paletteContainer.add(new Label(paletteNames[i]));
            paletteContainer.add(palettePanels[i]);
        }

        JLabel instruksjon = new JLabel("Klikk på en fargeboks for å endre fargen i paletten", SwingConstants.CENTER);
        
        add(instruksjon, BorderLayout.NORTH);
        add(paletteContainer, BorderLayout.CENTER);
    }

    // Håndterer fargevalg og oppdatering av paletten
    private void openColorChooser(int index) {
        // Åpner dialogen med gjeldende farge som forhåndsvalg
        Color selectedColor = JColorChooser.showDialog(
                this, 
                "Velg ny farge for palett " + (index + 1), 
                paletteColors[index]
        );

        // Hvis brukeren valgte en farge (og ikke trykket 'Cancel')
        if (selectedColor != null) {
            paletteColors[index] = selectedColor; // Oppdaterer farge-arrayet
            palettePanels[index].setBackground(selectedColor); // Oppdaterer grensesnittet
        }
    }

    public static void main(String[] args) {
        // Kjører GUI-tråden på en sikker måte
        SwingUtilities.invokeLater(() -> {
            new PaletteUpdaterExample().setVisible(true);
        });
    }
}
