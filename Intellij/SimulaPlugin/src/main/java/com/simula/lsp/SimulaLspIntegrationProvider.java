package com.simula.lsp;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
//import com.intellij.platform.lsp.api.LspClientStarter;
import com.intellij.platform.lsp.api.LspIntegrationProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SimulaLspIntegrationProvider implements LspIntegrationProvider {

    @Override
    public void fileOpened(@NotNull Project project, 
                            @NotNull VirtualFile file, 
                            @NotNull LspClientStarter clientStarter) {

         JOptionPane.showMessageDialog(null, "SimulaLspIntegrationProvider.fileOpened: "+file);
         IO.println("SimulaLspIntegrationProvider.fileOpened: "+file);

        // Check if the opened file belongs to your language
        if ("sim".equals(file.getExtension())) {
            IO.println("SimulaLspIntegrationProvider.fileOpened: CALL: clientStarter.ensureClientStarted: "+file);
            // Tell the IDE to ensure the client descriptor is started
            clientStarter.ensureClientStarted(new SimulaLspClientDescriptor(project, "SimulaPlugin"));
        }
    }


}
