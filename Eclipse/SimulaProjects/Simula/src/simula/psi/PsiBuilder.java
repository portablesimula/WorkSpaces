package simula.psi;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.syntaxClass.statement.BlockStatement;
import simula.compiler.utilities.Html;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.token.SimpleString;

public class PsiBuilder {
	
    private SimulaLexer lexer;
    public PsiTree psiRoot;
    public PsiTree psiTree;

//	public PsiBuilder() {
//		psiTree = psiRoot = new LocalPsiTree("ROOT", null);
//	}

	public void start(CharSequence txt) {
		psiTree = psiRoot = new PsiTree("ROOT", 1, null);
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
		getParserToken(); // Get next Parser Token while skipped LexTokens are added to current psiTree.
		psiTree = new PsiTree(debugName, getSourceLineNumber(), psiTree);
		psiTree.parent.addChild(psiTree);
		if(Option.TRACE_PSITREE_START_DONE > 0) {
			IO.println("PsiBuilder.startSubtree: "+debugName+" CALLED FROM: "+Util.calledFrom(3,6));
//	        IO.println("PsiBuilder.startSubtree: ============================ startSubtree: " + psiTree.debugName + ", parent =" + ((psiTree == null)?"null":psiTree.parent));
//	        psiRoot.printTree("============================ startSubtree: " + psiTree.debugName + " ROOT " + psiRoot.debugName);
		}
	}
	
	public void doneSubtree(SyntaxClass element) {
//		if(psiTree.isEmpty()) {
//			IO.println("PsiBuilder.doneSubtree: TREE IS EMPTY: " + element);
////			Util.IERR("PsiBuilder.doneSubtree: TREE IS EMPTY: " + element);
////			Util.STOP();
//			dropSubtree();
//			return;
//		}
		if(Option.TRACE_PSITREE_START_DONE > 0) {
			IO.println("PsiBuilder.doneSubtree: "+psiTree.debugName+" "+element.getClass().getSimpleName()+"="+element+", CALLED FROM: "+Util.calledFrom(3,6));
		}
//        psiTree.debugName = psiTree.debugName + " ==> " + element.getClass().getSimpleName();
//        psiTree.debugName = psiTree.debugName + " ==> " + element.getClass().getSimpleName()+", CALLED FROM: "+Util.calledFrom(3,6);
		if(element instanceof BlockStatement blk) {
	        psiTree.debugName = psiTree.debugName + " ==> " + Html.styledText(Html.styleKeyWord, blk.debugName);
			
		} else
        psiTree.debugName = psiTree.debugName + " ==> " + Html.styledText(Html.styleKeyWord, element.getClass().getSimpleName());
		
        element.psiTree = psiTree;
//        element.setPsiTree(psiTree);
        psiTree.syntaxClass = element;
		if(Option.TRACE_PSITREE_START_DONE > 0) {
        IO.println("PsiBuilder.doneSubtree: "+element.getClass().getSimpleName()+"'PSITree = "+psiTree);
			if(Option.TRACE_PSITREE_START_DONE > 1) {
		        psiTree.printPsiTree("============================ doneSubtree: " + psiTree.debugName);
		        psiRoot.printPsiTree("============================ doneSubtree: ROOT " + psiRoot.debugName);
			} 
		}
        psiTree = psiTree.parent;
	}
	
	public void dropSubtree() {
		if(Option.TRACE_PSITREE_START_DONE > 0) {
			IO.println("PsiBuilder.dropSubtree: "+psiTree.debugName+" CALLED FROM: "+Util.calledFrom(3,6));
//			IO.println("============================================= DROP SUB-TREE ====================================================================");
//			psiTree.printPsiTree("============================ dropSubtree: " + psiTree.debugName);
			psiRoot.printPsiTree("============================ dropSubtree: ROOT TREE BEFORE DROP TREE " + psiRoot.debugName);
		}
		psiTree.parent.removeLastChild();
		psiTree.parent.addTree(psiTree);
        psiTree = psiTree.parent;
		if(Option.TRACE_PSITREE_START_DONE > 0) {
			psiRoot.printPsiTree("============================ dropSubtree: RESULTING ROOT TREE " + psiRoot.debugName);
//			Util.IERR("PsiBuilder.dropSubtree: NOT IMPL");
		}
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

	/// Return current 'Parser' token.
	/// Skip Comment, Whitespace and Newline tokens.
	/// Concatenate successive Simple Strings into a single token.
    public LexToken getParserToken() {
    	// if(DEBUG > 1) IO.println("PsiBuilder.getParserToken: "+currentLexerToken);
        while(true) {
    		LexToken token = lexer.getCurrentLexerToken();
//        	IO.println("PsiBuilder.getParserToken: "+token);
    		if(token == null) {
//    			public LexToken(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int keyWord) {
    			token = lexer.getEOFToken();
//    			return null;
    		}
//        	IO.println("PsiBuilder.getParserToken: "+token);
        	if(token.keyWord == KeyWord.NEWLINE);          // Skip
        	else if(token.keyWord == KeyWord.WHITESPACES); // Skip
        	else if(token.keyWord == KeyWord.COMMENT);     // Skip
        	else {
//            	IO.println("PsiBuilder.getParserToken: RETURN TOKEN: "+token);
        		return token;
        	}
//        	IO.println("PsiBuilder.getParserToken: SKIP TOKEN: "+token);
        	advanceLexer();
		}
    }
    
    /// Get text string, possibly concatenated.
    /// 
    /// string = simple-string { string-separator simple-string }
    /// 
    ///    string-separator = token-separator { token-separator }
    /// 
    /// @param prevToken first SimpleSring
    /// @return resulting String
    /// 
    public String getTextString(LexToken prevToken) {
//		LexToken nextToken = prevToken;
//		IO.println("\n\nPsiBuilder.getTextString: nextToken: "+nextToken);
		String result = ((SimpleString)prevToken).value;
//    	while((nextToken=getParserToken()) instanceof SimpleString str) {
    	while(getParserToken() instanceof SimpleString str) {
    		result += str.value;
//       	IO.println("PsiBuilder.getTextString: RESULT: "+result);
//        	IO.println("PsiBuilder.getTextString: NEXT TOKEN: "+nextToken);
        	advanceLexer();
    	}
//    	IO.println("PsiBuilder.getTextString: RETURN TEXT: ]"+result+"[\n\n");
    	return result;
    }
//    private String OLD_getTextString(LexToken prevToken) {
//		LexToken nextToken = prevToken;
////		LexToken nextToken = getParserToken();
//		IO.println("\n\nPsiBuilder.getTextString: nextToken: "+nextToken);
//		String result = "";
//    	while(nextToken instanceof SimpleString str) {
//    		result += str.value;
//    		nextToken = getParserToken();
//        	advanceLexer();
//        	IO.println("PsiBuilder.getTextString: RESULT: "+result);
//        	IO.println("PsiBuilder.getTextString: NEXT TOKEN: "+nextToken);
////			PsiParse.nextToken(this);
//        	if(SEQU++ > 4) Util.STOP();
//    	}
////    	psiBuilder.pushBackLastParserToken();
////    	psiBuilder.rollBackTo(mark);
////    	psiBuilder.rollBackTo(nextToken);
//    	IO.println("PsiBuilder.getTextString: RETURN TEXT: ]"+result+"[\n\n");
////		Util.IERR("SJEKK DETTE");
////		psiBuilder.printPSI("PsiBuilder.getTextString: ");
////		Util.STOP();
//    	return result;
//    }
	
	public void printPSI(String title) {
		IO.println("printPSI: BEGIN *** "+title+" ***");
		psiRoot.printPsiTree(title);
		IO.println("printPSI: ENDOF *** "+title+" ***");
	}

}
