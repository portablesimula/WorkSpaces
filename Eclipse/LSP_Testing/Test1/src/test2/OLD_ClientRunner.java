package test2;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

public class OLD_ClientRunner {

    public static void startClient(InputStream serverInputStream, OutputStream serverOutputStream) {
        try {
            // 1. Instantiate your local client implementation
            OLD_MyLanguageClient client = new OLD_MyLanguageClient();

            // 2. Build the launcher to handle serialization and listening
            Launcher<LanguageServer> launcher = LSPLauncher.createClientLauncher(
                    client, 
                    serverInputStream, 
                    serverOutputStream
            );

            // 3. Extract the remote server proxy and assign it to the client
            LanguageServer serverProxy = launcher.getRemoteProxy();
            client.setServerProxy(serverProxy);

            // 4. Start listening for incoming JSON-RPC messages on a background thread
            Future<Void> listeningFuture = launcher.startListening();

            // 5. Trigger the LSP lifecycle by initializing the server
            InitializeParams initParams = new InitializeParams();
            // Set client capabilities and workspace configuration parameters here...
            
            InitializeResult initResult = serverProxy.initialize(initParams).get();
            logger.info("Server initialized. Capabilities: " + initResult.getCapabilities());

            // Notice the server that initialization is fully complete
            serverProxy.initialized(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
