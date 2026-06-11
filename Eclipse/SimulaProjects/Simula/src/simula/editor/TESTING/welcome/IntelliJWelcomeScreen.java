package simula.editor.TESTING.welcome;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class IntelliJWelcomeScreen extends JFrame {

    // Define IntelliJ-like Darcula Color Palette
    private static final Color BG_DARK = new Color(43, 45, 48);
    private static final Color SIDEBAR_BG = new Color(30, 31, 34);
    private static final Color TEXT_COLOR = new Color(223, 225, 229);
    private static final Color HOVER_COLOR = new Color(46, 50, 54);
    private static final Color ACCENT_BLUE = new Color(53, 116, 240);

    public IntelliJWelcomeScreen() {
        setTitle("Welcome to IntelliJ IDEA Clone");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);

        // 1. Create Sidebar
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // 2. Create Content Panel
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(200, 550));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // App Logo / Version Placeholder
        JLabel logo = new JLabel("IntelliJ Clone");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logo.setForeground(TEXT_COLOR);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Navigation Items
        sidebar.add(createSidebarButton("Projects", true));
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(createSidebarButton("Customize", false));
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(createSidebarButton("Plugins", false));

        return sidebar;
    }

    private JButton createSidebarButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 35));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setForeground(TEXT_COLOR);
        btn.setBackground(isActive ? HOVER_COLOR : SIDEBAR_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(isActive);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 10, 0, 0));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!isActive) btn.setBackground(HOVER_COLOR);
                btn.setContentAreaFilled(true);
            }
            public void mouseExited(MouseEvent e) {
                if (!isActive) btn.setContentAreaFilled(false);
            }
        });
        return btn;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Top Header inside Content Panel
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARK);

        JLabel title = new JLabel("Welcome to IntelliJ IDEA");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_COLOR);
        header.add(title, BorderLayout.WEST);

        // Action Buttons (New Project, Open)
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(BG_DARK);
        
        JButton btnNew = new JButton("New Project");
        btnNew.setBackground(ACCENT_BLUE);
        btnNew.setForeground(Color.WHITE);
        btnNew.setFocusPainted(false);
        
        JButton btnOpen = new JButton("Open");
        btnOpen.setBackground(HOVER_COLOR);
        btnOpen.setForeground(TEXT_COLOR);

        actions.add(btnNew);
        actions.add(btnOpen);
        header.add(actions, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        // Recent Projects Placeholder List
        JPanel projectsList = new JPanel();
        projectsList.setBackground(BG_DARK);
        projectsList.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, HOVER_COLOR));
        // Add your project item rows here...
        
        panel.add(projectsList, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IntelliJWelcomeScreen().setVisible(true));
    }
}
