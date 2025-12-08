package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.configurations.RunConfigurationOptions;
import com.intellij.openapi.components.StoredProperty;
import simula.plugin.util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DemoRunConfigurationOptions extends RunConfigurationOptions {
//    // Defines a stored string property with a default value
//    private val scriptFilePath: StoredProperty<String> = string("").provideDelegate(this, "scriptFilePath")
//
//    // Defines a stored map property for dynamic entries
//    private val dynamicOptions: StoredProperty<Map<String, String>> = map<String, String>().provideDelegate(this, "options")
//
//    // Getter and setter for scriptFilePath
//    fun getScriptFilePath(): String = scriptFilePath.getValue(this)
//    fun setScriptFilePath(path: String) = scriptFilePath.setValue(this, path)
//
//    // Getter and setter for dynamicOptions
//    fun getDynamicOptions(): Map<String, String> = dynamicOptions.getValue(this)
//    fun setDynamicOptions(options: Map<String, String>) = dynamicOptions.setValue(this, options)

    // Declare the StoredProperty for the Map.
    // The 'this.<String, String>map()' method is a helper provided by RunConfigurationOptions
    // to create a StoredProperty that serializes a Map<String, String> to XML.
    private final StoredProperty<Map<String, String>> options =
            this.<String, String>map() // "options" is the name of the XML tag/attribute
                    .provideDelegate(this, "options"); // Delegate it to this object with the property name

    /// Get a specific option value from the map.
    /// @param option The key of the option.
    /// @return The value, or an empty string if not found.
    public String getOption(String option) {
        return options.getValue(this).getOrDefault(option, "");
    }

    /// Set a specific option value in the map.
    /// @param option The key of the option.
    /// @param value The value to set.
    public void setOption(String option, String value) {
        // getValue(this) returns a mutable map which is then modified
        options.getValue(this).put(option, value);
    }

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

    private final StoredProperty<String> myScriptName =
            string("").provideDelegate(this, "scriptName");

    public String getScriptName() {
        return myScriptName.getValue(this);
    }

    public void setScriptName(String scriptName) {
        myScriptName.setValue(this, scriptName);
    }

    ///  =================================================================================

    public void setDefaults() {
        setOption("simula.compiler.compilerMode", "directClassFiles");
        setOption("simula.compiler.caseSensitive", "false");
        setOption("simula.compiler.verbose", "true");
        setOption("simula.compiler.noExecution", "false");
        setOption("simula.compiler.warnings", "false");
        setOption("simula.compiler.extensions", "true");
        setOption("simula.runtime.verbose", "true");
        setOption("simula.runtime.blockTracing", "false");
        setOption("simula.runtime.gotoTracing", "false");
        setOption("simula.runtime.qpsTracing", "false");
        setOption("simula.runtime.smlTracing", "false");

    }


}