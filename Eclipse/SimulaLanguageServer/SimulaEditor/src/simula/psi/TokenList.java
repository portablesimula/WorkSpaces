package simula.psi;

import java.util.List;
import simula.compiler.SourceModule;
import simula.compiler.utilities.Util;
import simula.core.builder.export.TokenManager;

public class TokenList {
//	public List<LexToken> tokenList;
	private SourceModule sourceModule;
	public List<Integer> tokens;
	
	public TokenList(SourceModule sourceModule, List<Integer> tokens) {
		this.sourceModule = sourceModule;
		this.tokens = tokens;
	}
		
	public String getText() {
		// TODO Auto-generated method stub
		Util.IERR("");
		return sourceModule.getUpdatedText();
	}

//	/// NOTE: SEE: simula.editor.LspTextPanel
//	/// @throws IOException 
//	public void fillLineAndTextPanel(LspTextPanel lspTextPanel, StyledDocument lin, StyledDocument doc, Style styleLineNumber) throws IOException {
//      String originalText = sourceModule.getOriginalText();
////		String originalText = sourceModule.getUpdatedText();
//		Set<String> errorLines = null;
//
//		int currentLine = 0;
//		int currentStartChar = 0;
//		int beginIndex = 0;
//		int x = 0;
//		while(x < tokens.size()) {
//			int deltaLine = tokens.get(x++);
//			int deltaStart = tokens.get(x++);
//			int length = tokens.get(x++);
//			int tokenTypeIndex = tokens.get(x++);
//			int tokenModifiersBitmask = tokens.get(x++);
//
//			// 1. Calculate absolute line location
//			currentLine += deltaLine;
//
//			// 2. Calculate absolute character index within that line
//			if (deltaLine == 0) {
//				currentStartChar += deltaStart;
//			} else {
//				currentStartChar = deltaStart;
//			}
//			beginIndex += deltaStart; 
//			String tokenText = originalText.substring(beginIndex, beginIndex + length);
//			// Print tracking information
//			System.out.printf("Token at [Line %d, Char %d] (Len: %d, TypeId: %d:%s) -> \"%s\"%n", 
//					currentLine, currentStartChar, length, tokenTypeIndex, TokenManager.tokenTypes.get(tokenTypeIndex), tokenText);
//
//
//			try {
//			if (deltaLine == 0) {
//				String lineString = edLineNumber(currentLine+1);
//				// Should only be here AFTER a complete line is rendered
//				SimpleAttributeSet attrs = getTooltipAttrs(currentLine, errorLines);
//				if(attrs != null) {
//					lin.insertString(lin.getLength(),lineString, attrs);					    		
//					errorLines = null;
//				} else {
//					lin.insertString(lin.getLength(),lineString, styleLineNumber);
//				}
//			}
//
////			errorLines = lexToken.accumErrors(errorLines);
////			SimpleAttributeSet attrs = lexToken.getTooltipAttrs(null);
////			if(attrs != null) {
////				doc.insertString(doc.getLength(), tokenText, attrs);
////			} else {
//				doc.insertString(doc.getLength(), tokenText, lspTextPanel.getStyle(tokenTypeIndex));				    		
////			}
//
//			//          Util.IERR("");
//			} catch (BadLocationException ble) {
//				System.err.println("Couldn't insert text into text pane.");
//			}
//		}
//	}
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
//
//
//	    public Set<String> accumErrors(int lineNumber, Set<String> errorLines) {
//	    	Set<String> errors = null; // getErrors(lineNumber);
//	    	if(errors != null) {
//	    		if(errorLines == null) errorLines = new HashSet<String>();
//	    		errorLines.addAll(errors);
//	    	}
//	    	return errorLines;
//	    }
//
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
