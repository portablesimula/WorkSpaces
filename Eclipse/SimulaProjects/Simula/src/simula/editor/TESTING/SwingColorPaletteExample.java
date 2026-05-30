package simula.editor.TESTING;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingColorPaletteExample {

    private JFrame frame;
    private JPanel displayPanel;

    public static void main(String[] args) {
        // Ensure UI updates run on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new SwingColorPaletteExample().createAndShowGUI();
        });
    }

    public void createAndShowGUI() {
        // 1. Setup the main application window
        frame = new JFrame("Dynamic Swing Color Palette Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout(10, 10));

        // 2. Create the display panel that changes color
        displayPanel = new JPanel();
        displayPanel.setBackground(Color.WHITE); // Default background
        displayPanel.setBorder(BorderFactory.createTitledBorder("Color Preview Area"));
        
        // Add a centered label inside the display panel
        JLabel label = new JLabel("Click a color from the palette below!");
        displayPanel.add(label);

        // 3. Create the Palette container panel
        JPanel palettePanel = new JPanel();
        // 2 rows, 6 columns grid for 12 color swatch buttons
        palettePanel.setLayout(new GridLayout(2, 6, 5, 5)); 
        palettePanel.setBorder(BorderFactory.createTitledBorder("Select a Palette Color"));

        // Define our custom palette layout colors
        Color[] paletteColors = {
            Color.RED, Color.GREEN, Color.BLUE, 
            Color.YELLOW, Color.CYAN, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.LIGHT_GRAY, 
            Color.DARK_GRAY, new Color(128, 0, 128), // Purple
            new Color(255, 127, 80)                  // Coral
        };

        // 4. Generate swatch buttons and attach listeners dynamically
        for (Color color : paletteColors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setOpaque(true);
            colorButton.setBorderPainted(false); // Clean flat swatch appearance

            // Define the update trigger action when clicking the button
            colorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Update the color of the target component
                    displayPanel.setBackground(color);
                    
                    // Force the panel to visually refresh with the new look
                    displayPanel.repaint(); 
                }
            });

            palettePanel.add(colorButton);
        }

        // 5. Layout the components inside the frame windows
        frame.add(displayPanel, BorderLayout.CENTER);
        frame.add(palettePanel, BorderLayout.SOUTH);

        // Center window placement on screen and display
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
