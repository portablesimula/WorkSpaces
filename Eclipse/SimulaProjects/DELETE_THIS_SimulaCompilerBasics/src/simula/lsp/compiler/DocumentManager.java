package simula.lsp.compiler;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import simula.builder.SimulaBuilder;
import simula.compiler.syntaxClass.declaration.StandardClass;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.SimulaDiagnostic;
import simula.compiler.utilities.Util;

/// Vi må lagre innholdet til dokumentene som er åpne i editoren.
/// Siden editoren kan ha endringer som ikke er lagret til disken ennå,
/// må serveren stole på teksten den får tilsendt over LSP
/// 
/// @author Øystein Myhre Andersen
/// @author Google AI
public class DocumentManager {

	private String documentUri;
	private int version;
	public String sourceCode;
	
	public ProgramModule syntaxTree; // Root of Syntax Tree
	public List<SimulaDiagnostic> diagnostics;
	public List<LspToken> tokenList;
	
    // Nøkkelen er filens URI (f.eks. file:///path/to/file.txt)
//    private final ConcurrentHashMap<String, SourceDocumentItem> openDocuments = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, DocumentManager> openDocuments = new ConcurrentHashMap<>();

//    public void put(String documentUri, DocumentManager document) {
//        openDocuments.put(documentUri, document);
//    }
//
//    public DocumentManager get(String documentUri) {
//        return openDocuments.get(documentUri);
//    }
//
//    public void remove(String documentUri) {
//		if (openDocuments.containsKey(documentUri))
//        openDocuments.remove(documentUri);
//    }

    public DocumentManager(String documentUri, int version, String sourceCode) {
    	this.documentUri = documentUri;
    	this.version = version;
    	this.sourceCode = sourceCode;
    }

    /// Debug Utility
    public static DocumentManager GetDocumentManager(String documentUri) {
    	return openDocuments.get(documentUri);
    }
    
	public void printAll(String title) {
		printSyntaxTree(title);
		printDiagnostics(title);
		printTokenList(title);
	}
	
	public void printSyntaxTree(String title) {
		IO.println("======================================== BEGIN SYNTAX TREE: " + title + " ============================ ");
		syntaxTree.print(0);
		IO.println("======================================== ENDOF SYNTAX TREE: " + title + " ============================ ");
	}

	public void printDiagnostics(String title) {
		LOG.info("++++++++++++++++ BEGIN DIAGNOSTICS: " + title + " ++++++++++++++++++");
		for(SimulaDiagnostic diagnostic:diagnostics) LOG.info(diagnostic.toString());			
		LOG.info("++++++++++++++++ ENDOF DIAGNOSTICS: " + title + " ++++++++++++++++++");
	}
	
	public void printTokenList(String title) {
		IO.println("======================================== BEGIN TOKEN LIST: " + title + " ============================ ");
		for(LspToken token:tokenList) {
			IO.println(""+token);
		}
		IO.println("======================================== ENDOF TOKEN LIST: " + title + " ============================ ");		
	}


	/// Get the text document's SyntaxTree.
	public ProgramModule getSyntaxTree() {
		return syntaxTree;
	}

	/// Set the text document's SyntaxTree.
	public void setSyntaxTree(final ProgramModule syntaxTree) {
		this.syntaxTree = syntaxTree;
	}

	/// Get the text document's diagnostics.
	public List<SimulaDiagnostic> getDiagnostis() {
		return diagnostics;
	}

	public void initDiagnostics() {
		diagnostics = new Vector<SimulaDiagnostic>();
	}
	
	public void addDiagnostic(SimulaDiagnostic diagnostic) {
		diagnostics.add(diagnostic);
	}

//	/// Set the text document's diagnostics.
//	public void setDiagnostics(final List<SimulaDiagnostic> diagnostics) {
//		this.diagnostics = diagnostics;
//	}

	/// Get the text document's uri.
	public String getUri() {
		return documentUri;
	}

	/// Set the text document's uri.
	public void setUri(final String uri) {
		documentUri = uri;
	}

	/// Get the version number of this document (it will strictly increase after each change, including undo/redo).
	public int getVersion() {
		return version;
	}

	/// Set the version number of this document (it will strictly increase after each change, including undo/redo).
	public void setVersion(final int version) {
		this.version = version;
	}

	/// Get the content of the opened text document.
	public String getText() {
		return sourceCode;
	}

	/// Set the content of the opened text document.
	public void setText(final String text) {
		sourceCode = text;
	}
    
	/**
	 * The document open notification is sent from the client to the server to
	 * signal newly opened text documents. The document's truth is now managed
	 * by the client and the server must not try to read the document's truth
	 * using the document's uri.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentRegistrationOptions}
	 */
//    public static void didOpen(DidOpenTextDocumentParams params, SimulaLanguageServer server) {
    public static void didOpen(final String documentUri, final int version, final String sourceCode) {
//        public DocumentManager documentManager = DocumentManager.get(documentUri);
    	LOG.info("DocumentManager.didOpen: BEGIN");
    	if(openDocuments.get(documentUri) != null) {
    		Util.IERR("DOKUMENTET FINNES FRA FØR");
    	}
    	DocumentManager documentManager = new DocumentManager(documentUri, version, sourceCode);
    	openDocuments.put(documentUri, documentManager);
    	
    	buildPsiAndSyntaxTrees(documentManager);

    	LOG.info("DocumentManager.didOpen: RETURNS");
    }
    

	/**
	 * The document change notification is sent from the client to the server to
	 * signal changes to a text document.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentChangeRegistrationOptions}
	 */
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
    	
    	buildPsiAndSyntaxTrees(documentManager);

    	LOG.info("DocumentManager.didChange: RETURNS");
	}

	
	/**
	 * The document will save notification is sent from the client to the server before the document is actually saved.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentRegistrationOptions}
	 */
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

	
	/**
	 * The document save notification is sent from the client to the server when
	 * the document is saved in the client.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentSaveRegistrationOptions}
	 */
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
//			SimulaLspCompiler.runCompilerOrValidator(documentUri, fullText);
	    	buildPsiAndSyntaxTrees(documentManager);
		} catch (Exception e) {
			// Log your errors appropriately
			e.printStackTrace();
		}
	}
	

	/**
	 * The document close notification is sent from the client to the server
	 * when the document got closed in the client. The document's truth now
	 * exists where the document's uri points to (e.g. if the document's uri is
	 * a file uri the truth now exists on disk).
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentRegistrationOptions}
	 */
//	public static void didClose(DidCloseTextDocumentParams params, SimulaLanguageServer server) {
	public static void didClose(final String documentUri) {
		
//    	DocumentManager documentManager = openDocuments.get(documentUri);
        openDocuments.remove(documentUri);

		System.out.println("Document closed on client side: " + documentUri);
	}
	

	public static void buildPsiAndSyntaxTrees(DocumentManager documentManager) {
    	LOG.info("SimulaLspCompiler.buildPsiAndSyntaxTrees: BEGIN");
    	documentManager.initDiagnostics();
		String sourceText = documentManager.getText();
		SimulaBuilder simBuilder = new SimulaBuilder(documentManager);
		ProgramModule syntaxTree = null;
		try {
			simBuilder.start(sourceText);
    		IO.println("DocumentManager.buildPsiAndSyntaxTrees: " + sourceText.replace("\n", "\\n").replace("\r", "\\r"));
    		syntaxTree = new ProgramModule(simBuilder);
		} catch (Exception e) {
			IO.println("DocumentManager.buildPsiAndSyntaxTrees: GOT EXCEPTION: " + e.getMessage());
			e.printStackTrace();
		}
		
		documentManager.setSyntaxTree(syntaxTree);
		if(Option.TESTING_WITHOUT_PSI) {
			IO.println("======================================== BEGIN RESULT AFTER buildPsiAndSyntaxTrees ============================ ");
			syntaxTree.print(0);
			IO.println("======================================== ENDOF RESULT AFTER buildPsiAndSyntaxTrees ============================ ");
		}
		
		StandardClass.ENVIRONMENT.doChecking();
		Global.duringParsing = false;
		syntaxTree.doChecking();
	}

}
