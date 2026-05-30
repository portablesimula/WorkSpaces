package simula.editor.TESTING;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdatePaletteExample {

    private boolean isDarkMode = false;
    private JFrame frame;

    public void createAndShowGUI() {
        // Create the main window frame
        frame = new JFrame("Dynamic Palette Update Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new GridBagLayout());

        // Create standard Swing components to showcase the update
        JLabel label = new JLabel("Welcome to the Theme Palette App!");
        JButton toggleButton = new JButton("Toggle Dark Mode");
        JTextField textField = new JTextField("Type something here...", 15);
        JCheckBox checkBox = new JCheckBox("Accept terms");

        // Layout constraints setup
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components to the frame
        gbc.gridy = 0; frame.add(label, gbc);
        gbc.gridy = 1; frame.add(textField, gbc);
        gbc.gridy = 2; frame.add(checkBox, gbc);
        gbc.gridy = 3; frame.add(toggleButton, gbc);

        // Add action listener to update the color palette on click
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDarkMode = !isDarkMode;
                updatePalette(isDarkMode);
                
                // Toggle text button feedback
                toggleButton.setText(isDarkMode ? "Toggle Light Mode" : "Toggle Dark Mode");
            }
        });

        // Initialize with default light theme
        updatePalette(false);

        // Center and display the window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void updatePalette(boolean dark) {
        if (dark) {
            // Dark Mode Palette Configuration
            Color darkBg = new Color(45, 45, 45);
            Color darkSurface = new Color(60, 63, 65);
            Color lightFg = new Color(220, 220, 220);

            UIManager.put("Panel.background", new ColorUIResource(darkBg));
            UIManager.put("Label.foreground", new ColorUIResource(lightFg));
            UIManager.put("TextField.background", new ColorUIResource(darkSurface));
            UIManager.put("TextField.foreground", new ColorUIResource(lightFg));
            UIManager.put("CheckBox.background", new ColorUIResource(darkBg));
            UIManager.put("CheckBox.foreground", new ColorUIResource(lightFg));
            UIManager.put("Button.background", new ColorUIResource(darkSurface));
            UIManager.put("Button.foreground", new ColorUIResource(lightFg));
        } else {
            // Light Mode Palette Configuration (Standard defaults)
            Color lightBg = new Color(240, 240, 240);
            Color lightSurface = Color.WHITE;
            Color darkFg = Color.BLACK;

            UIManager.put("Panel.background", new ColorUIResource(lightBg));
            UIManager.put("Label.foreground", new ColorUIResource(darkFg));
            UIManager.put("TextField.background", new ColorUIResource(lightSurface));
            UIManager.put("TextField.foreground", new ColorUIResource(darkFg));
            UIManager.put("CheckBox.background", new ColorUIResource(lightBg));
            UIManager.put("CheckBox.foreground", new ColorUIResource(darkFg));
            UIManager.put("Button.background", new ColorUIResource(lightBg));
            UIManager.put("Button.foreground", new ColorUIResource(darkFg));
        }

        // CRITICAL: Propagate UIManager property changes down through the GUI tree
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public static void main(String[] args) {
        // Always run Swing applications on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new UpdatePaletteExample().createAndShowGUI());
    }
}
