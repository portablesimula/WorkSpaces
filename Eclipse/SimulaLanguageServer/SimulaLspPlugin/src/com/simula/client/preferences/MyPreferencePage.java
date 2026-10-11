package com.simula.client.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.simula.client.DEF;

public class MyPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public MyPreferencePage() {
        super(GRID);
        // Link this visual UI directly to your plugin's store
//        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setPreferenceStore(PlatformUI.getPreferenceStore());
        setDescription("Configure settings for My Plugin:");
    }

    @Override
    protected void createFieldEditors() {
        // Field editors automatically load, display, and save values back to the store
        addField(new BooleanFieldEditor(DEF.P_BOOLEAN_SETTING, 
                "&Enable experimental features", getFieldEditorParent()));
 
        addField(new BooleanFieldEditor(DEF.P_INTEGER_SETTING, 
                "&Tracing level", getFieldEditorParent()));
 
        addField(new StringFieldEditor(DEF.P_STRING_SETTING, 
                "&User License Key:", getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench workbench) {}
}
