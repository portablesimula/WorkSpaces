package simula.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import simula.Option;
import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.syntaxClass.declaration.StandardClass;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.SimulaDiagnostic;
import simula.compiler.utilities.Util;
import simula.exception.EOTException;
import simula.lsp.compiler.DocumentManager;
import simula.token.LexToken;
import simula.token.SimpleString;

public class SimulaBuilder {
	public DocumentManager documentManager;
    private SimulaLexer lexer;
	
    private LexToken prevParserToken;
    private LexToken currentParserToken;
	/// The saved Token used by 'saveCurrentToken'
	private LexToken savedToken;

	// Builder generated data structure:
	public ProgramModule syntaxTree; // Root of Syntax Tree
	public List<SimulaDiagnostic> diagnostics;
	public List<LexToken> tokenList;

    private LexTokenRange lexTokenRange;

	public SimulaBuilder(DocumentManager documentManager) {
		boolean builderTerminateNormally = false;
		this.documentManager = documentManager;
    	// INIT:
    	this.diagnostics = new ArrayList<>();
    	this.tokenList   = new ArrayList<>();
		lexTokenRange = new LexTokenRange(null);
        lexer = new SimulaLexer(this, documentManager.sourceCode);
        // Do the actual Building
        getNextParserToken();
    	syntaxTree = new ProgramModule(this);
        try {
        	syntaxTree.doBuild();
        	builderTerminateNormally = true;
        } catch(EOTException e) {
			System.err.println("SimulaBuilder: GOT EXCEPTION: " + e.getMessage());
//			e.printStackTrace();
			lexer.flush();
        }

    	LOG.info("SimulaBuilder: syntaxTree, tokenList and diagnostics DONE");
    	IO.println("SimulaBuilder: this.syntaxTree: "+this.syntaxTree); // Root of Syntax Tree
    	IO.println("SimulaBuilder: this.diagnostics: "+this.diagnostics);
    	IO.println("SimulaBuilder: this.tokenList: "+this.tokenList);
		
    	printAll(" AFTER NEW SimulaBuilder: ");
		
    	if(Option.LEX_VERIFY) {
        	IO.println("SimulaBuilder: documentManager.sourceCode: "+documentManager.sourceCode);
    		StringBuilder sb = new StringBuilder();
    		for(LexToken token : tokenList)	sb.append(token.getText());
    		String reconstr = sb.toString().replace("\r", "\\r").replace("\n", "\\n");
    		String original = documentManager.sourceCode.replace("\r", "\\r").replace("\n", "\\n");
    		int lng1 = documentManager.sourceCode.length();
    		if(! reconstr.equals(original)) {
    			LOG.error("SimulaBuilder: VERIFIER FAILED: Reconstructed text differ from original text");
    			LOG.error("Original Text(lng:"+lng1+"): " + original);
    			LOG.error("Reconstr Text(lng:"+sb.length()+"): " + reconstr);
    			Util.IERR("");
    		}
    	}
    	
//		Util.IERR("STOP HER INTILL VIDERE");	
		if(builderTerminateNormally) {	
			StandardClass.ENVIRONMENT.doChecking();
			CoreGlobal.duringParsing = false;
			this.syntaxTree.doChecking();
		} else {
			
		}
		Util.IERR("STOP HER INTILL VIDERE");	
	}
	
	public void addDiagnostic(SimulaDiagnostic diagnostic) {
		diagnostics.add(diagnostic);
	}
	
    public int getSourceLineNumber() {
    	return lexer.getSourceLineNumber();
    }

    ///  ????  ?????
    /// 
    ///  Invariant: Lexer'currentLexerToken is a ParserToken
    /// 
    /// Invariant: Lexer'parserToken is first token of construct
    /// 
    public void startTokenRange() {}
    public void startTokenRange(String debugName) {
//    	LexToken first = this.prevParserToken;
    	LexToken first = this.currentParserToken;
    	IO.println("SimulaBuilder.startTokenRange: " + debugName + ", first=" + first);
    	
    	
    }
//    public void startTokenRange(PsiTree.Kind kind, String debugName) {
    public void startTokenRange(String debugName,LexToken first) {
//    	LexToken first = this.prevParserToken;
//    	LexToken first = this.currentParserToken;
    	IO.println("SimulaBuilder.startTokenRange: " + debugName + ", first=" + first);
    	lexTokenRange = new LexTokenRange(lexTokenRange);
    	lexTokenRange.addChild(first);
	}
    public void startTokenRange(LexToken first) {
//    	LexToken first = this.prevParserToken;
//    	LexToken first = this.currentParserToken;
    	IO.println("SimulaBuilder.startTokenRange: " + ", first=" + first + Util.calledFrom(3, 25));
    	lexTokenRange = new LexTokenRange(lexTokenRange);
    	lexTokenRange.addChild(first);
	}
		
	public void doneTokenRange(SyntaxElement syntaxElement) {
		IO.println("PsiBuilder.doneTokenRange: syntaxElement="+syntaxElement);
//		Vector<SyntaxElement> syntaxElements = new Vector<SyntaxElement>();
//		if(syntaxElement != null) syntaxElements.add(syntaxElement);
//		doneTokenRange(syntaxElements);
	}
	
	public void doneTokenRange(Vector<SyntaxElement> syntaxElements) {
		IO.println("\nPsiBuilder.doneTokenRange: lexTokenRange=]"+lexTokenRange.getDebugText()+'[');
		
		for(SyntaxElement syntaxElement:syntaxElements) {
			IO.println("PsiBuilder.doneTokenRange: syntaxElement="+syntaxElement);
			syntaxElement.lexTokenRange = lexTokenRange;
		}
		lexTokenRange.syntaxElements = syntaxElements;

		lexTokenRange = lexTokenRange.parent;
	}
	
	public void dropTokenRange() {
//		lexTokenRange = lexTokenRange.parent;
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
		getNextParserToken();
	}
	
	public void setParsingBoundPairList(boolean parsingBoundPairList) {
		lexer.setParsingBoundPairList(parsingBoundPairList);
	}

	public void	rollBackTo(LexToken prev, String debugInfo) {
		Util.IERR("xxx - Skal ikke burukes");
//		IO.println("PsiBuilder.rollBackTo: "+prev);
//		IO.println("PsiBuilder.rollBackTo: CurrentLexerToken: "+getCurrentLexerToken());
//		IO.println("PsiBuilder.rollBackTo: LastParserChild: "+psiTree.getLastParserChild());
//		LOOP:while(true) {
//			IO.println("PsiBuilder.rollBackTo: REMOVE: last="+psiTree.getLastChild());
//			if(psiTree.removeLastChild() == prev) break LOOP;
//		}
//		lexer.rollBackToBefore(prev, debugInfo);
	}

//	public LexToken prevToken() {
//		return lexer.getPrevLexerToken();
//	}

//	public LexToken prevParserToken() {
//		return lexer.getPrevParserToken();
//	}

	/// Return next 'Parser' token.
	/// Skip Comment, Whitespace and Newline tokens.
	public LexToken getNextParserToken() {
		prevParserToken = currentParserToken;
//		lexTokenRange.addChild(currentParserToken);      // Add 'old' current LexToken to the lexTokenRange
    	if(savedToken != null) {
    		currentParserToken = savedToken;
    		savedToken = null;
    	} else currentParserToken = lexer.getNextParserToken(); // And then advance the lexer.	
    	return currentParserToken;
	}

	/// Return previous 'Parser' token.
	/// Skip Comment, Whitespace and Newline tokens.
    public LexToken getPrevParserToken() {
    	return prevParserToken;
    }

	/// Return current 'Parser' token.
	/// Skip Comment, Whitespace and Newline tokens.
    public LexToken getCurrentParserToken() {
    	return currentParserToken;
    }

	/// Save current Token
	public void saveCurrentToken() {
		IO.println("SimulaLexer.saveCurrentToken: "+currentParserToken+", prevParserToken="+prevParserToken);
		if (savedToken != null) Util.IERR("SimulaLexer.saveCurrentToken: Already called");
		savedToken = getCurrentParserToken();
		currentParserToken = prevParserToken;
		prevParserToken = null;
//    	Util.STOP();
	}

	public boolean eof() {
//		return lexer.EOF != null;
		return lexer.eof();
	}

//	public LexToken getCurrentLexerToken() {
//		return lexer.getCurrentLexerToken();
//	}
//
//	public LexToken getPrevLexerToken() {
//		return lexer.getPrevLexerToken();
//	}
    
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
//		Util.IERR("SJEKK DETTE");
		LexToken nextToken = prevToken;
		IO.println("\n\nPsiBuilder.getTextString: nextToken: "+nextToken);
		String result = ((SimpleString)prevToken).value;
    	while(getCurrentParserToken() instanceof SimpleString str) {
    		result += str.value;
    		IO.println("PsiBuilder.getTextString: RESULT: "+result);
        	IO.println("PsiBuilder.getTextString: NEXT TOKEN: "+nextToken);
//    		Util.IERR("SJEKK DETTE");
        	getNextParserToken();
    	}
    	IO.println("SimulaBuilder.getTextString: RETURN TEXT: ]"+result+"[\n\n");
    	return result;
    }

    
	public void printAll(String title) {
    	IO.println("\n");
		printSyntaxTree(title);
		printDiagnostics(title);
		printTokenList(title);
	}
	
	public void printSyntaxTree(String title) {
		IO.println("======================================== BEGIN SYNTAX TREE: " + title + " ============================ ");
		if(syntaxTree.mainModule != null) syntaxTree.print(0);
		IO.println("======================================== ENDOF SYNTAX TREE: " + title + " ============================ ");
	}

	public void printDiagnostics(String title) {
		LOG.info("++++++++++++++++ BEGIN DIAGNOSTICS: " + title + " ++++++++++++++++++");
		for(SimulaDiagnostic diagnostic:diagnostics) LOG.info(diagnostic.toString());			
		LOG.info("++++++++++++++++ ENDOF DIAGNOSTICS: " + title + " ++++++++++++++++++");
	}
	
	public void printTokenList(String title) {
		IO.println("======================================== BEGIN TOKEN LIST: " + title + " ============================ ");
		for(LexToken token:tokenList) {
			IO.println(""+token);
		}
		IO.println("======================================== ENDOF TOKEN LIST: " + title + " ============================ ");		
	}

}
