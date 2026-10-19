package com.simula.client.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import com.simula.client.DEF;
import com.simula.client.SimulaPluginActivator;

/// extension point="org.eclipse.core.runtime.preferences"
public class SimulaPreferenceInitializer extends AbstractPreferenceInitializer {
    @Override
    public void initializeDefaultPreferences() {
        // Obtain your plugin's preference store instance
//    	IPreferenceStore store = PlatformUI.getPreferenceStore();
        IPreferenceStore store = SimulaPluginActivator.getDefault().getPreferenceStore();
       
        // Define default settings
        store.setDefault(DEF.P_BOOLEAN_SETTING, false);
        store.setDefault(DEF.P_INTEGER_SETTING, 0);
        store.setDefault(DEF.P_STRING_SETTING, "Default Value");
        
        store.setDefault(DEF.P_VERBOSE, true);
        store.setDefault(DEF.P_CASE_SENSITIVE, false);
        store.setDefault(DEF.P_LANUAGE_EXTENSIONS, true);
        store.setDefault(DEF.P_WARNINGS, true);

        // Set Choice One as the default string choice
        store.setDefault(DEF.SIMULA_COMPILER_MODE, 
                         DEF.CHOICE_DIRECT_CLASS_FILES);

        // Set the fallback/default string path here
//        store.setDefault(DEF.SIMULA_KEEP_JAVA_PATH, "/usr/bin/simlah");

    }
}
