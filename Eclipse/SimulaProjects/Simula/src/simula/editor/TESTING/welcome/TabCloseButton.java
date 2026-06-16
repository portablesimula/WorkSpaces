package simula.editor.TESTING.welcome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TabCloseButton extends JButton {
    public TabCloseButton(JTabbedPane pane, Component tabContent) {
        int size = 17;
        setPreferredSize(new Dimension(size, size));
        setToolTipText("Lukk denne fanen");
        
        // Gjør knappen gjennomsiktig og fjern standard ramme
        setContentAreaFilled(false);
        setFocusable(false);
        setBorder(BorderFactory.createEmptyBorder());
        setRolloverEnabled(true);

        // Håndter klikk for å fjerne fanen
        addActionListener(e -> {
            int i = pane.indexOfComponent(tabContent);
            if (i != -1) {
                pane.remove(i);
            }
        });
    }

    // Tegner "X"-symbolet og håndterer rollover-fargen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Aktiver antialiasing for penere linjer
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        // Endre farge basert på om musen er over knappen (Rollover)
        if (getModel().isRollover()) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(Color.GRAY);
        }

        // Tegn x-en sentrert i knappen
        int delta = 5;
        g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
        g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
        
        g2.dispose();
    }
}
