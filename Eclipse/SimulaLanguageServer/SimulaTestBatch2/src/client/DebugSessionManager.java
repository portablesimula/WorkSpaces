package client;
import org.eclipse.lsp4j.debug.ConfigurationDoneArguments;
import org.eclipse.lsp4j.debug.InitializeRequestArguments;
import org.eclipse.lsp4j.debug.SetBreakpointsArguments;
import org.eclipse.lsp4j.debug.launch.DSPLauncher;
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class DebugSessionManager {

    private IDebugProtocolServer debugServer;
    private Future<Void> listenFuture;

    public void startDebugSession(InputStream inStream, OutputStream outStream) {
        RunDebugClient client = new RunDebugClient();

        // Wire up the client and get a proxy handle to the server
        org.eclipse.lsp4j.jsonrpc.Launcher<IDebugProtocolServer> launcher = 
            DSPLauncher.createClientLauncher(client, inStream, outStream);

        this.debugServer = launcher.getRemoteProxy();
        
        // Start listening to the input stream asynchronously
        this.listenFuture = launcher.startListening();
        
        // Begin the initialization sequence
        initializeDebugAdapter();
    }
    
    private void initializeDebugAdapter() {
        // 1. Initialize the adapter handshake
        InitializeRequestArguments initArgs = new InitializeRequestArguments();
        initArgs.setClientID("my-custom-java-client");
        initArgs.setAdapterID("my-language-debugger");
        initArgs.setLinesStartAt1(true);
        initArgs.setColumnsStartAt1(true);
        initArgs.setPathFormat("path");

        debugServer.initialize(initArgs).thenAccept(capabilities -> {
            System.out.println("Server capabilities received: " + capabilities.toString());
            
            // 2. Launch or Attach to the target application
            // Map containing custom settings your debug adapter expects (e.g., program path, env variables)
            Map<String, Object> launchArgs = new HashMap<>();
            launchArgs.put("program", "/path/to/workspace/main.src");
            launchArgs.put("stopOnEntry", false);
            
            // Call launch (or debugServer.attach(...) depending on mode)
            debugServer.launch(launchArgs).thenAccept(v -> {
                System.out.println("Launch request sent successfully.");
                
                // 3. Set initial breakpoints if any exist in the UI
                SetBreakpointsArguments bpArgs = new SetBreakpointsArguments();
                // Populate file path and source line constraints here...
                debugServer.setBreakpoints(bpArgs);

                // 4. Signal that configuration is done. The adapter will now start execution.
                ConfigurationDoneArguments configDone = new ConfigurationDoneArguments();
                debugServer.configurationDone(configDone);
            });
        });
    }

}
