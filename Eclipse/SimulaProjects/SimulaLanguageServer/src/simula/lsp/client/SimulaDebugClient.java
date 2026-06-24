package simula.lsp.client;

import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.VersionedTextDocumentIdentifier;
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
	public void start(SimulaLanguageServer server) {
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
	    
	    IO.println("\nOpen Document with errors:");
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
	    textDocumentItem.setUri("URI_1");
	    textDocumentItem.setVersion(0);
	    textDocumentItem.setText(program);
	    didOpenTextDocumentParams.setTextDocument(textDocumentItem);
	    TextDocumentService textDocumentService = server.getTextDocumentService();
	    textDocumentService.didOpen(didOpenTextDocumentParams);
	    
	    
	    IO.println("\nChange Document with no errors:");
	    DidChangeTextDocumentParams didChangeTextDocumentParams = new DidChangeTextDocumentParams();
	    
	    int version = 1;
	    VersionedTextDocumentIdentifier versionedTextDocumentIdentifier = new VersionedTextDocumentIdentifier("URI_1", version);
	    didChangeTextDocumentParams.setTextDocument(versionedTextDocumentIdentifier);
	    
	    Range range = new Range(new Position(4, 14), new Position(4, 14));
	    List<TextDocumentContentChangeEvent> contentChanges = new Vector<TextDocumentContentChangeEvent>();
	    TextDocumentContentChangeEvent change = new TextDocumentContentChangeEvent(range, "(");
	    contentChanges.add(change);
	    didChangeTextDocumentParams.setContentChanges(contentChanges);

	    textDocumentService.didChange(didChangeTextDocumentParams);
	}

    // Du må også implementere de obligatoriske standardmetodene fra LanguageClient:
    @Override public void telemetryEvent(Object object) {
    	LOG.info("SimulaEditorClient.telemetryEvent: IS NOT IMPLEMENTED");
    	
    }
    @Override public void publishDiagnostics(PublishDiagnosticsParams diagnostics) {
    	LOG.info("SimulaEditorClient.publishDiagnostics: IS NOT IMPLEMENTED");
    	
    }
    @Override public void showMessage(MessageParams messageParams) {
    	LOG.info("SimulaEditorClient.showMessage: IS NOT IMPLEMENTED");
    	
    }
    @Override public void logMessage(MessageParams messageParams) {
    	LOG.info("SimulaEditorClient.logMessage: IS NOT IMPLEMENTED");
    }

	@Override
	public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
		// TODO Auto-generated method stub
		return null;
	}
}
