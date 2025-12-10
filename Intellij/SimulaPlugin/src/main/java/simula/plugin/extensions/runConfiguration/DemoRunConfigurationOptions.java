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

}