package com.simula.client.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.simula.client.DEF;
import com.simula.client.SimulaPluginActivator;


/// extension point="org.eclipse.ui.preferencePages"
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
      // Field editors automatically load, display, and save values back to the store
      addField(new BooleanFieldEditor(DEF.P_BOOLEAN_SETTING, 
              "&Enable experimental features", getFieldEditorParent()));

      addField(new BooleanFieldEditor(DEF.P_INTEGER_SETTING, 
              "&Tracing level", getFieldEditorParent()));

      addField(new StringFieldEditor(DEF.P_STRING_SETTING, 
              "&User License Key:", getFieldEditorParent()));
    }
    
//    public class MyPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
//
//        public MyPreferencePage() {
//            super(GRID);
//            // Link this visual UI directly to your plugin's store
////            setPreferenceStore(Activator.getDefault().getPreferenceStore());
//            setPreferenceStore(PlatformUI.getPreferenceStore());
//            setDescription("Configure settings for My Plugin:");
//        }
//
//        @Override
//        protected void createFieldEditors() {
//            // Field editors automatically load, display, and save values back to the store
//            addField(new BooleanFieldEditor(DEF.P_BOOLEAN_SETTING, 
//                    "&Enable experimental features", getFieldEditorParent()));
//     
//            addField(new BooleanFieldEditor(DEF.P_INTEGER_SETTING, 
//                    "&Tracing level", getFieldEditorParent()));
//     
//            addField(new StringFieldEditor(DEF.P_STRING_SETTING, 
//                    "&User License Key:", getFieldEditorParent()));
//        }
//
//        @Override
//        public void init(IWorkbench workbench) {}
//    }

}
