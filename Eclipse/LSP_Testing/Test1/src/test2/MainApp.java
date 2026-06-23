package test2;

import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.jsonrpc.Launcher;
//import org.eclipse.lsp4j.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;

import simula.lsp.SimulaLanguageServer;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.logging.Logger;

public class MainApp {
	private static final Logger logger = Logger.getLogger(MainApp.class.getName());

	public static void main(String[] args) throws Exception {
        // 1. Setup communication streams (Piped streams used here for an in-memory demo)
        PipedInputStream serverIn = new PipedInputStream();
        PipedOutputStream clientOut = new PipedOutputStream(serverIn);

        PipedInputStream clientIn = new PipedInputStream();
        PipedOutputStream serverOut = new PipedOutputStream(clientIn);

        // 2. Instantiate server components
        SimulaLanguageServer serverImpl = new SimulaLanguageServer();

        // 3. Create the Server Launcher 
        // Passing serverImpl automatically invokes connect() if it implements LanguageClientAware
        Launcher<LanguageClient> serverLauncher = LSPLauncher.createServerLauncher(
                serverImpl, 
                serverIn, 
                serverOut
        );

        // Start listening for incoming JSON-RPC requests on the server side
        serverLauncher.startListening();

        // 4. Create and start the Client Launcher
        MyLanguageClient clientImpl = new MyLanguageClient();
        Launcher<LanguageServer> clientLauncher = LSPLauncher.createClientLauncher(
                clientImpl, 
                clientIn, 
                clientOut
        );
        
        // Start listening for incoming JSON-RPC notifications on the client side
        clientLauncher.startListening();

        // Obtain the remote proxy proxy objects
        LanguageServer serverProxy = clientLauncher.getRemoteProxy();

        // 5. Explicitly invoke the lifecycle handshake as mandated by LSP spec
        logger.info("Sending initialize request...");
        serverProxy.initialize(new InitializeParams()).get(); 
        
        logger.info("Sending initialized notification...");
        serverProxy.initialized(new InitializedParams());

        logger.info("Language Server is running and fully initialized!");
        
        
    }
}
