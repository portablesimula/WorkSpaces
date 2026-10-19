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

	
    // LSP Preferences
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

}
