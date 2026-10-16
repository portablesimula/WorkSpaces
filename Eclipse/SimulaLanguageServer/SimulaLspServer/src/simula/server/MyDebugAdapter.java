package simula.server;
import org.eclipse.lsp4j.debug.*;
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer;
import org.eclipse.lsp4j.debug.services.IDebugProtocolClient;
import java.util.concurrent.CompletableFuture;
import java.util.Map;

public class MyDebugAdapter implements IDebugProtocolServer {
    private IDebugProtocolClient client;

    public void connect(IDebugProtocolClient client) {
        this.client = client;
    }

    @Override
    public CompletableFuture<Capabilities> initialize(InitializeRequestArguments args) {
        Capabilities capabilities = new Capabilities();
        capabilities.setSupportsConfigurationDoneRequest(true);
        capabilities.setSupportsEvaluateForHovers(true);
        return CompletableFuture.completedFuture(capabilities);
    }

    @Override
    public CompletableFuture<Void> launch(Map<String, Object> args) {
        // Parse args (e.g., target program script path) and start the runtime process
        
        // Notify the client that initialization is ready
        client.initialized(); 
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> disconnect(DisconnectArguments args) {
        return CompletableFuture.completedFuture(null);
    }
    
    // Implement other mandatory DAP methods such as setBreakpoints, stackTrace, scopes, threads, etc.
}
