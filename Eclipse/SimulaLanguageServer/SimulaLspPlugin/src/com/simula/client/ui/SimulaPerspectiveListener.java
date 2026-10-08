package com.simula.client.ui;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import com.simula.client.DEF;

public class SimulaPerspectiveListener implements IPerspectiveListener {
	
	public static void switchToSimulaPerspective() {
	    // ID-en du har definert for perspektivet ditt i plugin.xml

	    // Kjør på UI-tråden for å unngå ugyldige tråd-tilganger
	    PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
	        @Override
	        public void run() {
	            try {
	                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	                if (window != null) {
	                    PlatformUI.getWorkbench().showPerspective(DEF.SIMULA_PERSPECTIVE_ID, window);
	                }
	            } catch (WorkbenchException e) {
	                // Håndter eventuell feil hvis perspektiv-ID-en ikke finnes
	                e.printStackTrace();
	            }
	        }
	    });
	}

    @Override
    public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
    	IO.println("SimulaPerspectiveListener.perspectiveActivated: " + perspective.getId());
        if (DEF.SIMULA_PERSPECTIVE_ID.equals(perspective.getId())) {
            try {
                // Find and maximize the global intro/welcome view
            	IViewReference viewRef = page.findViewReference("org.eclipse.ui.internal.introview");
            	IO.println("SimulaPerspectiveListener.perspectiveActivated: viewRef: " + viewRef);
            	IO.println("SimulaPerspectiveListener.perspectiveActivated: viewRef: " + viewRef.getId());
                page.setPartState(
                	viewRef, 
                    IWorkbenchPage.STATE_MAXIMIZED
                );
            } catch (Exception e) {
                // Handle fallback gracefully if the intro view is unavailable
            	IO.println("SimulaPerspectiveListener.perspectiveActivated: " + perspective.getId() + "  FAILED");
           }
            
            
        }
    }

    @Override
    public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
        // Unused interface method
    }
}
