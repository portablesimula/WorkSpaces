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

        // Detailed Preferences
    	store.setDefault(DEF.P_TRACING, false); // "tracing";
    	store.setDefault(DEF.P_DEBUGGING, false); // "debugging";		// Set by EditorMenues - doDebugAction

    	// Lexer Trace Options
    	store.setDefault(DEF.P_TRACE_LEXER, false); // "trace-lexer";
    	store.setDefault(DEF.P_TRACE_NEW_LEXTOKEN, false); // "trace-new-lextoken";

    	// Parser Trace Options
    	store.setDefault(DEF.P_TRACE_PARSE, false); // "trace-Parse";
    	store.setDefault(DEF.P_PRINT_SYNTAX_TREE, false); // "print-syntax-tree";
    	store.setDefault(DEF.P_TRACE_ATTRIBUTE_OUTPUT, false); // "trace-attribute-output";
    	store.setDefault(DEF.P_TRACE_ATTRIBUTE_INPUT, false); // "trace-attribute-input";

    	// Checker Trace Options
    	store.setDefault(DEF.P_TRACE_CHECKER, false); // "trace-checker";
    	store.setDefault(DEF.P_TRACE_CHECKER_OUTPUT, false); // "trace-checker-output";
    	store.setDefault(DEF.P_TRACE_FIND_MEANING, false); // "trace-find-meaning";
    	
    	// Semantic Token Trace Options
    	store.setDefault(DEF.P_TRACE_NEW_SEMTOKEN, false); // "trace-new-semtoken";
    	store.setDefault(DEF.P_TRACE_VERIFY_TOKEN, false); // "trace-verify-token";
    	store.setDefault(DEF.P_LEX_VERIFY, true); // "lexer-verifyer";
    	store.setDefault(DEF.P_CHG_VERIFY, true); // "change-verifyer";
    	

    	// Java Coder Options
    	store.setDefault(DEF.P_TRACE_CODING, false); // "trace-coding";         // Only when .java output
    	store.setDefault(DEF.P_GNERATE_LINE_CALLS, false); // "generate-line-calls";   // Only when .java output

    	// Byte code engineering Options
    	store.setDefault(DEF.P_TRACE_BYTECODE_OUTPUT, false); // "trace-bytecode-output";
    	store.setDefault(DEF.P_LIST_REPAIRED_INSTRUCTION_LIST, false); // "trace-repaired-instruction-list";
    	store.setDefault(DEF.P_TRACE_REPAIRING, false); // "trace-repairing";
    	store.setDefault(DEF.P_LIST_INPUT_INSTRUCTION_LIST, false); // "list-input-instructuion-list";
    	store.setDefault(DEF.P_TRACE_REPAIRING_INPUT, false); // "trace-repairing-input";
    	store.setDefault(DEF.P_TRACE_REPAIRING_OUTPUT, false); // "trace-repairing-output";

    }
}
