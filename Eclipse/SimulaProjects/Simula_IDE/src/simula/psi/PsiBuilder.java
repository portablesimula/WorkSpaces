package simula.psi;

import java.util.Vector;

import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.compiler.syntaxClass.statement.BlockStatement;
import simula.compiler.utilities.Html;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.token.SimpleString;

public class PsiBuilder {
	
    private SimulaLexer lexer;
    public PsiTree psiRoot;
    public PsiTree psiTree;
    CharSequence sourceText;

	private LexToken prevWasNEWLINE = null; // Used by PSI_VERIFY

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

    ///  ????  ?????
    /// 
    ///  Invariant: Lexer'currentLexerToken is a ParserToken
    /// 
    /// Invariant: Lexer'parserToken is first token of construct
    /// 
    public void startSubtree(PsiTree.Kind kind, String debugName) {
    	LexToken checkPoint = this.getCurrentParserToken();
//		IO.println("\n\nPsiBuilder.startSubtree: "+debugName+", CHECKPOINT: "+checkPoint+" CALLED FROM: "+Util.calledFrom(3,6));
//    	lexer.snapShot("START SubTree: "+ lexer.getCurrentLexerToken()+ " " + debugName);
    	int startOffset = checkPoint.startOffset;
    	
//		getCurrentParserToken(); // Get next Parser Token while skipped LexTokens are added to current psiTree.
//		psiTree = new PsiTree(debugName, startOffset, getSourceLineNumber(), psiTree);
		psiTree = new PsiTree(psiTree, getSourceLineNumber(), sourceText, startOffset, debugName);
		psiTree.kind = kind;
		psiTree.checkPoint = checkPoint;
		psiTree.parent.addChild(psiTree);
		if(Option.TRACE_PSITREE_START_DONE > 0) {
			String ID = ""+psiTree.level()+':'+kind+"]: "+debugName;
			IO.println("\n\nPsiBuilder.startSubtree["+ID+" CALLED FROM: "+Util.calledFrom(3,6));
//			psiTree.startState.printState(ID);
//			psiTree.printAncesterChain(ID);
//	        IO.println("PsiBuilder.startSubtree: ============================ startSubtree: " + psiTree.debugName + ", parent =" + ((psiTree == null)?"null":psiTree.parent));
//	        psiRoot.printTree("============================ startSubtree: " + psiTree.debugName + " ROOT " + psiRoot.debugName);
		}
//		IO.println("PsiBuilder.startSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+", checkPoint: "+checkPoint+" CALLED FROM: "+Util.calledFrom(3,6));
	}
		
	public void doneSubtree(PsiTree.Kind kind, SyntaxElement syntaxElement) {
//		IO.println("PsiBuilder.doneSubtree: syntaxElement="+syntaxElement);
		Vector<SyntaxElement> syntaxElements = new Vector<SyntaxElement>();
		if(syntaxElement != null) syntaxElements.add(syntaxElement);
		doneSubtree(kind, syntaxElements);
	}
	
	public void doneSubtree(PsiTree.Kind kind, Vector<SyntaxElement> syntaxElements) {
//		IO.println("\n\nPsiBuilder.doneSubtree["+psiTree.level()+':'+kind+"]: "+psiTree.edChildrenText()+" CALLED FROM: "+Util.calledFrom(3,6));
		if(psiTree.kind != kind) {
//			IO.println("PsiBuilder.doneSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+" "+syntaxElement.getClass().getSimpleName()+"="+syntaxElement+", CALLED FROM: "+Util.calledFrom(3,6));
			psiTree.printAncesterChain("");
//			Util.IERR("PsiBuilder.doneSubtree: Wrong top PsiTree kind: " + psiTree.kind + ", expected: " + kind);
			Util.IERR("PsiBuilder.doneSubtree: Can't terminate PsiTree as kind: " + kind + ", because top kind = " + psiTree.kind + " " + psiTree.debugName);
//			Util.STOP();
		}
		if(Option.TRACE_PSITREE_START_DONE > 0) {
//			IO.println("PsiBuilder.doneSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+" "+syntaxElement.getClass().getSimpleName()+"="+syntaxElement+", CALLED FROM: "+Util.calledFrom(3,6));
		}
//		if(Option.PSI_VERIFY) {
//			LOOP:for(SyntaxElement syntaxElement:syntaxElements) {
//				if(syntaxElement instanceof BlockDeclaration blk) {
//					IO.println("PsiBuilder.doneSubtree: BlockDeclaration: isMainModule=" + blk.isMainModule);
//					IO.println("PsiBuilder.doneSubtree: blk.sourceBlockLevel: " + blk.getClass().getSimpleName() + " " + blk.sourceBlockLevel);
//					if(blk.sourceBlockLevel == 1) {
//						// Testing Declaration END-Condition
////						LexToken token = this.psiTree.getLastChild();
//						LexToken token = this.psiTree.getLastParserChild();
//						if(token != null && token.keyWord != KeyWord.END) {
//							psiTree.printPsiTree("PsiBuilder.doneSubtree: PSI VERIFIER FAILED");
//							Util.IERR("PsiBuilder.doneSubtree:  Wrong termination of " + syntaxElement.getClass().getSimpleName()+" lastChild="+token+" Should be END");
//						}
//						break LOOP;
//					}
//				}
//				if(syntaxElement instanceof Declaration) {
//					// Testing Declaration END-Condition
////					LexToken token = this.psiTree.getLastChild();
//					LexToken token = this.psiTree.getLastParserChild();
//					if(token != null && token.keyWord != KeyWord.SEMICOLON) {
//						psiTree.printPsiTree("PsiBuilder.doneSubtree: PSI VERIFIER FAILED");
//						Util.IERR("PsiBuilder.doneSubtree:  Wrong termination of " + syntaxElement.getClass().getSimpleName()+" lastChild="+token+" Should be ;");
//					}
//				}
//			}
//		}
		
		psiTree.endOffset = psiTree.getEndOffset();
		for(SyntaxElement syntaxElement:syntaxElements) {
//			IO.println("PsiBuilder.doneSubtree: syntaxElement="+syntaxElement);
			syntaxElement.psiTree = psiTree;
		}
        psiTree.syntaxElements = syntaxElements;
		
		if(Option.PSI_VERIFY) {
			String text = psiTree.getText().replace("\n", "\\n").replace("\r", "\\r");
			String original = psiTree.getOriginalText().replace("\n", "\\n").replace("\r", "\\r");
			if(! text.equals(original)) {
				System.err.println("PsiBuilder.doneSubtree: "+psiTree.debugName+": created  text: "+text);
				System.err.println("PsiBuilder.doneSubtree: "+psiTree.debugName+": original text: "+original);					
				Util.IERR("Resulting text differ from original text");
				Util.STOP();
			}
		}
		
		if(! syntaxElements.isEmpty()) {
			SyntaxElement syntaxElement = syntaxElements.firstElement();
			if(syntaxElement instanceof BlockStatement blk) {
//		        psiTree.debugName = psiTree.debugName + " ==> " + Html.styledText(Html.styleKeyWord, blk.psiKind());
		        psiTree.debugName = Html.styledText(Html.styleKeyWord, blk.psiKind());
			} else if(syntaxElement instanceof MaybeBlockDeclaration blk && blk.declarationKind == ObjectKind.SimulaProgram) {
//				psiTree.debugName = psiTree.debugName + " ==> " + Html.styledText(Html.styleKeyWord, "SimulaProgram");				
				psiTree.debugName = Html.styledText(Html.styleKeyWord, "SimulaProgram");				
			} else {
//				psiTree.debugName = psiTree.debugName + " ==> " + Html.styledText(Html.styleKeyWord, syntaxElement.getClass().getSimpleName());
				psiTree.debugName = Html.styledText(Html.styleKeyWord, syntaxElement.getClass().getSimpleName());
			}
		}
        
		if(Option.TRACE_PSITREE_START_DONE > 1) {
//        IO.println("PsiBuilder.doneSubtree: "+syntaxElement.getClass().getSimpleName()+"'PSITree = "+psiTree);
			if(Option.TRACE_PSITREE_START_DONE > 1) {
		        psiTree.printPsiTree("============================ doneSubtree: " + psiTree.debugName);
		        psiRoot.printPsiTree("============================ doneSubtree: ROOT " + psiRoot.debugName);
			} 
		}
        psiTree = psiTree.parent;
	}
	
	public void dropSubtree(PsiTree.Kind kind, String debugName) {
//		IO.println("\n\nPsiBuilder.dropSubtree["+psiTree.level()+':'+kind+"]: "+psiTree.edChildrenText()+" CALLED FROM: "+Util.calledFrom(3,6));
		if(psiTree.kind != kind) {
			IO.println("PsiBuilder.dropSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+" CALLED FROM: "+Util.calledFrom(3,6));
			psiTree.printAncesterChain("");
			Util.IERR("PsiBuilder.dropSubtree: Wrong PsiTree kind: " + kind + ", expected: " + psiTree.kind);
			Util.STOP();
		}
		
		if(Option.TRACE_PSITREE_START_DONE > 0) {
			IO.println("\n\nPsiBuilder.dropSubtree["+psiTree.level()+':'+kind+"]: \" "+psiTree.debugName+" CALLED FROM: "+Util.calledFrom(3,6));
			IO.println("PsiBuilder.dropSubtree: CurrentLexerToken=" + lexer.getCurrentLexerToken());
			if(Option.TRACE_PSITREE_START_DONE > 1) {
//				IO.println("============================================= DROP SUB-TREE ====================================================================");
//				psiTree.printPsiTree("============================ dropSubtree: " + psiTree.debugName);
				psiRoot.printPsiTree("============================ dropSubtree: ROOT TREE BEFORE DROP TREE: "+psiTree.debugName+debugName+" CurrentLexerToken=" + lexer.getCurrentLexerToken());
			}
		}
		if(Option.internal.TRACE_ADVANCE_LEXER) {
			String mss = (psiTree.isEmpty())? "-- Empty" : psiTree.edChildren();
			IO.println("PsiBuilder.advanceLexer: DROP SUBTREE: " + mss + "\n\n");
		}
		
		if(! psiTree.isEmpty()) {
			lexer.resetCheckPoint("dropSubTree", psiTree);
		}
		
		psiTree.parent.removeLastChild();
        psiTree = psiTree.parent;
		if(Option.TRACE_PSITREE_START_DONE > 1) {
			psiRoot.printPsiTree("============================ dropSubtree: ROOT TREE AFTER DROP TREE: "+psiTree.debugName+" CurrentLexerToken=" + lexer.getCurrentLexerToken());
		}
//    	lexer.snapShot("END DROP SubTree: "+ lexer.getCurrentLexerToken()+ " " + debugName);
		if(Option.internal.TRACE_ADVANCE_LEXER)
			IO.println("PsiBuilder.advanceLexer: GOT CURRENT LEXTOKEN: " + getCurrentLexerToken());
    	if(Option.PSI_VERIFY) {
//    		lastAdvancedLineNumber = getCurrentLexerToken().lineNumber;
    	}
	}
	
	public void advanceLexer() {
		psiTree.addChild(getCurrentLexerToken()); // Add 'old' current LexToken to the PsiTree
		lexer.advance();                          // And the advance the lexer.
		if(Option.internal.TRACE_ADVANCE_LEXER)
			IO.println("PsiBuilder.advanceLexer: NEW CURRENT LEXTOKEN: " + getCurrentLexerToken());
    	if(Option.PSI_VERIFY) {
    		if(prevWasNEWLINE != null && getCurrentLexerToken().keyWord != KeyWord.EOF) {
    			if((prevWasNEWLINE.lineNumber + 1) != (getCurrentLexerToken().lineNumber) ) {
        			IO.println("SimulaLexer.advance: prevWasNEWLINE: " + prevWasNEWLINE);
        			IO.println("SimulaLexer.advance: currentLexerToken: " + getCurrentLexerToken());
            		Util.IERR("ERROR: CHECK NEWLINE FAILED. Prev Token: " + prevWasNEWLINE +", Current Token: " + getCurrentLexerToken());
    				Util.STOP();
    			}
    		}
    		if(getCurrentLexerToken().keyWord == KeyWord.NEWLINE) {
    			prevWasNEWLINE = getCurrentLexerToken();
    		} else prevWasNEWLINE = null;
    	}
	}
	
	public void consume(int... keyWords) {
		LexToken lexToken = getCurrentParserToken();
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
//		if(lexToken != getCurrentLexerToken()) Util.STOP();
//		psiTree.addChild(lexToken);
//		lexer.advance();
		advanceLexer();
	}
	
	public void setParsingBoundPairList(boolean parsingBoundPairList) {
		lexer.setParsingBoundPairList(parsingBoundPairList);
	}

	public void	rollBackTo(LexToken prev, String debugInfo) {
//		IO.println("PsiBuilder.rollBackTo: "+prev);
//		IO.println("PsiBuilder.rollBackTo: CurrentLexerToken: "+getCurrentLexerToken());
//		IO.println("PsiBuilder.rollBackTo: LastParserChild: "+psiTree.getLastParserChild());
		LOOP:while(true) {
//			IO.println("PsiBuilder.rollBackTo: REMOVE: last="+psiTree.getLastChild());
			if(psiTree.removeLastChild() == prev) break LOOP;
		}
		lexer.rollBackToBefore(prev, debugInfo);
	}

	public LexToken prevToken() {
		LexToken prev = psiTree.getLastChild();
		return prev;
	}

	public LexToken prevParserToken() {
		LexToken prev = psiTree.getLastParserChild();
//		if(prev == null) {
//			this.psiTree.printPsiTree("PsiBuilder.prevParserToken: CURRENT TREE");
//			this.getRoot().printPsiTree("PsiBuilder.prevParserToken: WHOLE TREE");
//			Util.STOP();
//		}
		return prev;
	}

	public boolean eof() {
		return lexer.EOF != null;
	}

	public LexToken getCurrentLexerToken() {
		return lexer.getCurrentLexerToken();
	}

	/// Return next 'Parser' token.
	/// Skip Comment, Whitespace and Newline tokens.
    public LexToken getNextParserToken() {
    	// if(DEBUG > 1) IO.println("PsiBuilder.getNextParserToken: "+currentLexerToken);
    	Util.IERR("DENNE ER IKKE BRUKT FØR - MÅ SJEKKES");
    	getCurrentParserToken();
    	advanceLexer();
    	return getCurrentParserToken();
    }

	/// Return current 'Parser' token.
	/// Skip Comment, Whitespace and Newline tokens.
    public LexToken getCurrentParserToken() {
    	// if(DEBUG > 1) IO.println("PsiBuilder.getCurrentParserToken: "+currentLexerToken);
        while(true) {
    		LexToken token = getCurrentLexerToken();
    		if(token == null) {
    			token = lexer.getEOFToken();
    		}
        	if(token.isParserToken()) return token;
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
//    	while((nextToken=getCurrentParserToken()) instanceof SimpleString str) {
    	while(getCurrentParserToken() instanceof SimpleString str) {
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
////		LexToken nextToken = getCurrentParserToken();
//		IO.println("\n\nPsiBuilder.getTextString: nextToken: "+nextToken);
//		String result = "";
//    	while(nextToken instanceof SimpleString str) {
//    		result += str.value;
//    		nextToken = getCurrentParserToken();
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
