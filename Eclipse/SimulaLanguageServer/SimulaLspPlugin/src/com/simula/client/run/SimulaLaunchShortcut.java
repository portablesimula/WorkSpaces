package com.simula.client.run;

import java.net.URI;
import java.nio.file.Path;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

import com.simula.dap.SimulaDapClient;

public class SimulaLaunchShortcut implements ILaunchShortcut {

	@Override
    public void launch(ISelection selection, String mode) {
    	IO.println("SimulaLaunchShortcut.launch:" + mode + ", selection: " + selection);
        if (selection instanceof IStructuredSelection) {
            Object element = ((IStructuredSelection) selection).getFirstElement();
            IFile file = null;
            
            // Adapt the selection to an IFile
            if (element instanceof IFile)
                file = (IFile) element;
            else if (element instanceof IAdaptable)
                file = ((IAdaptable) element).getAdapter(IFile.class);
            
            if (file != null) launchFile(file, mode);
        }
    }

    @Override
    public void launch(IEditorPart editor, String mode) {
    	IO.println("SimulaLaunchShortcut.launch:" + mode + ", editor: " + editor);
        IFile file = editor.getEditorInput().getAdapter(IFile.class);
        if (file != null) {
            launchFile(file, mode);
        }
    }

    private void launchFile(IFile file, String mode) {
        try {
        	IO.println("SimulaLaunchShortcut.launchFile: " + file);
        	IO.println("SimulaLaunchShortcut.launchFile: mode: " + mode);
            ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
            ILaunchConfigurationType type = launchManager.getLaunchConfigurationType("com.simula.client.run.launchType");
        	IO.println("SimulaLaunchShortcut.launchFile: ILaunchConfigurationType: " + type);
            
            // Hent IPath-objektet
            IPath location = file.getLocation(); 

            if (location != null) {
                // Gjør om til en standard OS-streng (f.eks. C:\Prosjekter\Simula\Main.sim eller /home/user/...)
                String absolutePath = location.toOSString(); 
             	IO.println("SimulaLaunchShortcut.launchFile: absolutePath: " + absolutePath);
            }
            
            // NOTE: To ensure correct URI: Eg.g  file:///
            //       By converting the URI into a Path object and then back into a URI,
            //       Java will automatically format it according to the applicable standard (file:/// on Windows/Unix).
            URI documentUri = Path.of(file.getLocationURI()).toUri();
            IO.println("SimulaLaunchShortcut.launchFile: documentUri: " + documentUri);

        	IO.println("SimulaLaunchShortcut.launchFile: DETTE MÅ RETTES/IMPLEMENTERES SEINERE: ");
        	
//        	launchSimulaSession(file, mode);
        	SimulaDapClient simulaDapClient = new SimulaDapClient();
        	simulaDapClient.launchSimula(documentUri, mode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
