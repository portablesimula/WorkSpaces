package client;

import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.TextDocumentItem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	
    public static void main(String[] args) {
        try {
            // Start språkserveren som en OS-prosess
            String jarPath = "C:/Users/omyhr/Simula/SimulaLspServer.jar";
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarPath, "--stdio");
            pb.redirectError(ProcessBuilder.Redirect.INHERIT); 
            
            Process serverProcess = pb.start();
            IO.println("Main: Start done: " + jarPath);

            LspClientLauncher clientLauncher = new LspClientLauncher();
            clientLauncher.start(serverProcess);
            IO.println("Main: clientLauncher.start done: " + serverProcess);

            // Nå kan du bruke clientLauncher.getServer() til å interagere med serveren
            // Eksempel: Åpne et dokument
            
            Path path = Paths.get("C:/Users/omyhr/Simula_OLD2/Simula-2.0/samples/Test.sim");
            String uri = path.toUri().toString();
            String content = Files.readString(path);
       
            TextDocumentItem document = new TextDocumentItem();
            document.setUri(uri);
            document.setLanguageId("simula");
            document.setVersion(1);
            document.setText(content);

            clientLauncher.getServer().getTextDocumentService().didOpen(new DidOpenTextDocumentParams(document));

            // Hold applikasjonen i gang for å motta asynkron diagnostikk
            Thread.sleep(10000);

            clientLauncher.stop();
            serverProcess.destroy();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
