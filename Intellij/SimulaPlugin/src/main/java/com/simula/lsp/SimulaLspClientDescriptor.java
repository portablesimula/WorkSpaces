package com.simula.lsp;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.lsp.api.LspServerDescriptor;
import com.intellij.platform.lsp.api.customization.LspDiagnosticsSupport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

public class SimulaLspClientDescriptor extends LspServerDescriptor {

    /**
     * Constructs the LSP descriptor.
     * @param project The current IntelliJ project context.
     * @param presentableName The display name of your language server visible to users.
     */
    public SimulaLspClientDescriptor(@NotNull Project project, @NotNull String presentableName) {
        // You pass a unique server ID string and the project instance to the superclass
        super(project, presentableName);
        JOptionPane.showMessageDialog(null, "NEW SimulaLspClientDescriptor: "+presentableName);
        IO.println("NEW SimulaLspClientDescriptor: "+presentableName);
    }

    /**
     * Determines whether this LSP server should be started for a specific file.
     * This is typically filtered by file type or file extension.
     */
    @Override
    public boolean isSupportedFile(@NotNull VirtualFile file) {
        JOptionPane.showMessageDialog(null, "SimulaLspClientDescriptor.isSupportedFile: "+file);
        IO.println("SimulaLspClientDescriptor.isSupportedFile: "+file);
        IO.println("SimulaLspClientDescriptor.isSupportedFile: extension: "+file.getExtension());
       // Example: Only activate the server for files with the ".sim" extension
        boolean res = "sim".equals(file.getExtension());
        IO.println("SimulaLspClientDescriptor.isSupportedFile: RETURNS: "+res);
        Thread.dumpStack();
        return res;
    }

    /**
     * Starts the external Language Server process.
     * Define how the OS command line should look to execute your server.
     */
//    @NotNull
//    @Override
    public GeneralCommandLine OLD_createCommandLine() throws ExecutionException {
        JOptionPane.showMessageDialog(null, "SimulaLspClientDescriptor.createCommandLine: "+ SimulaLspServerDescriptor.SimulaServerExecutable);
        IO.println("SimulaLspClientDescriptor.createCommandLine: "+ SimulaLspServerDescriptor.SimulaServerExecutable);
        // Example: launcher command for a node-based LSP server
        GeneralCommandLine commandLine = new GeneralCommandLine();

        commandLine.setExePath("node");
//        commandLine.addParameter("/path/to/your/language-server/bin/server.js");
//        commandLine.addParameter("C:/GitHub/WorkSpaces/Intellij/SimulaPlugin/src/main/resources/jars/SimulaLspServer.jar");
        commandLine.addParameter(SimulaLspServerDescriptor.SimulaServerExecutable);
        commandLine.addParameter("--stdio"); // Instructs the server to communicate via standard I/O
        
        // You can set the working directory to the project's root folder
        commandLine.setWorkDirectory(getProject().getBasePath());

        JOptionPane.showMessageDialog(null, "SimulaLspClientDescriptor.createCommandLine: "+commandLine);
        IO.println("SimulaLspClientDescriptor.createCommandLine: "+commandLine);
        return commandLine;
    }

    @NotNull
    @Override
    public GeneralCommandLine createCommandLine() {
        JOptionPane.showMessageDialog(null, "SimulaLspClientDescriptor.createCommandLine: "+ SimulaLspServerDescriptor.SimulaServerExecutable);
        IO.println("SimulaLspClientDescriptor.createCommandLine: "+ SimulaLspServerDescriptor.SimulaServerExecutable);
        // Finn banen til din installerte plugin dynamically
//        var plugin = PluginManagerCore.getPlugin(PluginId.getId("com.example.simula.pluginId")); // Bruk din faktiske plugin-ID
        // See: plugin.xml
        var plugin = PluginManagerCore.getPlugin(PluginId.getId("com.simula.SimulaLspServer")); // Bruk din faktiske plugin-ID
        IO.println("SimulaLspClientDescriptor.createCommandLine: plugin: "+plugin);
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
        commandLine.addParameter("--stdio"); // Instructs the server to communicate via standard I/O

        // Definer workspace/kjøremappe om nødvendig
        Project project = getProject();

//        commandLine.setWorkDirectory(getProject().getBasePath());
        String workingDir = SimulaLspManager.getWorkingDir(project);
        commandLine.setWorkDirectory(workingDir);

        JOptionPane.showMessageDialog(null, "SimulaLspClientDescriptor.createCommandLine: "+commandLine);
        IO.println("SimulaLspClientDescriptor.createCommandLine: commandLine: "+commandLine);
        return commandLine;
    }

    /**
     * Optional: Tells the platform whether your LSP server supports diagnostics/error highlighting.
     */
    @Nullable
//    @Override
    public LspDiagnosticsSupport getDiagnosticsSupport() {
        return new LspDiagnosticsSupport();
    }
}
