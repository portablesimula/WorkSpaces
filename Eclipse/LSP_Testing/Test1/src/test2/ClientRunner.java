package test2;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.launch.LSPLauncher;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

public class ClientRunner {

    public static void main(String[] args) {
        try {
            // 1. Initialize your client instance
            MyLanguageClient client = new MyLanguageClient();

            // 2. Obtain standard or network streams talking to the server process
            InputStream inputStream = System.in; 
            OutputStream outputStream = System.out;

            // 3. Create the client launcher orchestration infrastructure
            Launcher<LanguageServer> launcher = LSPLauncher.createClientLauncher(
                    client, 
                    inputStream, 
                    outputStream
            );

            // 4. Retrieve the communication remote proxy matching the LanguageServer contract
            LanguageServer serverProxy = launcher.getRemoteProxy();

            // 5. Explicitly check and hand off the remote proxy using LanguageClientAware
            if (serverProxy instanceof LanguageClientAware) {
                // Cast and supply the client instance back down to the target server instance
                ((LanguageClientAware) serverProxy).connect(client);
            }

            // 6. Spawn the active background JSON-RPC thread pool parsing incoming requests
            Future<Void> listeningThread = launcher.startListening();
            
            logger.info("LSP Client successfully attached and listening.");
            
            // Keep app running by blocking on the communication handle
            listeningThread.get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
