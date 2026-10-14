package com.simula.dap;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;
import org.osgi.framework.Bundle;

public class ResourceJarDebugAdapterProvider extends ProcessStreamConnectionProvider {

    public ResourceJarDebugAdapterProvider() {
        // Invoke the super constructor with our dynamically calculated commands
        super(getLaunchCommands(), null);
    }

    private static List<String> getLaunchCommands() {
        List<String> commands = new ArrayList<>();
        
        // 1. Locate standard java executable
        String javaHome = System.getProperty("java.home");
        String javaExecutable = javaHome + File.separator + "bin" + File.separator + "java";
        commands.add(javaExecutable);
        
        // 2. Locate the embedded JAR file in your plugin resources
        commands.add("-jar");
        try {
            Bundle bundle = Platform.getBundle("com.example.debug.plugin.id"); // Replace with your exact bundle symbolic name
            URL fileURL = bundle.getEntry("resources/my-adapter.jar"); // Path inside your plugin structure
            URL resolvedURL = FileLocator.toFileURL(fileURL);
            
            // Extract the absolute file path on disk
            File jarFile = new File(resolvedURL.toURI());
            commands.add(jarFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to locate the embedded debug adapter JAR", e);
        }
        
        // (Optional) Add arguments required by your DA (e.g., standard IO flag)
        commands.add("--stdio"); 
        
        return commands;
    }
}
