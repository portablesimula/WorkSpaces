import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        try {
            // Start språkserveren som en OS-prosess
            String jarPath = "C:/Users/omyhr/Simula/SimulaLspServer.jar";
//            ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarPath, "--stdio");
            File file = new File(jarPath);
            IO.println("Main: file.exists: " + file.exists());
            IO.println("Main: file.canExecute: " + file.canExecute());
            IO.println("Main: file.canRead: " + file.canRead());
            IO.println("Main: file.canWrite: " + file.canWrite());
            
//            ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarPath);
//            ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarPath, "--stdio");
            
            // 1. Identify paths to your main JAR and the additions
            String mainJar = jarPath; // "path/to/runable.jar";
            
//            String extraDependencies = "path/to/extensions/*"; // Wildcard for extra JARs
			String lib1 = "C:/Program Files/Eclipse_LSP/org.eclipse.lsp4j-1.0.0.jar";
			String lib2 = "C:/Program Files/Eclipse_LSP/org.eclipse.lsp4j.jsonrpc-0.24.0.jar";
			String lib3 =  "C:/Program Files/Eclipse_LSP/gson-2.9.0.jar";

            // 2. Build the OS-specific classpath string
            String pathSeparator = File.pathSeparator; // ";" on Windows, ":" on Unix
			String extraDependencies = lib1 + pathSeparator + lib2 + pathSeparator + lib3;
            String combinedClasspath = mainJar + pathSeparator + extraDependencies;

            // 3. Define the main class inside the executable JAR
            String mainClass = "simula.server.Main"; 

            // 4. Construct the command array
            List<String> command = new ArrayList<>();
            command.add("java");
            command.add("-cp");
            command.add(combinedClasspath);
            command.add(mainClass);
            
            // Pass any runtime arguments your application needs
            command.add("arg1");
            command.add("arg2");

            // 5. Execute via ProcessBuilder
            ProcessBuilder pb = new ProcessBuilder(command);

            pb.redirectError(ProcessBuilder.Redirect.INHERIT); 
            
            Process serverProcess = pb.start();
            IO.println("Main: Start done: " + jarPath);

            LspClientLauncher clientLauncher = new LspClientLauncher();
            clientLauncher.start(serverProcess);

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
