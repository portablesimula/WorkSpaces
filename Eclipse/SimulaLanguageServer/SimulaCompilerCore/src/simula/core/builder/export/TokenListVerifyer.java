package simula.core.builder.export;

import java.util.List;

import simula.core.utilities.Util;

public class TokenListVerifyer {
	private final static boolean TESTING = true;

	public static void verifyTokenList(String originalText, List<Integer> semanticTokens, List<LexToken> lexTokenList) {
		printLexTokenList("TokenListVerifyer.verifyTokenList: ", lexTokenList);
		if(TESTING) IO.println("\nTokenListVerifyer.verifyTokenList: " + lexTokenList.size());
//        String originalText = sourceModule.getOriginalText();
//		String originalText = sourceModule.getUpdatedText();
        int currentLine = 0;
        int currentStartChar = 0;
//        String documentLines = 
        int beginIndex = 0;
        StringBuilder sb = new StringBuilder();
		int x = 0;
		int lexTokenIndex = 0;
		while(x < semanticTokens.size()) {
            int deltaLine = semanticTokens.get(x++);
            int deltaStart = semanticTokens.get(x++);
            int length = semanticTokens.get(x++);
            int tokenTypeIndex = semanticTokens.get(x++);
            int tokenModifiersBitmask = semanticTokens.get(x++);

            // 1. Calculate absolute line location
            currentLine += deltaLine;

            // 2. Calculate absolute character index within that line
            if (deltaLine == 0) {
                currentStartChar += deltaStart;
            } else {
                currentStartChar = deltaStart;
            }
            beginIndex += deltaStart; 

            String NEWLINE_Chars ="\r\n";
            while((deltaLine--) > 0) {
            	sb.append(NEWLINE_Chars);
            	if(TESTING) IO.println("TokenListVerifyer.verifyTokenList: Append: " + Util.printable(NEWLINE_Chars) + " ==> " + Util.printable(""+sb));
            	beginIndex += NEWLINE_Chars.length();
            }
            
            String tokenText = originalText.substring(beginIndex, beginIndex + length);
            sb.append(tokenText);

            if(TESTING) {
	    		String str = (tokenText == null)? "UNKNOWN" :  Util.printable(tokenText);
	    		IO.println("Line " + currentLine + ": " + TokenManager.tokenTypes.get(tokenTypeIndex) + "[col:" + currentStartChar + ", lng:" + length + "] Text: \"" + str + '"');
            	IO.println("TokenListVerifyer.verifyTokenList: Append: " + Util.printable(tokenText) + " ==> " + Util.printable(""+sb));
            }
            
            
            
            // Test against original LexToken
            LexToken lexToken = lexTokenList.get(lexTokenIndex++);
            String lexTokenText = lexToken.getText();
            IO.println("TokenListVerifyer.verifyTokenList: LexToken: " + lexToken);
            IO.println("LexToken: text="+Util.printable(lexTokenText));
            if(! Util.printable(tokenText).equals(Util.printable(lexToken.getText()))) Util.IERR(" Bad text");
            if(beginIndex != lexToken.column) Util.IERR(" Bad beginIndex");
            if(length != lexToken.length) Util.IERR(" Bad Length");
            
            
//            Util.IERR("TEST MOT ORIGINAL LEX TOKEN");
		}
        String reconstructedText = sb.toString();
		if(! reconstructedText.equals(originalText)) {
			String reconstr = Util.printable(reconstructedText);
			String original = Util.printable(originalText);
			int lng1 = original.length();
			int lng2 = reconstr.length();
			System.err.println("SimulaBuilder: VERIFIER FAILED: Reconstructed text differ from original text");
			System.err.println("Original Text(lng:"+lng1+"): " + original);
			System.err.println("Reconstr Text(lng:"+lng2+"): " + reconstr);
			int n = Math.min(lng1, lng2);
			LOOP:for(int i=0;i<n;i++) {
				if(reconstr.charAt(i) != original.charAt(i)) { 
	    			System.err.println("First deviation at pos " + i + ", original: " + original.charAt(i) + ", reconstr: " + reconstr.charAt(i));
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
	}

	public static void printSemTokenList(String originalText, List<Integer> tokens) {
		
	}

	public static void printLexTokenList(String title, List<LexToken> lexTokenList) {
		IO.println("================ " + title +": LexTokenList ================");
		for(LexToken lexToken:lexTokenList) {
			IO.println(""+lexToken);
		}
	}

}
