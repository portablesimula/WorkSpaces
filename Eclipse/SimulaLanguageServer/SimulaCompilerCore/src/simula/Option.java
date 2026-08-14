/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula;

import java.io.File;
import simula.compiler.SimulaCompiler;
import simula.compiler.SimulaCompiler.CompilerMode;
import simula.compiler.utilities.Util;

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
		IO.println("DocumentManager.packetName      " + SimulaCompiler.packetName);
		IO.println("DocumentManager.simulaRtsLib    " + SimulaCompiler.simulaRtsLib);
		
		IO.println("SimulaBuilder.outputDir         " + SimulaCompiler.outputDir);
		IO.println("SimulaBuilder.simulaTempDir     " + SimulaCompiler.simulaTempDir);
		IO.println("SimulaBuilder.tempJavaFileDir   " + SimulaCompiler.tempJavaFileDir);
		IO.println("SimulaBuilder.tempClassFileDir  " + SimulaCompiler.tempClassFileDir);
		IO.println("SimulaBuilder.extLib            " + SimulaCompiler.extLib);

	}
	/// Kalles før parsing og checking
	public static void decodeArguments(String[] argv) {
//		IO.println("Option.decodeArguments: ");
		SimulaCompiler.verbose = false;
		SimulaCompiler.WARNINGS = true;
		SimulaCompiler.EXTENSIONS = true;

		// Parse command line arguments.
		for(int i=0;i<argv.length;i++) {
			String arg=argv[i];
//			IO.println("Option.decodeArguments: arg: " + arg);
			switch(arg) {
//				case "-help": help(); break;
				case "-caseSensitive": SimulaCompiler.CaseSensitive = true; break;
//				case "-compilerMode": Option.setCompilerMode(argv[++i]); break;
//				case "-noexec": SimulaCompiler.noExecution=true; break;
				case "-noextension": SimulaCompiler.EXTENSIONS = false; break;
				case "-noPopup": SimulaCompiler.noPopup = true; break;
				case "-nowarn": SimulaCompiler.WARNINGS = false; break;
				case "-verbose": SimulaCompiler.verbose = true; break;
//				case "-version": printVersion(); break;
//				case "-select": setSelectors(argv[++i]); break;				
//				case "-keepJava": Option.internal.keepJava = new File(argv[++i]); break;

//				case "-output": SimulaCompiler.outputDir = new File(argv[++i]); break;
//				case "-extLib": SimulaCompiler.extLib = new File(argv[++i]); break;
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
				else if (arg.equalsIgnoreCase("-noexec")) SimulaCompiler.noExecution = true;
//				else if (arg.equalsIgnoreCase("-noextension")) SimulaCompiler.EXTENSIONS=false;
				else if (arg.equalsIgnoreCase("-noPopup")) SimulaCompiler.noPopup = true;
				else if (arg.equalsIgnoreCase("-nowarn")) SimulaCompiler.WARNINGS = false;
				else if (arg.equalsIgnoreCase("-verbose")) SimulaCompiler.verbose = true;
//				else if (arg.equalsIgnoreCase("-version")) printVersion();
//				else if (arg.equalsIgnoreCase("-select")) setSelectors(argv[++i]);				
				else if (arg.equalsIgnoreCase("-keepJava")) Option.internal.keepJava = new File(argv[++i]);

				else if (arg.equalsIgnoreCase("-simulaRtsLib")) SimulaCompiler.simulaRtsLib = new File(argv[++i]);
				else if (arg.equalsIgnoreCase("-output")) SimulaCompiler.outputDir = new File(argv[++i]);
				else if (arg.equalsIgnoreCase("-extLib")) SimulaCompiler.extLib = new File(argv[++i]);
				
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
		SimulaCompiler.compilerMode = SimulaCompiler.CompilerMode.directClassFiles;
		SimulaCompiler.CaseSensitive = false;
		SimulaCompiler.verbose = false;
		SimulaCompiler.noExecution = false;
		SimulaCompiler.WARNINGS = true;
		SimulaCompiler.EXTENSIONS = true;
		
		Option.internal.InitCompilerOptions();
	}

	/// Editor Utility: Set Compiler Mode.
	/// @param id the mode String.
	public static void setCompilerMode(String id) {
		if(id.equals("viaJavaSource")) {
			SimulaCompiler.compilerMode = CompilerMode.viaJavaSource;
		} else if(id.equals("directClassFiles")) {
			SimulaCompiler.compilerMode = CompilerMode.directClassFiles;
		}
	}


}