package com.simula.client;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class SimulaPluginActivator extends AbstractUIPlugin {

    private static SimulaPluginActivator plugin;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        
//		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//		page.addPartListener(new EditorDropInitializer());
		
//		IWorkbench workbench = PlatformUI.getWorkbench();
//		IWorkbenchWindow[] workbenchWindows = workbench.getWorkbenchWindows();
//		IO.print("SimulaPluginActivator.start: workbenchWindows: "+workbenchWindows);
//		
//		for(IWorkbenchWindow win:workbenchWindows) {
//			IWorkbenchPage activePage = win.getActivePage();
//			IO.print("SimulaPluginActivator.start: activePage: "+activePage);
//		}
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static SimulaPluginActivator getDefault() {
        return plugin;
    }
}
