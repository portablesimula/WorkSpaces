package simula.editor.TESTING.closeableTab;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

//2. Implementation / Usage Example
public class MainApp {
 public static void main(String[] args) {
     SwingUtilities.invokeLater(() -> {
         JFrame frame = new JFrame("Tab System");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(500, 300);

         JTabbedPane tabbedPane = new JTabbedPane();

         // Helper method to add custom tabs
         addCustomTab(tabbedPane, "Home Tab", new JLabel("Home Content", SwingConstants.CENTER));
         addCustomTab(tabbedPane, "Settings Tab", new JLabel("Settings Content", SwingConstants.CENTER));

         frame.add(tabbedPane);
         frame.setVisible(true);
     });
 }

 private static void addCustomTab(JTabbedPane pane, String title, Component content) {
     pane.addTab(null, content); // Add content first
     int index = pane.getTabCount() - 1;
     pane.setTabComponentAt(index, new ClosableTabPanel(title, pane, content)); // Set custom header
 }
}
