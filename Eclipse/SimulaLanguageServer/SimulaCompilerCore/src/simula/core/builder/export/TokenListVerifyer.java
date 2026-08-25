package simula.core.builder.export;

import java.util.List;

import simula.core.SemanticTextReconstructor;
import simula.core.utilities.KeyWord;
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
	private final static boolean TESTING = true;
	private static int FORMAT = 2;//1;

	public static void REMOVED_verifyTokenList(String originalText, List<Integer> semanticTokens, List<LexToken> lexTokenList) {
		printLexTokenList("TokenListVerifyer.verifyTokenList: ", lexTokenList);
		if(TESTING) IO.println("\nTokenListVerifyer.verifyTokenList:" + lexTokenList.size());

        StringBuilder sb = new StringBuilder();
        int currentLine = 0;
        int currentChar = 0;
        
        // NYE:
        int prevLength = 0;
        int prevStart = 0;

//        // Loop gjennom arrayet i hopp på 5 (LSP spec)
//        for (int i = 0; i < rawTokens.length; i += 5) {
//            int deltaLine = rawTokens[i];
//            int deltaStart = rawTokens[i + 1];
//            int length = rawTokens[i + 2];
//            // index i+3 (tokenType) og i+4 (tokenModifiers) ignoreres for ren tekst-rekonstruksjon
		int x = 0;
		int lexTokenIndex = 0;
		while(x < semanticTokens.size()) {
            LexToken lexToken = lexTokenList.get(lexTokenIndex++);
            int deltaLine = semanticTokens.get(x++);
            int deltaStart = semanticTokens.get(x++);
            int length = semanticTokens.get(x++);
            int tokenTypeIndex = semanticTokens.get(x++);
            // int tokenModifiersBitmask = semanticTokens.get(x++);
            x ++; // Skip tokenModifiersBitmask
			if(TESTING) {
	            IO.println("\nTokenListVerifyer.verifyTokenList: TESTING LexToken: " + lexToken);				
	            IO.println(  "TokenListVerifyer.verifyTokenList: TESTING SemToken: deltaLine " + deltaLine
	            		+ ": " + TokenManager.tokenTypes.get(tokenTypeIndex)
	            		+ "[deltaStart:" + deltaStart + ", length:" + length + "] Text: " + Util.printable(lexToken.getText()));			
			}

            // 1. Håndter ny linje
            String NEWLINE_Chars ="\r\n";
            if (deltaLine > 0) {
                for (int d = 0; d < deltaLine; d++) {
                    sb.append(NEWLINE_Chars);
                }
                currentLine += deltaLine;
                currentChar = 0; // Nullstill tegntelling for ny linje
            }

            // 2. Beregn absolutt startposisjon på gjeldende linje
            int absoluteStartChar = (deltaLine == 0) ? (currentChar + deltaStart) : deltaStart;

            // 3. Fyll inn manglende mellomrom før tokenet starter
            /// For å beregne avstanden (spacesNeeded) mellom to tokens på samme linje,
            /// trekkes forrige tokens lengde fra det nåværende tokenets deltaStartChar.
            int spacesNeeded = 0;;
//            switch(FORMAT) {
//	            case 1: spacesNeeded = absoluteStartChar - currentChar; break;
//	            case 2: spacesNeeded = deltaStart - prevLength; break;
//	            case 3: spacesNeeded = absoluteStartChar - currentChar - prevLength; break;
//	            default: Util.IERR("");
//            }
            if(deltaLine == 0) {
            	// meaning the current token is on the same line as the previous token),
            	// deltaStart is relative to the start character (column offset) of the previous token.
            	//
            	// token  token   token    | prevLength = 5, deltaStart = 8
            	//        ------->         | spacesNeeded = deltaStart - prevLength = 8 - 5 = 3
            	spacesNeeded = deltaStart - prevLength;
            } else {
            	// meaning the current token is on a new line relative to the previous token),
            	// deltaStart is relative to 0 (the absolute beginning/left margin of that new line).
            	//
            	// token   token    | prevLength = 5, deltaStart = 8
            	// ------->         | spacesNeeded = deltaStart - prevLength = 8 - 5 = 3
            	spacesNeeded = deltaStart - prevLength;            	
            }
            IO.println("TokenListVerifyer.verifyTokenList: currentChar=" + currentChar +", absoluteStartChar=" + absoluteStartChar +" ==> spacesNeeded= " + spacesNeeded);
            for (int s = 0; s < spacesNeeded; s++) {
                sb.append(" ");
            }

            // 4. Generer plassholder-tekst for selve tokenet (siden vi ikke har ordboken)
//            for (int l = 0; l < length; l++) {
//                sb.append("T"); // 'T' indikerer et tekst-tegn fra tokenet
//            }
            sb.append(lexToken.getText());

            // 5. Oppdater markøren til slutten av gjeldende token
            currentChar = absoluteStartChar + length;
            
            prevLength = length;
            prevStart = deltaStart;
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
	
	public static void verifyTokenList(String originalText, List<Integer> semanticTokens, List<LexToken> lexTokenList) {
		printLexTokenList("TokenListVerifyer.verifyTokenList: ", lexTokenList);
		if(TESTING) IO.println("\nTokenListVerifyer.verifyTokenList:" + lexTokenList.size());
		
        String reconstructedText = SemanticTextReconstructor.reconstruct(originalText, semanticTokens, lexTokenList);
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
