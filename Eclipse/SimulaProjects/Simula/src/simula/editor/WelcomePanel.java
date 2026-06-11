package simula.editor;

import javax.swing.*;

import simula.compiler.utilities.Global;
import simula.compiler.utilities.Util;
import simula.editor.TESTING.welcome.WelcomePanelWithImage;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/// @author Google AI
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public class WelcomePanel extends JPanel {
	JButton button1, button2, button3;
	
    public WelcomePanel() {
//        setTitle("Moderne Velkomstpanel");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(400, 600);
//        setLocationRelativeTo(null); // Sentrerer vinduet

        // Hovedpanel med vertikal BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 246, 248)); // Lys, moderne bakgrunn
//        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

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
        button1 = createModernButton("Select a .sim file from the current WorkSpace");
        button2 = createModernButton("Write a new .sim file");
        button3 = createModernButton("Select new current WorkSpace");

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
//        Color normalColor = new Color(63, 81, 181); // Elegant blå
//        Color hoverColor = new Color(48, 63, 159);  // Mørkere blå for hover
//        Color textColor = Color.WHITE;
        Color normalColor = Color.LIGHT_GRAY;
        Color hoverColor = Color.GRAY;
        Color textColor = Color.BLACK;

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
            
            @Override
            public void mouseClicked(MouseEvent e) {
            	IO.println("mouseClicked: " + e.paramString());
            	// Find which button triggered the event
                Object source = e.getSource(); 
                
                if (source == button1) {
                    System.out.println("Button1 was clicked");
                    TabbedTextHandler.doOpenFileAction();
                } else if (source == button2) {
                    System.out.println("Button2 was clicked");  
                } else if (source == button3) {
                    System.out.println("Button3 was clicked");  
                    SwingUtilities.invokeLater(() -> {
                        SimulaEditor.doSelectWorkspace();
                    });
                }
//                Util.STOP();
            }
        });

        return button;
    }

}
