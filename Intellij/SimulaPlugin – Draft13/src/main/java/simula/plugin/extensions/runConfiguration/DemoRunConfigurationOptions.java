package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.configurations.RunConfigurationOptions;
import com.intellij.openapi.components.StoredProperty;
import simula.plugin.util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DemoRunConfigurationOptions extends RunConfigurationOptions {

    // Declare the StoredProperty for the Map.
    // The 'this.<String, String>map()' method is a helper provided by RunConfigurationOptions
    // to create a StoredProperty that serializes a Map<String, String> to XML.
    private final StoredProperty<Map<String, String>> options =
            this.<String, String>map().provideDelegate(this, "options"); // Delegate it to this object with the property name

//    /// Get a specific option value from the map.
//    /// @param option The key of the option.
//    /// @return The value, or an empty string if not found.
//    public String getOption(String option) {
//        return options.getValue(this).getOrDefault(option, "");
//    }

//    /// Set a specific option value in the map.
//    /// @param option The key of the option.
//    /// @param value The value to set.
//    public void setOption(String option, String value) {
//        // getValue(this) returns a mutable map which is then modified
//        options.getValue(this).put(option, value);
//    }

    /// Get the entire map (useful for UI binding or complex operations).
    /// @return The options map.
    public Map<String, String> getOptionsMap() {
        return options.getValue(this);
    }

    /// Set the entire map (useful for UI binding or complex operations).
    /// @param newOptions The new options map.
    public void setOptionsMap(Map<String, String> newOptions) {
        options.setValue(this, newOptions);
    }

    private final StoredProperty<String> jarFileDir = string("").provideDelegate(this, "jarFileDirName");
    public String getJarFileDirName() { return jarFileDir.getValue(this); }
    public void setJarFileDirName(String dirName) { jarFileDir.setValue(this, dirName); }

    public static void setDefaults(Map<String, String> optionMap) {
        setDefaultOption(optionMap, "simula.compiler.compilerMode", "directClassFiles");
        setDefaultOption(optionMap, "simula.compiler.caseSensitive", "false");
        setDefaultOption(optionMap, "simula.compiler.verbose", "true");
        setDefaultOption(optionMap, "simula.compiler.noExecution", "false");
        setDefaultOption(optionMap, "simula.compiler.warnings", "false");
        setDefaultOption(optionMap, "simula.compiler.noextension", "false");
        setDefaultOption(optionMap, "simula.runtime.verbose", "true");
        setDefaultOption(optionMap, "simula.runtime.noPopup", "false");
        setDefaultOption(optionMap, "simula.runtime.blockTracing", "false");
        setDefaultOption(optionMap, "simula.runtime.gotoTracing", "false");
        setDefaultOption(optionMap, "simula.runtime.qpsTracing", "false");
        setDefaultOption(optionMap, "simula.runtime.smlTracing", "false");
    }

    private static void setDefaultOption(Map<String, String> optionsMap, String id, String val){
        String prev = optionsMap.get(id);
        if(prev == null) optionsMap.put(id, val);
    }

}