package simula.core.builder.export;

import java.util.List;

import simula.Comn;
import simula.Option;
import simula.core.SemanticTextReconstructor;
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

	public static void verifyTokenList(String originalText, List<Integer> semanticTokens, List<LexToken> lexTokenList) {
		if(Option.internal.TRACE_VERIFY_TOKEN > 0) {
			printLexTokenList("TokenListVerifyer.verifyTokenList: ", lexTokenList);
			IO.println("\nTokenListVerifyer.verifyTokenList:" + lexTokenList.size());
		}
		
        String reconstructedText = reconstruct(originalText, semanticTokens, lexTokenList);
		if(! reconstructedText.equals(originalText)) {
			String reconstr = Comn.printable(reconstructedText);
			String original = Comn.printable(originalText);
			int lng1 = original.length();
			int lng2 = reconstr.length();
			System.err.println("SimulaBuilder: VERIFIER FAILED: Reconstructed text differ from original text");
			System.err.println("Original Text(lng:"+lng1+"): " + original);
			System.err.println("Reconstr Text(lng:"+lng2+"): " + reconstr);
			int n = Math.min(lng1, lng2);
			LOOP:for(int i=0;i<n;i++) {
				if(reconstr.charAt(i) != original.charAt(i)) {
					int orgnal = original.charAt(i);
					int recstr = reconstr.charAt(i);
	    			System.err.println("First deviation at pos " + i + ", original: " + orgnal + ':' + Comn.printable((char)orgnal)
	    			                                                 + ", reconstr: " + recstr + ':' + Comn.printable((char)recstr));
					break LOOP;
				}
			}
			if(lng1 != lng2) {
				int pos = Math.max(0, n - 100);
    			System.err.println("Original Tail: " + original.substring(pos));
    			System.err.println("Reconstr Tail: " + reconstr.substring(pos));
			}
			Util.IERR("");

		}
		if(Option.internal.TRACE_VERIFY_TOKEN > 0) IO.println("TokenListVerifyer.verifyTokenList: OK - lexTokenList.size=" + lexTokenList.size());		
	}

//	public static void printSemTokenList(String originalText, List<Integer> tokens) {
//		
//	}

	public static void printLexTokenList(String title, List<LexToken> lexTokenList) {
		IO.println("================ " + title +": LexTokenList ================");
		for(LexToken lexToken:lexTokenList) {
			IO.println(""+lexToken);
		}
	}


	private static boolean TESTING = false;//true;

	// ****************************************************************
	// *** reconstruct  -- SEE: LspTextPanel.fillTextPane
	// ****************************************************************
    private static String reconstruct(String originalText, List<Integer> semanticTokens, List<LexToken> lexTokenList) {
        StringBuilder result = new StringBuilder();
        int sourcePos = 0;
        int lineNumber = 0;
        int prevTextLength = 0;

        if(Option.internal.TRACE_VERIFY_TOKEN > 0) IO.println("\nSemanticTextReconstructor.reconstruct: SOURCE:"+Comn.printable(originalText));
        int x = 0;
        int lexTokenIndex = 0;
		while(x < semanticTokens.size()) {
            LexToken lexToken = lexTokenList.get(lexTokenIndex++);
            int deltaLine = semanticTokens.get(x++);
            int deltaStartChar = semanticTokens.get(x++);
            int length = semanticTokens.get(x++);
            @SuppressWarnings("unused")
			int tokenTypeIndex = semanticTokens.get(x++);
            @SuppressWarnings("unused")
			int tokenModifiersBitmask = semanticTokens.get(x++);
          
            if(TESTING) {
	            IO.println("\nSemanticTextReconstructor.reconstruct: LOOP START: lexToken:"+lexToken);
	            IO.println("SemanticTextReconstructor.reconstruct: LOOP START: semToken: deltaLine="+deltaLine + ", deltaStartChar="+deltaStartChar+", length="+length);
            }
            
            // 1. Calculate absolute positions based on LSP delta rules
            if (deltaLine > 0) {
        		// Start NEWLINE
            	// meaning the current token is on a new line relative to the previous token),
            	// deltaStart is relative to 0 (the absolute beginning/left margin of that new line).
        		//
        		// |    token    | lexToken.column = 17, lastChar = 9
        		// |--->         | deltaStart = lexToken.column - lastChar = 17 - 9 = 8
            	while((deltaLine--) > 0) {
                    result.append('\n');
            		if(Option.internal.TRACE_VERIFY_TOKEN > 0) IO.println("APPEND tokenText|" + Comn.printable('\n') + "| ==> |" + Comn.printable(""+result) + '|');
//            		if(TESTING && (!Option.TESTING_VERIFY)) {
//	                    lineNumber++;            		
//	                    Util.ASSERT(lexToken.keyWord == KeyWord.NEWLINE, "Not a NEWLINE Token: " + lexToken);
//	                    String checkText = originalText.substring(sourcePos, sourcePos + 1);
//	    				if(! checkText.equals('\n')) Util.IERR("Bad NEWLINE: " + Comn.printable(checkText) + ", lexToken=" + lexToken);
//	                    lexToken = lexTokenList.get(lexTokenIndex++);
//	                    IO.println("UPDATE NEWLINE lexToken: " + lexToken);
//    				}
               	    sourcePos ++;
            	}
                prevTextLength = 0;
                if(TESTING) IO.println("\nStart NEWLINE: sourcePos="+sourcePos+", TAIL|"+Comn.printable(originalText.substring(sourcePos)));
            } else {
                // token.deltaLine == 0
        		// Fortsett på samme linje
        		// meaning the current token is on the same line as the previous token),
        		// deltaStart is relative to the start character (column offset) of the previous token.
        		//
        		// |  prev   token    | lexToken.column = 17, lastChar = 9
        		// |  ------>         | deltaStart = lexToken.column - lastChar = 17 - 9 = 8
            	if(TESTING) IO.println("\nCONTINUE LINE: sourcePos="+sourcePos+", TAIL|"+Comn.printable(originalText.substring(sourcePos)));
            }

            // 3. Pad missing characters on the current line
            int gap = deltaStartChar - prevTextLength;
            if(gap != 0) {
            	if(TESTING) IO.println("\nPAD SPACE Characters: gap = " + gap);  
        		while((gap--) > 0) {
                    result.append(" ");
                    if(Option.internal.TRACE_VERIFY_TOKEN > 0) IO.println("APPEND tokenText| | ==> |" + Comn.printable(""+result) + '|');
            	    sourcePos++;
            	    if(TESTING) IO.println("UPDATE LINE: sourcePos="+sourcePos+", TAIL|"+Comn.printable(originalText.substring(sourcePos)));
        		}
//        		if(TESTING && (!Option.TESTING_VERIFY)) {
//                    Util.ASSERT(lexToken.keyWord == KeyWord.WHITESPACES, "Not a WHITESPACES Token");
//                    while(lexToken.keyWord == KeyWord.WHITESPACES) {
//                    	lexToken = lexTokenList.get(lexTokenIndex++);
//                    	if(TESTING) IO.println("UPDATE SPACES lexToken: " + lexToken);
//                    }
//        		}
            }

            // 4. Insert the token text
            if(TESTING) IO.println("\nINSERT TEXT: length = " + length + ", TAIL|"+Comn.printable(originalText.substring(sourcePos)));
            String tokenText = originalText.substring(sourcePos, sourcePos + length);
            result.append(tokenText);
            if(Option.internal.TRACE_VERIFY_TOKEN > 0) IO.println("APPEND tokenText|" + Comn.printable(tokenText) + "| ==> |" + Comn.printable(""+result) + '|');
            sourcePos += length;
			if(Option.LEX_VERIFY) {
				if(TESTING) IO.println("AFTER INSERT: sourcePos="+sourcePos+", TAIL|"+Comn.printable(originalText.substring(sourcePos)));
				String lexTokenText = lexToken.getText();
				if(! tokenText.equals(lexTokenText)) {
			    	IO.println("SemanticTextReconstructor.reconstruct: Original: |" + Comn.printable(originalText) + '|');
					Util.IERR("Bad TEXT: " + Comn.printable(tokenText) + ", lexToken=" + lexToken);
				}
			}

            // 5. Update buffer character tracking
            // Note: If tokenText contains internal newlines, handle lineNumber updates here.
        	prevTextLength = length;
        }

        return result.toString();
    }


}
