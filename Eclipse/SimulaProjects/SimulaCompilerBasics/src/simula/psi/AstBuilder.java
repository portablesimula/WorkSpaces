package simula.psi;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.SimulaDiagnostic;
import simula.compiler.utilities.Util;
import simula.lsp.compiler.DocumentManager;
import simula.lsp.compiler.LspToken;
import simula.token.SimpleString;

public class AstBuilder {
	
    CharSequence sourceText;
	
    private SimulaLexer lexer;
//    public PsiTree psiRoot; // TESTING_WITHOUT_PSI = false
//    public PsiTree psiTree; // TESTING_WITHOUT_PSI = false
    public LexTokenRange astRoot;       // TESTING_WITHOUT_PSI = true
    public LexTokenRange lexTokenRange; // TESTING_WITHOUT_PSI = true
	public List<SimulaDiagnostic> diagnostics;

	public List<LspToken> tokenList;
//	private ProgramModule syntaxTree; // Root of Syntax Tree
//	private LexToken prevWasNEWLINE = null; // Used by PSI_VERIFY

	public AstBuilder(DocumentManager documentManager) {
//		this.sourceDocumentItem = sourceDocumentItem;
		this.diagnostics = new ArrayList<>();
	}

	public void start(CharSequence sourceText) {
		this.sourceText = sourceText;
//		if(Option.TESTING_WITHOUT_PSI) {
			tokenList = new ArrayList<>();
			lexTokenRange = astRoot = new LexTokenRange(null);
	        lexer = new SimulaLexer(this);
		    lexer.start(sourceText);
//		} else {
//			psiTree = psiRoot = new PsiTree(null, 1, sourceText, 1, "ROOT");
//	        lexer = new SimulaLexer(this);
//			int startOffset = 0;
//			int endOffset = sourceText.length();
//		    lexer.startPsi(sourceText, startOffset, endOffset);
//		}
	}
	
	public void addDiagnostic(SimulaDiagnostic diagnostic) {
		diagnostics.add(diagnostic);
	}
	
    public PsiTree getRoot() {
		if(Option.TESTING_WITHOUT_PSI) Util.IERR("getRoot - Skal ikke burukes");
    	return null;//psiRoot;
    }

    public int getSourceLineNumber() {
    	return lexer.getSourceLineNumber();
    }
    
    public int psiLevel() {
		if(Option.TESTING_WITHOUT_PSI) Util.IERR("psiLevel - Skal ikke burukes");
    	return 0;// psiTree.level();
    }
    
    public void checkLevel(int level) {
		if(Option.TESTING_WITHOUT_PSI) Util.IERR("xxx - Skal ikke burukes");
//    	if(psiTree.level() != level) {
//    		Util.IERR("ERROR: CheclLevel FAILED. "+psiTree.level() + " != " + level);
//    	}
    }

    ///  ????  ?????
    /// 
    ///  Invariant: Lexer'currentLexerToken is a ParserToken
    /// 
    /// Invariant: Lexer'parserToken is first token of construct
    /// 
    public void startSubtree(PsiTree.Kind kind, String debugName) {
    	lexTokenRange = new LexTokenRange(lexTokenRange);//, getSourceLineNumber(), sourceText, startOffset, debugName);
	}
		
	public void doneSubtree(PsiTree.Kind kind, SyntaxElement syntaxElement) {
//		IO.println("PsiBuilder.doneSubtree: syntaxElement="+syntaxElement);
		Vector<SyntaxElement> syntaxElements = new Vector<SyntaxElement>();
		if(syntaxElement != null) syntaxElements.add(syntaxElement);
		doneSubtree(kind, syntaxElements);
	}
	
	public void doneSubtree(PsiTree.Kind kind, Vector<SyntaxElement> syntaxElements) {
		IO.println("\nPsiBuilder.doneSubtree: lexTokenRange=]"+lexTokenRange.getDebugText()+'[');
		
		for(SyntaxElement syntaxElement:syntaxElements) {
			IO.println("PsiBuilder.doneSubtree: syntaxElement="+syntaxElement);
			syntaxElement.lexTokenRange = lexTokenRange;
		}
		lexTokenRange.syntaxElements = syntaxElements;

		lexTokenRange = lexTokenRange.parent;
	}
	
	public void dropSubtree(PsiTree.Kind kind, String debugName) {
		lexTokenRange = lexTokenRange.parent;
	}
	
	public void advanceLexer() {
		lexTokenRange.addChild(getCurrentLexerToken()); // Add 'old' current LexToken to the AstTree
		lexer.advance();                                // And the advance the lexer.			
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
		if(Option.TESTING_WITHOUT_PSI) Util.IERR("xxx - Skal ikke burukes");
//		IO.println("PsiBuilder.rollBackTo: "+prev);
//		IO.println("PsiBuilder.rollBackTo: CurrentLexerToken: "+getCurrentLexerToken());
//		IO.println("PsiBuilder.rollBackTo: LastParserChild: "+psiTree.getLastParserChild());
//		LOOP:while(true) {
//			IO.println("PsiBuilder.rollBackTo: REMOVE: last="+psiTree.getLastChild());
//			if(psiTree.removeLastChild() == prev) break LOOP;
//		}
//		lexer.rollBackToBefore(prev, debugInfo);
	}

	public LexToken prevToken() {
//		if(Option.TESTING_WITHOUT_PSI) {
			return lexer.getPrevLexerToken();
//		} else {
//			LexToken prev = psiTree.getLastChild();
//			return prev;
//		}
	}

	public LexToken prevParserToken() {
//		if(Option.TESTING_WITHOUT_PSI) {
			return lexer.getPrevParserToken();
//		} else {
//			LexToken prev = psiTree.getLastParserChild();
//			return prev;
//		}
	}

	public boolean eof() {
		return lexer.EOF != null;
	}

	public LexToken getCurrentLexerToken() {
		return lexer.getCurrentLexerToken();
	}

	public LexToken getPrevLexerToken() {
		return lexer.getPrevLexerToken();
	}

	/// Return next 'Parser' token.
	/// Skip Comment, Whitespace and Newline tokens.
    public LexToken getNextParserToken() {
		if(Option.TESTING_WITHOUT_PSI) Util.IERR("xxx - Skal ikke burukes");
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
	
	public void printPSI(String title) {
		if(Option.TESTING_WITHOUT_PSI) Util.IERR("xxx - Skal ikke burukes");
//		IO.println("printPSI: BEGIN *** "+title+" ***");
//		psiRoot.printPsiTree(title);
//		IO.println("printPSI: ENDOF *** "+title+" ***");
	}

}
