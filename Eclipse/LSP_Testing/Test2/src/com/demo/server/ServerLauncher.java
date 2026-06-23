package com.demo.server;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.launch.LSPLauncher;

public class ServerLauncher {
    public static void main(String[] args) {
        MyLanguageServer server = new MyLanguageServer();
        // Kobler serveren til Standard Input og Output
        Launcher<LanguageClient> launcher = LSPLauncher.createServerLauncher(
                server, System.in, System.out);
        
        server.connect(launcher.getRemoteProxy());
        launcher.startListening();
    }
}
