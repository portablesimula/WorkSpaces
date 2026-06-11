package simula.editor.TESTING.welcome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class WelcomePanelDemo extends JFrame {

    public WelcomePanelDemo() {
        setTitle("Moderne Velkomstpanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Hovedpanel med hvit bakgrunn
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Bilde / Logo (Erstatt URL med din egen bildefil)
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        try {
            // Bruker et plassholderbilde fra nett, bytt til lokal fil ved behov
            URL imageUrl = new URL("https://picsum.photos");
            ImageIcon icon = new ImageIcon(imageUrl);
            imageLabel.setIcon(icon);
        } catch (Exception e) {
            imageLabel.setText("[ Bilde Kunne Ikke Lastes ]");
            imageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Strekker seg over 3 kolonner (én for hver knapp)
        mainPanel.add(imageLabel, gbc);

        // Velkomsttekst
        JLabel welcomeText = new JLabel("Velkommen tilbake!", JLabel.CENTER);
        welcomeText.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeText.setForeground(new Color(51, 51, 51));
        gbc.gridy = 1;
        mainPanel.add(welcomeText, gbc);

        // Tilbakestill bredde for knappene så de står side om side
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.33;

        // 2. Opprett tre moderne knapper med roll-over
        ModernButton btn1 = new ModernButton("Kom i gang");
        ModernButton btn2 = new ModernButton("Innstillinger");
        ModernButton btn3 = new ModernButton("Avslutt");

        // Legg til knapper i panelet
        gbc.gridx = 0;
        mainPanel.add(btn1, gbc);

        gbc.gridx = 1;
        mainPanel.add(btn2, gbc);

        gbc.gridx = 2;
        mainPanel.add(btn3, gbc);

        add(mainPanel);
    }

    // Egendefinert knappeklasse for moderne utseende og roll-over
    static class ModernButton extends JButton {
        private final Color normalColor = new Color(79, 70, 229); // Indigo blå
        private final Color hoverColor = new Color(67, 56, 202);  // Mørkere indigo
        private final Color pressedColor = new Color(49, 46, 129);

        public ModernButton(String text) {
            super(text);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setBackground(normalColor);
            
            // Fjerner standard Swing-styling
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Lytter for roll-over (hover) effekter
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(normalColor);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    setBackground(pressedColor);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    setBackground(hoverColor);
                }
            });
        }

        // Tegner knappen på nytt for å gi den runde hjørner og glatte kanter
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            // Aktiverer antialiasing (glatte kanter)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Tegner den avrundede bakgrunnen
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            
            g2.dispose();
            super.paintComponent(g); // Tegner teksten oppå
        }
    }

    public static void main(String[] args) {
        // Sikrer at Swing kjører på riktig tråd
        SwingUtilities.invokeLater(() -> {
            new WelcomePanelDemo().setVisible(true);
        });
    }
}
