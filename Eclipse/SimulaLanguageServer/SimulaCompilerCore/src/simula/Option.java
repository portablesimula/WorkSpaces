/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula;

import java.io.File;

import simula.core.CoreGlobal2;
import simula.core.CoreGlobal2.CompilerMode;
import simula.core.coder.SimulaCoder;
import simula.core.utilities.Util;

/// Compile Time Options.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/utilities/Option.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class Option {
	public static boolean LEX_VERIFY = true;

	public static int TRACE_ACCEPT_STATEMENT = 0;
	public static boolean KEEP_CLASS_LOADER = true;//false;
	
	/// The UI-Scale factor
	/// See: https://docs.oracle.com/en/java/javase/25/troubleshoot/java-2d-properties.html
	public static String editorUIScale;
	
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
	

	public static void print(String title) {
		IO.println("------------  Option.print: " + title + "  ------------");
		IO.println("DocumentManager.packetName      " + CoreGlobal2.packetName);
		IO.println("DocumentManager.simulaRtsLib    " + CoreGlobal2.simulaRtsLib);
		
		IO.println("SimulaBuilder.outputDir         " + SimulaCoder.outputDir);
		IO.println("SimulaBuilder.simulaTempDir     " + SimulaCoder.simulaTempDir);
		IO.println("SimulaBuilder.tempJavaFileDir   " + SimulaCoder.tempJavaFileDir);
		IO.println("SimulaBuilder.tempClassFileDir  " + SimulaCoder.tempClassFileDir);
		IO.println("SimulaBuilder.extLib            " + SimulaCoder.extLib);

	}
	/// Kalles før parsing og checking
	public static void decodeArguments(String[] argv) {
//		IO.println("Option.decodeArguments: ");
		CoreGlobal2.verbose = false;
		CoreGlobal2.WARNINGS = true;
		CoreGlobal2.EXTENSIONS = true;

		// Parse command line arguments.
		for(int i=0;i<argv.length;i++) {
			String arg=argv[i];
//			IO.println("Option.decodeArguments: arg: " + arg);
			switch(arg) {
//				case "-help": help(); break;
				case "-caseSensitive": CoreGlobal2.CaseSensitive = true; break;
//				case "-compilerMode": Option.setCompilerMode(argv[++i]); break;
//				case "-noexec": SimulaCompiler.noExecution=true; break;
				case "-noextension": CoreGlobal2.EXTENSIONS = false; break;
				case "-noPopup": CoreGlobal2.noPopup = true; break;
				case "-nowarn": CoreGlobal2.WARNINGS = false; break;
				case "-verbose": CoreGlobal2.verbose = true; break;
//				case "-version": printVersion(); break;
//				case "-select": setSelectors(argv[++i]); break;				
//				case "-keepJava": Option.internal.keepJava = new File(argv[++i]); break;

//				case "-output": SimulaCoder.outputDir = new File(argv[++i]); break;
//				case "-extLib": SimulaCoder.extLib = new File(argv[++i]); break;
				default: IO.println("Simula ERROR: Unknown option " + arg);
			}
		}
	}

	/// Kalles av 'run' før coding og exec
	public static void decodeArguments2(String[] argv) {
//		IO.println("Option.decodeArguments2: ");

		// Parse command line arguments.
		for(int i=0;i<argv.length;i++) {
			String arg=argv[i];
			if (arg.charAt(0) == '-') { // command line option
				if (arg.equalsIgnoreCase("-compilerMode")) Option.setCompilerMode(argv[++i]);
				else if (arg.equalsIgnoreCase("-noexec")) CoreGlobal2.noExecution = true;
//				else if (arg.equalsIgnoreCase("-noextension")) SimulaCompiler.EXTENSIONS=false;
				else if (arg.equalsIgnoreCase("-noPopup")) CoreGlobal2.noPopup = true;
				else if (arg.equalsIgnoreCase("-nowarn")) CoreGlobal2.WARNINGS = false;
				else if (arg.equalsIgnoreCase("-verbose")) CoreGlobal2.verbose = true;
//				else if (arg.equalsIgnoreCase("-version")) printVersion();
//				else if (arg.equalsIgnoreCase("-select")) setSelectors(argv[++i]);				
				else if (arg.equalsIgnoreCase("-keepJava")) Option.internal.keepJava = new File(argv[++i]);

				else if (arg.equalsIgnoreCase("-simulaRtsLib")) CoreGlobal2.simulaRtsLib = new File(argv[++i]);
				else if (arg.equalsIgnoreCase("-output")) SimulaCoder.outputDir = new File(argv[++i]);
				else if (arg.equalsIgnoreCase("-extLib")) SimulaCoder.extLib = new File(argv[++i]);
				
				// Special RT Options
//				else if (arg.equalsIgnoreCase("-source")) Option.SOURCE_FILE=argv[++i];
//				else if (arg.equalsIgnoreCase("-sourceFileDir")) sourceFileDir=argv[++i];
				else if (arg.equalsIgnoreCase("-runtimeUserDir")) Option.internal.RUNTIME_USER_DIR=argv[++i];
				else {
					IO.println("Simula ERROR: Unknown option " + arg);
//					help();
				}
			} else Util.IERR(arg);
		}
//		Util.IERR("STOP HER INTILL VIDERE");
	}

	
	/// Initiate Compiler options.
	public static void InitCompilerOptions() {
		Option.editorUIScale = "1.0";
//		Option.selectedTheme = Palette.themeNames[0];
//		CompilerMode compilerMode=CompilerMode.viaJavaSource;
		CoreGlobal2.compilerMode = CoreGlobal2.CompilerMode.directClassFiles;
		CoreGlobal2.CaseSensitive = false;
		CoreGlobal2.verbose = false;
		CoreGlobal2.noExecution = false;
		CoreGlobal2.WARNINGS = true;
		CoreGlobal2.EXTENSIONS = true;
		
		Option.internal.InitCompilerOptions();
	}

	/// Editor Utility: Set Compiler Mode.
	/// @param id the mode String.
	public static void setCompilerMode(String id) {
		if(id.equals("viaJavaSource")) {
			CoreGlobal2.compilerMode = CompilerMode.viaJavaSource;
		} else if(id.equals("directClassFiles")) {
			CoreGlobal2.compilerMode = CompilerMode.directClassFiles;
		}
	}


}