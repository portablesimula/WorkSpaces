package simula.editor;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import simula.compiler.utilities.Global;
import simula.editor.TESTING.ColorPaletteExample;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/// @author Google AI
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public class WelcomePanel extends JPanel {
	JButton openFileButton, newFileButton, selectWorkspaceButton;
    
    // Definer farger
//    Color normalColor = new Color(63, 81, 181); // Elegant blå
//    Color hoverColor = new Color(48, 63, 159);  // Mørkere blå for hover
//    Color textColor = Color.WHITE;
    Color normalColor = Color.LIGHT_GRAY;
    Color hoverColor = Color.GRAY;
    Color textColor = Color.BLACK;
//  mainPanel.setBackground(new Color(245, 246, 248)); // Lys, moderne bakgrunn
    Font textFont = new Font("Segoe UI", Font.BOLD, 14);
	
    public WelcomePanel() {
        this.setBackground(Color.WHITE);
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try { imageLabel.setIcon(Global.simulaIcon);
        } catch (Exception e) {
            imageLabel.setText("Welcome to Simula IDE");
            imageLabel.setPreferredSize(new Dimension(150, 150));
        }
        
        // Opprett tekstområde
        JTextArea textArea = new JTextArea();
        textArea.setText("This is a Simula System created by the Open Source Project 'Portable Simula Revisited'.\n"
        		+ "The project was initiated as a response to the lecture held by James Gosling at the 50th\n"
        		+ "anniversary of Simula at Ifi, University of Oslo (UiO) on 27th September, 2017.\n\n"
        		+ "This Simula System is written in pure Java and compiles directly to executable .jar\n"
        		+ "files using the new Java Classfile API.\n\n"
        		+ "What will you like to do:");
        textArea.setFont(textFont);
        
        JTextArea dropArea = new JTextArea("\n                     Drop any .sim file here ...\n\n");
        dropArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        dropArea.setFont(textFont);
        dropArea.setTransferHandler(new FilePathDropTarget());

        openFileButton = createModernButton("Select a .sim file from the current WorkSpace");
        newFileButton = createModernButton("Write a new .sim file");
        selectWorkspaceButton = createModernButton("Select new current WorkSpace");

        mainPanel.add(imageLabel);     mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(textArea);       mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(dropArea);       mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(openFileButton); mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(newFileButton);  mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(selectWorkspaceButton);
        
        add(mainPanel);
    }

    // Metode for å lage en ren, moderne knapp med rollover-effekt
    private JButton createModernButton(String text) {
        JButton button = new JButton(text);

        // Styling av knappen
        button.setFont(textFont);
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
                
                if (source == openFileButton) {
                    System.out.println("openFileButton was clicked");
                    SwingUtilities.invokeLater(() -> {
                        TabbedTextHandler.doOpenFileAction();
                    });
                } else if (source == newFileButton) {
                    System.out.println("newFileButton was clicked");
                    SwingUtilities.invokeLater(() -> {
                        TabbedTextHandler.doNewFileAction();
                    });
                } else if (source == selectWorkspaceButton) {
                    System.out.println("selectWorkspaceButton was clicked");  
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
