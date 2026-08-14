package simula.compiler;

import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import simula.SimTextDocumentContentChangeEvent;
import simula.builder.SimulaBuilder;
import simula.compiler.syntaxClass.declaration.StandardClass;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.SimulaDiagnostic;
import simula.compiler.utilities.Util;
import simula.token.LexToken;

/// Vi må lagre innholdet til dokumentene som er åpne i editoren.
/// Siden editoren kan ha endringer som ikke er lagret til disken ennå,
/// må serveren stole på teksten den får tilsendt over LSP
/// 
/// @author Øystein Myhre Andersen
/// @author Google AI
public class DocumentManager {

	final public String documentUri;
	final public File sourceFileDir;
	public int documentVersion;
//	final public String sourceName; // The source file name without .sim
	public String sourceCode;
	public SimulaBuilder simBuilder;
	
	
	/// The source file name.
	public static String sourceFileName;

	/// The source file name without .sim
	public static String sourceName;
	
	/// The set of external .jar files.
	public static Vector<File> externalJarFiles;

	
    // Nøkkelen er filens URI (f.eks. file:///path/to/file.txt)
//    private final ConcurrentHashMap<String, SourceDocumentItem> openDocuments = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, DocumentManager> openDocuments = new ConcurrentHashMap<>();

    public DocumentManager(String documentUri, int documentVersion, String sourceCode) {
    	this.documentUri = documentUri;
    	this.sourceFileDir = new File(documentUri).getParentFile();
    	this.documentVersion = documentVersion;
    	this.sourceCode = sourceCode;
    	sourceFileName = this.documentUri;
		sourceName = getSourceName(this.documentUri);
		this.externalJarFiles = new Vector<File>();
		StandardClass.INITIATE();
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
    	return openDocuments.get(documentUri);
    }

	/// Get the text document's Token List.
	public List<LexToken> getTokenList() {
		return simBuilder.tokenList;
	}

	/// Get the text document's getDiagnostic List.
	public List<SimulaDiagnostic> getDiagnostics() {
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
	public List<SimulaDiagnostic> getDiagnostis() {
		return simBuilder.diagnostics;
	}
	
//	public void addDiagnostic(SimulaDiagnostic diagnostic) {
//		simBuilder.diagnostics.add(diagnostic);
//	}

//	/// Set the text document's diagnostics.
//	public void setDiagnostics(final List<SimulaDiagnostic> diagnostics) {
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
		return sourceCode;
	}

	/// Set the content of the opened text document.
	public void setText(final String text) {
		sourceCode = text;
	}
    
	/// The document open notification is sent from the client to the server to
	/// signal newly opened text documents. The document's truth is now managed
	/// by the client and the server must not try to read the document's truth
	/// using the document's uri.
//    public static void didOpen(DidOpenTextDocumentParams params, SimulaLanguageServer server) {
    public static void didOpen(final String documentUri, final int version, final String sourceCode) {
//        public DocumentManager documentManager = DocumentManager.get(documentUri);
    	LOG.info("DocumentManager.didOpen: BEGIN");
    	if(openDocuments.get(documentUri) != null) {
    		Util.IERR("DOKUMENTET FINNES FRA FØR");
    	}
    	DocumentManager documentManager = new DocumentManager(documentUri, version, sourceCode);
    	openDocuments.put(documentUri, documentManager);
    	
    	documentManager.tryCreateBuilder();

    	LOG.info("DocumentManager.didOpen: RETURNS");
    }
    

    /// The document change notification is sent from the client to the server to
	/// signal changes to a text document.
//	public static void didChange(DidChangeTextDocumentParams params, SimulaLanguageServer server) {
	public static void didChange(final String documentUri, final List<SimTextDocumentContentChangeEvent> changes) {
    	LOG.info("DocumentManager.didChange: BEGIN");
    	DocumentManager documentManager = openDocuments.get(documentUri);
		
//		String currentText = sourceItem.getText();
		String currentText = documentManager.getText();
    	LOG.info("DocumentManager.didChange: Current Text: " + currentText);

//    	String updatedText = DocumentTextUpdater.applyChanges(currentText, params.getContentChanges());
    	String updatedText = DocumentTextUpdater.applyChanges(currentText, changes);
    	LOG.info("DocumentManager.didChange: Updated Text: " + updatedText);
    	documentManager.setText(updatedText);
    	
    	documentManager.tryCreateBuilder();

    	LOG.info("DocumentManager.didChange: RETURNS");
	}

	
	///The document will save notification is sent from the client to the server before the document is actually saved.
//	public static void willSave(WillSaveTextDocumentParams params, SimulaLanguageServer server) {
	public static void willSave(final String documentUri, final String reason) {
//    	DocumentManager documentManager = openDocuments.get(documentUri);

		// 2. Add your pre-save routine here
		LOG.info("Document is about to save: " + documentUri + " due to reason: " + reason);

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
		
    	DocumentManager documentManager = openDocuments.get(documentUri);
		
		String fullText =  documentManager.getText();

		if (fullText == null) {
			// Fallback: If cache missed, you could read directly from the URI/disk
			return;
		}

		// 3. Trigger server-side logic (e.g., Compilation, Re-indexing, Validation)
		try {
//			DocumentManager.runCompilerOrValidator(documentUri, fullText);
	    	documentManager.tryCreateBuilder();
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
		
//    	DocumentManager documentManager = openDocuments.get(documentUri);
        openDocuments.remove(documentUri);

		System.out.println("Document closed on client side: " + documentUri);
	}
	

	public void tryCreateBuilder() {
    	LOG.info("DocumentManager.tryCreateBuilder: BEGIN");
//		String sourceText = this.getText();
		SimulaBuilder newSimBuilder = new SimulaBuilder(this);
		// TRY BUILD SYNTAX TREE ...
		try {
			newSimBuilder.doBuilding();
//    		IO.println("DocumentManager.tryCreateBuilder: " + Util.printable(sourceText));
    		simBuilder = newSimBuilder;
		} catch (Exception e) {
			IO.println("DocumentManager.tryCreateBuilder: GOT EXCEPTION: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// ***************************************************************
	// *** Semantic Checker
	// ***************************************************************
//	public void semanticChecker() {
//		ProgramModule  programModule = this.getSyntaxTree();
//		if (Option.internal.TRACING)
//			IO.println("BEGIN Semantic Checker");
//		simBuilder.duringChecking = true;
//		programModule.doChecking();
//		if (Option.internal.TRACING) {
//			IO.println("END Semantic Checker: \"" + programModule + "\"");
//			if (Option.internal.TRACE_CHECKER_OUTPUT && programModule != null)
//				programModule.print(0);
//		}
//		if(SimulaCompiler.verbose) IO.println("SimulaCompiler.doCompile: " + SimulaCompiler.sourceName + ": Semantic Checker completed");
//		simBuilder.duringChecking = false;
//		if(Option.internal.PRINT_SYNTAX_TREE > 0) {
//			IO.println("\nSimulaCompiler.doCompile: =========== Resulting Syntax Tree after Checking ================");
//			programModule.printTree(1);
//		}
//		
//		if (Util.nError > 0) {
//			String msg="Compiler terminate " + SimulaCompiler.sourceName + " after " + Util.nError + " errors during semantic checking";
//			IO.println(msg);
////			Thread.dumpStack();
//			throw new RuntimeException(msg);
//		}
//		
//	}

}
