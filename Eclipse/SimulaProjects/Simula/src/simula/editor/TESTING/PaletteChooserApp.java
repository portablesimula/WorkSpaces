package simula.editor.TESTING;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaletteChooserApp extends JFrame {

    // Standardfarger for tilbakestilling
    private final Color[] defaultColors = {
        Color.RED, Color.GREEN, Color.BLUE,
        Color.YELLOW, Color.MAGENTA, Color.CYAN,
        Color.ORANGE, Color.PINK, Color.GRAY
    };

    // Komponenter som må oppdateres globalt
    private final JPanel[] colorPanels = new JPanel[9];
    private final JLabel[] colorLabels = new JLabel[9];
    private final Color[] currentColors = new Color[9];

    public PaletteChooserApp() {
        // 1. Vindusinnstillinger
        setTitle("Fargepalett Oppdatering");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 2. Overskrift
        JLabel titleLabel = new JLabel("Min Fargepalett (9 Linjer)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // 3. Palett-område (9 linjer)
        JPanel palettePanel = new JPanel();
        palettePanel.setLayout(new GridLayout(9, 1, 5, 5));
        palettePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        for (int i = 0; i < 9; i++) {
            currentColors[i] = defaultColors[i];
            palettePanel.add(createPaletteLine(i));
        }
        add(palettePanel, BorderLayout.CENTER);

        // 4. Reset-knapp i bunnen
        JButton resetButton = new JButton("Nullstill Palett");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 14));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetPalette();
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(resetButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Metode for å opprette én enkelt linje i paletten
    private JPanel createPaletteLine(int index) {
        JPanel line = new JPanel(new BorderLayout(15, 0));

        // Tekstlabel som viser linjenummer og HEX-verdi
        JLabel label = new JLabel("Linje " + (index + 1) + " (" + getHexColor(currentColors[index]) + ")");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(currentColors[index]);
        colorLabels[index] = label;

        // Fargevisning (enkel boks)
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(50, 30));
        colorBox.setBackground(currentColors[index]);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        colorPanels[index] = colorBox;

        // Knapp for å endre fargen via JColorChooser
        JButton changeButton = new JButton("Velg farge");
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Åpner fargevelger-dialogen
                Color chosenColor = JColorChooser.showDialog(
                        PaletteChooserApp.this, 
                        "Velg farge for linje " + (index + 1), 
                        currentColors[index]
                );
                
                // Hvis brukeren valgte en farge (og ikke trykket avbryt)
                if (chosenColor != null) {
                    updateLine(index, chosenColor);
                }
            }
        });

        // Setter sammen komponentene på linjen
        line.add(label, BorderLayout.WEST);
        line.add(colorBox, BorderLayout.CENTER);
        line.add(changeButton, BorderLayout.EAST);

        return line;
    }

    // Oppdaterer en spesifikk linje med ny farge
    private void updateLine(int index, Color color) {
        currentColors[index] = color;
        colorPanels[index].setBackground(color);
        colorLabels[index].setForeground(color);
        colorLabels[index].setText("Linje " + (index + 1) + " (" + getHexColor(color) + ")");
    }

    // Nullstiller hele paletten tilbake til standardfargene
    private void resetPalette() {
        for (int i = 0; i < 9; i++) {
            updateLine(i, defaultColors[i]);
        }
    }

    // Hjelpemetode for å gjøre om Color til #RRGGBB tekst
    private String getHexColor(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    // Start programmet
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PaletteChooserApp().setVisible(true);
            }
        });
    }
}
