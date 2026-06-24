package simula.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/// @author Google AI
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
class ClosableTabPanel extends JPanel {
    private final JTabbedPane pane;
    private final Component content;

    public ClosableTabPanel(String title, JTabbedPane pane, Component content) {
        this.pane = pane;
        this.content = content;
        
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        // Add the tab title text
        JLabel label = new JLabel(title);
        add(label);

        // Create the close button
        JButton closeButton = new JButton("X");
        configureCloseButton(closeButton);
        add(closeButton);
    }

    private void configureCloseButton(JButton button) {
        button.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        button.setContentAreaFilled(false);
        button.setFocusable(false);
        
        // Tooltip effect
        button.setToolTipText("Close this tab");

        // Hover / Rollover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.RED);
                button.setContentAreaFilled(true);
                button.setBackground(new Color(240, 240, 240));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.BLACK);
                button.setContentAreaFilled(false);
            }
        });

        // Close action
        button.addActionListener(e -> {
            int index = pane.indexOfComponent(content);
            if (index != -1) {
                pane.remove(index);
            }
        });
    }
}
