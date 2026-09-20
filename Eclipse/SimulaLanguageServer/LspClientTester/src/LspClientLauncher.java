import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.ClientCapabilities;
import org.eclipse.lsp4j.TextDocumentClientCapabilities;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class LspClientLauncher {

    private LanguageServer server;
    private MyLanguageClient client;
    private Future<Void> listenFuture;

    public void start(Process serverProcess) throws Exception {
    	IO.println("LspClientLauncher.start: serverProcess: " + serverProcess);
        // 1. Hent I/O strømmer fra server-prosessen
        InputStream in = serverProcess.getInputStream();
        OutputStream out = serverProcess.getOutputStream();

        this.client = new MyLanguageClient();

        // 2. Bruk LSP4J Launcher for å koble JSON-RPC til I/O-strømmene
        Launcher<LanguageServer> launcher = Launcher.createLauncher(
                client, 
                LanguageServer.class, 
                in, 
                out
        );
    	IO.println("LspClientLauncher.start: launcher: " + launcher.getClass());

        // 3. Start lytting på en egen tråd
        this.listenFuture = launcher.startListening();
        this.server = launcher.getRemoteProxy();
    	IO.println("LspClientLauncher.start: RemoteProxy: " + server.getClass());

        // 4. Initialiser serveren (Handshake)
        InitializeParams initParams = new InitializeParams();
        initParams.setProcessId((int) ProcessHandle.current().pid());
        initParams.setRootUri(System.getProperty("user.dir")); // Sett rotmappe for prosjektet
        initParams.setCapabilities(new ClientCapabilities());
    	IO.println("LspClientLauncher.start: InitializeParams: " + initParams);

        CompletableFuture<InitializeResult> initialize = server.initialize(initParams);
        InitializeResult result = initialize.get(); // Vent på at serveren blir klar
    	IO.println("LspClientLauncher.start: InitializeResult: " + result);
        
        System.out.println("LSP Server initialisert! Serverinfo: " + result.getServerInfo().getName());
        
        // 5. Fortell serveren at klienten er ferdig initialisert
        server.initialized();
    }

    public LanguageServer getServer() {
        return this.server;
    }

    public void stop() {
        if (listenFuture != null) {
            listenFuture.cancel(true);
        }
    }
}
