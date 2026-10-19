package com.simula.client.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.simula.client.DEF;
import com.simula.client.SimulaPluginActivator;


/// extension point="org.eclipse.ui.preferencePages"
public class SimulaDetailsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	   // Define a constant for the preference key
    public static final String P_VERBOSE = "verbosePreference";
    
    public SimulaDetailsPreferencePage() {
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

//    	addField(new BooleanFieldEditor(DEF.P_VERBOSE, "&Produce verbose tracing", parent));
//    	addField(new BooleanFieldEditor(DEF.P_CASE_SENSITIVE, "&Case sensitive source files", parent));
//    	addField(new BooleanFieldEditor(DEF.P_LANUAGE_EXTENSIONS, "&Add Simula language extensions", parent));
//    	addField(new BooleanFieldEditor(DEF.P_WARNINGS, "&Show warning messages", parent));
    	
    	

//        // Add the checkbox field editor
//        addField(new BooleanFieldEditor(
//            P_VERBOSE,
//            "&Enable verbose output",
//            getFieldEditorParent()
//        ));
//        
//    	// Create the numeric field editor
//    	IntegerFieldEditor numericEditor = new IntegerFieldEditor(
//    			DEF.P_INTEGER_SETTING,        // Preference key
//    			"Enter a &number:",        // Label text (with mnemonic shortcut)
//    			parent                     // Parent composite
//    			);
//
//    	// Optional: Enforce bounds validation (e.g., must be between 1 and 100)
//    	numericEditor.setValidRange(1, 100);
//    	numericEditor.setErrorMessage("The value must be an integer between 1 and 100.");
//
//    	// Add the field to the page
//    	addField(numericEditor);
//
//        // Defines the labels and string data mappings for the 2 choices
//        String[][] labelAndValues = new String[][] {
//            { "Direct class files", DEF.CHOICE_DIRECT_CLASS_FILES },
//            { "Simula Beta Option", DEF.CHOICE_VIA_JAVA_SOURCES }
//        };
//
//        // Add the chooser editor to the page
//        addField(new RadioGroupFieldEditor(
//            DEF.SIMULA_CHOOSER_KEY,
//            "Choose a Simula Backend Compiler Mode:",
//            1, // Number of columns to display the radio elements
//            labelAndValues,
//            getFieldEditorParent(),
//            true // Use a group box enclosure frame
//        ));

    	
    }

}
