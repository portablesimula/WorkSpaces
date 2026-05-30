package simula.editor.TESTING;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorPaletteExample extends JFrame {

    public ColorPaletteExample() {
        setTitle("ColorPaletteExample: Fargepalett med 9 Linjer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 1, 5, 5)); // 9 rader, 1 kolonne

        // Standardfarger for de 9 linjene
        Color[] initialColors = {
            Color.RED, Color.GREEN, Color.BLUE,
            Color.YELLOW, Color.CYAN, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.GRAY
        };

        // Opprett 9 linjer
        for (int i = 0; i < 9; i++) {
            add(createPaletteLine("Fargelinje " + (i + 1), initialColors[i]));
        }

        pack();
        setLocationRelativeTo(null); // Sentrer på skjermen
    }

    private JPanel createPaletteLine(String labelText, Color initialColor) {
        JPanel linePanel = new JPanel(new BorderLayout(10, 0));
        linePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Tekstetikett
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        // Fargefirkant (visuell indikator)
        JPanel colorSquare = new JPanel();
        colorSquare.setPreferredSize(new Dimension(50, 25));
        colorSquare.setBackground(initialColor);
        colorSquare.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Gjør fargefirkanten klikkbar
        colorSquare.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Åpne JColorChooser dialogboks
                Color selectedColor = JColorChooser.showDialog(
                        linePanel,
                        "Velg ny farge for " + labelText,
                        colorSquare.getBackground()
                );

                // Oppdater fargen hvis brukeren trykket OK
                if (selectedColor != null) {
                    colorSquare.setBackground(selectedColor);
                }
            }
        });

        linePanel.add(label, BorderLayout.WEST);
        linePanel.add(colorSquare, BorderLayout.EAST);

        return linePanel;
    }

    public static void main(String[] args) {
        // Kjør GUI-tråden sikkert
        SwingUtilities.invokeLater(() -> {
            new ColorPaletteExample().setVisible(true);
        });
    }
}
