package simula;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.core.resources.ResourcesPlugin;

public class CustomProjectView extends ViewPart {
    
    // The ID must match the plugin.xml declaration precisely
    public static final String ID = "com.example.myplugin.projectview";
//    private TreeViewer viewer;
    private TreeViewer viewer;

    public void createMenuManager() {
    	// 1. Create the MenuManager
    	MenuManager menuMgr = new MenuManager("#PopupMenu"); // You can name it anything
    	menuMgr.setRemoveAllWhenShown(true);

    	// 2. Add a listener so the menu clears/rebuilds dynamically when right-clicked
    	menuMgr.addMenuListener(new IMenuListener() {
    	    @Override
    	    public void menuAboutToShow(IMenuManager manager) {
    	        // (Optional) Fill your own internal actions here
    	        // manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
    	    }
    	});

    	// 3. Create the SWT Menu control and link it to the TreeViewer's Control
    	Menu menu = menuMgr.createContextMenu(viewer.getControl());
    	viewer.getControl().setMenu(menu);

    	// 4. Register the context menu with the Eclipse Workbench Site
    	// This is the crucial step that opens up the menu for external contributions
    	getSite().registerContextMenu("com.example.myplugin.menu.id", menuMgr, viewer);

    	// 5. Make the viewer the official selection provider for the site
    	getSite().setSelectionProvider(viewer);

    }
    
    @Override
    public void createPartControl(Composite parent) {
        // Create the tree UI component
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        
        // Provide workspace resource data to the tree
        viewer.setContentProvider(new BaseWorkbenchContentProvider());
        viewer.setLabelProvider(new WorkbenchLabelProvider());
        viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());

        // CRITICAL: Register the viewer context menu so Eclipse can contribute items to it
        getSite().registerContextMenu(ID, viewer, viewer);
        
        // Make this viewer the source of selections for the workbench
        getSite().setSelectionProvider(viewer);
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}
