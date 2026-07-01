package simula.lsp.client;

import simula.compiler.utilities.Global;
import simula.compiler.utilities.LOG;
import simula.lsp.compiler.DocumentManager;
import simula.lsp.compiler.SimTextDocumentContentChangeEvent;
import simula.lsp.compiler.TokenManager;
import simula.lsp.util.SimPosition;
import simula.lsp.util.SimRange;

import java.util.List;
import java.util.Vector;

/// @author Øystein Myhre Andersen
/// @author Google AI
public class SimulaDebugClient {

	public static void main(String[] argv) {
		// Remove time, date, and headers from Logger output.
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");

		Global.initiate();
		
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
		
//		IO.println("SimulaEditorClient.start: Client is starting ...");
//		InitializeParams params = new InitializeParams();
////		params.setProcessId(null);
////		params.setRootUri(null);
////		params.setCapabilities(null);
//	    CompletableFuture<InitializeResult> result = server.initialize(params);
//		IO.println("SimulaEditorClient.start: server.initialize ... " + result.isDone());
//	    server.initialized();
	    
	    // =======================================================
	    IO.println("\nOpen Document with errors:");
	    // =======================================================
	    String demoUri = "file:///demo/program.sim";

	    String program = "class Demo; begin\r\n"              // Line 0
	    		+ "    comment demo mockup: 1;\r\n"           // Line 1
	    		+ "    procedure p(x); integer x; begin\r\n"  // Line 2
	    		+ "       real pi = 3.14;\r\n"                // Line 3
	    		+ "       outtext\"line with error\");\r\n"   // Line 4 missing ( at pos 14
//	    		+ "    end;\r\n"                              // Line 5
//	    		+ " end";                                     // Line 6
	    		+ "    end proc p\r\n"                        // Line 5
	    		+ "        endelig\r\n"                       // Line 6
	    		+ "    ;"                                     // Line 7
	    		+ " end";                                     // Line 8

//	    String program = "class A; begin\r\n"              // Line 0
//	    		+ "    end proc p\r\n"                        // Line 5
//	    		+ "        endelig\r\n"                       // Line 6
//	    		+ "    ;"                                     // Line 7
//	    		+ " end";                                     // Line 8

	    int version = 0;
	    DocumentManager.didOpen(demoUri, version, program);
	    
	    
	    // =======================================================
	    IO.println("\nChange Document with no errors:");
	    // =======================================================
	    
	    version = 1;
//	    VersionedTextDocumentIdentifier versionedTextDocumentIdentifier = new VersionedTextDocumentIdentifier(demoUri, version);
//	    didChangeTextDocumentParams.setTextDocument(versionedTextDocumentIdentifier);
	    
	    List<SimTextDocumentContentChangeEvent> contentChanges = new Vector<SimTextDocumentContentChangeEvent>();
	    SimRange range = new SimRange(new SimPosition(4, 14), new SimPosition(4, 14));
	    SimTextDocumentContentChangeEvent change = new SimTextDocumentContentChangeEvent(range, "(");
	    contentChanges.add(change);
	    range = new SimRange(new SimPosition(4, 26), new SimPosition(4, 26));
	    change = new SimTextDocumentContentChangeEvent(range, "no ");
	    contentChanges.add(change);
//	    didChangeTextDocumentParams.setContentChanges(contentChanges);

//	    textDocumentService.didChange(didChangeTextDocumentParams);
	    DocumentManager.didChange(demoUri, contentChanges);
	    
	    
	    // =======================================================
	    IO.println("\nSignal Save the Changed Document:");
	    // =======================================================
//	    TextDocumentIdentifier textDocumentIdentifier = new TextDocumentIdentifier(demoUri);
//	    WillSaveTextDocumentParams willSaveTextDocumentParams = new WillSaveTextDocumentParams(textDocumentIdentifier, TextDocumentSaveReason.Manual);
//	    textDocumentService.willSave(willSaveTextDocumentParams);
	    DocumentManager.willSave(demoUri, "Manual");
	    
	    // =======================================================
	    IO.println("\nDidSave the Changed Document:");
	    // =======================================================
//	    DidSaveTextDocumentParams didSaveTextDocumentParams = new DidSaveTextDocumentParams(textDocumentIdentifier);
//	    textDocumentService.didSave(didSaveTextDocumentParams);
	    DocumentManager.didSave(demoUri);
	    
	    // =======================================================
	    IO.println("\nRetreave ALL Tokens of the Changed Document:");
	    // =======================================================
//	    SemanticTokensParams semanticTokensParams = new SemanticTokensParams();
//	    semanticTokensParams.setTextDocument(textDocumentIdentifier);
//
//	    SimulaTextDocumentService simulaTextDocumentService = (SimulaTextDocumentService)server.getTextDocumentService();
//	    String updatedText = simulaTextDocumentService.getUpdatedText(demoUri);
//	    SemanticTokens semanticTokens = simulaTextDocumentService.getAllSemanticTokens(semanticTokensParams);
	    
	    DocumentManager documentManager = DocumentManager.GetDocumentManager(demoUri);
	    String updatedText = documentManager.getText();
//	    List<LspToken> tokenList = documentManager.tokenList;
	    
	    List<Integer> semanticTokens = TokenManager.getAllSemanticTokens(demoUri);
	    
	    System.out.println("SimulaDebugClient.updatedText: " + updatedText);
//	    System.out.println("SimulaDebugClient.semanticTokens: " + semanticTokens);

	    if (semanticTokens != null) {
//	    	List<Integer> data = semanticTokens.getData();
	    	// Process the data array here (e.g., update UI via Display.getDefault().asyncExec)
	    	System.out.println("Received semanticTokens size: " + semanticTokens.size());
	    	int i = 0;
	    	while(i < semanticTokens.size()) {
                int deltaLine = semanticTokens.get(i++);
                int deltaChar = semanticTokens.get(i++);
                int length = semanticTokens.get(i++);
                int tokenTypeIndex = semanticTokens.get(i++);
                int tokenModifiersBitmask = semanticTokens.get(i++);
                LOG.info("SimulaDebugClient.Received: token: "
                		+ " <== deltaLine:" + deltaLine
                		+ ", deltaChar: " + deltaChar
                		+ ", length:" + length
                		+ ", type:" + tokenTypeIndex + ':' + TokenManager.edSimulaTokenType(tokenTypeIndex));

	    	}
	    }


	}

}
