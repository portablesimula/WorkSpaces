/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.utilities;

import java.io.File;
import java.util.Properties;

/// Compile Time Options.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/utilities/Option.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class Option {
	public static boolean PSI_VERIFY = true;

//	public static boolean TESTING_CLOSEABLE_TAB = true;
//	public static boolean TESTING_EOF = true;
	
	public static int TRACE_PSITREE_START_DONE = 0;
	public static int TRACE_ACCEPT_EXPRESSION = 0;
	public static int TRACE_ACCEPT_STATEMENT = 0;
	
	/// The UI-Scale factor
	/// See: https://docs.oracle.com/en/java/javase/25/troubleshoot/java-2d-properties.html
	public static String editorUIScale;

	/// The currently selected Color Theme
	public static String selectedTheme;
	
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
		/** Debug option */	public static boolean TRACING = false;
		/** Debug option */	public static boolean DEBUGGING = false;		// Set by EditorMenues - doDebugAction

		// Lexer Trace Options
		/** Debug option */	public static int TRACE_LEXER = 0;
		/** Debug option */	public static int TRACE_NEW_LEXTOKEN = 0;
		/** Debug option */	public static boolean TRACE_ADVANCE_LEXER = false;//true;
		/** Debug option */	public static boolean TRACE_COMMENTS = false;

		// Parser Trace Options
		/** Debug option */	public static boolean TRACE_PARSE = false;
		/** Debug option */	public static boolean TRACE_PSITREE_GROW = false;
		/** Debug option */	public static int     PRINT_SYNTAX_TREE = 0;
		/** Debug option */	public static boolean TRACE_ATTRIBUTE_OUTPUT = false;
		/** Debug option */	public static boolean TRACE_ATTRIBUTE_INPUT = false;

		// Checker Trace Options
		/** Debug option */	public static boolean TRACE_CHECKER = false;
		/** Debug option */	public static boolean TRACE_CHECKER_OUTPUT = false;
		/** Debug option */	public static int     TRACE_FIND_MEANING = 0;

		// Java Coder Options
		/** Debug option */	public static boolean TRACE_CODING = false;         // Only when .java output
		/** Debug option */	public static boolean GNERATE_LINE_CALLS = false;   // Only when .java output

		// Byte code engineering Options
		/** Debug option */	public static boolean TRACE_BYTECODE_OUTPUT = false;
		/** Debug option */	public static boolean LIST_REPAIRED_INSTRUCTION_LIST = false;
		/** Debug option */	public static boolean TRACE_REPAIRING = false;
		/** Debug option */	public static boolean LIST_INPUT_INSTRUCTION_LIST = false;
		/** Debug option */	public static boolean TRACE_REPAIRING_INPUT = false;
		/** Debug option */	public static boolean TRACE_REPAIRING_OUTPUT = false;

		/** Runtime Options */ public static String SOURCE_FILE = "";
		/** Runtime Options */ public static String RUNTIME_USER_DIR = "";
		
		/// Initiate Compiler options
		public static void InitCompilerOptions() {

			Option.internal.TRACING = false;
			Option.internal.DEBUGGING = false;

			// Scanner Trace Options
			Option.internal.TRACE_LEXER = 0;
			Option.internal.TRACE_COMMENTS = false;

			// Parser Trace Options
			Option.internal.TRACE_PARSE = false;

			// Checker Trace Options
			Option.internal.TRACE_CHECKER = false;
			Option.internal.TRACE_CHECKER_OUTPUT = false;

			// Coder Trace Options
			Option.internal.TRACE_CODING = false;
		}

	}
	
	/// The default constructor
	private Option() {}
	
	/// Initiate Compiler options.
	public static void InitCompilerOptions() {
		Option.editorUIScale = "1.0";
//		Option.selectedTheme = Palette.themeNames[0];
//		CompilerMode compilerMode=CompilerMode.viaJavaSource;
		compilerMode = CompilerMode.directClassFiles;
//		compilerMode = CompilerMode.simulaClassLoader;
		Option.CaseSensitive = false;
		Option.verbose = false;
		Option.noExecution = false;
		Option.WARNINGS = true;
		Option.EXTENSIONS = true;
		
		Option.internal.InitCompilerOptions();
	}
	
	/// Get Compiler options from property file.
	/// @param properties the properties used.
	public static void getCompilerOptions(Properties properties) {
//		Option.editorUIScale = properties.getProperty("simula.editor.UIScale", "1.0");
//		Option.selectedTheme = properties.getProperty("simula.editor.theme", Palette.themeNames[0]);
//		setCompilerMode(properties.getProperty("simula.compiler.option.mode", "directClassFiles"));
//		Option.CaseSensitive = properties.getProperty("simula.compiler.option.CaseSensitive", "false").equalsIgnoreCase("true");
//		Option.verbose = properties.getProperty("simula.compiler.option.verbose", "false").equalsIgnoreCase("true");
//		Option.noExecution = properties.getProperty("simula.compiler.option.noExecution", "false").equalsIgnoreCase("true");
//		Option.WARNINGS = properties.getProperty("simula.compiler.option.WARNINGS", "true").equalsIgnoreCase("true");
//		Option.EXTENSIONS = properties.getProperty("simula.compiler.option.EXTENSIONS", "true").equalsIgnoreCase("true");
	}
	
	/// Set Compiler options in property file.
	/// @param properties the properties used.
	public static void setCompilerOptions(Properties properties) {
		properties.setProperty("simula.editor.UIScale", Option.editorUIScale);
		properties.setProperty("simula.editor.theme", Option.selectedTheme);
		properties.setProperty("simula.compiler.option.mode", ""+Option.compilerMode);
		properties.setProperty("simula.compiler.option.CaseSensitive", ""+Option.CaseSensitive);
		properties.setProperty("simula.compiler.option.verbose", ""+Option.verbose);
		properties.setProperty("simula.compiler.option.noExecution", ""+Option.noExecution);
		properties.setProperty("simula.compiler.option.WARNINGS", ""+Option.WARNINGS);
		properties.setProperty("simula.compiler.option.EXTENSIONS", ""+Option.EXTENSIONS);
	}

	/// Editor Utility: Set Compiler Mode.
	/// @param id the mode String.
	public static void setCompilerMode(String id) {
		if(id.equals("viaJavaSource")) {
			Option.compilerMode = CompilerMode.viaJavaSource;
		} else if(id.equals("directClassFiles")) {
			Option.compilerMode = CompilerMode.directClassFiles;
		} else if(id.equals("simulaClassLoader")) {
			Option.compilerMode = CompilerMode.simulaClassLoader;
		}
	}

	/// Returns the option name 'id'
	/// @param id option id
	/// @return the option name 'id'
	public static boolean getOption(String id) {
		if(id.equalsIgnoreCase("CaseSensitive")) return(CaseSensitive); 
		if(id.equalsIgnoreCase("VERBOSE")) return(verbose); 
		if(id.equalsIgnoreCase("noExecution")) return(noExecution); 
		if(id.equalsIgnoreCase("WARNINGS")) return(WARNINGS); 
		if(id.equalsIgnoreCase("EXTENSIONS")) return(EXTENSIONS); 
		if(id.equalsIgnoreCase("TRACING")) return(internal.TRACING); 
		if(id.equalsIgnoreCase("TRACE_LEXER")) return(internal.TRACE_LEXER > 0); 
		if(id.equalsIgnoreCase("TRACE_COMMENTS")) return(internal.TRACE_COMMENTS); 
		if(id.equalsIgnoreCase("TRACE_PARSE")) return(internal.TRACE_PARSE); 
		if(id.equalsIgnoreCase("TRACE_ATTRIBUTE_OUTPUT")) return(internal.TRACE_ATTRIBUTE_OUTPUT); 
		if(id.equalsIgnoreCase("TRACE_ATTRIBUTE_INPUT")) return(internal.TRACE_ATTRIBUTE_INPUT); 
		if(id.equalsIgnoreCase("TRACE_CHECKER")) return(internal.TRACE_CHECKER); 
		if(id.equalsIgnoreCase("TRACE_CHECKER_OUTPUT")) return(internal.TRACE_CHECKER_OUTPUT); 
		if(id.equalsIgnoreCase("TRACE_CODING")) return(internal.TRACE_CODING); 
		if(id.equalsIgnoreCase("TRACE_BYTECODE_OUTPUT")) return(internal.TRACE_BYTECODE_OUTPUT); 
		return(false);
	}

	/// Set the option named 'id' to the given value
	/// @param id option id
	/// @param val new option value
	public static void setOption(String id,boolean val) {
		if(id.equalsIgnoreCase("CaseSensitive")) CaseSensitive=val; 
		if(id.equalsIgnoreCase("VERBOSE")) verbose=val; 
		if(id.equalsIgnoreCase("noExecution")) noExecution=val; 
		if(id.equalsIgnoreCase("WARNINGS")) WARNINGS=val; 
		if(id.equalsIgnoreCase("EXTENSIONS")) EXTENSIONS=val; 
		if(id.equalsIgnoreCase("TRACING")) internal.TRACING=val; 
		if(id.equalsIgnoreCase("TRACE_LEXER")) internal.TRACE_LEXER=(val)?1:0; 
		if(id.equalsIgnoreCase("TRACE_COMMENTS")) internal.TRACE_COMMENTS=val; 
		if(id.equalsIgnoreCase("TRACE_PARSE")) internal.TRACE_PARSE=val; 
		if(id.equalsIgnoreCase("TRACE_ATTRIBUTE_OUTPUT")) internal.TRACE_ATTRIBUTE_OUTPUT=val; 
		if(id.equalsIgnoreCase("TRACE_ATTRIBUTE_INPUT")) internal.TRACE_ATTRIBUTE_INPUT=val; 
		if(id.equalsIgnoreCase("TRACE_CHECKER")) internal.TRACE_CHECKER=val; 
		if(id.equalsIgnoreCase("TRACE_CHECKER_OUTPUT")) internal.TRACE_CHECKER_OUTPUT=val; 
		if(id.equalsIgnoreCase("TRACE_CODING")) internal.TRACE_CODING=val; 
		if(id.equalsIgnoreCase("TRACE_BYTECODE_OUTPUT")) internal.TRACE_BYTECODE_OUTPUT=val; 
	}


}