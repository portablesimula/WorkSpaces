package com.simula.dap;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

public class SimulaLaunchShortcut implements ILaunchShortcut {
    @Override
    public void launch(ISelection selection, String mode) {
    	IO.println("SimulaLaunchShortcut.launch:" + mode + ", selection: " + selection.getClass().getSimpleName());
        if (selection instanceof IStructuredSelection) {
            Object element = ((IStructuredSelection) selection).getFirstElement();
            if (element instanceof IFile) {
                launchFile((IFile) element, mode);
            }
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
            ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
            ILaunchConfigurationType type = launchManager.getLaunchConfigurationType("com.simula.debug.launchConfigurationType");
            
            ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, file.getName());
            // This maps your UI launch setup back to the DAP Type declared in step 3
            workingCopy.setAttribute("org.eclipse.lsp4e.debug.DEBUG_ADAPTER_TYPE", "com.simula.debug.adapter");
            workingCopy.setAttribute("program", file.getLocation().toOSString());
            
            DebugUITools.launch(workingCopy, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
