package simula.server;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.*;

import simula.Comn;
import simula.Option;
import simula.core.CoreGlobal;
import simula.core.utilities.LOG;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SimulaLanguageServer implements LanguageServer, LanguageClientAware {

    public static LanguageClient languageClient; // The live hook to the IDE/Editor
    public static ClientInfo clientInfo;

    private final TextDocumentService textDocumentService;
    private final WorkspaceService workspaceService;
    public ClientCapabilities clientCapabilities;

    
    
    public SimulaLanguageServer() {
        this.textDocumentService = new SimulaTextDocumentService(this);
        this.workspaceService = new SimulaWorkspaceService();
    }

    public static void main(String[] args) {
        Comn.popUp("SERVER: SimulaLanguageServer.main: ");
        
    	SimulaLanguageServer server = new SimulaLanguageServer();
//        Comn.popUp("SimulaLanguageServer.main: server: " + server);
        
        // Wire up the launcher to read from standard input/output streams 
        // provided by the IntelliJ Client process
        var launcher = LSPLauncher.createServerLauncher(server, System.in, System.out);

        // Fetch the proxy client representing IntelliJ and pass it to your server
        LanguageClient clientProxy = launcher.getRemoteProxy();
        server.connect(clientProxy);

        // Start listening to the input streams
        launcher.startListening();
    }

    
    /// --- LanguageClientAware Implementation ---
    /// 
    /// To obtain a reference to the client in your Eclipse language server
    /// implementation using Eclipse LSP4J, you need to implement the
    /// LanguageClientAware interface on your primary server class.
    ///  [1] (https://medium.com/ballerina-techblog/practical-guide-for-the-language-server-protocol-3091a122b750),
    ///  [2] (https://www.typefox.io/blog/eclipse-lsp4j-is-here/)
    /// 
    /// The framework will then pass the remote client proxy directly to your server during initialization.
    ///  [1] (https://www.typefox.io/blog/eclipse-lsp4j-is-here/),
    ///  [2] (https://medium.com/ballerina-techblog/practical-guide-for-the-language-server-protocol-3091a122b750)
    /// This method is called automatically by LSPLauncher
    @Override
    public void connect(LanguageClient languageClient) {
        // LSP4J injects the client proxy right after the launcher starts
        SimulaLanguageServer.languageClient = languageClient;
        
//        if(true) throw new RuntimeException("");
//        Comn.popUp("SimulaLanguageServer.connect: " + languageClient.getClass());
        
//        Util.redirectSystemIO();
        LOG.info("MESSAGE TEXT 2");
    }

    /// --- LanguageServer Implementation ---
    ///	The initialize request is sent as the first request from the client to the server.
    ///	If the server receives requests or notifications before the initialize request,
    /// it should act as follows:
    ///
    ///	- for a request, the response should be errored with: ResponseErrorCode.ServerNotInitialized.
    ///   The message can be picked by the server.
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
    /// 
	/// When the client starts the language server, it issues the initial initialize request.
	/// The client wraps startup configurations inside the initializationOptions field
	/// of the InitializeParams object.
	/// [1] (https://github.com/eclipse-jdtls/eclipse.jdt.ls/issues/1957),
	/// [2] (https://github.com/eclipse-lsp4j/lsp4j/blob/main/org.eclipse.lsp4j/src/main/java/org/eclipse/lsp4j/services/LanguageServer.java), 
	/// [3] (https://github.com/eclipse-jdtls/eclipse.jdt.ls/issues/1785),
	/// [4] (https://bugs.eclipse.org/bugs/show_bug.cgi?id=538245)
    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
//    	IO.println("SERVER: SimulaLanguageServer.initialize: " + params.getClientInfo());
//    	if(true) throw new RuntimeException("Server initialize was called: " + params);
//        Comn.popUp("SimulaLanguageServer.initialize: " + params.getClientInfo());
        
//        return CompletableFuture.supplyAsync(() -> {
//        	return initialize_local(params);
//        });
        return CompletableFuture.completedFuture(initialize_local(params));
    }
    
    public InitializeResult initialize_local(InitializeParams params) {
        // Retrieve the raw options object sent by the client
        Object options = params.getInitializationOptions(); 
        
        // Parse it using a library like Gson into your configuration class
        if (options != null) {
//            MyConfig config = new Gson().fromJson(options.toString(), MyConfig.class);
//            // Apply options to the server instance...
        }
        
        // 1. Capture what the client is capable of doing
        this.clientCapabilities = params.getCapabilities();
        
        // Pick up: 
        clientInfo = params.getClientInfo();
        
        
        /// The trace parameter accepts one of three specific string values:
        /// 
        ///  'off':      Tracing is completely disabled. The server should not send any $/logTrace notifications.
        ///              If the parameter is omitted from InitializeParams entirely, it defaults to 'off'.
        /// 
        ///  'messages': The server logs basic communication events, such as when requests are received and
        ///              when responses are sent, without dumping full payloads.
        /// 
        ///  'verbose':  The server logs granular execution details,
        ///              full JSON-RPC payload messages, performance metrics, and deep debugging information.
        /// 
        String trace = params.getTrace();
        if(trace == null) {
        	Option.lspTrace = 0;
        } else switch(trace) {
	        case TraceValue.Off -> Option.lspTrace = 0;
	        case TraceValue.Messages -> Option.lspTrace = 1;
	        case TraceValue.Verbose -> Option.lspTrace = 2;
        }
        
//        CALL: SimulaCoreInitialize.initiate(...);
        
        // 1. Create the container for server capabilities
        ServerCapabilities serverCapabilities = new ServerCapabilities();

        // 2. Define how you want documents to sync (Full text or Incremental changes)
        //serverCapabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
        serverCapabilities.setTextDocumentSync(TextDocumentSyncKind.Incremental);
        
        // 3. Declare features your server supports
        serverCapabilities.setCompletionProvider(new CompletionOptions(true, null));
        serverCapabilities.setDefinitionProvider(true);
        serverCapabilities.setHoverProvider(true);
        serverCapabilities.setSemanticTokensProvider(getSemanticOptions());

        // 4. Return the capabilities wrapped in an InitializeResult object
        InitializeResult reply = new InitializeResult(serverCapabilities, new ServerInfo(CoreGlobal.serverName, CoreGlobal.serverVersion));
        return reply;
    }
    

    // 1. Define the ordered array of Token Types. 
    // The index positions (0, 1, 2...) are what the server will transmit later.
    private static final List<String> SUPPORTED_TOKEN_TYPES = Arrays.asList(
    		SemanticTokenTypes.Namespace,
    		"namespace", // Index 0
        "type",      // Index 1
        "class",     // Index 2
        "enum",      // Index 3
        "interface", // Index 4
        "struct",    // Index 5
        "typeParameter", // Index 6
        "parameter", // Index 7
        "variable",  // Index 8
        "property",  // Index 9
        "macro",     // Index 10
        "function",  // Index 11
        "method"     // Index 12
    );

    // Leave modifiers empty for this baseline configuration
    private static final List<String> SUPPORTED_TOKEN_MODIFIERS = Arrays.asList();

    private SemanticTokensWithRegistrationOptions getSemanticOptions() {
    	// Set up semantic tokens options with your token types and modifiers legend
        SemanticTokensWithRegistrationOptions semanticOptions = new SemanticTokensWithRegistrationOptions();
//        SemanticTokensLegend legend = new SemanticTokensLegend(
//            Arrays.asList("class", "interface", "variable", "function"), 
//            Arrays.asList("declaration", "readonly")
//        );
        SemanticTokensLegend legend = new SemanticTokensLegend(
                SUPPORTED_TOKEN_TYPES, 
                SUPPORTED_TOKEN_MODIFIERS
            );
        semanticOptions.setLegend(legend);
        semanticOptions.setFull(true); // Enable full document semantic tokens
        return semanticOptions;
    }
    
    
    

    @Override
    public void initialized(InitializedParams params) {
        // Safely interact with the client now that the connection handshake is fully closed
        if (languageClient != null) {
        	languageClient.logMessage(new MessageParams(MessageType.Info, "Language Server connected successfully!"));
        }
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        return CompletableFuture.completedFuture(new Object());
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    @Override
    public TextDocumentService getTextDocumentService() {
        return this.textDocumentService;
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        return this.workspaceService;
    }

    // Getter to allow sub-services to use the client connection
    public LanguageClient getClient() {
        return this.languageClient;
    }
}
