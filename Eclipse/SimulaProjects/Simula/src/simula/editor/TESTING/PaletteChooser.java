package simula.editor.TESTING;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class PaletteChooser extends JFrame {

    // Definerer de 9 linjene/fargekategoriene
    private final String[] LABELS = {
        "Bakgrunn", "Tekst Hoved", "Tekst Sekundær", 
        "Aksent Farge", "Borders", "Lenker", 
        "Suksess (Grønn)", "Advarsel (Gul)", "Feil (Rød)"
    };

    // Standard fargepalett (Default)
    private final Color[] DEFAULT_COLORS = {
        Color.WHITE, Color.BLACK, Color.DARK_GRAY,
        Color.BLUE, Color.LIGHT_GRAY, Color.CYAN,
        Color.GREEN, Color.ORANGE, Color.RED
    };

    // Aktuelle farger i bruk
    private final Map<String, Color> currentPalette = new HashMap<>();
    private final Map<String, JPanel> colorPreviews = new HashMap<>();
    
    // Eksempel på tre tema-paneler som skal oppdateres
    private JPanel lightThemePreview;
    private JPanel darkThemePreview;
    private JPanel highContrastPreview;

    public PaletteChooser() {
        setTitle("IntelliJ Style Palette Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        // Initialiser standardfarger
        resetToDefaults();

        // Hovedlayout: To kolonner (Venstre: Redigering, Høyre: Forhåndsvisning av temaer)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createEditorPanel());
        splitPane.setRightComponent(createThemesPreviewPanel());
        splitPane.setDividerLocation(450);

        add(splitPane);
        updateThemeComponents(); // Første visuelle oppdatering
    }

    // Oppretter venstre side: Overskrift, 9 linjer med farger, og Reset-knapp
    private JPanel createEditorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Overskrift
        JLabel headerLabel = new JLabel("Tema Fargepalett");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Reset-knapp (IntelliJ-style)
        JButton resetButton = new JButton("Reset til standard");
        resetButton.addActionListener((ActionEvent e) -> {
            resetToDefaults();
            updateThemeComponents();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerLabel, BorderLayout.WEST);
        topPanel.add(resetButton, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // 2. Palett-linjer (Grid med 9 rader)
        JPanel gridPanel = new JPanel(new GridLayout(9, 1, 5, 5));
        
        for (int i = 0; i < LABELS.length; i++) {
            String labelText = LABELS[i];
            
            JPanel row = new JPanel(new BorderLayout(10, 0));
            JLabel label = new JLabel(labelText);
            
            // Fargeindikator (Klikkbar boks)
            JPanel colorBox = new JPanel();
            colorBox.setPreferredSize(new Dimension(35, 20));
            colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            colorBox.setBackground(currentPalette.get(labelText));
            
            // Lagre referanse for å oppdatere den senere
            colorPreviews.put(labelText, colorBox);

            // Knappen som åpner JColorChooser
            JButton chooseBtn = new JButton("Velg...");
            chooseBtn.addActionListener(e -> {
                Color initialColor = currentPalette.get(labelText);
                Color selectedColor = JColorChooser.showDialog(this, "Velg farge for " + labelText, initialColor);
                
                if (selectedColor != null) {
                    currentPalette.put(labelText, selectedColor);
                    colorBox.setBackground(selectedColor);
                    updateThemeComponents(); // Oppdaterer alle temaer umiddelbart
                }
            });

            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            actionPanel.add(colorBox);
            actionPanel.add(chooseBtn);

            row.add(label, BorderLayout.WEST);
            row.add(actionPanel, BorderLayout.EAST);
            gridPanel.add(row);
        }

        panel.add(new JScrollPane(gridPanel), BorderLayout.CENTER);
        return panel;
    }

    // Oppretter høyre side: Viser hvordan de "flere temaene" endrer seg
    private JPanel createThemesPreviewPanel() {
        JPanel mainPreview = new JPanel(new GridLayout(3, 1, 10, 10));
        mainPreview.setBorder(BorderFactory.createTitledBorder("Live Tema Oppdatering"));

        lightThemePreview = new JPanel(new FlowLayout());
        darkThemePreview = new JPanel(new FlowLayout());
        highContrastPreview = new JPanel(new FlowLayout());

        mainPreview.add(createThemeCard("Lyst Tema Mockup", lightThemePreview));
        mainPreview.add(createThemeCard("Mørkt Tema Mockup", darkThemePreview));
        mainPreview.add(createThemeCard("Høykontrast Mockup", highContrastPreview));

        return mainPreview;
    }

    private JPanel createThemeCard(String title, JPanel contentPanel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder(title));
        card.add(contentPanel, BorderLayout.CENTER);
        return card;
    }

    // Nullstiller dataene til standardverdier
    private void resetToDefaults() {
        for (int i = 0; i < LABELS.length; i++) {
            currentPalette.put(LABELS[i], DEFAULT_COLORS[i]);
            if (colorPreviews.containsKey(LABELS[i])) {
                colorPreviews.get(LABELS[i]).setBackground(DEFAULT_COLORS[i]);
            }
        }
    }

    // Logikk som oversetter paletten til de ulike temaene
    private void updateThemeComponents() {
        Color bg = currentPalette.get("Bakgrunn");
        Color text = currentPalette.get("Tekst Hoved");
        Color accent = currentPalette.get("Aksent Farge");
        Color error = currentPalette.get("Feil (Rød)");

        // 1. Oppdater Lyst Tema (Bruker fargene direkte)
        lightThemePreview.setBackground(bg);
        lightThemePreview.removeAll();
        JLabel l1 = new JLabel("Normal Tekst"); l1.setForeground(text);
        JButton b1 = new JButton("Aksent Knapp"); b1.setBackground(accent);
        JLabel le1 = new JLabel("Error melding"); le1.setForeground(error);
        lightThemePreview.add(l1); lightThemePreview.add(b1); lightThemePreview.add(le1);

        // 2. Oppdater Mørkt Tema (Inverterer bakgrunn/tekst kunstig for å simulere et annet tema)
        darkThemePreview.setBackground(new Color(255 - bg.getRed(), 255 - bg.getGreen(), 255 - bg.getBlue()));
        darkThemePreview.removeAll();
        JLabel l2 = new JLabel("Invertert Tekst"); l2.setForeground(new Color(255 - text.getRed(), 255 - text.getGreen(), 255 - text.getBlue()));
        JButton b2 = new JButton("Aksent"); b2.setBackground(accent.darker());
        darkThemePreview.add(l2); darkThemePreview.add(b2);

        // 3. Oppdater Høykontrast (Bruker kun rå svart/hvitt modifisert av din valgte aksent)
        highContrastPreview.setBackground(Color.BLACK);
        highContrastPreview.removeAll();
        JLabel l3 = new JLabel("Høykontrast"); l3.setForeground(Color.WHITE);
        JButton b3 = new JButton("Aksent Border"); b3.setBorder(BorderFactory.createLineBorder(accent, 3));
        highContrastPreview.add(l3); highContrastPreview.add(b3);

        // Forny visningen i vinduet
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new PaletteChooser().setVisible(true);
        });
    }
}
