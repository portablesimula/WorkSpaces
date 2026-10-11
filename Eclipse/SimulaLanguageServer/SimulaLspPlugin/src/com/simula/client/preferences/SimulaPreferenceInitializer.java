package com.simula.client.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

import com.simula.client.DEF;

/// <extension point="org.eclipse.core.runtime.preferences">
///     <initializer class="com.simula.client.preferences.SimulaPreferenceInitializer"/>
/// </extension>
public class SimulaPreferenceInitializer extends AbstractPreferenceInitializer {
    @Override
    public void initializeDefaultPreferences() {
        // Obtain your plugin's preference store instance
//        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    	IPreferenceStore store = PlatformUI.getPreferenceStore();
        
        // Define default settings
        store.setDefault(DEF.P_BOOLEAN_SETTING, true);
        store.setDefault(DEF.P_INTEGER_SETTING, 0);
        store.setDefault(DEF.P_STRING_SETTING, "Default Value");
    }
}
