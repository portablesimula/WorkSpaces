package com.example.myplugin.navigator;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class MapperLabelProvider extends LabelProvider {
    
    @Override
    public String getText(Object element) {
        if (element instanceof String) {
            return (String) element;
        }
        return super.getText(element);
    }

    @Override
    public Image getImage(Object element) {
        // Return your custom icon here
        return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
    }
}
