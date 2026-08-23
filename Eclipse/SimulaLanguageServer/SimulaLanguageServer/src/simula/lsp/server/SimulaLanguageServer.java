package simula.lsp.server;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;

import java.util.concurrent.CompletableFuture;

import simula.SimulaCoreClient;
import simula.SimulaCoreExports;

public class SimulaLanguageServer implements LanguageServer, LanguageClientAware {

    private final TextDocumentService textDocumentService;
    private final WorkspaceService workspaceService;
    private LanguageClient client; // The live hook to the IDE/Editor
    private ClientCapabilities clientCapabilities;

    public SimulaLanguageServer() {
        this.textDocumentService = new SimulaTextDocumentService(this);
        this.workspaceService = new SimulaWorkspaceService();
    }

    // --- LanguageClientAware Implementation ---
    @Override
    public void connect(LanguageClient client) {
        // LSP4J injects the client proxy right after the launcher starts
        this.client = client;
        SimulaCoreClient simulaCoreClient = new SimulaCoreClientProxy(client);
        SimulaCoreExports.initiate(simulaCoreClient, "packetName");
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
    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
        // 1. Capture what the client is capable of doing
        this.clientCapabilities = params.getCapabilities();

        // 2. Define what your server supports back to the client
        ServerCapabilities serverCapabilities = new ServerCapabilities();
        serverCapabilities.setTextDocumentSync(TextDocumentSyncKind.Incremental);
        serverCapabilities.setCompletionProvider(new CompletionOptions(true, null));

        InitializeResult result = new InitializeResult(serverCapabilities);
        return CompletableFuture.completedFuture(result);
    }
    
    
    
    
    

    @Override
    public void initialized(InitializedParams params) {
        // Safely interact with the client now that the connection handshake is fully closed
        if (client != null) {
            client.logMessage(new MessageParams(MessageType.Info, "Language Server connected successfully!"));
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
        return this.client;
    }
}
