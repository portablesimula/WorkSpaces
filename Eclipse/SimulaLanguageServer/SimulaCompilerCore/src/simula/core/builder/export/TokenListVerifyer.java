package simula.core.builder.export;

import java.util.List;

import simula.Comn;
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
	private final static boolean TESTING = true;

	public static void verifyTokenList(String originalText, List<Integer> semanticTokens, List<LexToken> lexTokenList) {
		printLexTokenList("TokenListVerifyer.verifyTokenList: ", lexTokenList);
		if(TESTING) IO.println("\nTokenListVerifyer.verifyTokenList:" + lexTokenList.size());
		
        String reconstructedText = SemanticTextReconstructor.reconstruct(originalText, semanticTokens, lexTokenList);
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
		IO.println("TokenListVerifyer.verifyTokenList: OK - lexTokenList.size=" + lexTokenList.size());		
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
