package com.simula.dap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.lsp4e.debug.debugmodel.DSPDebugTarget;
import org.eclipse.lsp4e.debug.debugmodel.TransportStreams;

public class REMOVED_SimulaLaunchDelegate implements ILaunchConfigurationDelegate {
//    @Override
//    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
//        // 1. Extract Simula file or project details from the configuration
//        // 2. Start or connect to your SimulaLspServer with DAP active
//        // 3. Initiate the DAP session via LSP4E Debug framework
//    }
	
    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        
        // 1. Start din debug-adapter prosess eller hent en tilkobling (Stream/Socket)
        Process debugAdapterProcess = startSimulaDebugAdapter(configuration);
        
        // 2. Opprett parametere som skal sendes til DAP "initialize"-kallet
        Map<String, Object> dspParameters = new HashMap<>();
        dspParameters.put("program", configuration.getAttribute("SIMULA_FILE_PATH", ""));
        // Legg til andre Simula-spesifikke attributter...

        // 3. Opprett og registrer DSPDebugTarget på din launch
//    	public DSPDebugTarget(ILaunch launch, Supplier<TransportStreams> streamsSupplier, Map<String, Object> dspParameters) {

//        DSPDebugTarget target = new DSPDebugTarget(
//            launch, 
//            debugAdapterProcess, 
//            debugAdapterProcess.getInputStream(), 
//            debugAdapterProcess.getOutputStream(), 
//            dspParameters
//        );
        
        Supplier<TransportStreams> streamsSupplier = null;
        DSPDebugTarget target = new DSPDebugTarget(launch, streamsSupplier, dspParameters);
        
        // 4. Initialiser målet for å starte kommunikasjon over DAP
        target.initialize(); 
        launch.addDebugTarget(target);
    }
	
}
