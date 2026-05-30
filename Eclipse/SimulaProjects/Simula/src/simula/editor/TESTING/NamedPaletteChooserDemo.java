package simula.editor.TESTING;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class NamedPaletteChooserDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("NamedPaletteChooserDemo: Custom 9 Named Colors Palette");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(650, 450);
            frame.setLocationRelativeTo(null);

            // Create standard JColorChooser
            JColorChooser colorChooser = new JColorChooser();

            // Inject our custom 9-color palette panel
            AbstractColorChooserPanel customPanel = new NamedColorPalettePanel();
            colorChooser.setChooserPanels(new AbstractColorChooserPanel[]{ customPanel });

            // Optional: Create a label to track live color selections
            JLabel colorTrackerLabel = new JLabel("Selected Color Display", SwingConstants.CENTER);
            colorTrackerLabel.setOpaque(true);
            colorTrackerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            colorTrackerLabel.setPreferredSize(new Dimension(frame.getWidth(), 60));

            // Sync the track label with selection changes
            colorChooser.getSelectionModel().addChangeListener(e -> {
                Color selectedColor = colorChooser.getColor();
                colorTrackerLabel.setBackground(selectedColor);
                
                // Keep text readable depending on darkness
                int brightness = (selectedColor.getRed() * 299 + selectedColor.getGreen() * 587 + selectedColor.getBlue() * 114) / 1000;
                colorTrackerLabel.setForeground(brightness < 128 ? Color.WHITE : Color.BLACK);
            });

            frame.add(colorChooser, BorderLayout.CENTER);
            frame.add(colorTrackerLabel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }
}

/**
 * Custom color chooser panel containing 9 explicitly named swatches.
 */
class NamedColorPalettePanel extends AbstractColorChooserPanel {
    private final Map<String, Color> namedColors = new LinkedHashMap<>();
    private final JPanelGridContainer gridPanel = new JPanelGridContainer();

    public NamedColorPalettePanel() {
        // Initialize our fixed collection of 9 named colors
        namedColors.put("Crimson Red", new Color(220, 20, 60));
        namedColors.put("Deep Orange", new Color(255, 140, 0));
        namedColors.put("Amber Yellow", new Color(255, 191, 0));
        namedColors.put("Emerald Green", new Color(8, 143, 143));
        namedColors.put("Cobalt Blue", new Color(0, 71, 171));
        namedColors.put("Amethyst Purple", new Color(153, 102, 204));
        namedColors.put("Hot Pink", new Color(255, 105, 180));
        namedColors.put("Chocolate Brown", new Color(123, 63, 0));
        namedColors.put("Slate Charcoal", new Color(54, 69, 79));
    }

    @Override
    protected void buildChooser() {
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new GridLayout(3, 3, 8, 8));
        container.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create individual interactive cells
        for (Map.Entry<String, Color> entry : namedColors.entrySet()) {
            ColorSwatchCell cell = new ColorSwatchCell(entry.getKey(), entry.getValue());
            container.add(cell);
        }
        add(container, BorderLayout.CENTER);
    }

    @Override
    public void updateChooser() {
        // Automatically invoked by Swing when the JColorChooser's active selection shifts
        Color currentSelected = getColorFromModel();
        
        // Loop through cells and visually flag the selected index
        Container container = (Container) getComponent(0);
        for (Component comp : container.getComponents()) {
            if (comp instanceof ColorSwatchCell) {
                ColorSwatchCell cell = (ColorSwatchCell) comp;
                cell.setSelectedState(cell.getSwatchColor().equals(currentSelected));
            }
        }
    }

    @Override
    public String getDisplayName() {
        return "Named 9-Palette"; // The label displayed on the tab header
    }

    @Override
    public Icon getSmallDisplayIcon() { return null; }

    @Override
    public Icon getLargeDisplayIcon() { return null; }

    /**
     * Inner helper class defining individual clickable grid swatches.
     */
    private class ColorSwatchCell extends JPanel {
        private final String colorName;
        private final Color swatchColor;
        private final JLabel nameLabel;

        public ColorSwatchCell(String name, Color color) {
            this.colorName = name;
            this.swatchColor = color;
            
            setLayout(new BorderLayout());
            setBackground(color);
            setOpaque(true);
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            nameLabel = new JLabel(name, SwingConstants.CENTER);
            nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            
            // Adjust label text color for baseline contrast mapping
            int luminance = (color.getRed() * 299 + color.getGreen() * 587 + color.getBlue() * 114) / 1000;
            nameLabel.setForeground(luminance < 128 ? Color.WHITE : Color.BLACK);
            add(nameLabel, BorderLayout.CENTER);

            // Handle user clicks to pass selection back up to the model
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    getColorSelectionModel().setSelectedColor(swatchColor);
                }
            });
        }

        public Color getSwatchColor() { return swatchColor; }

        public void setSelectedState(boolean isSelected) {
            if (isSelected) {
                setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
                nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            } else {
                setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            }
            repaint();
        }
    }
    
    // Dummy class just to support compile integrity inside single wrapper
    private static class JPanelGridContainer extends JPanel {}
}
