package simula;

import java.util.Vector;

import org.eclipse.lsp4j.ClientCapabilities;
import org.eclipse.lsp4j.ClientInfo;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.TraceValue;
import org.eclipse.lsp4j.services.LanguageClient;

import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.utilities.LOG;
import simula.core.utilities.Util;

public class SimulaCoreInitialize {
	
	///	The initialize request is sent as the first request from the client to the server.
	///	If the server receives requests or notifications before the initialize request,
	/// it should act as follows:
	///
	///	- for a request, the response should be errored with: ResponseErrorCode.ServerNotInitialized.
	///   The message can be picked by the server.
	/// 
	///	- notifications should be dropped, except for the exit notification.
	///   This will allow the client to exit a server without an initialize request.
	/// 
	///	Until the server has responded to the initialize request with an InitializeResult,
	/// the client must not send any additional requests or notifications to the server.
	///
	///	During the initialize request, the server is allowed to send the notifications window/showMessage,
	/// window/logMessage, and telemetry/event, as well as the request window/showMessageRequest, to the client.
	/// 
	/// +------------------+                   +----------------------+
	/// |  VS Code Client  |                   |  Eclipse JDT Server  |
	/// +------------------+                   +----------------------+
	///          |                                         |
	///          |  1. Spawns Java process with args       |
	///          |---------------------------------------->| (JVM Starts up)
	///          |                                         |
	///          |  2. Sends "initialize" JSON-RPC request |
	///          |---------------------------------------->| `JDTLanguageServer.initialize()`
	///          |                                         | Maps capabilities & workspace
	///          |                                         |
	///          |  3. Responds with Server Capabilities   |
	///          |<----------------------------------------| `InitializeResult` sent back
	///          |                                         |
	///          |                                         |
	///          |  4. Responds with Notification          |
	///          |---------------------------------------->| `Initialized` sent back ?????
	///          |                                         |
	/// 
	/// When the client starts the language server, it issues the initial initialize request.
	/// The client wraps startup configurations inside the initializationOptions field
	/// of the InitializeParams object.
	/// [1] (https://github.com/eclipse-jdtls/eclipse.jdt.ls/issues/1957),
	/// [2] (https://github.com/eclipse-lsp4j/lsp4j/blob/main/org.eclipse.lsp4j/src/main/java/org/eclipse/lsp4j/services/LanguageServer.java), 
	/// [3] (https://github.com/eclipse-jdtls/eclipse.jdt.ls/issues/1785),
	/// [4] (https://bugs.eclipse.org/bugs/show_bug.cgi?id=538245)
	/// 
	///	CompletableFuture<InitializeResult> initialize(InitializeParams params)
		public static boolean initialize(InitializeParams params) {
//			TokenManager.tokenTypes = tokenTypes;
			Util.STOP();
			return true;
		}

	
	/// Debug Utility
	/// Called from:
	///   - SimulaEditor:    Simula.main
	///   - SimulaTestBatch: TestBatchLauncher
	///   - LangugeServer:   SimulaLanguageServer.initialize
	public static void connect(LanguageClient client) {
		CoreGlobal.initiate();
//		SimulaLanguageServer.languageClient = client;	
		CoreGlobal.simulaLanguageServer.connect(client);
		
		ClientCapabilities capabilities = null;
		ClientInfo clientInfo = new ClientInfo("SimulaEditor");
//		String trace = TraceValue.Off;      // No Tracing
//		String trace = TraceValue.Messages; // Single message trace
		String trace = TraceValue.Verbose;  // Full systematic trace

		InitializeParams params = new InitializeParams();
//		params.setCapabilities(capabilities);
		params.setClientInfo(clientInfo);
		params.setTrace(trace);
	    InitializeResult result = CoreGlobal.simulaLanguageServer.initialize_local(params);
	}
		
	/// Debug Utility
	/// Called from:
	///   - SimulaEditor:    Simula.main
	///   - SimulaTestBatch: TestBatchLauncher
	///   - LangugeServer:   SimulaLanguageServer.initialize
	public static void initiate(Vector<String> argv) {
		LOG.info("SimulaCoreInitialize.initiate: ");
		String[] args = argv.toArray(new String[0]);
		Option.decodeArguments(args);
	}

}
