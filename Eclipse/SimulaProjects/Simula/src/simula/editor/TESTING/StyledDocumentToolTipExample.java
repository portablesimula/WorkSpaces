package simula.editor.TESTING;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class StyledDocumentToolTipExample {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("StyledDocument ToolTip Eksempel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        // 1. Lag en JTextPane som overstyrer getToolTipText
        JTextPane textPane = new JTextPane() {
            @Override
            public String getToolTipText(MouseEvent event) {
                // Finn ut hvilken tegn-indeks i teksten musen peker på
                int pos = viewToModel2D(event.getPoint());
                
                if (pos >= 0) {
                    StyledDocument doc = getStyledDocument();
                    // Hent ut stil-egenskapene (attributes) for dette tegnet
                    AttributeSet attr = doc.getCharacterElement(pos).getAttributes();
                    
                    // Sjekk om vår egendefinerte "tooltip"-egenskap eksisterer
                    if (attr.isDefined("myToolTipText")) {
                        return (String) attr.getAttribute("myToolTipText");
                    }
                }
                return super.getToolTipText(event);
            }
        };

        // 2. Registrer komponenten hos ToolTipManager (viktig!)
        ToolTipManager.sharedInstance().registerComponent(textPane);
        textPane.setEditable(false);

        // 3. Bygg opp det formaterte dokumentet
        StyledDocument doc = textPane.getStyledDocument();
        
        try {
            // Standard tekststil
            SimpleAttributeSet normalStyle = new SimpleAttributeSet();
            doc.insertString(doc.getLength(), "Dette er en vanlig tekst. ", normalStyle);

            // Stil for ordet med ToolTip
            SimpleAttributeSet keywordStyle = new SimpleAttributeSet();
            StyleConstants.setForeground(keywordStyle, Color.BLUE);
            StyleConstants.setUnderline(keywordStyle, true);
            // Vi legger til en egendefinert nøkkel/verdi for verktøytipset
            keywordStyle.addAttribute("myToolTipText", "Dette er hjelpeteksten for nøkkelordet!");

            doc.insertString(doc.getLength(), "Hold musen her", keywordStyle);
            doc.insertString(doc.getLength(), " for å se et tooltip.", normalStyle);
            
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        frame.add(new JScrollPane(textPane), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
