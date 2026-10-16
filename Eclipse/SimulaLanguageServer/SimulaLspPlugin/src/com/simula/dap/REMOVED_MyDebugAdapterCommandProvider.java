package com.simula.dap;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.lsp4e.debug.DAPStreamConnectionProvider; // Adjust based on your exact LSP4E target API
import org.osgi.framework.Bundle;

public class REMOVED_MyDebugAdapterCommandProvider extends DAPStreamConnectionProvider {

    @Override
    public List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        
        // 1. Specify the Java runtime binary
        commands.add("java");
        commands.add("-jar");
        
        // 2. Resolve the path to your internal resource jar dynamically
        try {
            Bundle bundle = Platform.getBundle("com.example.mydebugger"); // Your plugin ID
            URL resourceUrl = bundle.getEntry("resources/my-adapter.jar"); // Relative path in your project
            
            // Converts bundle URL (bundleentry://...) to a file system path (file:/...)
            URL fileUrl = FileLocator.toFileURL(resourceUrl);
            File jarFile = new File(fileUrl.getPath());
            
            commands.add(jarFile.getAbsolutePath());
        } catch (IOException e) {
            // Handle fallback or logging
            e.printStackTrace();
        }
        
        // Optional: append any required launch arguments for your DAP server here
        // commands.add("--server"); 
        commands.add("--stdio"); 
        
        return commands;
    }
}
