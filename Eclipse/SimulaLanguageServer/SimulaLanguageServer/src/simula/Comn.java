package simula;

import simula.core.utilities.Util;

public class Comn {

	
	/// Replaces invisible control character with visible text escapes.
	public static String printable(char c) {
		switch (c) {
			case '\t': return "\\t";
			case '\b': return "\\b";
			case '\n': return "\\n";
			case '\r': return "\\r";
			case '\f': return "\\f";
			case '\'': return "\\'";
			case '\"': return "\\\"";
			case '\\': return "\\\\";
			default:
				if (Character.isISOControl(c)) {
					// Formats as u-code (ex: \u0000)
					return String.format("\\u%04x", (int) c);
				} else return ""+c;
		}
	}
	
	/// Replaces invisible control characters with visible text escapes.
	public static String printable(String input) {
		if (input == null) return null;
		StringBuilder sb = new StringBuilder();
		for (char c : input.toCharArray()) {
			sb.append(printable(c));
		}
		return sb.toString();
	}
    
    public static String modifySourceCode(String sourceCode) {
    	// CRLF replaced by LF and Line trailing blanks removed
    	StringBuilder sb = new StringBuilder();
    	int nBlanks = 0;
    	for (int i = 0; i < sourceCode.length(); i++) {
    	    char c = sourceCode.charAt(i);
    	    switch(c) {
	    	    case '\r': break;
	    	    case '\n': nBlanks = 0; sb.append(c); break;
	    	    case '\t': // Fall through
	    	    default:
	        	    if(c != '\t' && Character.isWhitespace(c)) {
	        	    	nBlanks++;
	        	    } else {
		    	    	while((nBlanks--) > 0) sb.append(' ');
		    	             sb.append(c); nBlanks = 0;
	        	    }
    	    }
    	    
    	}
    	IO.println("Comn.modifySourceCode: Original: |" + Comn.printable(sourceCode) + '|');
    	IO.println("Comn.modifySourceCode: Modified: |" + Comn.printable(sb.toString()) + '|');

		return sb.toString();
	}

}
