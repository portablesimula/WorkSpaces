package com.simula.dap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

/// extension point="org.eclipse.debug.core.launchConfigurationTypes"
/// 
/// SEE ALSO:  delegate="org.eclipse.acute.dotnetrun.DotnetRunDelegate"
///            delegate="org.eclipse.acute.dotnettest.DotnetTestDelegate"
///            delegate="org.eclipse.acute.debug.DotnetDebugLaunchDelegate"
///            delegate="org.eclipse.acute.debug.DotnetDebugAttachDelegate"
///
public class SimulaLaunchDelegate implements ILaunchConfigurationDelegate {
    
    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        IO.println("SimulaLaunchDelegate.launch: configuration: " + configuration);
        // 1. Hent ut attributter brukeren har lagt inn (f.eks. filbane eller argumenter)
        String scriptPath = configuration.getAttribute("SCRIPT_PATH", "");
        
        if (mode.equals("run")) {
            // 2. Kjøre-logikk: Start ekstern prosess eller kjør internt i Eclipse
            IO.println("SimulaLaunchDelegate.launch: RUN: " + scriptPath);
        } else if (mode.equals("debug")) {
            // 3. Feilsøkings-logikk: Koble til en debugger
            IO.println("SimulaLaunchDelegate.launch: DEBUG: " + scriptPath);
        }
    }
}
