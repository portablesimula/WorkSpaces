package com.simula.client.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.simula.client.SimulaPluginActivator;
//import com.example.simula.SimulaPlugin; // Your plugin Activator class


public class SimulaPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public SimulaPreferencePage() {
        // Use GRID layout and tie the page to your plugin's preference store
        super(GRID);
        setPreferenceStore(SimulaPluginActivator.getDefault().getPreferenceStore());
        setDescription("Configure general settings for the Simula language plugin.");
    }

    @Override
    public void init(IWorkbench workbench) {
        // Initialization code if needed
    }

    @Override
    protected void createFieldEditors() {
        // Add individual UI components tied to preference keys
        addField(new BooleanFieldEditor("ENABLE_SIMULA_LINTER", "Enable automatic code linting", getFieldEditorParent()));
        addField(new StringFieldEditor("SIMULA_COMPILER_PATH", "Path to Simula Compiler:", getFieldEditorParent()));
    }
}
