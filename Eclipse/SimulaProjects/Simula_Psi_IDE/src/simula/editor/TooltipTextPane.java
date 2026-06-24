package simula.editor;

import javax.swing.JTextPane;
import javax.swing.JToolTip;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyledDocument;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

/// The Tooltip text Pane.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/editor/TooltipTextPane.java"><b>Source File</b></a>.
/// 
/// @author Google AI
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public class TooltipTextPane extends JTextPane {

	public TooltipTextPane() {
        // Required to register the component with the ToolTipManager
        setToolTipText(""); 
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        // Get the mouse pointer coordinate inside the text component
        int pos = viewToModel2D(e.getPoint());
        
        if (pos >= 0) {
            StyledDocument doc = getStyledDocument();
            // Get attributes of the character at the mouse position
            AttributeSet attr = doc.getCharacterElement(pos).getAttributes();
            
            // Check for your custom tooltip attribute
            if (attr.isDefined("tooltip")) {
                return (String) attr.getAttribute("tooltip");
            }
        }
//		return super.getToolTipText(e);
        return null;
    }
    
    @Override
    public JToolTip createToolTip() {
        JToolTip tip = super.createToolTip();
//        tip.setBackground(Color.YELLOW);
//        tip.setBackground(Color.WHITE);
//        tip.setForeground(Color.RED);
        tip.setBackground(Palette.HoverBackground);
        tip.setForeground(Palette.HoverForeground);
//        tip.setBorder(new LineBorder(Palette.HoverForeground));
//        tip.setFont(new Font("Courier New", Font.PLAIN, 14));
        tip.setFont(new Font("Courier New", Font.BOLD, 12));
        return tip;
    }
    
}
