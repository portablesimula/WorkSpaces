package com.simula.client.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.simula.client.DEF;
import com.simula.client.SimulaPluginActivator;


/// extension point="org.eclipse.ui.preferencePages"
public class SimulaLspPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public SimulaLspPreferencePage() {
        // Use GRID layout and tie the page to your plugin's preference store
        super(GRID);
        setPreferenceStore(SimulaPluginActivator.getDefault().getPreferenceStore());
        setDescription("Configure LSP settings for the Simula language plugin.");
    }

    @Override
    public void init(IWorkbench workbench) {
        // Initialization code if needed
    }

    @Override
    protected void createFieldEditors() {
        Composite parent = getFieldEditorParent();

        // Add individual UI components tied to preference keys
        addField(new BooleanFieldEditor("ENABLE_SIMULA_LINTER", "Enable automatic code linting", parent));
        addField(new StringFieldEditor("SIMULA_COMPILER_PATH", "Path to Simula Compiler:", parent));
        
      // Field editors automatically load, display, and save values back to the store
      addField(new BooleanFieldEditor(DEF.P_BOOLEAN_SETTING, "&Enable experimental features", parent));
      addField(new IntegerFieldEditor(DEF.P_INTEGER_SETTING, "&Tracing level", parent));
      addField(new StringFieldEditor(DEF.P_STRING_SETTING, "&User License Key:", parent));
      
     
      // Create the numeric field editor
      IntegerFieldEditor numericEditor = new IntegerFieldEditor(
          DEF.P_INTEGER_SETTING,        // Preference key
          "Enter a &number:",        // Label text (with mnemonic shortcut)
          parent                     // Parent composite
      );

      // Optional: Enforce bounds validation (e.g., must be between 1 and 100)
      numericEditor.setValidRange(1, 100);
      numericEditor.setErrorMessage("The value must be an integer between 1 and 100.");

      // Add the field to the page
      addField(numericEditor);

    }

}
