package com.simula.lsp;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.lsp.api.LspServerDescriptor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;

public class SimulaLspServerDescriptor extends LspServerDescriptor {
    public static final String SimulaServerExecutable
            = "C:/GitHub/WorkSpaces/Intellij/SimulaPlugin/src/main/resources/jars/SimulaLspServer.jar";

    /**
     * Constructs the descriptor for a specific project and sets up a unique ID.
     */
    public SimulaLspServerDescriptor(@NotNull Project project) {
        // "myLanguageLsp" serves as a unique identifier for this server session type
        super(project, "Simula Language LSP");
        JOptionPane.showMessageDialog(null, "NEW SimulaLspServerDescriptor: "+project);
        IO.println("NEW SimulaLspServerDescriptor: "+project);
    }

    /**
     * Determines if a newly opened file should trigger or attach to this Language Server.
     */
    @Override
    public boolean isSupportedFile(@NotNull VirtualFile file) {
        JOptionPane.showMessageDialog(null, "SimulaLspServerDescriptor.isSupportedFile: "+file);
        IO.println("SimulaLspServerDescriptor.isSupportedFile: "+file);
        IO.println("SimulaLspServerDescriptor.isSupportedFile: getExtension: "+file.getExtension());
       // Example: Only start the server for files ending with ".sim"
        return "sim".equals(file.getExtension());
    }

    /**
     * Spawns the underlying OS process hosting your language server.
     * The IDE communicates with this process via standard I/O (stdin/stdout).
     */
//    @NotNull
//    @Override
    public GeneralCommandLine OLD_createCommandLine() throws ExecutionException {
        JOptionPane.showMessageDialog(null, "SimulaLspServerDescriptor.createCommandLine: "+SimulaServerExecutable);
        IO.println("SimulaLspServerDescriptor.createCommandLine: "+SimulaServerExecutable);
        // Build the command line to launch your language server binary/script
        return new GeneralCommandLine()
//                .withExePath("/path/to/your/lsp-server-executable")
                .withExePath(SimulaServerExecutable)
                .withParameters("--stdio") // Standard language server stdio argument
                .withWorkDirectory(getProject().getBasePath());
    }

    @NotNull
    @Override
    public GeneralCommandLine createCommandLine() {
        // Finn banen til din installerte plugin dynamically
//        var plugin = PluginManagerCore.getPlugin(PluginId.getId("com.example.simula.pluginId")); // Bruk din faktiske plugin-ID
        // See: plugin.xml
        var plugin = PluginManagerCore.getPlugin(PluginId.getId("com.simula.SimulaLspServer")); // Bruk din faktiske plugin-ID
        if (plugin == null) {
            throw new IllegalStateException("Kunne ikke finne plugin-ressurser");
        }

//        Path pluginPath = plugin.getPluginPath();
//        // Ressurser havner vanligvis under 'lib/server/' eller 'server/' avhengig av hvordan du pakker med gradle
//        File jarFile = pluginPath.resolve("server/SimulaLspServer.jar").toFile();

        File jarFile = SimulaLspManager.getLspJarFile();

        if (!jarFile.exists()) {
            throw new IllegalStateException("Fant ikke SimulaLanguageServer.jar på bane: " + jarFile.getAbsolutePath());
        }
        // Bygg kommandoen: java -jar SimulaLanguageServer.jar
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setExePath("java");
        commandLine.addParameters("-jar", jarFile.getAbsolutePath());

        // Definer workspace/kjøremappe om nødvendig
//        commandLine.setWorkDirectory(getProject().getBasePath());
        Project project = getProject();
        String workingDir = SimulaLspManager.getWorkingDir(project);
        commandLine.setWorkDirectory(workingDir);

        JOptionPane.showMessageDialog(null, "SimulaLspServerDescriptor.createCommandLine: "+commandLine);
        IO.println("SimulaLspClientDescriptor.createCommandLine: commandLine: "+commandLine);
        return commandLine;
    }

    }
