package client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

import org.eclipse.lsp4j.ClientInfo;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.TraceValue;

import simula.Option;
import simula.core.CoreGlobal;
import simula.server.SimulaExecutor;
import simula.server.SimulaTextDocumentService;

public class TestBatchLauncher {

	public static void run(String fileName, Vector<String> argv, Vector<String> argv2) {
		// Remove time, date, and headers from Logger output.
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");

		// Parse runtime arguments.
		String[] args = argv.toArray(new String[0]);
		Option.decodeArguments(args);

		// Start SimulaLanguageServer and Connect
		CoreGlobal.initiate();
		CoreGlobal.INLINE_CONNECTED = true;
		CoreGlobal.simulaLanguageServer.connect(new TestBatchClient());
			
		// Initialize SimulaLanguageServer
//		ClientCapabilities capabilities = null;
		ClientInfo clientInfo = new ClientInfo("SimulaEditor");
		String trace = TraceValue.Off;      // No Tracing
//		String trace = TraceValue.Messages; // Single message trace
//		String trace = TraceValue.Verbose;  // Full systematic trace

		InitializeParams initializeParams = new InitializeParams();
//		initializeParams.setCapabilities(capabilities);
		initializeParams.setClientInfo(clientInfo);
		initializeParams.setTrace(trace);
		InitializeResult result = CoreGlobal.simulaLanguageServer.initialize_local(initializeParams);
			
//		SimulaCoreInitialize.initiate(argv);
		
		String documentUri = fileName;
		int version = 1;
		try {
			String content = Files.readString(Path.of(fileName));
			SimulaTextDocumentService simulaTextDocumentService = CoreGlobal.getSimulaTextDocumentService();
			TextDocumentItem textDocument = new TextDocumentItem(documentUri, "Simula", version, content);

			DidOpenTextDocumentParams params = new DidOpenTextDocumentParams();
			params.setTextDocument(textDocument);
//			IO.println("SourceModule.doOpenSimulaModule: " + params);
			simulaTextDocumentService.didOpen(params);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SimulaExecutor.run(documentUri, argv2);
	}
}
