package simula.core.builder;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import simula.Option;
import simula.core.DocumentManager;
import simula.core.builder.export.LexToken;
import simula.core.builder.export.SimulaDiagnostic;
import simula.core.builder.util.SimpleString;
import simula.core.DocumentManager;
import simula.core.syntaxClass.declaration.DeclarationScope;
import simula.core.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.core.syntaxClass.declaration.StandardClass;
import simula.core.syntaxClass.statement.ProgramModule;
import simula.core.utilities.KeyWord;
import simula.core.utilities.LOG;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;
import simula.exception.EOTException;

public class SimulaBuilder {

	final public DocumentManager documentManager;
	
    public SimulaLexer lexer;
    
    public DocumentManager simulaCompiler;
    
	/// Compiler state: True while Parsing
	public boolean duringParsing;

	/// Compiler state: True while Checking
	public boolean duringChecking;
	
	
    private LexToken prevParserToken;
    private LexToken currentParserToken;
//	/// The saved Token used by 'saveCurrentToken'
//	private LexToken savedToken;
	/// The rollBackIndex Token used by 'saveCurrentToken', 'rollBackTo' and 'getNextParserToken'
	private int rollBackIndex;

	// Builder generated data structure:
	public ProgramModule syntaxTree; // Root of Syntax Tree
	public int nErrors;
	public List<SimulaDiagnostic> diagnostics;
	public List<LexToken> tokenList;

	public SimulaBuilder(DocumentManager documentManager) {
		this.documentManager = documentManager;
		documentManager.simBuilder = this;
    	// INIT:
		this.nErrors = 0;
    	this.diagnostics = new ArrayList<>();
    	this.tokenList   = new ArrayList<>();
        lexer = new SimulaLexer(this, documentManager.sourceCode);

		File desktop = new File(System.getProperty("user.home"), "Desktop");
		if (DocumentManager.verbose) {
			// https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html
			IO.println("------------  SIMULA ENVIRONMENT SUMMARY  ------------");
			IO.println("Java Home            " + System.getProperty("java.home"));
			IO.println("User Home            " + System.getProperty("user.home"));
			IO.println("Working Directory    " + System.getProperty("user.dir"));
			String s = (desktop.exists()) ? "true " : "false";
			IO.println("Desktop Exists=" + s + " " + desktop.toString());
			IO.println("Java Class Path      " + System.getProperty("java.class.path"));
			IO.println("Java Class Version   " + System.getProperty("java.class.version"));
			IO.println("Java Version         " + System.getProperty("java.version"));
			IO.println("Java VM Spec Version " + System.getProperty("java.vm.specification.version"));
			IO.println("Java Vendor          " + System.getProperty("java.vendor"));
			IO.println("OS name              " + System.getProperty("os.name"));
			IO.println("OS architecture      " + System.getProperty("os.arch"));
			IO.println("OS version           " + System.getProperty("os.version"));
			IO.println("file.encoding        " + System.getProperty("file.encoding"));
			IO.println("defaultCharset       " + Charset.defaultCharset());
//			IO.println("compilerMode         " + args.compilerMode);

			// This will list the current system properties
			// System.getProperties().list(System.out);

			IO.println("------------  SIMULA VARIABLES SUMMARY  ------------");
			IO.println("DocumentManager.sourceFileName     " + documentManager.documentUri);
			IO.println("DocumentManager.sourceFileDir   " + documentManager.sourceFileDir);
			IO.println("DocumentManager.documentVersion " + documentManager.documentVersion);

			Option.print(" SIMULA VARIABLES SUMMARY");
		}
	}
	
	public void doBuilding() {
		boolean builderTerminateNormally = this.doParsing(this);
    	
    	if(Option.LEX_VERIFY) {
//        	IO.println("SimulaBuilder: documentManager.sourceCode: "+documentManager.sourceCode);
    		StringBuilder sb = new StringBuilder();
    		for(LexToken token : tokenList)	sb.append(token.getText());
    		String reconstr = Util.printable(sb.toString());
    		String original = Util.printable(documentManager.sourceCode);
    		int lng1 = original.length();
    		if(! reconstr.equals(original)) {
    			int lng2 = reconstr.length();
    			LOG.error("SimulaBuilder: VERIFIER FAILED: Reconstructed text differ from original text");
    			LOG.error("Original Text(lng:"+lng1+"): " + original);
    			LOG.error("Reconstr Text(lng:"+lng2+"): " + reconstr);
    			int n = Math.min(lng1, lng2);
    			LOOP:for(int i=0;i<n;i++) {
    				if(reconstr.charAt(i) != original.charAt(i)) { 
    	    			LOG.error("First deviation at pos " + i + ", original: " + original.charAt(i) + ", reconstr: " + reconstr.charAt(i));
    					break LOOP;
    				}
    			}
    			if(lng1 != lng2) {
    				int pos = Math.max(0, n - 100);
        			LOG.error("Original Tail: " + original.substring(pos));
        			LOG.error("Reconstr Tail: " + reconstr.substring(pos));
    			}
    			Util.IERR("");
    		}
    	}

//		Util.IERR("STOP HER INTILL VIDERE");	
		if(builderTerminateNormally) {	
//			simulaCompiler.doChecking(this);
			this.doChecking();
		} else {
			Util.IERR("");
		}
//		Util.IERR("STOP HER INTILL VIDERE");	
		documentManager.publishDiagnostics(diagnostics);
	}

	// ***************************************************************
	// *** Scanning and Parsing
	// ***************************************************************
	public boolean doParsing(SimulaBuilder simBuilder) {
		boolean builderTerminateNormally = false;
		documentManager.simBuilder.duringParsing = true;
    	LOG.info("SimulaBuilder.doParsing: BEGIN");
    	
        // Do the actual Building
		simBuilder.getNextParserToken();
		simBuilder.syntaxTree = new ProgramModule(simBuilder.documentManager);
        try {
        	simBuilder.syntaxTree.doBuild();
        	builderTerminateNormally = true;
        } catch(EOTException e) {
			System.err.println("SimulaBuilder: GOT EXCEPTION: " + e.getMessage());
//			e.printStackTrace();
			simBuilder.lexer.flush();
        }

    	LOG.info("SimulaBuilder: syntaxTree, tokenList and diagnostics DONE");
//    	IO.println("SimulaBuilder: this.syntaxTree: "+simBuilder.syntaxTree); // Root of Syntax Tree
//    	IO.println("SimulaBuilder: this.diagnostics: "+simBuilder.diagnostics);
//    	IO.println("SimulaBuilder: this.tokenList: "+simBuilder.tokenList);
//		
//    	simBuilder.printAll(" AFTER NEW SimulaBuilder: ");
		
    	return builderTerminateNormally;
	}

	// ***************************************************************
	// *** Semantic Checker
	// ***************************************************************
	public void doChecking() {
		if (Option.internal.TRACING)
			IO.println("BEGIN Semantic Checker");
		documentManager.simBuilder.duringParsing = false;
		documentManager.simBuilder.duringChecking = true;
    	LOG.info("SimulaBuilder.doChecking: BEGIN");
		StandardClass.ENVIRONMENT.doChecking();
		ProgramModule programModule = documentManager.simBuilder.syntaxTree;
		programModule.doChecking();
		
//		programModule.doChecking();
		if (Option.internal.TRACING) {
			IO.println("END Semantic Checker: \"" + programModule + "\"");
			if (Option.internal.TRACE_CHECKER_OUTPUT && programModule != null)
				programModule.print(0);
		}
		if(DocumentManager.verbose) IO.println("SimulaBuilder.doChecking: " + documentManager.simBuilder.documentManager.sourceName + ": Semantic Checker completed");
		documentManager.simBuilder.duringChecking = false;
		if(Option.internal.PRINT_SYNTAX_TREE > 0) {
			IO.println("\nSimulaCompiler.doCompile: =========== Resulting Syntax Tree after Checking ================");
			programModule.printTree(1);
		}
		
		if (nErrors > 0) {
			String msg="Compiler terminate " + documentManager.simBuilder.documentManager.sourceName + " after " + nErrors + " errors during semantic checking";
			IO.println(msg);
//			Thread.dumpStack();
			throw new RuntimeException(msg);
		}

	}


	public void addError(SimulaDiagnostic diagnostic) {
		diagnostics.add(diagnostic);
		nErrors++;
	}

	public void addDiagnostic(SimulaDiagnostic diagnostic) {
		diagnostics.add(diagnostic);
	}
	
    public int getSourceLineNumber() {
    	return lexer.getSourceLineNumber();
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
		rollBackIndex = tokenList.size() - 1;
		
		currentParserToken = prevParserToken;
		prevParserToken = null;
	}

	public void	rollBackTo(LexToken prev, String debugInfo) {
//		IO.println("PsiBuilder.rollBackTo: "+prev);
//		IO.println("PsiBuilder.rollBackTo: CurrentParserToken: "+getCurrentParserToken());
//		IO.println("PsiBuilder.rollBackTo: =========== tokenList: BAKLENGS");
		int n = tokenList.size();
		rollBackIndex = -1;
		LOOP:for(int i=n-1;i>=0;i--) {
//			IO.println("PsiBuilder.rollBackTo: token "+i+": "+tokenList.get(i));	
			if(tokenList.get(i) == prev) {
//				IO.println("PsiBuilder.rollBackTo: FOUND: " + i + ": " + prev);	
				rollBackIndex = i + 1;
				break LOOP;
			}
		}
		if(rollBackIndex < 0) Util.IERR("");
		currentParserToken = prev;
//		IO.println("PsiBuilder.rollBackTo: END rollBackIndex="+rollBackIndex);
	}

	/// Return next 'Parser' token.
	/// Skip Comment, Whitespace and Newline tokens.
	public LexToken getNextParserToken() {
		prevParserToken = currentParserToken;
		if(rollBackIndex > 0) {
			do { currentParserToken = tokenList.get(rollBackIndex++);
			} while(! currentParserToken.isParserToken());
			if(rollBackIndex >= tokenList.size()) rollBackIndex = 0;
//			IO.println("PsiBuilder.getNextParserToken: currentParserToken="+currentParserToken);
    	} else currentParserToken = lexer.getNextParserToken(); // And then advance the lexer.	
    	return currentParserToken;
	}

	public boolean eof() {
		return lexer.eof();
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
//		Util.IERR("SJEKK DETTE");
		LexToken nextToken = prevToken;
//		IO.println("\n\nSimulaBuilder.getTextString: nextToken: "+nextToken);
		String result = ((SimpleString)prevToken).value;
    	while(getCurrentParserToken() instanceof SimpleString str) {
    		result += str.value;
//    		IO.println("SimulaBuilder.getTextString: RESULT: "+result);
//        	IO.println("SimulaBuilder.getTextString: NEXT TOKEN: "+nextToken);
//    		Util.IERR("SJEKK DETTE");
        	getNextParserToken();
    	}
//    	IO.println("SimulaBuilder.getTextString: RETURN TEXT: ]"+result+"[\n\n");
    	return result;
    }
    
	public void printAll(String title) {
    	IO.println("\n");
//    	dumpSyntaxTree();
		printSyntaxTree(title);
		printDiagnostics(title);
		printTokenList(title);
//		Util.STOP();
	}
	
	private void dumpSyntaxTree() {
		IO.println("SimulaBuilder.dumpSyntaxTree: ProgramModule: " + syntaxTree);
		DeclarationScope mainModule = syntaxTree.mainModule;
		IO.println("SimulaBuilder.dumpSyntaxTree: ProgramModule.mainModule: " + mainModule.getClass().getSimpleName() + " " + mainModule);
		if(mainModule instanceof MaybeBlockDeclaration blk) {
			IO.println("SimulaBuilder.dumpSyntaxTree: Block: " + ObjectKind.edit(blk.declarationKind));
//			blk.print(4);
			blk.printTree(4);
		}
		Util.STOP();
	}
	
	public void printSyntaxTree(String title) {
		IO.println("======================================== BEGIN SYNTAX TREE: " + title + " ============================ ");
//		if(syntaxTree.mainModule != null) syntaxTree.print(0);
		if(syntaxTree.mainModule != null) syntaxTree.printTree(0);
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
