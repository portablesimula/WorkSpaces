package simula.editor.TESTING;

import javax.swing.*;

import simula.editor.Palette;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class ThemePaletteChooser extends JFrame {

    // Standardfarger for paletten (9 linjer)
    private final Color[] DEFAULT_COLORS = {
        Color.RED, Color.ORANGE, Color.YELLOW, 
        Color.GREEN, Color.BLUE, Color.CYAN, 
        Color.MAGENTA, Color.PINK, Color.GRAY
    };

    private final String[] LABELS = {
        "Hovedfarge", "Sekundærfarge", "Bakgrunn", 
        "Tekstfarge", "Panelbakgrunn", "Rammer", 
        "Aksentfarge", "Aktiv Status", "Inaktiv Status"
    };

    // Temavalg
    private final String[] THEMES = {"Standard Tema", "Mørkt Tema", "Pastell Tema"};
    private JComboBox<String> themeDropdown;
    
    // Lagring av farger per tema
    private Map<String, Color[]> themeStorage;
    
    // UI-komponenter for fargelinjene
    private JPanel palettePanel;
    private JPanel[] colorPanels;
    private JLabel[] colorLabels;

    public ThemePaletteChooser() {
        setTitle("Tema- og Palettbygger");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initThemeData();
        initComponents();
        loadThemeColors((String) themeDropdown.getSelectedItem());
    }

    private void initThemeData() {
        themeStorage = new HashMap<>();
        
        // Standard tema
        themeStorage.put(THEMES[0], DEFAULT_COLORS.clone());
        
        // Mørkt tema pre-set
        themeStorage.put(THEMES[1], new Color[]{
            new Color(0x1A, 0x1A, 0x1A), new Color(0x33, 0x33, 0x33), new Color(0x4D, 0x4D, 0x4D),
            new Color(0xFF, 0xFF, 0xFF), new Color(0x22, 0x22, 0x22), new Color(0x55, 0x55, 0x55),
            new Color(0x00, 0xAD, 0xB5), new Color(0x39, 0x3E, 0x46), new Color(0x22, 0x28, 0x31)
        });

        // Pastell tema pre-set
        themeStorage.put(THEMES[2], new Color[]{
            new Color(0xFF, 0xB7, 0xB2), new Color(0xFF, 0xDA, 0xB9), new Color(0xE2, 0xF0, 0xCB),
            new Color(0xB5, 0xEA, 0xD7), new Color(0xC7, 0xCE, 0xEA), new Color(0xFF, 0x9A, 0x9E),
            new Color(0xFE, 0xCF, 0xD9), new Color(0xE8, 0xEA, 0xED), new Color(0xD3, 0xC5, 0xE3)
        });
    }

    private void initComponents() {
        // --- TOPPANEL (Overskrift og Temavelger) ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Konfigurer Fargetemaer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(titleLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        themeDropdown = new JComboBox<>(THEMES);
        themeDropdown.setMaximumSize(new Dimension(200, 30));
        themeDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        themeDropdown.addActionListener(e -> loadThemeColors((String) themeDropdown.getSelectedItem()));
        topPanel.add(themeDropdown);

        add(topPanel, BorderLayout.NORTH);

        // --- MIDTPANEL (9 Linjer med Label og Farge) ---
        palettePanel = new JPanel(new GridLayout(9, 1, 5, 5));
        palettePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        colorPanels = new JPanel[9];
        colorLabels = new JLabel[9];

        for (int i = 0; i < 9; i++) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            
            // Tekstforklaring
            colorLabels[i] = new JLabel(LABELS[i]);
            colorLabels[i].setFont(new Font("Arial", Font.PLAIN, 14));
            row.add(colorLabels[i], BorderLayout.WEST);

            // Fargevisning og knapp i ett
            colorPanels[i] = new JPanel();
            colorPanels[i].setPreferredSize(new Dimension(150, 30));
            colorPanels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            JButton chooseBtn = new JButton("Velg farge");
            final int index = i;
            chooseBtn.addActionListener(e -> openColorChooser(index));
            
            JPanel rightGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            rightGroup.add(colorPanels[i]);
            rightGroup.add(chooseBtn);
            
            row.add(rightGroup, BorderLayout.EAST);
            palettePanel.add(row);
        }
        add(palettePanel, BorderLayout.CENTER);

        // --- BUNNPANEL (Reset-knapp) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton resetBtn = new JButton("Nullstill gjeldende tema");
        resetBtn.addActionListener(e -> resetCurrentTheme());
        bottomPanel.add(resetBtn);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Oppdaterer visningen når du bytter tema i dropdownmenyen
    private void loadThemeColors(String themeName) {
        Color[] colors = themeStorage.get(themeName);
        for (int i = 0; i < 9; i++) {
            colorPanels[i].setBackground(colors[i]);
        }
        palettePanel.repaint();
    }

    // Åpner JColorChooser og lagrer den nye fargen i valgt tema
    private void openColorChooser(int index) {
        String currentTheme = (String) themeDropdown.getSelectedItem();
        Color currentColor = themeStorage.get(currentTheme)[index];
        
        Color newColor = JColorChooser.showDialog(this, "Velg farge for " + LABELS[index], currentColor);
        
        if (newColor != null) {
            themeStorage.get(currentTheme)[index] = newColor;
            colorPanels[index].setBackground(newColor);
        }
    }

    // Nullstiller valgt tema tilbake til fabrikkinnstillingene (DEFAULT_COLORS)
    private void resetCurrentTheme() {
        String currentTheme = (String) themeDropdown.getSelectedItem();
        themeStorage.put(currentTheme, DEFAULT_COLORS.clone());
        loadThemeColors(currentTheme);
        Palette.storeCurrentThemeProperties();
    }

    public static void main(String[] Array) {
        SwingUtilities.invokeLater(() -> {
            new ThemePaletteChooser().setVisible(true);
        });
    }
}
