package simula.editor.TESTING.welcome;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WelcomePanelExample extends JFrame {

    public WelcomePanelExample() {
        setTitle("Apache NetBeans Style Welcome Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Hovedpanel med mørk, moderne bakgrunn
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(43, 43, 43)); // Dark charcoal
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 1. TOPP/HERO SEKSJON (Tittel og Logo)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        mainPanel.add(createHeaderSection(), gbc);

        // 2. VENSTRE SEKSJON (Hurtigstart / Handlinger)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.4;
        gbc.weighty = 0.9;
        mainPanel.add(createActionSection(), gbc);

        // 3. HØYRE SEKSJON (Nylige prosjekter)
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.6;
        mainPanel.add(createRecentProjectsSection(), gbc);

        add(mainPanel);
    }

    // Lager topp-banneret
    private JPanel createHeaderSection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel("Apache NetBeans");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(240, 240, 240));

        JLabel subtitleLabel = new JLabel(" IDE 2026");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(150, 150, 150));

        panel.add(titleLabel);
        panel.add(subtitleLabel);
        return panel;
    }

    // Lager venstresiden med knapper
    private JPanel createActionSection() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel sectionTitle = new JLabel("Quick Start");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(180, 180, 180));
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(sectionTitle);

        // Legg til flate, moderne knapper
        panel.add(createModernButton(" New Project...", "Opprett et nytt prosjekt"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createModernButton(" Open Project...", "Åpne eksisterende prosjekt fra disk"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createModernButton(" Install Plugins", "Utvid funksjonaliteten til din IDE"));

        return panel;
    }

    // Lager høyresiden med nylige prosjekter
    private JPanel createRecentProjectsSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(50, 50, 50));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 65, 65), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel sectionTitle = new JLabel("Recent Projects");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(180, 180, 180));
        sectionTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(sectionTitle, BorderLayout.NORTH);

        // Enkel liste for å simulere prosjekthistorikk
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement(" MyEnterpriseApp (~/Projects/Java/MyEnterpriseApp)");
        listModel.addElement(" CustomerDataAPI (~/Desktop/CustomerDataAPI)");
        listModel.addElement(" SwingGuiDemo (~/NetBeansProjects/SwingGuiDemo)");

        JList<String> projectList = new JList<>(listModel);
        projectList.setBackground(new Color(50, 50, 50));
        projectList.setForeground(new Color(200, 200, 200));
        projectList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        projectList.setSelectionBackground(new Color(75, 110, 175));
        projectList.setSelectionForeground(Color.WHITE);
        projectList.setFixedCellHeight(35);

        JScrollPane scrollPane = new JScrollPane(projectList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Hjelpemetode for å lage en moderne "flat" knapp med hover-effekt
    private JButton createModernButton(String text, String toolTip) {
        JButton button = new JButton(text);
        button.setToolTipText(toolTip);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(new Color(220, 220, 220));
        button.setBackground(new Color(60, 63, 65));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover-effekt (Mus inn/ut)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(75, 110, 175)); // NetBeans-aktig blåfarge
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(60, 63, 65));
                button.setForeground(new Color(220, 220, 220));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        // Sikrer at Swing kjører på riktig tråd
        SwingUtilities.invokeLater(() -> {
            // Valgfritt: Bruk systemets Look and Feel for bedre skrifttyper
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            
            new WelcomePanelExample().setVisible(true);
        });
    }
}
