package simula.editor;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Util;
import simula.psi.PsiTree;
import simula.psi.LexToken;
import simula.psi.SimulaLexer;

public class PsiBuilder {
	
    private SimulaLexer lexer;
    public PsiTree psiRoot;
    public PsiTree psiTree;

//	public PsiBuilder() {
//		psiTree = psiRoot = new PsiTree("ROOT", null);
//	}

	public void start(CharSequence txt) {
		psiTree = psiRoot = new PsiTree("ROOT", null);
        lexer = new SimulaLexer();
		CharSequence buffer = txt;
		int startOffset = 0;
		int endOffset = buffer.length();
	    lexer.start(buffer, startOffset, endOffset);
	}
	
    public PsiTree getRoot() {
    	return psiRoot;
    }

    public int getSourceLineNumber() {
    	return lexer.getSourceLineNumber();
    }

	public void startSubtree(String debugName) {
		getParserToken(); // Skiped LexTokens into current psiTree.
//        IO.println("PsiBuilder.startSubtree: ============================ startSubtree: " + debugName + ", parent =" + ((psiTree == null)?"null":psiTree.parent));
		psiTree = new PsiTree(debugName, psiTree);
		psiTree.parent.addChild(psiTree);
//        IO.println("PsiBuilder.startSubtree: ============================ startSubtree: " + psiTree.debugName + ", parent =" + ((psiTree == null)?"null":psiTree.parent));
//        psiRoot.printTree("============================ startSubtree: " + psiTree.debugName + " ROOT " + psiRoot.debugName);
	}
	
	public void doneSubtree(SyntaxClass element) {
//        psiTree.debugName = element.getClass().getSimpleName();
        psiTree.debugName = psiTree.debugName + " ==> " + element.getClass().getSimpleName();
        element.psiTree = psiTree;
        psiTree.syntaxClass = element;
//        psiTree.printTree("============================ doneSubtree: " + psiTree.debugName);
//        psiRoot.printTree("============================ doneSubtree: ROOT " + psiRoot.debugName);
        
        psiTree = psiTree.parent;
	}

	
	public void dropSubtree() {
		IO.println("============================================= DROP SUB-TREE ====================================================================");
		psiTree.printPsiTree("============================ dropSubtree: " + psiTree.debugName);
//		if(psiTree.isEmpty()) {
//			psiTree.parent.removeLastChild();
//	        psiTree = psiTree.parent;
//		} else {
			psiTree.parent.removeLastChild();
			psiTree.parent.addTree(psiTree);
	        psiTree = psiTree.parent;
//			
////			Util.IERR("NOT IMPL");
//		}
		psiRoot.printPsiTree("============================ dropSubtree: ROOT " + psiRoot.debugName);
//		Util.IERR("PsiBuilder.dropSubtree: NOT IMPL");
	}
	
	public void advanceLexer() {
		psiTree.addChild(getCurrentLexerToken());
		IO.println("PsiBuilder.advanceLexer: " + getCurrentLexerToken() + " ==> " + psiTree);
//		psiRoot.printPsiTree("PsiBuilder.advanceLexer: " + getCurrentLexerToken() + " ==> ");
		lexer.advance();
	}
	
	public void consume(int... keyWords) {
		LexToken lexToken = getParserToken();
		IO.println("PsiBuilder.consume: lexToken=" + KeyWord.edit(lexToken.keyWord));
		boolean OK = false;
		LOOP:for(int keyWord:keyWords) {
			IO.println("PsiBuilder.consume: TEST: " + KeyWord.edit(keyWord) + " == " + KeyWord.edit(lexToken.keyWord));
			if(lexToken.keyWord == keyWord) {
				IO.println("PsiBuilder.consume: GOT IT: " + KeyWord.edit(keyWord) + " == " + KeyWord.edit(lexToken.keyWord));
				OK = true; break LOOP;
			}
		}
		if(! OK) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			for(int keyWord:keyWords) {
				if(! first) sb.append(','); first = false;
				sb.append(KeyWord.edit(keyWord));
			}
			Util.IERR("PsiBuilder.consume("+sb+"): NOT FOUND -- KeyWord " + KeyWord.edit(lexToken.keyWord) + " is not among the expected KeyWords");
		}
		psiTree.addChild(lexToken);
//		IO.println("PsiBuilder.advanceLexer: " + getCurrentLexerToken() + " ==> " + psiTree);
		psiRoot.printPsiTree("PsiBuilder.advanceLexer: " + getCurrentLexerToken() + " ==> ");
		lexer.advance();
	}
	
	static int SEQU = 1;
	public void rollBack() {
		psiTree.printPsiTree("REMOVE LAST FROM psiTree: ");
//		LexToken prev = psiTree.removeLastChild();
		
		LexToken prev;// = psiTree.removeLastChild();
//		while((prev = psiTree.removeLastChild()).keyWord == KeyWord.WHITESPACES) {
		while((prev = (LexToken) psiTree.removeLastChild()).isWhiteSpaces()) {
			psiTree.printPsiTree("REMOVE LAST FROM psiTree: ");
		}
		IO.println("DONE - REMOVE LAST FROM psiTree: " + prev);
		lexer.rollBackToBefore(prev);
//		if((SEQU++) > 2) Util.IERR();
	}

	public LexToken prevToken() {
		LexToken prev = psiTree.getLastChild();
//		Util.IERR("REMOVE FROM psiTree: " + prev);
		return prev;
	}

	public boolean eof() {
		// TODO Auto-generated method stub
		return getCurrentLexerToken() == null;
	}

	public LexToken getCurrentLexerToken() {
		return lexer.getCurrentLexerToken();
	}

	/// Return 'Parser' token. Skip Comment, Whitespace and Newline tokens.
    public LexToken getParserToken() {
    	// if(DEBUG > 1) System.out.println("SimulaLexer.getParserToken: "+currentLexerToken);
        while(true) {
    		LexToken token = lexer.getCurrentLexerToken();
//        	System.out.println("SimulaLexer.getParserToken: "+token);
    		if(token == null) return null;
//        	System.out.println("PsiBuilder.getParserToken: "+token);
        	if(token.keyWord == KeyWord.NEWLINE);          // Skip
        	else if(token.keyWord == KeyWord.WHITESPACES); // Skip
        	else if(token.keyWord == KeyWord.COMMENT);     // Skip
        	else {
//            	System.out.println("SimulaLexer.getParserToken: RETURN TOKEN: "+token);
        		return token;
        	}
//        	System.out.println("SimulaLexer.getParserToken: SKIP TOKEN: "+token);
        	advanceLexer();
		}
    }
	
	public void printPSI(String title) {
		IO.println("printPSI: BEGIN *** "+title+" ***");
		psiRoot.printPsiTree(title);
		IO.println("printPSI: ENDOF *** "+title+" ***");
	}

}
