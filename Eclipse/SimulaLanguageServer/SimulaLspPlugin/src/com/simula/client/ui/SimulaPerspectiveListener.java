package com.simula.client.ui;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroPart;

public class SimulaPerspectiveListener implements IPerspectiveListener {

    // Your custom Simula perspective ID declared in plugin.xml
    private static final String SIMULA_PERSPECTIVE_ID = "com.simula.client.ui.SimulaPerspective";

    @Override
    public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
    	IO.println("SimulaPerspectiveListener.perspectiveActivated: " + perspective.getId());
        if (SIMULA_PERSPECTIVE_ID.equals(perspective.getId())) {
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
