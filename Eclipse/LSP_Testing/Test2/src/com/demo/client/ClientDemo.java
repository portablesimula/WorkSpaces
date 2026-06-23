package com.demo.client;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageServer;

import com.google.gson.TypeAdapterFactory;

import org.eclipse.lsp4j.launch.LSPLauncher;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientDemo {
	TypeAdapterFactory TEST;
	
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // 1. Start server-prosessen (Husk å kompilere serveren til en JAR eller kjør via Java-kommando)
        // For enkelhets skyld bruker vi her samme JVM-kommando dersom alt er i samme prosjekt:
        ProcessBuilder builder = new ProcessBuilder("java", "-cp", System.getProperty("java.class.path"), "com.demo.server.ServerLauncher");
        Process serverProcess = builder.start();

        // 2. Koble klienten til serverens input/output strømmer
        MyLanguageClient client = new MyLanguageClient();
        Launcher<LanguageServer> launcher = LSPLauncher.createClientLauncher(
                client, serverProcess.getInputStream(), serverProcess.getOutputStream());

        launcher.startListening();
        LanguageServer server = launcher.getRemoteProxy();

        // 3. Send 'initialize' forespørsel til serveren
        InitializeParams initParams = new InitializeParams();
        initParams.setProcessId((int) ProcessHandle.current().pid());
        initParams.setRootUri("file:///mitt/demo/prosjekt");
        server.initialize(initParams).get();
        System.out.println("Klient: Serveren er initialisert!");

        // 4. Simuler at en fil blir åpnet
        DidOpenTextDocumentParams openParams = new DidOpenTextDocumentParams(
                new TextDocumentItem("file:///mitt/demo/prosjekt/test.txt", "plaintext", 1, "hallo"));
        server.getTextDocumentService().didOpen(openParams);

        // 5. Be om auto-fullføring (Completion)
        CompletionParams compParams = new CompletionParams(
                new TextDocumentIdentifier("file:///mitt/demo/prosjekt/test.txt"),
                new Position(0, 5));
        
        var completionList = server.getTextDocumentService().completion(compParams).get();
        
        System.out.println("Klient: Mottok forslag fra LSP-serveren:");
        
//        completionList.getLeft().getItems().forEach(item -> 
//            System.out.println(" - " + item.getLabel() + " (" + item.getDetail() + ")")
//        );
        
        List<CompletionItem> items = completionList.getLeft();
        for(CompletionItem item:items) {
          System.out.println(" - " + item.getLabel() + " (" + item.getDetail() + ")");      	
        }

        // 6. Rydd opp og avslutt
        server.shutdown().get();
        server.exit();
        serverProcess.destroy();
    }
}
