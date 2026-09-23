package simula.client;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;

public class NativeLanguageServerProvider extends ProcessStreamConnectionProvider {

    public NativeLanguageServerProvider() {
        List<String> commands = new ArrayList<>();
        
        // Example A: Spawning a local native executable binary
//        commands.add("C:/Users/omyhr/Simula/SimulaLspServer.jar"); 
//        commands.add("--stdio"); // Instruct the server to use standard I/O
        
//        /* 
        // Example B: Spawning a Java-based jar language server
        commands.add("java");
        commands.add("-jar");
        commands.add("C:/Users/omyhr/Simula/SimulaLspServer.jar"); 
        commands.add("--stdio"); // Instruct the server to use standard I/O

        // Pass the command list and the working directory to the parent constructor
        setCommands(commands);
        setWorkingDirectory(System.getProperty("user.home"));
    }
}
