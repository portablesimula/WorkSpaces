package simula.editor.TESTING;

import javax.swing.*;
import java.awt.*;

public class CardLayoutExample extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    public CardLayoutExample() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Create individual views
        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Screen One"));
        
        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("Screen Two"));

        // Add views to the manager with a unique identifier string
        mainContainer.add(panel1, "Screen1");
        mainContainer.add(panel2, "Screen2");

        add(mainContainer, BorderLayout.CENTER);

        // Setup a button to switch between them
        JButton toggleButton = new JButton("Next Screen");
        add(toggleButton, BorderLayout.SOUTH);

        toggleButton.addActionListener(e -> {
            // Flip to the specific panel instantly
            cardLayout.show(mainContainer, "Screen2"); 
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CardLayoutExample().setVisible(true));
    }
}
