package simula.psi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import simula.compiler.SourceModule;
import simula.compiler.utilities.Util;

public class DiagnosticHandler {
//	public List<LexToken> tokenList;
	private SourceModule sourceModule;
	public List<Integer> tokens;
	
	public DiagnosticHandler(SourceModule sourceModule, List<Integer> tokens) {
		this.sourceModule = sourceModule;
		this.tokens = tokens;
	}
		
	public String getText() {
		// TODO Auto-generated method stub
		Util.IERR("");
		return sourceModule.getUpdatedText();
	}

//	
//	public SimpleAttributeSet getTooltipAttrs(int lineNumber, Set<String> errorLines) {
//	    	
////	    	if(errorLines != null || getErrors() != null) {
////	    		IO.println("\n\nPsiTextPanel.getTooltipText: BEGIN: errorLines: "+errorLines);
////	    		IO.println("PsiTextPanel.getTooltipText: BEGIN: lexErrors: "+getErrors());
////	    	}
//	    	errorLines = accumErrors(lineNumber, errorLines);
//	    	
//	    	if(errorLines == null) return null;
////	    	IO.println("PsiTextPanel.getTooltipText: RENDER: errorLines: "+errorLines);
//	    	
//	    	String tooltipText = null;
//	    	if(errorLines.size() == 1) {
////	    		tooltipText = errorLines.firstElement();
//	    		for(String msg:errorLines) {
//	    			tooltipText = msg;
//	    		}    		
//	    	} else {
//	    		String res = "<html>Multiple markers on this line:<ul>";
//	    		for(String msg:errorLines) {
//	    			res = res + "<li>" + msg + "</li>";
//	    		}
////	        	IO.println("PsiTextPanel.getTooltipText: RESULT: "+res);
//	        	tooltipText = res + "</ul>";
//	    	}
//
//			SimpleAttributeSet attrs = new SimpleAttributeSet();
//	        StyleConstants.setFontFamily(attrs, "Courier New");
//			StyleConstants.setForeground(attrs, Palette.ErrorForeground);
//			StyleConstants.setBackground(attrs, Palette.ErrorBackground);
//	        StyleConstants.setBold(attrs, true);
//			attrs.addAttribute("tooltip", tooltipText);
//	    	return attrs;
//	    }


	    public static Set<String> accumErrors(SourceModule sourceModule, int lineNumber, Set<String> errorLines) {
	    	Set<String> errors = getErrors(lineNumber);
	    	if(errors != null) {
	    		if(errorLines == null) errorLines = new HashSet<String>();
	    		errorLines.addAll(errors);
	    	}
	    	return errorLines;
	    }

//	// ****************************************************************
//	// *** Utilities
//	// ****************************************************************
//	/// Utility: Edit right justified line number string.
//	/// 
//	/// @param n the length of line number field
//	/// @return the resulting line number string
//    private String edLineNumber(int n) {
//	    String fill="";
//	    if(n<10) fill="   ";
//	    else if(n<100) fill="  ";
//	    else if(n<1000) fill=" ";
//    	return(fill+n+": \n");
//    }
//
}
