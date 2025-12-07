/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.plugin.util;

import java.io.File;

/// Compile Time Options.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/EclipseWorkSpaces/blob/main/SimulaCompiler2/Simula/src/simula/compiler/utilities/Option.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class CTOption {

	/// The Compiler Modes.
	public enum CompilerMode { 
    	/** Generate Java source and use Java compiler to generate JavaClass files. */					viaJavaSource,
    	/** Generate JavaClass files directly. No Java source files are generated. */ 					directClassFiles,
    	/** Generate ClassFile byte array and load it directly. No intermediate files are created. */	simulaClassLoader
    }

	/// The Compiler mode.
	public static CompilerMode compilerMode;
	
	/// Source file is case sensitive.
	public static boolean CaseSensitive=false;
	
	/// Output messages about what the compiler is doing.
	public static boolean verbose = false; 
	
	/// Generate warning messages
	public static boolean WARNINGS=true;

	/// TRUE:Do not create popUps at runtime
	public static boolean noPopup = false; 
	
	/// true: Don't execute generated .jar file
	public static boolean noExecution = false;
	
	/// false: Disable all language extensions. In other words,
	/// follow the Simula Standard literally
	public static boolean EXTENSIONS=true;

	/// Testing and debugging options
	public static class internal {
		/** Default Constructor: NOT USED */ public internal() { Util.IERR(); }

		/// Used to insert code to enforce 'stack size mismatch'
		public static boolean TESTING_STACK_SIZE = false;

		/// List generated .class files
		public static boolean LIST_GENERATED_CLASS_FILES = false;

		/// INLINE_TESTING on/off
		public static boolean INLINE_TESTING = false; 
		
		/// Used by Java-Coding to save the generated .java files.
		/// If not set, a temp directory is used/created.
		public static File keepJava = null;


		// Overall TRACING Options
		/** Debug option */	public static boolean TRACING=false;
		/** Debug option */	public static boolean DEBUGGING=false;		// Set by EditorMenues - doDebugAction

		// Scanner Trace Options
		/** Debug option */	public static boolean TRACE_SCAN=false;
		/** Debug option */	public static boolean TRACE_COMMENTS=false;

		// Parser Trace Options
		/** Debug option */	public static boolean TRACE_PARSE=false;
		/** Debug option */	public static int     PRINT_SYNTAX_TREE=0;
		/** Debug option */	public static boolean TRACE_ATTRIBUTE_OUTPUT=false;
		/** Debug option */	public static boolean TRACE_ATTRIBUTE_INPUT=false;

		// Checker Trace Options
		/** Debug option */	public static boolean TRACE_CHECKER=false;
		/** Debug option */	public static boolean TRACE_CHECKER_OUTPUT=false;
		/** Debug option */	public static int     TRACE_FIND_MEANING=0;

		// Java Coder Options
		/** Debug option */	public static boolean TRACE_CODING=false;         // Only when .java output
		/** Debug option */	public static boolean GNERATE_LINE_CALLS=false;   // Only when .java output

		// Byte code engineering Options
		/** Debug option */	public static boolean TRACE_BYTECODE_OUTPUT=false;
		/** Debug option */	public static boolean LIST_REPAIRED_INSTRUCTION_LIST=false;
		/** Debug option */	public static boolean TRACE_REPAIRING=false;
		/** Debug option */	public static boolean LIST_INPUT_INSTRUCTION_LIST=false;
		/** Debug option */	public static boolean TRACE_REPAIRING_INPUT=false;
		/** Debug option */	public static boolean TRACE_REPAIRING_OUTPUT=false;

		/** Runtime Options */ public static String SOURCE_FILE="";
		/** Runtime Options */ public static String RUNTIME_USER_DIR="";
		
		/// Initiate Compiler options
		public static void InitCompilerOptions() {

			internal.TRACING=false;
			internal.DEBUGGING=false;

			// Scanner Trace Options
			internal.TRACE_SCAN=false;
			internal.TRACE_COMMENTS=false;

			// Parser Trace Options
			internal.TRACE_PARSE=false;

			// Checker Trace Options
			internal.TRACE_CHECKER=false;
			internal.TRACE_CHECKER_OUTPUT=false;

			// Coder Trace Options
			internal.TRACE_CODING=false;
		}

	}
	
	/// The default constructor
    public CTOption() {}
	
	/// Initiate Compiler options.
	public static void InitCompilerOptions() {
//		CompilerMode compilerMode=CompilerMode.viaJavaSource;
		compilerMode=CompilerMode.directClassFiles;
//		compilerMode=CompilerMode.simulaClassLoader;
		CTOption.CaseSensitive=false;
		CTOption.verbose = false;
		CTOption.noExecution = false;
		CTOption.WARNINGS=true;
		CTOption.EXTENSIONS=true;
		
		internal.InitCompilerOptions();
	}

}