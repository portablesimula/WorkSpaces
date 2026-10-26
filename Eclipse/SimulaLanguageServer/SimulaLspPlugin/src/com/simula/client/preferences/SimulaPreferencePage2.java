package com.simula.client.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;

import java.util.List;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.simula.client.DEF;
import com.simula.client.SimulaPluginActivator;


/// extension point="org.eclipse.ui.preferencePages"
public class SimulaPreferencePage2 extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	   // Define a constant for the preference key
    public static final String P_VERBOSE = "verbosePreference";
    
    public SimulaPreferencePage2() {
        // Use GRID layout and tie the page to your plugin's preference store
        super(GRID);
        setPreferenceStore(SimulaPluginActivator.getDefault().getPreferenceStore());
        setDescription("Configure Options for the Simula language server.");
    }

    @Override
    public void init(IWorkbench workbench) {
        // Initialization code if needed
    }

    
    public static void addCommands(List<String> commands) {
    	IPreferenceStore preferenceStore = SimulaPluginActivator.getDefault().getPreferenceStore();
    	if(preferenceStore.getBoolean(DEF.P_TRACING))                        commands.add(DEF.P_TRACING);
    	if(preferenceStore.getBoolean(DEF.P_DEBUGGING))                     commands.add(DEF.P_DEBUGGING);  // Overall: Enable debugging", overallGroup));

    	// Lexer Trace Options
    	if(preferenceStore.getBoolean(DEF.P_TRACE_LEXER))                   commands.add(DEF.P_TRACE_LEXER);  // Trace Lexer", lexerGroup));
    	if(preferenceStore.getBoolean(DEF.P_TRACE_NEW_LEXTOKEN))            commands.add(DEF.P_TRACE_NEW_LEXTOKEN);  // Trace new lextoken", lexerGroup));

    	// Parser Trace Options
    	if(preferenceStore.getBoolean(DEF.P_TRACE_PARSE))                   commands.add(DEF.P_TRACE_PARSE);  // Trace Parse", parserGroup));
    	if(preferenceStore.getBoolean(DEF.P_PRINT_SYNTAX_TREE))             commands.add(DEF.P_PRINT_SYNTAX_TREE);  // Print Syntax tree", parserGroup));
    	if(preferenceStore.getBoolean(DEF.P_TRACE_ATTRIBUTE_OUTPUT))        commands.add(DEF.P_TRACE_ATTRIBUTE_OUTPUT);  // Trace Attribute output", parserGroup));
    	if(preferenceStore.getBoolean(DEF.P_TRACE_ATTRIBUTE_INPUT))         commands.add(DEF.P_TRACE_ATTRIBUTE_INPUT);  // Trace Attribute input", parserGroup));

    	// Java Coder Options
    	if(preferenceStore.getBoolean(DEF.P_TRACE_CODING))                  commands.add(DEF.P_TRACE_CODING);  // Trace coding", coderGroup));         // Only when .java output
    	if(preferenceStore.getBoolean(DEF.P_GNERATE_LINE_CALLS))            commands.add(DEF.P_GNERATE_LINE_CALLS);  // Generate line calls", coderGroup));   // Only when .java output

    	// Checker Trace Options
    	if(preferenceStore.getBoolean(DEF.P_TRACE_CHECKER))                  commands.add(DEF.P_TRACE_CHECKER);  // Trace Checker", checkerGroup));
    	if(preferenceStore.getBoolean(DEF.P_TRACE_CHECKER_OUTPUT))           commands.add(DEF.P_TRACE_CHECKER_OUTPUT);  // Trace Checker output", checkerGroup));
    	if(preferenceStore.getBoolean(DEF.P_TRACE_FIND_MEANING))             commands.add(DEF.P_TRACE_FIND_MEANING);  // Trace find Meaning", checkerGroup));

    	// Semantic Token Trace Options
    	if(preferenceStore.getBoolean(DEF.P_TRACE_NEW_SEMTOKEN))             commands.add(DEF.P_TRACE_NEW_SEMTOKEN);  // Trace new semtoken", checkerGroup));
    	if(preferenceStore.getBoolean(DEF.P_TRACE_VERIFY_TOKEN))             commands.add(DEF.P_TRACE_VERIFY_TOKEN);  // Trace verify token", checkerGroup));
    	if(preferenceStore.getBoolean(DEF.P_LEX_VERIFY))                     commands.add(DEF.P_LEX_VERIFY);  // Enable lexer Verifyer", checkerGroup));
    	if(preferenceStore.getBoolean(DEF.P_CHG_VERIFY))                     commands.add(DEF.P_CHG_VERIFY);  // Enable change Verifyer", checkerGroup));


    	// Byte code engineering Options
    	if(preferenceStore.getBoolean(DEF.P_TRACE_BYTECODE_OUTPUT))          commands.add(DEF.P_TRACE_BYTECODE_OUTPUT);  // Trace bytecode output", engineGroup));
    	if(preferenceStore.getBoolean(DEF.P_LIST_REPAIRED_INSTRUCTION_LIST)) commands.add(DEF.P_LIST_REPAIRED_INSTRUCTION_LIST);  // Trace repaired instruction list", engineGroup));
    	if(preferenceStore.getBoolean(DEF.P_TRACE_REPAIRING))                commands.add(DEF.P_TRACE_REPAIRING);  // Trace repairing", engineGroup));
    	if(preferenceStore.getBoolean(DEF.P_LIST_INPUT_INSTRUCTION_LIST))    commands.add(DEF.P_LIST_INPUT_INSTRUCTION_LIST);  // List input instructuion list", engineGroup));
    	if(preferenceStore.getBoolean(DEF.P_TRACE_REPAIRING_INPUT))          commands.add(DEF.P_TRACE_REPAIRING_INPUT);  // Trace repairing input", engineGroup));
    	if(preferenceStore.getBoolean(DEF.P_TRACE_REPAIRING_OUTPUT))         commands.add(DEF.P_TRACE_REPAIRING_OUTPUT);  // Trace repairing output", engineGroup));
    }
    
    @Override
    protected void createFieldEditors() {
    	Composite parent = getFieldEditorParent();

    	// Add individual UI components tied to preference keys
//    	addField(new BooleanFieldEditor("ENABLE_SIMULA_LINTER", "Enable automatic code linting", parent));
    	addField(new StringFieldEditor("SIMULA_COMPILER_PATH", "Path to Simula Compiler:", parent));

    	// Field editors automatically load, display, and save values back to the store
//    	addField(new BooleanFieldEditor(DEF.P_BOOLEAN_SETTING, "&Enable experimental features", parent));
    	addField(new IntegerFieldEditor(DEF.P_INTEGER_SETTING, "&Tracing level", parent));
    	addField(new StringFieldEditor(DEF.P_STRING_SETTING, "&User License Key:", parent));

//    	addField(new BooleanFieldEditor(DEF.P_VERBOSE, "&Produce verbose tracing", parent));
//    	addField(new BooleanFieldEditor(DEF.P_CASE_SENSITIVE, "&Case sensitive source files", parent));
//    	addField(new BooleanFieldEditor(DEF.P_LANUAGE_EXTENSIONS, "&Add Simula language extensions", parent));
//    	addField(new BooleanFieldEditor(DEF.P_WARNINGS, "&Show warning messages", parent));
    	
    	
        // Detailed Preferences
        Group overallGroup = createGroup(parent, "Overall Settings");
//    	addField(new BooleanFieldEditor(DEF.P_TRACING, "&tracing", parent));
//    	addField(new BooleanFieldEditor(DEF.P_DEBUGGING, "&debugging", parent));		// Set by EditorMenues - doDebugAction
    	addField(new BooleanFieldEditor(DEF.P_TRACING, "Overall: Enable tracing", overallGroup));
    	addField(new BooleanFieldEditor(DEF.P_DEBUGGING, "Overall: Enable debugging", overallGroup));
        
    	// Lexer Trace Options
        Group lexerGroup = createGroup(parent, "Lexer Settings");
    	addField(new BooleanFieldEditor(DEF.P_TRACE_LEXER, "Trace Lexer", lexerGroup));
    	addField(new BooleanFieldEditor(DEF.P_TRACE_NEW_LEXTOKEN, "Trace new lextoken", lexerGroup));

    	// Parser Trace Options
        Group parserGroup = createGroup(parent, "Parser Settings");
    	addField(new BooleanFieldEditor(DEF.P_TRACE_PARSE, "Trace Parse", parserGroup));
    	addField(new BooleanFieldEditor(DEF.P_PRINT_SYNTAX_TREE, "Print Syntax tree", parserGroup));
    	addField(new BooleanFieldEditor(DEF.P_TRACE_ATTRIBUTE_OUTPUT, "Trace Attribute output", parserGroup));
    	addField(new BooleanFieldEditor(DEF.P_TRACE_ATTRIBUTE_INPUT, "Trace Attribute input", parserGroup));

    	// Java Coder Options
        Group coderGroup = createGroup(parent, "Coder Settings");
    	addField(new BooleanFieldEditor(DEF.P_TRACE_CODING, "Trace coding", coderGroup));         // Only when .java output
    	addField(new BooleanFieldEditor(DEF.P_GNERATE_LINE_CALLS, "Generate line calls", coderGroup));   // Only when .java output

    	// Checker Trace Options
        Group checkerGroup = createGroup(parent, "Checker Settings");
    	addField(new BooleanFieldEditor(DEF.P_TRACE_CHECKER, "Trace Checker", checkerGroup));
    	addField(new BooleanFieldEditor(DEF.P_TRACE_CHECKER_OUTPUT, "Trace Checker output", checkerGroup));
    	addField(new BooleanFieldEditor(DEF.P_TRACE_FIND_MEANING, "Trace find Meaning", checkerGroup));
    	
    	// Semantic Token Trace Options
    	addField(new BooleanFieldEditor(DEF.P_TRACE_NEW_SEMTOKEN, "Trace new semtoken", checkerGroup));
    	addField(new BooleanFieldEditor(DEF.P_TRACE_VERIFY_TOKEN, "Trace verify token", checkerGroup));
    	addField(new BooleanFieldEditor(DEF.P_LEX_VERIFY, "Enable lexer Verifyer", checkerGroup));
    	addField(new BooleanFieldEditor(DEF.P_CHG_VERIFY, "Enable change Verifyer", checkerGroup));
    	

    	// Byte code engineering Options
        Group engineGroup = createGroup(parent, "Byte code engineering");
    	addField(new BooleanFieldEditor(DEF.P_TRACE_BYTECODE_OUTPUT, "Trace bytecode output", engineGroup));
    	addField(new BooleanFieldEditor(DEF.P_LIST_REPAIRED_INSTRUCTION_LIST, "Trace repaired instruction list", engineGroup));
    	addField(new BooleanFieldEditor(DEF.P_TRACE_REPAIRING, "Trace repairing", engineGroup));
    	addField(new BooleanFieldEditor(DEF.P_LIST_INPUT_INSTRUCTION_LIST, "List input instructuion list", engineGroup));
    	addField(new BooleanFieldEditor(DEF.P_TRACE_REPAIRING_INPUT, "Trace repairing input", engineGroup));
    	addField(new BooleanFieldEditor(DEF.P_TRACE_REPAIRING_OUTPUT, "Trace repairing output", engineGroup));

    	
    }

    /**
     * Helper method to create a titled visual group.
     */
    private Group createGroup(Composite parent, String title) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(title);
        
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 1;
        group.setLayoutData(gd);
        
        return group;
    }

}
