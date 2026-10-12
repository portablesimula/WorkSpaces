package com.simula.client.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

import com.simula.client.DEF;

/// extension point="org.eclipse.core.runtime.preferences"
public class SimulaPreferenceInitializer extends AbstractPreferenceInitializer {
    @Override
    public void initializeDefaultPreferences() {
        // Obtain your plugin's preference store instance
    	IPreferenceStore store = PlatformUI.getPreferenceStore();
        
        // Define default settings
        store.setDefault(DEF.P_BOOLEAN_SETTING, false);
        store.setDefault(DEF.P_INTEGER_SETTING, 0);
        store.setDefault(DEF.P_STRING_SETTING, "Default Value");
    }
}
