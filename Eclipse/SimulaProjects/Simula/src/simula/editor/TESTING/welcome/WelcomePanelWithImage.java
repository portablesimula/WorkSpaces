package simula.editor.TESTING.welcome;

import javax.swing.*;

import simula.compiler.utilities.Global;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class WelcomePanelWithImage extends JFrame {

    public WelcomePanelWithImage() {
        setTitle("Moderne Velkomstpanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null); // Sentrerer vinduet

        // Hovedpanel med vertikal BoxLayout
        JPanel mainPanel = new JPanel();
//        mainPanel.setBackground(new Color(245, 246, 248)); // Lys, moderne bakgrunn
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 1. Bilde på toppen
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try { imageLabel.setIcon(Global.simulaIcon);
        } catch (Exception e) {
            imageLabel.setText("Welcome to Simula IDE");
            imageLabel.setPreferredSize(new Dimension(150, 150));
        }

        // 2. Avstand mellom bilde og knapper
        Component spacer = Box.createRigidArea(new Dimension(0, 40));

        // 3. Opprett tre moderne knapper
        JButton button1 = createModernButton("Kom i gang");
        JButton button2 = createModernButton("Innstillinger");
        JButton button3 = createModernButton("Avslutt");

        // Legg til komponenter i panelet
        mainPanel.add(imageLabel);
        mainPanel.add(spacer);
        mainPanel.add(button1);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Avstand mellom knappene
        mainPanel.add(button2);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(button3);

        add(mainPanel);
    }

    // Metode for å lage en ren, moderne knapp med rollover-effekt
    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        
        // Definer farger
        Color normalColor = new Color(63, 81, 181); // Elegant blå
        Color hoverColor = new Color(48, 63, 159);  // Mørkere blå for hover
        Color textColor = Color.WHITE;

        // Styling av knappen
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(normalColor);
        button.setForeground(textColor);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Full bredde, fast høyde
        
        // Fjern standard Java-styling
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Viser hånd-ikon ved hover

        // Rollover / Hover-effekt med MouseListener
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });

        return button;
    }

    public static void main(String[] args) {
    	Global.initiate();
        // Kjører GUI på Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new WelcomePanelWithImage().setVisible(true);
        });
    }
}
