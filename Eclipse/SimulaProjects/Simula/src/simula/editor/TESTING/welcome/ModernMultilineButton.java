package simula.editor.TESTING.welcome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernMultilineButton extends JButton {
    private Color backgroundColor = new Color(0x3B, 0x82, 0xF6); // Modern Blue
    private Color hoverColor = new Color(0x25, 0x63, 0xEB);       // Darker Blue
    private Color activeColor = new Color(0x1D, 0x4E, 0xD8);      // Clicked Blue
    private int cornerRadius = 12;

    public ModernMultilineButton(String title, String subtitle) {
        // Use HTML to handle multiline layout and formatting natively
        String htmlText = "<html><div style='text-align: center; font-family: sans-serif; color: white;'>"
                        + "<b style='font-size: 14px;'>" + title + "</b><br>"
                        + "<span style='font-size: 11px; opacity: 0.8;'>" + subtitle + "</span>"
                        + "</div></html>";
        
        setText(htmlText);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Full bredde, fast høyde
        
        // Reset defaults to allow custom background painting
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add mouse tracking to repaint the hover states
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { repaint(); }
            @Override public void mouseExited(MouseEvent e) { repaint(); }
            @Override public void mousePressed(MouseEvent e) { repaint(); }
            @Override public void mouseReleased(MouseEvent e) { repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determine current button state color
        ButtonModel model = getModel();
        if (model.isPressed()) {
            g2.setColor(activeColor);
        } else if (model.isRollover()) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(backgroundColor);
        }

        // Fill modern rounded rectangle
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        g2.dispose();

        // Let Swing draw the multiline HTML text automatically over our background
        super.paintComponent(g);
    }
}
