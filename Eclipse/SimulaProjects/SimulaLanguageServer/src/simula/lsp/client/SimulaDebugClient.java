package simula.lsp.client;

import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.TextDocumentSaveReason;
import org.eclipse.lsp4j.VersionedTextDocumentIdentifier;
import org.eclipse.lsp4j.WillSaveTextDocumentParams;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.TextDocumentService;

import simula.compiler.utilities.LOG;
import simula.lsp.SimulaLanguageServer;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

/// @author Øystein Myhre Andersen
/// @author Google AI
public class SimulaDebugClient implements LanguageClient {

//	@Override
	/// Dette er min egen rutine for å starte clienten
	public void start(@SuppressWarnings("exports") SimulaLanguageServer server) {
		// Oppstart rekkefølge definert av LSP-spesifikasjonen:
		//
		// 1. initialize (Forespørsel fra Klient):
		//    Klienten (denne klienten) sender en InitializeParams-pakke til serveren.
		//       Denne inneholder:
		//       1. processId: Prosess-ID-en til denne klienten-instansen.
		//       2. rootUri: URL-en til prosjektmappen som ble åpnet.
		//       3. capabilities: En stor liste over hva denne klienten støtter
		//          (f.eks. om denne klienten klarer å vise verktøytips, kodefullføring eller hierarkiske symboler).
		//
		// 2. InitializeResult (Svar fra Server):
		//    Serveren svarer med sine capabilities. Den forteller denne klienten nøyaktig hva den kan
		//    levere (f.eks: "Jeg støtter auto-complete, men jeg støtter ikke kodeformatering").
		//
		// 3. initialized (Notifikasjon fra Klient):
		//    Klienten sender en tom bekreftelse (InitializedParams) tilbake
		//    for å si: "Mottatt, nå er vi offisielt i gang!"
		
		IO.println("SimulaEditorClient.start: Client is starting ...");
		InitializeParams params = new InitializeParams();
//		params.setProcessId(null);
//		params.setRootUri(null);
//		params.setCapabilities(null);
	    CompletableFuture<InitializeResult> result = server.initialize(params);
		IO.println("SimulaEditorClient.start: server.initialize ... " + result.isDone());
	    server.initialized();
	    
	    // =======================================================
	    IO.println("\nOpen Document with errors:");
	    // =======================================================
	    String demoUri = "file:///demo/program.sim";
	    String program = "class Demo; begin\r\n"              // Line 0
	    		+ "    comment demo mockup: 1;\r\n"           // Line 1
	    		+ "    procedure p(x); integer x; begin\r\n"  // Line 2
	    		+ "       real pi = 3.14;\r\n"                // Line 3
	    		+ "       outtext\"line with error\");\r\n"   // Line 4 missing ( at pos 14
	    		+ "    end;\r\n"                              // Line 5
	    		+ " end";                                     // Line 6
	    DidOpenTextDocumentParams didOpenTextDocumentParams = new DidOpenTextDocumentParams();
	    
	    TextDocumentItem textDocumentItem = new TextDocumentItem();
	    textDocumentItem.setLanguageId("Simula");
	    textDocumentItem.setUri(demoUri);
	    textDocumentItem.setVersion(0);
	    textDocumentItem.setText(program);
	    didOpenTextDocumentParams.setTextDocument(textDocumentItem);
	    TextDocumentService textDocumentService = server.getTextDocumentService();
	    textDocumentService.didOpen(didOpenTextDocumentParams);
	    
	    
	    // =======================================================
	    IO.println("\nChange Document with no errors:");
	    // =======================================================
	    DidChangeTextDocumentParams didChangeTextDocumentParams = new DidChangeTextDocumentParams();
	    
	    int version = 1;
	    VersionedTextDocumentIdentifier versionedTextDocumentIdentifier = new VersionedTextDocumentIdentifier(demoUri, version);
	    didChangeTextDocumentParams.setTextDocument(versionedTextDocumentIdentifier);
	    
	    List<TextDocumentContentChangeEvent> contentChanges = new Vector<TextDocumentContentChangeEvent>();
	    Range range = new Range(new Position(4, 14), new Position(4, 14));
	    TextDocumentContentChangeEvent change = new TextDocumentContentChangeEvent(range, "(");
	    contentChanges.add(change);
	    range = new Range(new Position(4, 26), new Position(4, 26));
	    change = new TextDocumentContentChangeEvent(range, "no ");
	    contentChanges.add(change);
	    didChangeTextDocumentParams.setContentChanges(contentChanges);

	    textDocumentService.didChange(didChangeTextDocumentParams);
	    
	    
	    // =======================================================
	    IO.println("\nSignal Save the Changed Document:");
	    // =======================================================
	    TextDocumentIdentifier textDocumentIdentifier = new TextDocumentIdentifier(demoUri);
	    WillSaveTextDocumentParams willSaveTextDocumentParams = new WillSaveTextDocumentParams(textDocumentIdentifier, TextDocumentSaveReason.Manual);
	    textDocumentService.willSave(willSaveTextDocumentParams);
	    
	    // =======================================================
	    IO.println("\nDidSave the Changed Document:");
	    // =======================================================
	    DidSaveTextDocumentParams didSaveTextDocumentParams = new DidSaveTextDocumentParams(textDocumentIdentifier);
	    textDocumentService.didSave(didSaveTextDocumentParams);
	}

    // Du må også implementere de obligatoriske standardmetodene fra LanguageClient:
    @Override public void telemetryEvent(Object object) {
    	LOG.info("SimulaEditorClient.telemetryEvent: IS NOT IMPLEMENTED");
    	
    }
    @Override public void publishDiagnostics(@SuppressWarnings("exports") PublishDiagnosticsParams diagnostics) {
    	LOG.info("SimulaEditorClient.publishDiagnostics: IS NOT IMPLEMENTED");
    	
    }
    @Override public void showMessage(@SuppressWarnings("exports") MessageParams messageParams) {
    	LOG.info("SimulaEditorClient.showMessage: IS NOT IMPLEMENTED");
    	
    }
    @SuppressWarnings("exports")
	@Override public void logMessage(MessageParams messageParams) {
    	LOG.info("SimulaEditorClient.logMessage: IS NOT IMPLEMENTED");
    }

	@SuppressWarnings("exports")
	@Override
	public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
		// TODO Auto-generated method stub
		return null;
	}
}
