package com.simula.client.run;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

/// extension point="org.eclipse.debug.core.launchConfigurationTypes"
/// 
/// SEE ALSO:  delegate="org.eclipse.acute.dotnetrun.DotnetRunDelegate"
///            delegate="org.eclipse.acute.dotnettest.DotnetTestDelegate"
///            delegate="org.eclipse.acute.debug.DotnetDebugLaunchDelegate"
///            delegate="org.eclipse.acute.debug.DotnetDebugAttachDelegate"
///
public class SimulaLaunchDelegate extends LaunchConfigurationDelegate {

    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
            throws CoreException {
    	
        IO.println("SimulaLaunchDelegate.launch: DETTE MÅ IMPLEMENTERES !");
        
        // Use modern SubMonitor instead of traditional monitor structures
        SubMonitor subMonitor = SubMonitor.convert(monitor, "Launching Simula Application", 100);
        
        try {
            // 1. Retrieve attributes from the launch configuration
            String mainClass = configuration.getAttribute("SIMULA_MAIN_CLASS", "");
            boolean isDebug = "debug".equals(mode);
            
            subMonitor.split(20);
            
            // 2. Perform your launch logic (e.g., building a process)
            if (subMonitor.isCanceled()) {
                return;
            }
            
            // TODO: Initialize your RuntimeProcess or DebugTarget here
            // Example: Process p = ...
            // DebugPlugin.newProcess(launch, p, "Simula Execution Process");
            
            subMonitor.split(80);
            
        } finally {
            // Monitor lifecycle is safely managed via try-finally or SubMonitor
            if (monitor != null) {
                monitor.done();
            }
        }
    }
}
