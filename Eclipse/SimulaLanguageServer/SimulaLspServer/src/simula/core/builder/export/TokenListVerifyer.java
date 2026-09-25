package simula.core.builder.export;

import java.util.List;
import java.util.stream.Collectors;

import simula.Comn;
import simula.Option;
import simula.core.utilities.Util;

/// Detailed Specification of deltaStart
/// 
/// - Definition: At index 5*i + 1, deltaStart represents the token's start character
///   offset relative to the start of the previous token.
///
/// - The Rule for Line Changes:
/// 
///   - If deltaLine is 0 (meaning the current token is on the same line as the previous token),
///     deltaStart is relative to the start character (column offset) of the previous token.
/// 
///   - If deltaLine is greater than 0 (meaning the current token is on a new line relative to
///     the previous token), deltaStart is relative to 0 (the absolute beginning/left margin of that new line).
/// 
/// - For the First Token (i = 0): deltaLine is relative to the start of the file/document (line 0),
///   and deltaStart is relative to column 0.
/// 
public class TokenListVerifyer {

	// ****************************************************************
	// *** reconstruct  -- SEE: LspTextPanel.fillTextPane
	// ****************************************************************
    public static void doVerify(String originalText, List<Integer> semanticTokens) {
    	
//    	Option.internal.TRACE_VERIFY_TOKEN = 1;
    	
    	List<String> sourceLines = originalText.lines().collect(Collectors.toList());
    	if(Option.internal.TRACE_VERIFY_TOKEN > 0) {
	    	int i = 1;
	    	for(String line:sourceLines) {
	    		Util.println("Line " + i++ + ": |" + Comn.printable(line) + '|');
	    	}
    	}
        StringBuilder reconstr = new StringBuilder();
        int sourcePos = 0;
        int lineNumber = 0;
        int prevTextLength = 0;

        if(Option.internal.TRACE_VERIFY_TOKEN > 0) Util.println("\nSemanticTextReconstructor.reconstruct: SOURCE:"+Comn.printable(originalText));
        int x = 0;
		while(x < semanticTokens.size()) {
            int deltaLine = semanticTokens.get(x++);
            int deltaStartChar = semanticTokens.get(x++);
            int length = semanticTokens.get(x++);
            @SuppressWarnings("unused")
			int tokenTypeIndex = semanticTokens.get(x++);
            @SuppressWarnings("unused")
			int tokenModifiersBitmask = semanticTokens.get(x++);
          
            if(Option.internal.TRACE_VERIFY_TOKEN > 0) {
	            Util.println("SemanticTextReconstructor.reconstruct: LOOP START: semToken: deltaLine="+deltaLine + ", deltaStartChar="+deltaStartChar+", length="+length);
            }
            
            // 1. Calculate absolute positions based on LSP delta rules
            if (deltaLine > 0) {
    			checkEqual("case 1", lineNumber, sourceLines.get(lineNumber++), reconstr.toString());
        		// Start NEWLINE
            	// meaning the current token is on a new line relative to the previous token),
            	// deltaStart is relative to 0 (the absolute beginning/left margin of that new line).
            	sourcePos = 0;
           	    reconstr = new StringBuilder();
            	while((deltaLine--) > 1) {
            		// Empty line
        			checkEqual("case 2", lineNumber, sourceLines.get(lineNumber++), "");
            	}
                prevTextLength = 0;
            }

            // 3. Pad missing characters on the current line
            int gap = deltaStartChar - prevTextLength;
            if(gap != 0) {
        		reconstr.append(" ".repeat(gap));
        		sourcePos += gap;
                if(Option.internal.TRACE_VERIFY_TOKEN > 0) Util.println("LINE " + lineNumber + ": PAD SPACE: gap = " + gap + " ==> LINE|" + reconstr + '|');
            }

            // 4. Insert the token text
            if(length > 0) {
	            String originalLine = sourceLines.get(lineNumber);
	            String tokenText = originalLine.substring(sourcePos, sourcePos + length);
	            reconstr.append(tokenText);
	            if(Option.internal.TRACE_VERIFY_TOKEN > 0) Util.println("LINE " + lineNumber + ": APPEND TEXT: length = " + length + ", TEXT|" + tokenText + "| ==> LINE|" + reconstr + '|');
	            sourcePos += length;
            }
        	prevTextLength = length;
		}
//		Util.println("sourceLines.size="+sourceLines.size()+", lineNumber="+lineNumber);
		while(lineNumber < sourceLines.size()) {
			checkEqual("case 3", lineNumber, sourceLines.get(lineNumber++), reconstr.toString());
		}
//    	Util.STOP();
    }
    
    private static void checkEqual(String debugName, int lineNumber, String original, String reconstr) {
        String originalLine = original.stripTrailing();
        String reconstrLine = reconstr.stripTrailing();
        if(Option.internal.TRACE_VERIFY_TOKEN > 0) {
        	Util.println("LINE " + lineNumber + ": RECONSTR: |" + Comn.printable(reconstrLine) + '|');
        	Util.println("LINE " + lineNumber + ": ORIGINAL: |" + Comn.printable(originalLine) + '|');
        }
        if(! reconstrLine.equals(originalLine)) {
        	System.err.println("SimulaBuilder: VERIFIER FAILED(" + debugName + "): Reconstructed text differ from original text on line " + lineNumber);
        	int lng1 = original.length();
        	int lng2 = reconstr.length();
        	System.err.println("Original Text(lng:"+lng1+"): |" + Comn.printable(original) + '|');
        	System.err.println("Reconstr Text(lng:"+lng2+"): |" + Comn.printable(reconstr) + '|');
        	System.exit(-1);
        }
    }

}
