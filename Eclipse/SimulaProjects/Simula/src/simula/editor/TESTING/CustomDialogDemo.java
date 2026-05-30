package simula.editor.TESTING;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomDialogDemo extends JDialog {

    private JComboBox<String> selectBox;
    private JPanel palettePanel;
    private JPanel demoPanel;
    private JButton resetButton;

    public CustomDialogDemo(JFrame parent) {
        super(parent, "Custom Configuration Dialog", true);
        initializeUI();
    }

    private void initializeUI() {
        setSize(500, 400);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // 1. Control Panel (Top)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectBox = new JComboBox<>(new String[]{"Option A", "Option B", "Option C"});
        controlPanel.add(new JLabel("Select Mode:"));
        controlPanel.add(selectBox);
        add(controlPanel, BorderLayout.NORTH);

        // 2. Center Panel (Splits Palette and Demo)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Palette Panel (Left side of center)
        palettePanel = new JPanel();
        palettePanel.setBorder(BorderFactory.createTitledBorder("Palette"));
        palettePanel.setBackground(Color.LIGHT_GRAY);
        // Add color buttons or tools here
        palettePanel.add(new JButton("Red"));
        palettePanel.add(new JButton("Blue"));

        // Demo Panel (Right side of center)
        demoPanel = new JPanel();
        demoPanel.setBorder(BorderFactory.createTitledBorder("Demo Canvas"));
        demoPanel.setBackground(Color.WHITE);

        centerPanel.add(palettePanel);
        centerPanel.add(demoPanel);
        add(centerPanel, BorderLayout.CENTER);

        // 3. Action Panel (Bottom)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resetButton = new JButton("Reset");
        actionPanel.add(resetButton);
        add(actionPanel, BorderLayout.SOUTH);

        // Setup Reset Logic
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetComponents();
            }
        });
    }

    private void resetComponents() {
        selectBox.setSelectedIndex(0);
        palettePanel.setBackground(Color.LIGHT_GRAY);
        demoPanel.setBackground(Color.WHITE);
        demoPanel.repaint();
    }

    public static void main(String[] args) {
        // Test launcher
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(200, 200);
            frame.setVisible(true);

            CustomDialogDemo dialog = new CustomDialogDemo(frame);
            dialog.setVisible(true);
        });
    }
}
