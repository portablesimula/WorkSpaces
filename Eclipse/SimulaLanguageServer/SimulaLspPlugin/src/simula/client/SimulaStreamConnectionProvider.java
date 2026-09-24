package simula.client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;
import org.osgi.framework.FrameworkUtil;


/// Project Structure
///
///   SimulaLspPlugin/
///   ├── META-INF/
///   │   └── MANIFEST.MF          <-- Må oppdateres
///   ├── src/                     <-- Din Java-kode for LSP-klienten
///   ├── server/                  <-- Lag mappen her hvis den ikke finnes
///   │   └── SimulaLspServer.jar  <-- LEGG FILEN HER
///   ├── build.properties         <-- Må oppdateres
///   ├── plugin.xml
///   └── .project

public class SimulaStreamConnectionProvider extends ProcessStreamConnectionProvider {

    public SimulaStreamConnectionProvider() {
        List<String> commands = new ArrayList<>();
        
        // Use the system java executable
        commands.add("java");
        commands.add("-jar");
        
        try {
            // Locate the jar inside your bundle directory structure
            URL bundleUrl = FrameworkUtil.getBundle(this.getClass()).getEntry("server/SimulaLspServer.jar");
            URL fileUrl = FileLocator.toFileURL(bundleUrl);
            File jarFile = new File(fileUrl.getPath());
            
            commands.add(jarFile.getAbsolutePath());
        } catch (IOException e) {
            // Fallback command setup or proper error logging
            e.printStackTrace();
        }

        setCommands(commands);
        setWorkingDirectory(System.getProperty("user.dir"));
    }
}
