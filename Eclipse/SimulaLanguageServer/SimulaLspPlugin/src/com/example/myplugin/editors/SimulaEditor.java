package com.example.myplugin.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class SimulaEditor extends EditorPart {

    private boolean isDirty = false;

    public SimulaEditor() {
        super();
    }

    /**
     * Initializes the editor with the site and the file input.
     */
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        setPartName(input.getName()); // Sets the tab title to the file name
    }

    /**
     * Creates the SWT widgets that make up your editor's visual interface.
     */
    @Override
    public void createPartControl(Composite parent) {
        // Example: Adding a simple label inside the editor
        Label label = new Label(parent, SWT.NONE);
        label.setText("Welcome to My Custom Editor UI!");
        
        // TODO: Build your actual UI layout here (text areas, forms, canvas, etc.)
    }

    /**
     * Passes focus to your primary interactive control when the editor tab is selected.
     */
    @Override
    public void setFocus() {
        // parentControl.setFocus();
    }

    /**
     * Indicates whether the file has changed and needs saving.
     */
    @Override
    public boolean isDirty() {
        return isDirty;
    }

    protected void setDirty(boolean dirty) {
        if (this.isDirty != dirty) {
            this.isDirty = dirty;
            firePropertyChange(PROP_DIRTY); // Notifies Eclipse to put an asterisk (*) on the tab
        }
    }

    /**
     * Triggered when the user hits Save (Ctrl+S).
     */
    @Override
    public void doSave(IProgressMonitor monitor) {
        // TODO: Perform file-saving logic using getEditorInput()
        setDirty(false);
    }

    @Override
    public void doSaveAs() {
        // Optional: Implement Save As functionality
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false; 
    }
}
