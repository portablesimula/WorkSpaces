package simula.editor.TESTING;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class ThemeChooserEditor extends JFrame {

    // Standard VS Code-inspirert mørkt tema (9 linjer)
    private final Map<String, Color> defaultTheme = new HashMap<>() {{
        put("Activity Bar BG", new Color(51, 51, 51));
        put("Sidebar BG", new Color(37, 37, 38));
        put("Editor BG", new Color(30, 30, 30));
        put("Status Bar BG", new Color(0, 122, 204));
        put("Title Bar BG", new Color(60, 60, 60));
        put("Text Color", new Color(220, 220, 220));
        put("Accent Color", new Color(0, 122, 204));
        put("Selection BG", new Color(38, 79, 120));
        put("Line Highlight", new Color(46, 46, 46));
    }};

    // Aktivt tema som brukeren kan endre
    private final Map<String, Color> currentTheme = new HashMap<>(defaultTheme);
    private final Map<String, JPanel> colorPreviewPanels = new HashMap<>();

    public ThemeChooserEditor() {
        setTitle("Theme Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        // Hovedpanel med mørk VS Code-stil
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Overskrift og Reset-knapp (Topp-panel)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Theme Color Palette");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JButton resetButton = new JButton("Reset to Default");
        resetButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resetButton.setFocusPainted(false);
        resetButton.addActionListener((ActionEvent e) -> resetToDefault());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(resetButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2. Palett med 9 linjer (Senter-panel)
        JPanel palettePanel = new JPanel(new GridLayout(9, 1, 0, 8));
        palettePanel.setOpaque(false);

        // Opprett de 9 linjene dynamisk basert på mappet
        for (String key : defaultTheme.keySet()) {
            palettePanel.add(createColorRow(key));
        }

        mainPanel.add(palettePanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    // Oppretter én linje med Label, Fargeboks og Endre-knapp
    private JPanel createColorRow(String labelText) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);

        // Merkelapp (Label)
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(200, 200, 200));
        label.setPreferredSize(new Dimension(150, 30));

        // Fargevisning (Liten firkant som viser fargen)
        JPanel colorPreview = new JPanel();
        colorPreview.setPreferredSize(new Dimension(35, 25));
        colorPreview.setBackground(currentTheme.get(labelText));
        colorPreview.setBorder(BorderFactory.createLineBorder(new Color(85, 85, 85), 1));
        colorPreviewPanels.put(labelText, colorPreview);

        // Kombiner farge og tekst til venstre
        JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        leftSide.setOpaque(false);
        leftSide.add(colorPreview);
        leftSide.add(label);

        // Endre-knapp som åpner JColorChooser
        JButton editButton = new JButton("Edit");
        editButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        editButton.addActionListener((ActionEvent e) -> {
            Color chosenColor = JColorChooser.showDialog(
                    this,
                    "Choose Color for " + labelText,
                    currentTheme.get(labelText)
            );
            if (chosenColor != null) {
                currentTheme.put(labelText, chosenColor);
                colorPreview.setBackground(chosenColor);
                // Her kan du også kalle en metode for å oppdatere selve app-temaet i sanntid
            }
        });

        row.add(leftSide, BorderLayout.WEST);
        row.add(editButton, BorderLayout.EAST);

        return row;
    }

    // Nullstiller alle 9 fargelinjer tilbake til standard
    private void resetToDefault() {
        for (String key : defaultTheme.keySet()) {
            Color defaultColor = defaultTheme.get(key);
            currentTheme.put(key, defaultColor);
            colorPreviewPanels.get(key).setBackground(defaultColor);
        }
    }

    public static void main(String[] args) {
        // Bruk systemets Look and Feel for renere knapper
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new ThemeChooserEditor().setVisible(true);
        });
    }
}
