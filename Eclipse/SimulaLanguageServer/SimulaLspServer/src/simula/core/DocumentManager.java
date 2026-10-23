package simula.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;

import simula.Comn;
import simula.core.builder.DocumentTextUpdater;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.SourceTextReader;
import simula.core.builder.token.LexToken;
import simula.core.builder.token.TokenManager;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.declaration.StandardClass;
import simula.core.syntaxClass.statement.ProgramModule;
import simula.core.utilities.LOG;
import simula.core.utilities.Util;
import simula.server.lsp.SimulaLanguageServer;

/// Vi må lagre innholdet til dokumentene som er åpne i editoren.
/// Siden editoren kan ha endringer som ikke er lagret til disken ennå,
/// må serveren stole på teksten den får tilsendt over LSP
/// 
/// @author Øystein Myhre Andersen
/// @author Google AI
public class DocumentManager {
	
    // Nøkkelen er filens URI (f.eks. file:///path/to/file.txt)
    private static final ConcurrentHashMap<String, DocumentManager> openDocuments = new ConcurrentHashMap<>();

	final public String documentUri;
	final public File sourceFileDir;
	public int documentVersion;
	
//	public String sourceCode;
	public List<String> sourceLines;
	
	public SimulaBuilder simBuilder;
	public SimulaCoder simCoder;
	

	/// The Compiler mode.
//	public CompilerMode compilerMode;
	public static boolean compileViaJavaSource;

	/// The source file name.
	public String sourceFileName;

	/// The source file name without .sim
	public String sourceName;
	
	/// The set of external .jar files.
	public Vector<String> externalJarFileNames;

	// Specifies where to place generated executable .jar file;
	// Also used to search for precompiled .jar attribute files
	public static File jarFileDir = null;
	
	// Specifies where to search for precompiled classes and procedures
	// If not found, jarFileDir is then searched
	public static File extLib = null;


	// ***************************************************************
	// *** Static variables
	// ***************************************************************

	/// The Simula release identification.
	/// 
	/// NOTE: When updating release id, change version in SimulaExtractor and RuntimeSystem
	public static final String simulaReleaseID = "Simula-2.0";

	/// Packet name used in generated .java files.
	/// NOTE: Must be a single identifier.
	public static String packetName = "simprog";

	/// Where to find the Simula Runtime System.
	public static File simulaRtsLib;
	
	/// Source file is case sensitive.
	public static boolean CaseSensitive=false;
	
	/// Output messages about what the compiler is doing.
	public static boolean verbose = false; 
	
	/// Generate warning messages
	public static boolean WARNINGS=true;

	/// TRUE:Do not create popUps at runtime
	public static boolean noPopup = false; 
	
	/// true: Don't execute generated .jar file
	public static boolean noExecution = false;
	
	/// false: Disable all language extensions. In other words,
	/// follow the Simula Standard literally
	public static boolean EXTENSIONS=true;

    public DocumentManager(String documentUri, int documentVersion, String sourceCode) {
    	this.documentUri = documentUri;
    	this.sourceFileDir = new File(documentUri).getParentFile();
    	this.documentVersion = documentVersion;
//    	this.sourceCode = sourceCode;    		    		
    	sourceFileName = this.documentUri;
		sourceName = getSourceName(this.documentUri);
		externalJarFileNames = new Vector<String>();
		compileViaJavaSource = false;
		StandardClass.INITIATE(this);
		createJarFilesDirectory();
		
//		sourceLines = sourceCode.lines()
//                .map(String::strip)
//                .toList();
		
		sourceLines = sourceCode.lines()
                .map(line -> line.stripTrailing() + "\n")
                .collect(Collectors.toList());
//		sourceLines = sourceCode.lines()
//                .map(line -> line.stripTrailing() + "\n")
//                .collect(Collectors.toCollection(LinkedList::new));
		sourceLines.add(""+(char)SourceTextReader.EOF_MARK);
		
//		for(String line:sourceLines) {
//			IO.println("Line|" + Comn.printable(line) + '|');
//		}
//		Util.STOP();
    }

	private void createJarFilesDirectory() {
    	// Create output .jar-files Directory
//		Util.println("DocumentManager.createJarFilesDirectory: sourceFileDir=" + documentManager.sourceFileDir);
//		Util.println("DocumentManager.createJarFilesDirectory: jarFileDir=" + SimulaCoder.jarFileDir);
    	if(this.jarFileDir == null) {
    		File userDir = new File(System.getProperty("user.dir"));
    		this.jarFileDir = new File(userDir,"bin");
    	}
    	LOG.trace("DocumentManager.createJarFilesDirectory: jarFileDir=" + this.jarFileDir);
    	this.jarFileDir.mkdirs();
    	if (! this.jarFileDir.canWrite()) {
    		Util.IERR("SimulaCompiler.setOutputDir: Unable to write to " + this.jarFileDir);
    	}    	
    }
    private String getSourceName(String documentUri) {
    	String sourceName = Util.getBaseName(documentUri);
		if (!Util.isJavaIdentifier(sourceName)) {
			String prevName = sourceName;
			sourceName = Util.makeJavaIdentifier(sourceName);
			Util.generalWarning("The source file name '" + prevName + "' is not a legal class identifier. Modified to: " + sourceName);
		}
    	return sourceName;
    }

    /// Debug Utility
    public static DocumentManager getDocumentManager(String documentUri)  {
    	DocumentManager res = openDocuments.get(documentUri);
    	if(res == null) Util.IERR("No such document: " + documentUri);
    	else LOG.trace("DocumentManager.getDocumentManager: documentUri ==> " + res.getClass());
    	return res;
    }

	/// Get the text document's Token List.
	public List<LexToken> getTokenList() {
		return simBuilder.lexTokenList;
	}

	/// Get the text document's getDiagnostic List.
	public List<Diagnostic> getDiagnostics() {
		return simBuilder.diagnostics;
	}

	/// Get the text document's SyntaxTree.
	public ProgramModule getSyntaxTree() {
		return simBuilder.syntaxTree;
	}

//	/// Set the text document's SyntaxTree.
//	public void setSyntaxTree(final ProgramModule syntaxTree) {
//		this.syntaxTree = syntaxTree;
//	}

	/// Get the text document's diagnostics.
	public List<Diagnostic> getDiagnostis() {
		return simBuilder.diagnostics;
	}
	
//	public void addDiagnostic(Diagnostic diagnostic) {
//		simBuilder.diagnostics.add(diagnostic);
//	}

//	/// Set the text document's diagnostics.
//	public void setDiagnostics(final List<Diagnostic> diagnostics) {
//		this.diagnostics = diagnostics;
//	}

	/// Get the text document's uri.
	public String getUri() {
		return documentUri;
	}

//	/// Set the text document's uri.
//	public void setUri(final String uri) {
//		documentUri = uri;
//	}

	/// Get the version number of this document (it will strictly increase after each change, including undo/redo).
	public int getVersion() {
		return documentVersion;
	}

	/// Set the version number of this document (it will strictly increase after each change, including undo/redo).
	public void setVersion(final int documentVersion) {
		this.documentVersion = documentVersion;
	}

	/// Get the content of the opened text document.
	public String getText() {
//		Util.IERR("DocumentManager.getText: NOT IMPL");
		for(String line:sourceLines) {
			LOG.trace("DocumentManager.getText: Line|" + Comn.printable(line) + '|');
		}
//		String sourceCode = String.join(System.lineSeparator(), sourceLines);
		String sourceCode = String.join("", sourceLines);
		return sourceCode;
	}

	/// Set the content of the opened text document.
	public void setText(final String text) {
//		Util.IERR("DocumentManager.setText: NOT IMPL");
		sourceLines = text.lines()
                .map(String::strip)
                .toList();
		for(String line:sourceLines) {
			LOG.trace("DocumentManager.setText: Line|" + Comn.printable(line) + '|');
		}
	}
    
	/// The document open notification is sent from the client to the server to
	/// signal newly opened text documents. The document's truth is now managed
	/// by the client and the server must not try to read the document's truth
	/// using the document's uri.
//    public static void didOpen(DidOpenTextDocumentParams params, SimulaLanguageServer server) {
    public static void didOpen(final String documentUri, final int version, final String sourceCode) {
//        public DocumentManager documentManager = DocumentManager.get(documentUri);
    	LOG.trace("DocumentManager.didOpen: BEGIN");
    	if(openDocuments.get(documentUri) != null) {
    		Util.IERR("DocumentManager.didOpen: DOKUMENTET FINNES FRA FØR: " + documentUri);
    	}
    	DocumentManager documentManager = new DocumentManager(documentUri, version, sourceCode);
    	openDocuments.put(documentUri, documentManager);
    	LOG.trace("DocumentManager.didOpen: openDocuments: " + openDocuments);
    	
//    	documentManager.tryCreateBuilder();

    	LOG.trace("DocumentManager.didOpen: RETURNS");
    }
    

    /// The document change notification is sent from the client to the server to
	/// signal changes to a text document.
//	public static void didChange(DidChangeTextDocumentParams params, SimulaLanguageServer server) {
	public static void didChange(final String documentUri, final List<TextDocumentContentChangeEvent> changes) {
    	LOG.trace("DocumentManager.didChange: BEGIN");
    	DocumentManager documentManager = getDocumentManager(documentUri);
    	documentManager.sourceLines = DocumentTextUpdater.applyChanges(documentManager.sourceLines, changes);
    	
//    	documentManager.tryCreateBuilder();

    	LOG.trace("DocumentManager.didChange: RETURNS");
	}

	
	///The document will save notification is sent from the client to the server before the document is actually saved.
//	public static void willSave(WillSaveTextDocumentParams params, SimulaLanguageServer server) {
	public static void willSave(final String documentUri, final String reason) {
//    	DocumentManager documentManager = openDocuments.get(documentUri);

		// 2. Add your pre-save routine here
		LOG.trace("Document is about to save: " + documentUri + " due to reason: " + reason);

		// Since TextDocumentSyncKind is 'Full', you already have the latest text state 
		// synchronized via your prior 'didChange' notification handlers.
		performPreSaveCleanup(documentUri);
	}

	private static void performPreSaveCleanup(String documentUri) {
		// Implement server-side logic here (e.g., locking states, clearing stale caches)
	}

	
	/// The document save notification is sent from the client to the server when
	/// the document is saved in the client.
//	public static void didSave(DidSaveTextDocumentParams params, SimulaLanguageServer server) {
	public static void didSave(final String documentUri) {
		
    	DocumentManager documentManager = getDocumentManager(documentUri);
		
		String fullText =  documentManager.getText();

		if (fullText == null) {
			// Fallback: If cache missed, you could read directly from the URI/disk
			return;
		}

		// 3. Trigger server-side logic (e.g., Compilation, Re-indexing, Validation)
		try {
//			DocumentManager.runCompilerOrValidator(documentUri, fullText);
//	    	documentManager.tryCreateBuilder();
		} catch (Exception e) {
			// Log your errors appropriately
			e.printStackTrace();
		}
	}
	

	/// The document close notification is sent from the client to the server
	/// when the document got closed in the client. The document's truth now
	/// exists where the document's uri points to (e.g. if the document's uri is
	/// a file uri the truth now exists on disk).
//	public static void didClose(DidCloseTextDocumentParams params, SimulaLanguageServer server) {
	public static void didClose(final String documentUri) {
		LOG.trace("DocumentManager.didClose: openDocuments: " + openDocuments);
        openDocuments.remove(documentUri);
        LOG.trace("DocumentManager.didClose: " + documentUri);
        LOG.trace("DocumentManager.didClose: openDocuments: " + openDocuments);
	}

	public void publishDiagnostics(List<Diagnostic> diagnostics) {
		PublishDiagnosticsParams params = new PublishDiagnosticsParams(documentUri, diagnostics);
		SimulaLanguageServer.languageClient.publishDiagnostics(params);
	}

	
	private final static boolean TESTING = true;
	/// The textDocument/semanticTokens/full request is sent from the client
	/// to the server to return the semantic tokens for a whole file.
	public static List<Integer> semanticTokensFull(String documentUri) {
		if(TESTING) Util.println("DocumentManager.semanticTokensFull: " + documentUri);
    	DocumentManager documentManager = getDocumentManager(documentUri);
    	documentManager.tryCreateBuilder();
    	List<Integer> semanticTokens = documentManager.simBuilder.semTokenList;
    	LOG.trace("DocumentManager.semanticTokensFull: semanticTokens.size=" + semanticTokens.size());
//		TokenManager.validateSemanticTokens(semanticTokens, TokenManager.STANDARD_TOKEN_TYPES.size() - 1);
		return semanticTokens;
	}
	
	private void tryCreateBuilder() {
    	LOG.trace("DocumentManager.tryCreateBuilder: BEGIN");
//		String sourceText = this.getText();
		SimulaBuilder newSimBuilder = new SimulaBuilder(this);
		// TRY BUILD SYNTAX TREE ...
//		try {
			newSimBuilder.doBuilding();
//    		Util.println("DocumentManager.tryCreateBuilder: " + Comn.printable(sourceText));
    		simBuilder = newSimBuilder;
//		} catch (Exception e) {
//			Util.println("DocumentManager.tryCreateBuilder: GOT EXCEPTION: " + e.getMessage());
//			e.printStackTrace();
//		}
	}

	// ***************************************************************
	// *** Semantic Checker
	// ***************************************************************
//	public void semanticChecker() {
//		ProgramModule  programModule = this.getSyntaxTree();
//		if (Option.internal.TRACING)
//			Util.println("BEGIN Semantic Checker");
//		documentManager.simBuilder.duringChecking = true;
//		programModule.doChecking();
//		if (Option.internal.TRACING) {
//			Util.println("END Semantic Checker: \"" + programModule + "\"");
//			if (Option.internal.TRACE_CHECKER_OUTPUT && programModule != null)
//				programModule.print(0);
//		}
//		if(SimulaCompiler.verbose) Util.println("SimulaCompiler.doCompile: " + SimulaCompiler.sourceName + ": Semantic Checker completed");
//		documentManager.simBuilder.duringChecking = false;
//		if(Option.internal.PRINT_SYNTAX_TREE > 0) {
//			Util.println("\nSimulaCompiler.doCompile: =========== Resulting Syntax Tree after Checking ================");
//			programModule.printTree(1);
//		}
//		
//		if (Util.nError > 0) {
//			String msg="Compiler terminate " + SimulaCompiler.sourceName + " after " + Util.nError + " errors during semantic checking";
//			Util.println(msg);
////			Thread.dumpStack();
//			throw new RuntimeException(msg);
//		}
//		
//	}


	/// Delete temporary .class files.
	/// @param dir temporary .class directory
	public static void deleteTempFiles(final File dir) {
		if(DocumentManager.verbose) {
			Util.println("SimulaCompiler.deleteTempFiles:  Delete: " + dir);
//			Thread.dumpStack();
		}
        if (! dir.exists()) {
            Util.IERR("File does not exist: " + dir);
            return;
        }
		Path path = dir.toPath();
        try { Files.walk(path)
	             // Sorts in reverse order (subfolders and files first)
	             .sorted(Comparator.reverseOrder())
	             .forEach(p -> {
	                 try {
	             		if(DocumentManager.verbose) {
	             			Util.println("SimulaCompiler.deleteTempFiles: Delete: " + p);
	             		}
	                     Files.delete(p);
	                 } catch (IOException e) {
	                     Util.IERR("Could not delete: " + p + " - " + e.getMessage());
	                 }
	             });
		} catch (Exception e) {
			Util.IERR("SimulaCompiler.deleteFiles FAILED: ", e);
			e.printStackTrace();
		}
    }

}
