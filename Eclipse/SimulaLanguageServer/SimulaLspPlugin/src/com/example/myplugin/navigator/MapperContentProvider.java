package com.example.myplugin.navigator;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.core.resources.IFile;

public class MapperContentProvider implements ITreeContentProvider {
    
    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof IFile) {
            IFile file = (IFile) parentElement;
            if ("mapper".equals(file.getFileExtension())) {
                // Parse your file here and return its structural nodes
                return new Object[] { "Mapping 1", "Mapping 2" }; 
            }
        }
        return new Object[0];
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        return getChildren(element).length > 0;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }
}
