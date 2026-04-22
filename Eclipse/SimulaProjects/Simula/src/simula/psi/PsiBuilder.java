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
    CharSequence sourceText;
    
//	public PsiBuilder() {
//		psiTree = psiRoot = new LocalPsiTree("ROOT", null);
//	}

	public void start(CharSequence sourceText) {
		this.sourceText = sourceText;
		psiTree = psiRoot = new PsiTree(null, 1, sourceText, 1, "ROOT");
        lexer = new SimulaLexer();
		int startOffset = 0;
		int endOffset = sourceText.length();
	    lexer.start(sourceText, startOffset, endOffset);
	}
	
    public PsiTree getRoot() {
    	return psiRoot;
    }

    public int getSourceLineNumber() {
    	return lexer.getSourceLineNumber();
    }
    
    public int psiLevel() {
    	return psiTree.level();
    }
    
    public void checkLevel(int level) {
    	if(psiTree.level() != level) {
    		Util.IERR("ERROR: CheclLevel FAILED. "+psiTree.level() + " != " + level);
//    		Util.STOP();
    	}
    }

    /// Invariant: Lexer'currentLexerToken is a ParserToken
    /// 
    /// Invariant: Lexer'currentLexerToken is first token of construct
    ///            I.e: lexer.currentLexerToken.keyWord == parameter'keyWord'
    /// 
    public void startSubtree(PsiTree.Kind kind, String debugName) {
    	lexer.snapShot("StartSubTree "+debugName);
		getParserToken(); // Get next Parser Token while skipped LexTokens are added to current psiTree.
    	LexToken checkPoint = lexer.getCurrentLexerToken();
//    	LexToken checkPoint = lexer.getPrevParserToken();
//    	int startOffset = lexer.getCurrentPosition();
    	int startOffset = lexer.getCurrentLexerToken().startOffset;
    	
//		psiTree = new PsiTree(debugName, startOffset, getSourceLineNumber(), psiTree);
		psiTree = new PsiTree(psiTree, getSourceLineNumber(), sourceText, startOffset, debugName);
		psiTree.kind = kind;
		psiTree.parent.addChild(psiTree);
		if(Option.TRACE_PSITREE_START_DONE > 0) {
			String ID = ""+psiTree.level()+':'+kind+"]: "+debugName;
			IO.println("\n\nPsiBuilder.startSubtree["+ID+" CALLED FROM: "+Util.calledFrom(3,6));
//			psiTree.startState.printState(ID);
//			psiTree.printAncesterChain(ID);
//	        IO.println("PsiBuilder.startSubtree: ============================ startSubtree: " + psiTree.debugName + ", parent =" + ((psiTree == null)?"null":psiTree.parent));
//	        psiRoot.printTree("============================ startSubtree: " + psiTree.debugName + " ROOT " + psiRoot.debugName);
		}
		IO.println("PsiBuilder.startSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+", checkPoint: "+checkPoint+" CALLED FROM: "+Util.calledFrom(3,6));
	}
		
	public void doneSubtree(PsiTree.Kind kind, SyntaxClass element) {
		IO.println("PsiBuilder.doneSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+", CALLED FROM: "+Util.calledFrom(3,6));
		if(psiTree.kind != kind) {
			IO.println("PsiBuilder.doneSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+" "+element.getClass().getSimpleName()+"="+element+", CALLED FROM: "+Util.calledFrom(3,6));
			psiTree.printAncesterChain("");
//			Util.IERR("PsiBuilder.doneSubtree: Wrong top PsiTree kind: " + psiTree.kind + ", expected: " + kind);
			Util.IERR("PsiBuilder.doneSubtree: Can't terminate PsiTree as kind: " + kind + ", because top kind = " + psiTree.kind + " " + psiTree.debugName);
//			Util.STOP();
		}
		if(Option.TRACE_PSITREE_START_DONE > 0) {
			IO.println("PsiBuilder.doneSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+" "+element.getClass().getSimpleName()+"="+element+", CALLED FROM: "+Util.calledFrom(3,6));
		}
		
		psiTree.endOffset = psiTree.getEndOffset();
        element.psiTree = psiTree;
        psiTree.syntaxClass = element;
		
		boolean TESTING = true;
		if(TESTING) {
			String text = psiTree.getText().replace("\n", "\\n").replace("\r", "\\r");
			String original = psiTree.getOriginalText().replace("\n", "\\n").replace("\r", "\\r");
			if(! text.equals(original)) {
				System.err.println("PsiBuilder.doneSubtree: "+psiTree.debugName+": created  text: "+text);
				System.err.println("PsiBuilder.doneSubtree: "+psiTree.debugName+": original text: "+original);					
				Util.IERR("Resulting text differ from original text");
			}
		}
		
		if(element instanceof BlockStatement blk) {
	        psiTree.debugName = psiTree.debugName + " ==> " + Html.styledText(Html.styleKeyWord, blk.psiKind());
		} else
        psiTree.debugName = psiTree.debugName + " ==> " + Html.styledText(Html.styleKeyWord, element.getClass().getSimpleName());
        
		if(Option.TRACE_PSITREE_START_DONE > 1) {
        IO.println("PsiBuilder.doneSubtree: "+element.getClass().getSimpleName()+"'PSITree = "+psiTree);
			if(Option.TRACE_PSITREE_START_DONE > 1) {
		        psiTree.printPsiTree("============================ doneSubtree: " + psiTree.debugName);
		        psiRoot.printPsiTree("============================ doneSubtree: ROOT " + psiRoot.debugName);
			} 
		}
        psiTree = psiTree.parent;
	}
	
	public void dropSubtree(PsiTree.Kind kind, String debugName) {
		IO.println("PsiBuilder.dropSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+" CALLED FROM: "+Util.calledFrom(3,6));
		if(psiTree.kind != kind) {
			IO.println("PsiBuilder.dropSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+" CALLED FROM: "+Util.calledFrom(3,6));
			psiTree.printAncesterChain("");
			Util.IERR("PsiBuilder.dropSubtree: Wrong PsiTree kind: " + kind + ", expected: " + psiTree.kind);
			Util.STOP();
		}
//		if(Option.TRACE_PSITREE_START_DONE > 0) {
			IO.println("\n\nPsiBuilder.dropSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+" CALLED FROM: "+Util.calledFrom(3,6));
			IO.println("PsiBuilder.dropSubtree: CurrentLexerToken=" + lexer.getCurrentLexerToken());
//			if(Option.TRACE_PSITREE_START_DONE > 1) {
//				IO.println("============================================= DROP SUB-TREE ====================================================================");
//				psiTree.printPsiTree("============================ dropSubtree: " + psiTree.debugName);
				psiRoot.printPsiTree("============================ dropSubtree: ROOT TREE BEFORE DROP TREE: "+psiTree.debugName+debugName+" CurrentLexerToken=" + lexer.getCurrentLexerToken());
//			}
//		}
		
		if(! psiTree.isEmpty()) {
			lexer.setNextLexerToken("dropSubTree", psiTree);
		}
		
		psiTree.parent.removeLastChild();
        psiTree = psiTree.parent;
//		if(Option.TRACE_PSITREE_START_DONE > 1) {
			psiRoot.printPsiTree("============================ dropSubtree: ROOT TREE AFTER DROP TREE: "+psiTree.debugName+" CurrentLexerToken=" + lexer.getCurrentLexerToken());
//		}
	}
	
	public void advanceLexer() {
		psiTree.addChild(getCurrentLexerToken());
//		IO.println("PsiBuilder.advanceLexer: " + getCurrentLexerToken() + " ==> " + psiTree.edPsiLine());
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
//		IO.println("PsiBuilder.advanceLexer: " + getCurrentLexerToken() + " ==> " + psiTree.edPsiLine());
//		psiRoot.printPsiTree("PsiBuilder.advanceLexer: " + getCurrentLexerToken() + " ==> ");
		lexer.advance();
	}
	
	public void setParsingBoundPairList(boolean parsingBoundPairList) {
		lexer.setParsingBoundPairList(parsingBoundPairList);
	}

//	public void rollBack(String debugInfo) {
////		IO.println("REMOVE LAST PARSER TOKEN FROM psiTree: Children: " + psiTree.edChildren());
//		LexToken prev = psiTree.getLastParserChild();
////		IO.println("DONE - REMOVE LAST PARSER TOKEN FROM psiTree: " + prev);
//		rollBackTo(prev, debugInfo);
////		IO.println("DONE - REMOVE LAST PARSER TOKEN FROM psiTree: Children: " + psiTree.edChildren());
//	}

	public void	rollBackTo(LexToken prev, String debugInfo) {
		IO.println("PsiBuilder.rollBackTo: "+prev);
		IO.println("PsiBuilder.rollBackTo: CurrentLexerToken: "+getCurrentLexerToken());
		IO.println("PsiBuilder.rollBackTo: LastParserChild: "+psiTree.getLastParserChild());
		psiTree.printPsiTree("PsiBuilder.rollBackTo: "+prev);
		LOOP:while(true) {
			IO.println("PsiBuilder.rollBackTo: REMOVE: last="+psiTree.getLastChild());
			if(psiTree.removeLastChild() == prev) break LOOP;
		}
//		psiTree.printPsiTree("PsiBuilder.rollBackTo: "+prev);
		lexer.rollBackToBefore(prev, debugInfo);
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
//    public LexToken getParserToken() {
//    	return lexer.getParserToken(psiTree);
//    }
    public LexToken getParserToken() {
    	// if(DEBUG > 1) IO.println("PsiBuilder.getParserToken: "+currentLexerToken);
        while(true) {
    		LexToken token = getCurrentLexerToken();
//        	IO.println("PsiBuilder.getParserToken: "+token);
    		if(token == null) {
//    			public LexToken(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int keyWord) {
    			token = lexer.getEOFToken();
//    			return null;
    		}
        	if(token.isParserToken()) return token;
//        	IO.println("PsiBuilder.getParserToken: SKIP TOKEN: "+token);
//        	psiTree.addChild(token);
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
