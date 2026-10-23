package com.simula.client.run;

import java.net.Socket;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.lsp4e.debug.DSPPlugin;

/// extension point="org.eclipse.debug.core.launchConfigurationTypes"
/// 
/// SEE ALSO:  delegate="org.eclipse.acute.dotnetrun.DotnetRunDelegate"
///            delegate="org.eclipse.acute.dotnettest.DotnetTestDelegate"
///            delegate="org.eclipse.acute.debug.DotnetDebugLaunchDelegate"
///            delegate="org.eclipse.acute.debug.DotnetDebugAttachDelegate"
///
public class REMOVED_SimulaLaunchDelegate implements ILaunchConfigurationDelegate {
    
    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        IO.println("SimulaLaunchDelegate.launch: configuration: " + configuration);
        // 1. Hent ut attributter brukeren har lagt inn (f.eks. filbane eller argumenter)
        String scriptPath = configuration.getAttribute("SCRIPT_PATH", "");
        
        IO.println("SimulaLaunchDelegate.launch: DETTE MÅ IMPLEMENTERES !");
//      if (mode.equals("run")) {
        if (mode.equals(ILaunchManager.RUN_MODE)) {
            // 2. Kjøre-logikk: Start ekstern prosess eller kjør internt i Eclipse
            IO.println("SimulaLaunchDelegate.launch: RUN: " + scriptPath);
            
//      } else if (mode.equals("debug")) {
        } else if (mode.equals(ILaunchManager.DEBUG_MODE)) {
            // 3. Feilsøkings-logikk: Koble til en debugger
            IO.println("SimulaLaunchDelegate.launch: DEBUG: " + scriptPath);
        }
        
        
        String host = "127.0.0.1";
        int port = 4711;
        
        try {
            // 1. Establish the network connection to your Simula DAP server
            Socket socket = new Socket(host, port);
            
            // 2. Delegate the input/output streams to Eclipse LSP4E Debug framework
            DSPPlugin.launchDebugSession(
                launch, 
                socket.getInputStream(), 
                socket.getOutputStream(), 
                monitor
            );
            
        } catch (Exception e) {
            throw new CoreException(new org.eclipse.core.runtime.Status(
                org.eclipse.core.runtime.IStatus.ERROR, 
                "your.plugin.id", 
                "Failed to connect to Simula DAP server on port " + port, 
                e
            ));
        }
        
    }
}
