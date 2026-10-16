package simula.server;

import org.eclipse.lsp4j.debug.*;
import org.eclipse.lsp4j.debug.services.IDebugProtocolClient;
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer;

import simula.core.RunDebugManager;

import java.util.concurrent.CompletableFuture;

public class SimulaDebugAdapter implements IDebugProtocolServer {
    
    private IDebugProtocolClient client;

    /**
     * Connects the remote debug client proxy to this server.
     */
    public void connect(IDebugProtocolClient client) {
        this.client = client;
    }

    @Override
    public CompletableFuture<Capabilities> initialize(InitializeRequestArguments args) {
        Capabilities capabilities = new Capabilities();
        // Tell the client what features your Simula debugger supports
        capabilities.setSupportsConfigurationDoneRequest(true);
        capabilities.setSupportsFunctionBreakpoints(false);
        capabilities.setSupportsConditionalBreakpoints(true);
        
        return CompletableFuture.completedFuture(capabilities);
    }

    @Override
    public CompletableFuture<Void> launch(java.util.Map<String, Object> args) {
        // Parse Simula runtime path, source folders, etc. from args
        // Spawn your underlying Simula runtime process in debug mode
    	RunDebugManager.launch(args);
        
        // Notify the client that the debugger is ready for configuration
        if (client != null) {
            client.initialized();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<SetBreakpointsResponse> setBreakpoints(SetBreakpointsArguments args) {
        SetBreakpointsResponse response = new SetBreakpointsResponse();
        // Map Eclipse UI/Client breakpoints to your Simula runtime engine
        Breakpoint[] confirmedBreakpoints = new Breakpoint[args.getBreakpoints().length];
        
        // ... build your breakpoint mapping logic ...
        
        response.setBreakpoints(confirmedBreakpoints);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<Void> configurationDone(ConfigurationDoneArguments args) {
        // Signals that configuration is complete. Resume your Simula target process.
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> disconnect(DisconnectArguments args) {
        // Terminate or detach from your Simula runtime execution environment
        return CompletableFuture.completedFuture(null);
    }
    
    // Implement remaining mandatory DAP steps (threads, stackTrace, scopes, variables, continue, next, etc.)
}
