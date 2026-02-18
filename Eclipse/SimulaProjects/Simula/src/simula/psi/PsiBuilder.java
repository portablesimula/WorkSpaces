package simula.psi;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Util;

public class PsiBuilder {
	
    private SimulaLexer lexer;
    public PsiTree psiRoot;
    public PsiTree psiTree;

//	public PsiBuilder() {
//		psiTree = psiRoot = new PsiTree("ROOT", null);
//	}

	public void start(CharSequence txt) {
		psiTree = psiRoot = new PsiTree(null, "ROOT", null);
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

	public PsiTree startSubtree(Class<?> clazz, String debugName) {

		getParserToken(); // Skiped LexTokens into current psiTree.
//        IO.println("PsiBuilder.startSubtree: ============================ startSubtree: " + debugName + ", parent =" + ((psiTree == null)?"null":psiTree.parent));
		psiTree = new PsiTree(clazz, debugName, psiTree);
		psiTree.parent.addChild(psiTree);
		IO.println("   ".repeat(psiTree.level())+"PsiBuilder.startSubtree("+psiTree.level()+"): "+debugName+" CALLED FROM: "+Util.calledFrom(3,6));
//        IO.println("PsiBuilder.startSubtree: ============================ startSubtree: " + psiTree.debugName + ", parent =" + ((psiTree == null)?"null":psiTree.parent));
//        psiRoot.printTree("============================ startSubtree: " + psiTree.debugName + " ROOT " + psiRoot.debugName);
		return psiTree;
	}
	
	public void doneSubtree(PsiTree psiTree, SyntaxClass element) {
		if(psiTree != this.psiTree) {
			IO.println("\nPsiBuilder.doneSubtree("+psiTree.level()+"): Wrong matching PsiTree. ");
			IO.println("PsiBuilder.doneSubtree: Curr-Tree: " + psiTree);
			IO.println("PsiBuilder.doneSubtree: Expecting: " + this.psiTree);
			int level1 = psiTree.level();
			int level2 = this.psiTree.level();
			PsiTree curr = this.psiTree;
			while(level1++ < level2) {
				IO.println("PsiBuilder.doneSubtree: PsiTree NOT TERMINATED: " + curr);
				curr = curr.parent;
			}
//			Util.IERR("PsiBuilder.doneSubtree: Wrong matching PsiTree - Got " + psiTree.clazz.getSimpleName() + " while expecting " + this.psiTree.clazz.getSimpleName());
			Util.IERR("PsiBuilder.doneSubtree: Wrong matching PsiTree - Current " + this.psiTree.debugName + " while expecting " + psiTree.debugName);
		}
		doneSubtree(element);
	}
	
//	public void doneDeclaration(SyntaxClass element) {
//		if(! (element instanceof Declaration)) Util.IERR("");
//		doneSubtree(element);
//	}
	
	public void doneSubtree(SyntaxClass element) {
		IO.println("   ".repeat(psiTree.level())+"PsiBuilder.doneSubtree("+psiTree.level()+"): "+psiTree.debugName+" CALLED FROM: "+Util.calledFrom(3,6));
//		if(! (element instanceof psiTree.clazz)) Util.IERR("");
//		if(! (psiTree.in(element.getClass()))) Util.IERR("");
		psiTree.checkLegalClass(element.getClass());
//        psiTree.debugName = element.getClass().getSimpleName();
        psiTree.debugName = psiTree.debugName + " ==> " + element.getClass().getSimpleName();
        element.psiTree = psiTree;
        psiTree.syntaxClass = element;
//        psiTree.printPsiTree("============================ doneSubtree: " + psiTree.debugName);
//        psiRoot.printPsiTree("============================ doneSubtree: ROOT " + psiRoot.debugName);
        
        psiTree = psiTree.parent;
	}

	
	
	public void dropSubtree(PsiTree psiTree) {
		if(psiTree != this.psiTree) Util.IERR("");
		dropSubtree();
	}
	
	public void dropSubtree() {
		IO.println("   ".repeat(psiTree.level())+"PsiBuilder.dropSubtree("+psiTree.level()+"): "+psiTree.debugName+" CALLED FROM: "+Util.calledFrom(3,6));
//		IO.println("============================================= DROP SUB-TREE ====================================================================");
//		psiTree.printPsiTree("============================ dropSubtree: " + psiTree.debugName);
		psiTree.parent.removeLastChild();
		psiTree.parent.addTree(psiTree);
        psiTree = psiTree.parent;
//		psiRoot.printPsiTree("============================ dropSubtree: ROOT " + psiRoot.debugName);
//		Util.IERR("PsiBuilder.dropSubtree: NOT IMPL");
	}
	
	public void advanceLexer() {
		psiTree.addChild(getCurrentLexerToken());
//		IO.println("PsiBuilder.advanceLexer: " + getCurrentLexerToken() + " ==> " + psiTree);
//		psiRoot.printPsiTree("PsiBuilder.advanceLexer: " + getCurrentLexerToken() + " ==> ");
		lexer.advance();
	}
	
	public void consume(int... keyWords) {
		LexToken lexToken = getParserToken();
//		IO.println("PsiBuilder.consume: lexToken=" + KeyWord.edit(lexToken.keyWord));
		boolean OK = false;
		LOOP:for(int keyWord:keyWords) {
//			IO.println("PsiBuilder.consume: TEST: " + KeyWord.edit(keyWord) + " == " + KeyWord.edit(lexToken.keyWord));
			if(lexToken.keyWord == keyWord) {
//				IO.println("PsiBuilder.consume: GOT IT: " + KeyWord.edit(keyWord) + " == " + KeyWord.edit(lexToken.keyWord));
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
//		psiRoot.printPsiTree("PsiBuilder.advanceLexer: " + getCurrentLexerToken() + " ==> ");
		lexer.advance();
	}
	
	public void setParsingBoundPairList(boolean parsingBoundPairList) {
		lexer.setParsingBoundPairList(parsingBoundPairList);
	}

	public void rollBack() {
//		IO.println("REMOVE LAST PARSER TOKEN FROM psiTree: Children: " + psiTree.edChildren());
		LexToken prev = psiTree.getLastParserChild();
//		IO.println("DONE - REMOVE LAST PARSER TOKEN FROM psiTree: " + prev);
		rollBackTo(prev);
//		IO.println("DONE - REMOVE LAST PARSER TOKEN FROM psiTree: Children: " + psiTree.edChildren());
	}

	public void	rollBackTo(LexToken prev) {
//		IO.println("PsiBuilder.rollBackTo: "+prev);
//		IO.println("PsiBuilder.rollBackTo: CurrentLexerToken: "+getCurrentLexerToken());
//		psiTree.printPsiTree("PsiBuilder.rollBackTo: "+prev);
		LOOP:while(true) {
//			IO.println("PsiBuilder.rollBackTo: REMOVE: last="+psiTree.getLastChild());
			if(psiTree.removeLastChild() == prev) break LOOP;
		}
//		psiTree.printPsiTree("PsiBuilder.rollBackTo: "+prev);
		lexer.rollBackToBefore(prev);
	}

	public LexToken prevToken() {
		LexToken prev = psiTree.getLastChild();
		return prev;
	}

	public LexToken prevParserToken() {
		LexToken prev = psiTree.getLastParserChild();
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
    		if(token == null) {
//    			public LexToken(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int keyWord) {
    			token = lexer.getEOFToken();
//    			return null;
    		}
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
