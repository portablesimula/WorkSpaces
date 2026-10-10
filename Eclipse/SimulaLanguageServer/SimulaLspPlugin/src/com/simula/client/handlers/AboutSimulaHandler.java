package com.simula.client.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

public class AboutSimulaHandler extends AbstractHandler {
	
	public AboutSimulaHandler() {
		IO.println("NEW AboutSimulaHandler: Was created");
	}
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
		IO.println("AboutSimulaHandler.execute: " + event);
        // Open the custom dialog box
        AboutSimulaDialog dialog = new AboutSimulaDialog(HandlerUtil.getActiveShell(event));
        dialog.open();
        return null;
    }
}
