package com.simula.client.ui;

import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.simula.client.DEF;

public class SimulaLabelDecorator implements ILabelDecorator {

    // Path matching your icon location
    private final Image simulaIcon;

    public SimulaLabelDecorator() {
        // Load your icon image safely using your Plugin's Activator/Bundle context
//        this.simulaIcon = AbstractUIPlugin.imageDescriptorFromPlugin(
//            "com.example.simula.plugin.id", DEF.FAV_ICON_PATH).createImage();
    	
//        this.simulaIcon = AbstractUIPlugin.imageDescriptorFromPlugin(DEF.SIMULA_PLUGIN_ID, DEF.FAV_ICON_PATH).createImage();

    	// 1. Grab your OSGi Bundle 
    	// Option A: If inside a class in the same plugin
    	Bundle bundle = FrameworkUtil.getBundle(getClass()); 
    	// Option B: Hardcoded using your plug-in ID
    	// Bundle bundle = Platform.getBundle("your.plugin.symbolic.id");

    	// 2. Locate the file inside the bundle
//    	String path = "icons/my_icon.png";
//    	URL url = FileLocator.find(bundle, new Path(DEF.FAV_ICON_PATH), null);
    	URL url = FileLocator.find(bundle, new Path(DEF.SIM_ICON_PATH), null);

    	// 3. Convert URL to ImageDescriptor and instantiate the Image
    	ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
//    	simulaIcon = imageDesc.createImage();
    	Image original = imageDesc.createImage();
//        simulaIcon = IconManager.resizeImage(original, 16, 16);
        simulaIcon = IconManager.resizeImage(original, 12, 12);
    }

    @Override
    public Image decorateImage(Image image, Object element) {
        if (element instanceof IFile) {
            IFile file = (IFile) element;
            // Check if the file extension belongs to Simula
            if ("sim".equalsIgnoreCase(file.getFileExtension())) {
                return simulaIcon;
            }
        }
        return null; // Return null to fall back to the default Eclipse file icon
    }

    @Override
    public String decorateText(String text, Object element) {
        return null; // Keep text labels unchanged
    }

    @Override
    public void dispose() {
        if (simulaIcon != null && !simulaIcon.isDisposed()) {
            simulaIcon.dispose();
        }
    }

    // Remaining standard boilerplate listener methods
    @Override public void addListener(ILabelProviderListener listener) {}
    @Override public void removeListener(ILabelProviderListener listener) {}
    @Override public boolean isLabelProperty(Object element, String property) { return false; }
}
