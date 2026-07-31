package simula.builder;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import simula.Option;
import simula.compiler.JarFileBuilder;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.SimulaCompiler;
import simula.compiler.syntaxClass.declaration.DeclarationScope;
import simula.compiler.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.SimulaDiagnostic;
import simula.compiler.utilities.Util;
import simula.lsp.compiler.DocumentManager;
import simula.token.LexToken;
import simula.token.SimpleString;

public class SimulaBuilder {

	final public DocumentManager documentManager;
    public SimulaLexer lexer;
    
    public SimulaCompiler simulaCompiler;
    
	public File generatedJarFile;
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
	public List<SimulaDiagnostic> diagnostics;
	public List<LexToken> tokenList;

	public SimulaBuilder(DocumentManager documentManager) {
		this.documentManager = documentManager;
    	// INIT:
    	this.diagnostics = new ArrayList<>();
    	this.tokenList   = new ArrayList<>();
        lexer = new SimulaLexer(this, documentManager.sourceCode);
        
//        if(args.outputDir != null) {
//        	outputDir = args.outputDir;
//        } else {
//        	outputDir = new File(documentManager.sourceFileDir, "bin");
//        }

		// Get Temp Directory:
		SimulaCompiler.simulaTempDir = CoreGlobal.getTempFileDir("simula/");
		deleteTempFiles(SimulaCompiler.simulaTempDir);

		File desktop = new File(System.getProperty("user.home"), "Desktop");
//		if (args.verbose) {
			// https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html
			Util.println("------------  SIMULA ENVIRONMENT SUMMARY  ------------");
			Util.println("Java Home            " + System.getProperty("java.home"));
			Util.println("User Home            " + System.getProperty("user.home"));
			Util.println("Working Directory    " + System.getProperty("user.dir"));
			String s = (desktop.exists()) ? "true " : "false";
			Util.println("Desktop Exists=" + s + " " + desktop.toString());
			Util.println("Java Class Path      " + System.getProperty("java.class.path"));
			Util.println("Java Class Version   " + System.getProperty("java.class.version"));
			Util.println("Java Version         " + System.getProperty("java.version"));
			Util.println("Java VM Spec Version " + System.getProperty("java.vm.specification.version"));
			Util.println("Java Vendor          " + System.getProperty("java.vendor"));
			Util.println("OS name              " + System.getProperty("os.name"));
			Util.println("OS architecture      " + System.getProperty("os.arch"));
			Util.println("OS version           " + System.getProperty("os.version"));
			Util.println("file.encoding        " + System.getProperty("file.encoding"));
			Util.println("defaultCharset       " + Charset.defaultCharset());
//			Util.println("compilerMode         " + args.compilerMode);

			// This will list the current system properties
			// System.getProperties().list(System.out);

//		}
			Util.println("------------  SIMULA VARIABLES SUMMARY  ------------");
			Util.println("DocumentManager.documentUri     " + documentManager.documentUri);
			Util.println("DocumentManager.sourceFileDir   " + documentManager.sourceFileDir);
			Util.println("DocumentManager.documentVersion " + documentManager.documentVersion);

			Option.print(" SIMULA VARIABLES SUMMARY");
//		Util.IERR("STOPP HER INNTIL VIDERE");
	}
	
	public void doBuilding() {
        simulaCompiler = new SimulaCompiler(documentManager);
        
		boolean builderTerminateNormally = simulaCompiler.doParsing(this);
    	
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
			simulaCompiler.doChecking(this);
		} else {
			Util.IERR("");
		}
//		Util.IERR("STOP HER INTILL VIDERE");	
		
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
		IO.println("SimulaLexer.saveCurrentToken: "+currentParserToken+", prevParserToken="+prevParserToken);
//		if (savedToken != null) Util.IERR("SimulaLexer.saveCurrentToken: Already called");
//		savedToken = getCurrentParserToken();
		
		rollBackIndex = tokenList.size() - 1;
		
		currentParserToken = prevParserToken;
		prevParserToken = null;
//    	Util.STOP();
	}

	public void	rollBackTo(LexToken prev, String debugInfo) {
//		IO.println("PsiBuilder.rollBackTo: "+prev);
//		IO.println("PsiBuilder.rollBackTo: CurrentParserToken: "+getCurrentParserToken());
//		IO.println("PsiBuilder.rollBackTo: =========== tokenList: BAKLENGS");
		int n = tokenList.size();
		rollBackIndex = -1;
		LOOP:for(int i=n-1;i>=0;i--) {
			IO.println("PsiBuilder.rollBackTo: token "+i+": "+tokenList.get(i));	
			if(tokenList.get(i) == prev) {
				IO.println("PsiBuilder.rollBackTo: FOUND: " + i + ": " + prev);	
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


	/// Delete temporary .class files.
	/// @param dir temporary .class directory
	private void deleteTempFiles(final File dir) {
		try {
			File[] elt = dir.listFiles();
			if (elt == null)
				return;
			for (File f : elt) {
				if (Option.internal.DEBUGGING) {
					if (f.isFile())
						Util.println("Delete: " + f);
				}
				if (f.isDirectory())
					deleteTempFiles(f);
				f.delete();
			}
		} catch (Exception e) {
			Util.IERR("SimulaCompiler.deleteFiles FAILED: ", e);
			e.printStackTrace();
		}
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
