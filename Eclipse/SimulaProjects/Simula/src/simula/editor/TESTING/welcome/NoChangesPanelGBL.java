package simula.editor.TESTING.welcome;

import javax.swing.*;
import java.awt.*;

public class NoChangesPanelGBL extends JPanel {
    public NoChangesPanelGBL() {
        // Set background color to mimic GitGub Desktop dark/light themes
        setBackground(new Color(248, 249, 250)); 
        setLayout(new GridBagLayout());

        // Create a central container to hold the content vertically
        JPanel centerContainer = new JPanel();
        centerContainer.setOpaque(false);
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));

        // 1. Icon (Using a placeholder label, replace with an actual ImageIcon)
        JLabel iconLabel = new JLabel("📦"); 
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Main Header text
        JLabel headerLabel = new JLabel("No local changes");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(new Color(36, 41, 47));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Description text
        JLabel descLabel = new JLabel("There are no uncommitted changes in this repository.");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(87, 96, 106));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with spacing (Glue/Struts)
        centerContainer.add(iconLabel);
        centerContainer.add(Box.createVerticalStrut(15));
        centerContainer.add(headerLabel);
        centerContainer.add(Box.createVerticalStrut(8));
        centerContainer.add(descLabel);

        // GridBagConstraint default behavior centers components perfectly
        add(centerContainer, new GridBagConstraints());
    }
}
