package com.simula.client.editor;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;

public class SimulaTextHoverProvider implements ITextHover {

    /**
     * Determines the exact text region (offset and length) over which the hover will trigger.
     */
    @Override
    public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
        // Example: Trigger on a specific single character position, or look up a full word bound
        return new Region(offset, 0); 
    }

    /**
     * Returns the information string to display inside the popup banner.
     */
    @Override
    public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
        if (hoverRegion == null) {
            return null;
        }
        
        int offset = hoverRegion.getOffset();
        
        try {
            // Read document text context if needed
            String documentText = textViewer.getDocument().get();
            
            // Return custom dynamic documentation message
            return "Documentation info for text at offset: " + offset;
            
        } catch (Exception e) {
            return null;
        }
    }
}
