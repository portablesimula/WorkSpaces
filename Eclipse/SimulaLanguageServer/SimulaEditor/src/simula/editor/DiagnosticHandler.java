package simula.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import simula.compiler.SourceModule;
import simula.core.builder.export.LexPosition;
import simula.core.builder.export.LexRange;
import simula.core.builder.export.SimulaDiagnostic;

public class DiagnosticHandler {
//	private SourceModule sourceModule;
//	private List<SimulaDiagnostic> diagnostics;
	
	class DiagnosticSet {
		Set<SimulaDiagnostic> onLine = new HashSet<SimulaDiagnostic>();
		int line;
		public DiagnosticSet(int line) { this.line = line; }
		public void add(SimulaDiagnostic diag) { onLine.add(diag); }
	}
	
	Map<Integer, DiagnosticSet> lineMap = new HashMap<>();
	
	public DiagnosticHandler(SourceModule sourceModule, List<SimulaDiagnostic> diagnostics) {
//		this.sourceModule = sourceModule;
//		this.diagnostics = diagnostics;
		
		for(SimulaDiagnostic diag:diagnostics) {
			LexRange range = diag.range;
			int firstLine = range.getStart().getLine();
			int lastLine = range.getEnd().getLine();
			for(int line = firstLine; line <= lastLine; line++) {
				DiagnosticSet set = lineMap.get(line);
				if(set == null) {
					set = new DiagnosticSet(line);
					lineMap.put(line, set);
				}
				set.add(diag);
			}
		}
	}

	/// For a single semToken
	public SimpleAttributeSet getTokenHoverAttrs(int lineNumber, int column, int length) {
		List<String> errorLines = getErrors(lineNumber, column, length);
    	return getHoverAttrs(errorLines);
    }

	/// For a complete line
	public SimpleAttributeSet getLineHoverAttrs(int lineNumber) {
		List<String> errorLines = getErrors(lineNumber);
    	return getHoverAttrs(errorLines);
    }

	private List<String> getErrors(int line) {
		DiagnosticSet set = lineMap.get(line);
		if(set == null) return null;
		List<String> res = new ArrayList<String>();
		for(SimulaDiagnostic diag:set.onLine) {
			res.add(diag.mss);
		}
		return res;
	}

	private List<String> getErrors(int line, int column, int length) {
		DiagnosticSet set = lineMap.get(line);
		if(set == null) return null;
		List<String> res = new ArrayList<String>();
		int start1 = (line << 16) | column;
		int slutt1 = start1 + length;
		for(SimulaDiagnostic diag:set.onLine) {
			LexRange range = diag.range;
			LexPosition start = range.getStart();
			LexPosition end = range.getEnd();
			int start2 = (start.getLine() << 16) | start.getCharacter();
			int slutt2 = (end.getLine() << 16) | end.getCharacter();
			if(overlaps(start1, slutt1, start2, slutt2))
				res.add(diag.mss);
		}
		return res;
	}

	private static boolean overlaps(int start1, int slutt1, int start2, int slutt2) {
		//
		//     start1-----------slutt1
		//                   start2--------------slutt2
	    // Two intervals overlap if the first does not end before the second starts,
		// AND the second does not end before the first starts.
	    return start1 <= slutt2 && start2 <= slutt1;
	}
	
	private SimpleAttributeSet getHoverAttrs(List<String> errorLines) {
    	if(errorLines == null) return null;
//    	IO.println("PsiTextPanel.getTooltipText: RENDER: errorLines: "+errorLines);
    	
    	String tooltipText = null;
    	if(errorLines.size() == 1) {
//    		tooltipText = errorLines.firstElement();
    		for(String msg:errorLines) {
    			tooltipText = msg;
    		}    		
    	} else {
    		String res = "<html>Multiple markers on this line:<ul>";
    		for(String msg:errorLines) {
    			res = res + "<li>" + msg + "</li>";
    		}
//        	IO.println("PsiTextPanel.getTooltipText: RESULT: "+res);
        	tooltipText = res + "</ul>";
    	}

		SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs, "Courier New");
		StyleConstants.setForeground(attrs, Palette.ErrorForeground);
		StyleConstants.setBackground(attrs, Palette.ErrorBackground);
        StyleConstants.setBold(attrs, true);
		attrs.addAttribute("tooltip", tooltipText);
    	return attrs;
    }

}
