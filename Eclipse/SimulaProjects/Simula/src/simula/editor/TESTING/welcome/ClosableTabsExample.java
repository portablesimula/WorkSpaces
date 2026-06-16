package simula.editor.TESTING.welcome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClosableTabsExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Lukkbare Faner");
        JTabbedPane tabbedPane = new JTabbedPane();

        // Legg til noen test-faner
        addClosableTab(tabbedPane, "Fane 1", new JLabel("Innhold i fane 1"));
        addClosableTab(tabbedPane, "Fane 2", new JLabel("Innhold i fane 2"));

        frame.add(tabbedPane);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void addClosableTab(JTabbedPane tabbedPane, String title, Component content) {
        // 1. Legg til selve innholdskomponenten først
        tabbedPane.addTab(title, content);
        int index = tabbedPane.getTabCount() - 1;

        // 2. Opprett et tilpasset panel for fane-overskriften
        JPanel tabHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        tabHeader.setOpaque(false); // Gjør panelet gjennomsiktig

        // 3. Tittel-tekst
        JLabel label = new JLabel(title);
        
        // 4. Lukkeknapp (X)
        JButton closeButton = new JButton("X");
        closeButton.setMargin(new Insets(0, 4, 0, 4));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusable(false);

        // 5. Handling når du klikker på X
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Finn gjeldende indeks dynamisk siden faner kan flytte seg
                int i = tabbedPane.indexOfComponent(content);
                if (i != -1) {
                    tabbedPane.remove(i);
                }
            }
        });

        // 6. Sett sammen panelet og overstyr standard fane-komponent
        tabHeader.add(label);
        tabHeader.add(closeButton);
        tabbedPane.setTabComponentAt(index, tabHeader);
    }
}
