/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec;

/// Options.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/Option.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class Option {

	/** Default Constructor */ public Option() {} 

	/** BEC Option */ public static boolean INLINE_TESTING = false;
	/** BEC Option */ public static boolean debugMode = false;
	/** BEC Option */ public static boolean verbose = false;
	/** BEC Option */ public static boolean execVerbose = false;
	/** BEC Option */ public static boolean nopopup = false;
	/** BEC Option */ public static int traceMode = 0;
	/** BEC Option */ public static boolean SCODE_INPUT_TRACE = false;
	/** BEC Option */ public static boolean TRACE_ALLOC_FRAME = false;
	/** BEC Option */ public static boolean PRINT_GENERATED_SVM_DATA = false;
	/** BEC Option */ public static boolean PRINT_GENERATED_SVM_CODE = false;
	/** BEC Option */ public static boolean ATTR_INPUT_TRACE = false;
	/** BEC Option */ public static boolean ATTR_OUTPUT_TRACE = false;
	/** BEC Option */ public static boolean ATTR_INPUT_DUMP = false;
	/** BEC Option */ public static boolean ATTR_OUTPUT_DUMP = false;
	/** BEC Option */ public static boolean SEGMENT_INPUT_DUMP = false;
	/** BEC Option */ public static boolean SEGMENT_OUTPUT_DUMP = false;
	/** BEC Option */ public static int EXEC_TRACE = 0;
	/** BEC Option */ public static int CALL_TRACE_LEVEL = 0;
	/** BEC Option */ public static boolean DUMPS_AT_EXIT = false;

}
