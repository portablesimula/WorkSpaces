package simula.editor.TESTING;

import javax.swing.text.Element;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class DocumentAnalyzer {

    public static void listElements(JTextPane textPane) {
        StyledDocument doc = textPane.getStyledDocument();
        Element root = doc.getDefaultRootElement();
        printElementTree(doc, root, 0);
    }

    public static void listElements(StyledDocument doc) {
        Element root = doc.getDefaultRootElement();
        printElementTree(doc, root, 0);
    }

    private static void printElementTree(StyledDocument doc, Element element, int depth) {
        // Create indentation for visual hierarchy
        String indent = "  ".repeat(depth);
        
        // Get element basic properties
        String name = element.getName();
        int start = element.getStartOffset();
        int end = element.getEndOffset();
        
//        System.out.printf("%sElement: [%s] Bounds: (%d, %d)%n", indent, name, start, end);
        IO.println(indent + "[" + name +"] Bounds: (" + start +", " + end +") Text: \""+ getElementText(doc, start, end) + '"');
        
        // Print element attributes if they exist
        AttributeSet attributes = element.getAttributes();
        if (attributes.getAttributeCount() > 0) {
            System.out.printf("%s  Attributes: %s%n", indent, attributes);
        }

        // If not a leaf, recurse into child elements
        if (!element.isLeaf()) {
            for (int i = 0; i < element.getElementCount(); i++) {
                printElementTree(doc, element.getElement(i), depth + 1);
            }
        }
    }
    
    private static String getElementText(StyledDocument doc, int start, int end) {
		try {
			return doc.getText(start, end - start).replace("\r", "\\r").replace("\n", "\\n");
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
    	return "NOT FOUND";
    }
}
