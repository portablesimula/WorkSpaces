package simula.lsp.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.DocumentDiagnosticParams;
import org.eclipse.lsp4j.DocumentDiagnosticReport;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.RelatedFullDocumentDiagnosticReport;
import org.eclipse.lsp4j.RelatedUnchangedDocumentDiagnosticReport;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.TextDocumentSaveReason;
import org.eclipse.lsp4j.WillSaveTextDocumentParams;

import simula.lsp.SimulaLanguageServer;

/// Vi må lagre innholdet til dokumentene som er åpne i editoren.
/// Siden editoren kan ha endringer som ikke er lagret til disken ennå,
/// må serveren stole på teksten den får tilsendt over LSP
public class DocumentManager {
    // Nøkkelen er filens URI (f.eks. file:///path/to/file.txt)
    private final ConcurrentHashMap<String, SourceDocumentItem> openDocuments = new ConcurrentHashMap<>();

    public void put(String documentUri, SourceDocumentItem document) {
        openDocuments.put(documentUri, document);
    }

    public SourceDocumentItem get(String documentUri) {
        return openDocuments.get(documentUri);
    }

    public void remove(String documentUri) {
		if (openDocuments.containsKey(documentUri))
        openDocuments.remove(documentUri);
    }

    

	/**
	 * The text document diagnostic request is sent from the client to the server to ask the server to compute the diagnostics
	 * for a given document. As with other pull requests the server is asked to compute the diagnostics for the currently
	 * synced version of the document.
	 */
//	public static CompletableFuture<DocumentDiagnosticReport> diagnostic(DocumentDiagnosticParams params, SimulaLanguageServer server) {
	public static DocumentDiagnosticReport diagnostic(DocumentDiagnosticParams params, SimulaLanguageServer server) {
       String documentUri = params.getTextDocument().getUri();
        String previousResultId = params.getPreviousResultId();

//        // 1. If the client passes a matching previous result ID and document hasn't changed, 
//        // return an unchanged report to optimize performance.
//        if (previousResultId != null && previousResultId.equals(this.lastResultId) && !isDocumentDirty(documentUri)) {
//            RelatedUnchangedDocumentDiagnosticReport unchangedReport = new RelatedUnchangedDocumentDiagnosticReport();
//            unchangedReport.setResultId(this.lastResultId);
//            
////            DocumentDiagnosticReport report = new DocumentDiagnosticReport(Either.forRight(unchangedReport));
//            RelatedUnchangedDocumentDiagnosticReport relatedUnchangedDocumentDiagnosticReport = null;
//            DocumentDiagnosticReport report = new DocumentDiagnosticReport(relatedUnchangedDocumentDiagnosticReport);
////            NOT IMPL - SJEKK DETTE
////            return CompletableFuture.completedFuture(report);
//            return report;
//        }
//
//        // 2. Otherwise, compute fresh diagnostics
//        List<Diagnostic> diagnostics = computeDiagnosticsFor(documentUri);
//
//        // 3. Build a full diagnostic report
//        RelatedFullDocumentDiagnosticReport fullReport = new RelatedFullDocumentDiagnosticReport();
//        fullReport.setItems(diagnostics);
//        
//        // Generate a unique result ID for this state (e.g., hash or an incremental counter)
//        this.lastResultId = "diagnostic-id-" + System.currentTimeMillis();
//        fullReport.setResultId(this.lastResultId);
//
//        //public class DocumentDiagnosticReport extends Either<RelatedFullDocumentDiagnosticReport, RelatedUnchangedDocumentDiagnosticReport> {
////        DocumentDiagnosticReport report = new DocumentDiagnosticReport(Either.forLeft(fullReport));
//        DocumentDiagnosticReport report = new DocumentDiagnosticReport(fullReport);
////        return CompletableFuture.completedFuture(report);
//        return report;

		DocumentManager documentManager = server.getDocumentManager();
		SourceDocumentItem sourceItem = documentManager.get(documentUri);
		List<Diagnostic> diagnostics = sourceItem.getDiagnostis();
		
		// 3. Build a diagnostic report
		RelatedFullDocumentDiagnosticReport report = new RelatedFullDocumentDiagnosticReport();
		report.setItems(diagnostics);
		
		return new DocumentDiagnosticReport(report);
	}

//    private static List<Diagnostic> computeDiagnosticsFor(String documentUri) {
//        List<Diagnostic> diagnostics = new ArrayList<>();
//        // Your parsing and validation logic goes here...
//        // Diagnostic diagnostic = new Diagnostic(range, message, severity, source);
//        return diagnostics;
//    }
//
//    private static boolean isDocumentDirty(String documentUri) {
//        // Implement logic checking if your internal abstract syntax tree (AST) 
//        // or document tracking has been modified since the last validation loop.
//        return false; 
//    }

    
    
	/**
	 * The document open notification is sent from the client to the server to
	 * signal newly opened text documents. The document's truth is now managed
	 * by the client and the server must not try to read the document's truth
	 * using the document's uri.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentRegistrationOptions}
	 */
    public static void didOpen(DidOpenTextDocumentParams params, SimulaLanguageServer server) {
    	// 1. Hent ut dokument-objektet fra parameterne sent av Eclipse (LSP4E)
    	TextDocumentItem document = params.getTextDocument();
    	if (document == null) { return; }
        if (server.getClient() == null) { return; }
  	  	SourceDocumentItem sourceItem = new SourceDocumentItem(document);

    	String documentUri = document.getUri();
    	String sourceCode = document.getText();
    	String languageId = document.getLanguageId();
    	int version = document.getVersion();

    	// 2. Lagre dokumentet i minnet (viktig for fremtidige didChange- eller hover-forespørsler)
    	DocumentManager documentManager = server.getDocumentManager();
    	documentManager.put(documentUri, sourceItem);

    	// 3. (Valgfritt) Kjør syntaks-sjekk / validering med en gang filen åpnes
    	List<Diagnostic> diagnostics = SimulaCompiler.runCompilerOrValidator(documentUri, sourceCode);
    	sourceItem.setDiagnostics(diagnostics);
    	
	    // 4. Send feilmeldingene tilbake til VS Code via LSP4J-klienten
	    server.getClient().publishDiagnostics(new PublishDiagnosticsParams(documentUri, diagnostics));
    }
    

	/**
	 * The document change notification is sent from the client to the server to
	 * signal changes to a text document.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentChangeRegistrationOptions}
	 */
	public static void didChange(DidChangeTextDocumentParams params, SimulaLanguageServer server) {
	    // 1. Hent ut den oppdaterte teksten fra VS Code
	    String sourceCode = params.getContentChanges().get(0).getText();
	    String documentUri = params.getTextDocument().getUri();

    	// 3. Kjør syntaks-sjekk / validering
    	List<Diagnostic> diagnostics = SimulaCompiler.runCompilerOrValidator(documentUri, sourceCode);
    	
	    // 4. Send feilmeldingene tilbake til VS Code via LSP4J-klienten
	    server.getClient().publishDiagnostics(new PublishDiagnosticsParams(documentUri, diagnostics));
	}

	
	/**
	 * The document will save notification is sent from the client to the server before the document is actually saved.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentRegistrationOptions}
	 */
	public static void willSave(WillSaveTextDocumentParams params, SimulaLanguageServer server) {
		// 1. Extract document context
		String documentUri = params.getTextDocument().getUri();
		TextDocumentSaveReason reason = params.getReason(); // Manual, AfterDelay, or FocusOut

		// 2. Add your pre-save routine here
		System.out.println("Document is about to save: " + documentUri + " due to reason: " + reason);

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
	public static void didSave(DidSaveTextDocumentParams params, SimulaLanguageServer server) {
		// 1. Extract the URI of the saved file
		String documentUri = params.getTextDocument().getUri();

		// 2. Retrieve your full document content from your local text sync cache
		DocumentManager documentManager = server.getDocumentManager();
//		String fullText = documentCache.get(documentUri);
		String fullText =  documentManager.get(documentUri).getText();

		if (fullText == null) {
			// Fallback: If cache missed, you could read directly from the URI/disk
			return;
		}

		// 3. Trigger server-side logic (e.g., Compilation, Re-indexing, Validation)
		try {
			SimulaCompiler.runCompilerOrValidator(documentUri, fullText);
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
	public static void didClose(DidCloseTextDocumentParams params, SimulaLanguageServer server) {
		// 1. Extract the unique URI of the closed document
		String documentUri = params.getTextDocument().getUri();

		// 2. Clear or clean up your server's in-memory cache for this file
		// 3. Clear existing diagnostics if applicable
		// 2. Clear out server-side memory allocated for this document
		DocumentManager documentManager = server.getDocumentManager();
		documentManager.remove(documentUri);

		System.out.println("Document closed on client side: " + documentUri);
	}
	

}
