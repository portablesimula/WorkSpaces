package com.simula.dap;

import java.io.IOException;
import java.util.Collections;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.lsp4e.debug.launcher.IDebugAdapterLaunchRepresenter;

public class REMOVED_SimulaDebugAdapterProvider implements IDebugAdapterLaunchRepresenter {
    
    @Override
    public Process startProcess(ILaunchConfiguration configuration, String mode) throws IOException {
        // Spawn the SimulaLspServer.jar in debug mode if your JAR handles it via process I/O streams
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", "/path/to/SimulaLspServer.jar", "--debug");
        return builder.start();
    }

    @Override
    public List<String> getAdapterArguments(ILaunchConfiguration configuration, String mode) {
        return Collections.emptyList();
    }
}
