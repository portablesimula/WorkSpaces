package com.simula.client;

public class DEF {
    
	public final static String SIMULA_PLUGIN_ID = "com.simula.lsp.client"; 
	public final static String SIMULA_NATURE_ID = "com.simula.client.simulanature";
	public final static String SIMULA_PERSPECTIVE_ID = "com.simula.client.ui.SimulaPerspective"; 
	
    public final static String FAV_ICON_PATH = "icons/facico.png"; 
    public final static String SIM_ICON_PATH = "icons/sim.png"; 
    public final static String GENERIC_EDITOR_ID = "org.eclipse.ui.genericeditor.GenericEditor";

    public final static String P_BOOLEAN_SETTING = "booleanPreferenceSetting";
    public final static String P_INTEGER_SETTING = "integerPreferenceSetting";
    public final static String P_STRING_SETTING = "stringPreferenceSetting";

	
    // Main Preferences
    public static final String P_VERBOSE = "verbose";
    public static final String P_CASE_SENSITIVE = "caseSensitive";
    public static final String P_LANUAGE_EXTENSIONS = "extension";
    public static final String P_WARNINGS = "warnings";
    
    // The key used to store the user's choice
    public static final String SIMULA_COMPILER_MODE = "simulaCompilerMode";
    
    // The two string values the user can choose from
    public static final String CHOICE_DIRECT_CLASS_FILES = "directClassFiles";
    public static final String CHOICE_VIA_JAVA_SOURCES = "viaJavaSource";
    
    // The key used to store the user's choice
    public static final String SIMULA_TRACE_MODE = "simulaCompilerMode";
    
    // The two string values the user can choose from
    public static final String CHOICE_TRACE_OFF = "traceOff";
    public static final String CHOICE_MESSAGE = "traceMessage";
    public static final String CHOICE_VERBOSE = "traceVerbose";

    public static final String SIMULA_KEEP_JAVA_PATH = "simulaKeepJavaPath";

    
    // Detailed Preferences
	public static final String  P_TRACING = "-tracing";
	public static final String  P_DEBUGGING = "-debugging";		// Set by EditorMenues - doDebugAction

	// Lexer Trace Options
	public static final String  P_TRACE_LEXER = "-trace-lexer";
	public static final String  P_TRACE_NEW_LEXTOKEN = "-trace-new-lextoken";

	// Parser Trace Options
	public static final String  P_TRACE_PARSE = "-trace-Parse";
	public static final String  P_PRINT_SYNTAX_TREE = "-print-syntax-tree";
	public static final String  P_TRACE_ATTRIBUTE_OUTPUT = "-trace-attribute-output";
	public static final String  P_TRACE_ATTRIBUTE_INPUT = "-trace-attribute-input";

	// Checker Trace Options
	public static final String  P_TRACE_CHECKER = "-trace-checker";
	public static final String  P_TRACE_CHECKER_OUTPUT = "-trace-checker-output";
	public static final String  P_TRACE_FIND_MEANING = "-trace-find-meaning";
	
	// Semantic Token Trace Options
	public static final String  P_TRACE_NEW_SEMTOKEN = "-trace-new-semtoken";
	public static final String  P_TRACE_VERIFY_TOKEN = "-trace-verify-token";
	public static final String  P_LEX_VERIFY = "-lexer-verifyer";
	public static final String  P_CHG_VERIFY = "-change-verifyer";
	

	// Java Coder Options
	public static final String  P_TRACE_CODING = "-trace-coding";         // Only when .java output
	public static final String  P_GNERATE_LINE_CALLS = "-generate-line-calls";   // Only when .java output

	// Byte code engineering Options
	public static final String  P_TRACE_BYTECODE_OUTPUT = "-trace-bytecode-output";
	public static final String  P_LIST_REPAIRED_INSTRUCTION_LIST = "-trace-repaired-instruction-list";
	public static final String  P_TRACE_REPAIRING = "-trace-repairing";
	public static final String  P_LIST_INPUT_INSTRUCTION_LIST = "-list-input-instructuion-list";
	public static final String  P_TRACE_REPAIRING_INPUT = "-trace-repairing-input";
	public static final String  P_TRACE_REPAIRING_OUTPUT = "-trace-repairing-output";

	
	/** Runtime Options */ public static final String  P_RTS_VERBOSE = "-RTS-verbose";
	/** Runtime Options */ public static final String  P_RTS_NOPOPUP = "-RTS-noPopup";
	/** Runtime Options */ public static final String  P_RTS_BLOCK_TRACING = "-RTS-block-tracing";
	/** Runtime Options */ public static final String  P_RTS_GOTO_TRACING = "-RTS-goto-tracing";
	/** Runtime Options */ public static final String  P_RTS_QPS_TRACING = "-RTS-qps-tracing";
	/** Runtime Options */ public static final String  P_RTS_SML_TRACING = "-RTS-sml-tracing";

//	/** Runtime Options */ public static final String SOURCE_FILE = "" = "-RTS-source-file";
	/** Runtime Options */ public static final String P_RTS_RUNTIME_USER_DIR = "-RTS-user-dir";

}
