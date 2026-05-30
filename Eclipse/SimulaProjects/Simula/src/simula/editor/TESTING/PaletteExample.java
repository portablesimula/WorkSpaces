package simula.editor.TESTING;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PaletteExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("PaletteExample: 9-Color Labeled Palette");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 3, 10, 10)); // 3x3 grid

        // Initialize 9 labeled colors
        for (int i = 1; i <= 9; i++) {
            frame.add(createColorButton("Color " + i, Color.LIGHT_GRAY));
        }

        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    private static JButton createColorButton(String label, Color initialColor) {
        JButton button = new JButton(label);
        button.setBackground(initialColor);
        button.setOpaque(true);
        button.setBorderPainted(false); // Helps background color visibility on some platforms

        button.addActionListener(e -> {
            // Open JColorChooser dialog
            Color newColor = JColorChooser.showDialog(null, "Update " + label, button.getBackground());
            if (newColor != null) {
                button.setBackground(newColor);
                // Dynamically update text color for readability (optional)
                button.setForeground(getContrastColor(newColor)); 
            }
        });
        return button;
    }

    // Helper to ensure text label remains visible on dark/light backgrounds
    private static Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }
}
