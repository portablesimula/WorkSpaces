package simula.server;

import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;

import simula.Comn;

public class Main {

    public static void main(String[] args) {
        Comn.popUp("SimulaLanguageServer.main: ");
        
    	SimulaLanguageServer server = new SimulaLanguageServer();
        Comn.popUp("SimulaLanguageServer.main: server: " + server);
        
        // Wire up the launcher to read from standard input/output streams 
        // provided by the IntelliJ Client process
        var launcher = LSPLauncher.createServerLauncher(server, System.in, System.out);

        // Fetch the proxy client representing IntelliJ and pass it to your server
        LanguageClient clientProxy = launcher.getRemoteProxy();
        server.connect(clientProxy);

        // Start listening to the input streams
        launcher.startListening();
    }

}
