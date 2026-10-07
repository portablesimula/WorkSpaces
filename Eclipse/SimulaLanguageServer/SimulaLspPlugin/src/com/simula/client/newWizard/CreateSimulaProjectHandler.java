package com.simula.client.newWizard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/// Used by: plugin.xml  <extension point="org.eclipse.ui.handlers">
public class CreateSimulaProjectHandler extends AbstractHandler {
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = HandlerUtil.getActiveShell(event);
        // Open your custom Simula Wizard Dialog here
        WizardDialog dialog = new WizardDialog(shell, new NewSimulaProjectWizard());
        dialog.open();
        return null;
    }
}
