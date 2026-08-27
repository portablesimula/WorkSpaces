package simula.core;

import java.util.List;

import simula.Comn;
import simula.Option;
import simula.core.builder.export.LexToken;
import simula.core.utilities.KeyWord;
import simula.core.utilities.Util;

public class SemanticTextReconstructor {

	private static boolean TESTING = true;

	// ****************************************************************
	// *** reconstruct  -- SEE: LspTextPanel.fillTextPane
	// ****************************************************************
    public static String reconstruct(String originalText, List<Integer> semanticTokens, List<LexToken> lexTokenList) {
        StringBuilder result = new StringBuilder();
        int sourcePos = 0;
        int lineNumber = 0;
        int prevTextLength = 0;

 		IO.println("\nTokenListVerifyer.verifyTokenList: SOURCE:"+Comn.printable(originalText));
        int x = 0;
        int lexTokenIndex = 0;
		while(x < semanticTokens.size()) {
            LexToken lexToken = lexTokenList.get(lexTokenIndex++);
            int deltaLine = semanticTokens.get(x++);
            int deltaStartChar = semanticTokens.get(x++);
            int length = semanticTokens.get(x++);
            int tokenTypeIndex = semanticTokens.get(x++);
            @SuppressWarnings("unused")
			int tokenModifiersBitmask = semanticTokens.get(x++);
          
            if(TESTING) {
	            IO.println("\nTokenListVerifyer.verifyTokenList: LOOP START: lexToken:"+lexToken);
	            IO.println("TokenListVerifyer.verifyTokenList: LOOP START: semToken: deltaLine="+deltaLine + ", deltaStartChar="+deltaStartChar+", length="+length);
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
            		IO.println("APPEND tokenText|" + Comn.printable('\n') + "| ==> |" + Comn.printable(""+result) + '|');
            		if(TESTING && (!Option.TESTING_VERIFY)) {
	                    lineNumber++;            		
	                    Util.ASSERT(lexToken.keyWord == KeyWord.NEWLINE, "Not a NEWLINE Token: " + lexToken);
	                    String checkText = originalText.substring(sourcePos, sourcePos + 1);
	    				if(! checkText.equals('\n')) Util.IERR("Bad NEWLINE: " + Comn.printable(checkText) + ", lexToken=" + lexToken);
	                    lexToken = lexTokenList.get(lexTokenIndex++);
	                    IO.println("UPDATE NEWLINE lexToken: " + lexToken);
    				}
               	    sourcePos ++;
            	}
                prevTextLength = 0;
        		IO.println("\nStart NEWLINE: sourcePos="+sourcePos+", TAIL|"+Comn.printable(originalText.substring(sourcePos)));
            } else {
                // token.deltaLine == 0
        		// Fortsett på samme linje
        		// meaning the current token is on the same line as the previous token),
        		// deltaStart is relative to the start character (column offset) of the previous token.
        		//
        		// |  prev   token    | lexToken.column = 17, lastChar = 9
        		// |  ------>         | deltaStart = lexToken.column - lastChar = 17 - 9 = 8
        		IO.println("\nCONTINUE LINE: sourcePos="+sourcePos+", TAIL|"+Comn.printable(originalText.substring(sourcePos)));
            }

            // 3. Pad missing characters on the current line
            int gap = deltaStartChar - prevTextLength;
            if(gap != 0) {
        		IO.println("\nPAD SPACE Characters: gap = " + gap);  
        		while((gap--) > 0) {
                    result.append(" ");
            		IO.println("APPEND tokenText| | ==> |" + Comn.printable(""+result) + '|');
            	    sourcePos++;
            		IO.println("UPDATE LINE: sourcePos="+sourcePos+", TAIL|"+Comn.printable(originalText.substring(sourcePos)));
        		}
        		if(TESTING && (!Option.TESTING_VERIFY)) {
                    Util.ASSERT(lexToken.keyWord == KeyWord.WHITESPACES, "Not a WHITESPACES Token");
                    while(lexToken.keyWord == KeyWord.WHITESPACES) {
                    	lexToken = lexTokenList.get(lexTokenIndex++);
    	                IO.println("UPDATE SPACES lexToken: " + lexToken);
                    }
        		}
            }

            // 4. Insert the token text
    		IO.println("\nINSERT TEXT: length = " + length + ", TAIL|"+Comn.printable(originalText.substring(sourcePos)));
            String tokenText = originalText.substring(sourcePos, sourcePos + length);
            result.append(tokenText);
    		IO.println("APPEND tokenText|" + Comn.printable(tokenText) + "| ==> |" + Comn.printable(""+result) + '|');
            sourcePos += length;
			if(Option.LEX_VERIFY) {
        		IO.println("AFTER INSERT: sourcePos="+sourcePos+", TAIL|"+Comn.printable(originalText.substring(sourcePos)));
				String lexTokenText = lexToken.getText();
				if(! tokenText.equals(lexTokenText)) {
			    	IO.println("TokenListVerifyer.verifyTokenList: Original: |" + Comn.printable(originalText) + '|');
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
