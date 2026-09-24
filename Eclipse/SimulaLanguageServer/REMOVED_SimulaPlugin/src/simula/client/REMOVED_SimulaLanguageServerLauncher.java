package simula.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;
import org.osgi.framework.Bundle;

public class REMOVED_SimulaLanguageServerLauncher extends ProcessStreamConnectionProvider {

    public REMOVED_SimulaLanguageServerLauncher() {
        List<String> commands = new ArrayList<>();
        
        // 1. Point to the java executable
        commands.add("java"); 
        commands.add("-jar");
        
        // 2. Locate the language server jar bundled inside your plugin
        try {
            Bundle bundle = Platform.getBundle("simula.plugin");
            File file = new File(FileLocator.toFileURL(bundle.getEntry("simula/server/SimulaLanguageServer.jar")).getPath());
            commands.add(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        setCommands(commands);
        setWorkingDirectory(System.getProperty("user.home"));
    }
}
