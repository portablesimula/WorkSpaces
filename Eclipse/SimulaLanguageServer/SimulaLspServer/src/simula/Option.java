/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula;

import java.io.File;

import simula.core.DocumentManager;
import simula.core.utilities.Util;

/// Compile Time Options.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/utilities/Option.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class Option {
//	public static boolean TESTING_NEWLINE = true;
	
	public static boolean LEX_VERIFY = true;
	public static boolean CHG_VERIFY = true;

	public static int TRACE_ACCEPT_STATEMENT = 0;
//	public static boolean KEEP_CLASS_LOADER = true;//false;
	
	/// The UI-Scale factor
	/// See: https://docs.oracle.com/en/java/javase/25/troubleshoot/java-2d-properties.html
	public static String editorUIScale;
	
	public static int lspTrace;

	public static boolean noPopup;

	public static boolean caseSensitive;
	
	/// Testing and debugging options
	public static class rts {
		/** Runtime Options */ public static boolean VERBOSE;
		/** Runtime Options */ public static boolean noPopup;
		/** Runtime Options */ public static boolean BLOCK_TRACING;
		/** Runtime Options */ public static boolean GOTO_TRACING;
		/** Runtime Options */ public static boolean QPS_TRACING;
		/** Runtime Options */ public static boolean SML_TRACING;

		/** Runtime Options */ public static String SOURCE_FILE = "";
		/** Runtime Options */ public static String RUNTIME_USER_DIR = "";
	}
	
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
//		/** Debug option */	public static boolean TRACE_ADVANCE_LEXER = false;//true;
//		/** Debug option */	public static boolean TRACE_COMMENTS = false;

		// Parser Trace Options
		/** Debug option */	public static boolean TRACE_PARSE = false;
//		/** Debug option */	public static boolean TRACE_PSITREE_GROW = false;
		/** Debug option */	public static int     PRINT_SYNTAX_TREE = 0;
		/** Debug option */	public static boolean TRACE_ATTRIBUTE_OUTPUT = false;
		/** Debug option */	public static boolean TRACE_ATTRIBUTE_INPUT = false;

		// Checker Trace Options
		/** Debug option */	public static boolean TRACE_CHECKER = false;
		/** Debug option */	public static boolean TRACE_CHECKER_OUTPUT = false;
		/** Debug option */	public static int     TRACE_FIND_MEANING = 0;
		
		// Semantic Token Trace Options
		/** Debug option */	public static int TRACE_NEW_SEMTOKEN = 0;
		/** Debug option */	public static int TRACE_VERIFY_TOKEN = 0;
		

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
		
		/// Initiate Compiler options
		public static void InitCompilerOptions() {

			Option.internal.TRACING = false;
			Option.internal.DEBUGGING = false;

			// Scanner Trace Options
			Option.internal.TRACE_LEXER = 0;
//			Option.internal.TRACE_COMMENTS = false;

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
		Util.println("------------  Option.print: " + title + "  ------------");
		Util.println("DocumentManager.packetName      " + DocumentManager.packetName);
		Util.println("DocumentManager.simulaRtsLib    " + DocumentManager.simulaRtsLib);
		
//		Util.println("SimulaBuilder.jarFileDir         " + SimulaCoder.jarFileDir);
//		Util.println("SimulaBuilder.simulaTempDir     " + SimulaCoder.simulaTempDir);
//		Util.println("SimulaBuilder.tempJavaFileDir   " + SimulaCoder.tempJavaFileDir);
//		Util.println("SimulaBuilder.tempClassFileDir  " + SimulaCoder.tempClassFileDir);
//		Util.println("SimulaBuilder.extLib            " + SimulaCoder.extLib);

	}
	
	/// TestBatch Utility:
	/// Kalles før parsing og checking
	public static void decodeArguments(String[] argv) {
//		Util.println("Option.decodeArguments: ");
		DocumentManager.verbose = false;
		DocumentManager.WARNINGS = true;
		DocumentManager.EXTENSIONS = true;

		// Parse command line arguments.
		for(int i=0;i<argv.length;i++) {
			String arg=argv[i];
//			Util.println("Option.decodeArguments: arg: " + arg);
			switch(arg) {
				case "--debugRun":      i++; break; // Skip it. Treated by SimulaLanguageServer.main
				case "-caseSensitive":  DocumentManager.CaseSensitive = true; break;
				case "-noextension":    DocumentManager.EXTENSIONS = false; break;
				case "-noPopup":        DocumentManager.noPopup = true; break;
				case "-nowarn":         DocumentManager.WARNINGS = false; break;
				case "-verbose":        DocumentManager.verbose = true; break;
//				case "-version":        printVersion(); break;
//				case "-select":         setSelectors(argv[++i]); break;				
				
				// Simula Compiler Options:
				case "-compilerMode":   Option.setCompilerMode(argv[++i]); break;
				case "-noexec":         DocumentManager.noExecution = true; break;
				case "-keepJava":       Option.internal.keepJava = new File(argv[++i]); break;

				case "-simulaRtsLib":   DocumentManager.simulaRtsLib = new File(argv[++i]); break;
				case "-output":         DocumentManager.jarFileDir = new File(argv[++i]); break;
				case "-extLib":         DocumentManager.extLib = new File(argv[++i]); break;
				
				// Special RT Options
//				case "-source": Option.SOURCE_FILE=argv[++i]; break;
//				case "-sourceFileDir":  sourceFileDir=argv[++i]; break;
//				case "-runtimeUserDir": Option.internal.RUNTIME_USER_DIR=argv[++i]; break;
				case "-RTS-verbose":       Option.rts.VERBOSE = true; break;
				case "-RTS-noPopup":       Option.rts.noPopup = true; break;
				case "-RTS-block-tracing": Option.rts.BLOCK_TRACING = true; break;
				case "-RTS-goto-tracing":  Option.rts.GOTO_TRACING = true; break;
				case "-RTS-qps-tracing":   Option.rts.QPS_TRACING = true; break;
				case "-RTS-sml-tracing":   Option.rts.SML_TRACING = true; break;

//				case SOURCE_FILE = "" = "-RTS-source-file";
//				case RUNTIME_USER_DIR = "-RTS-user-dir";

				
			    // Detailed Preferences
				case "-tracing": Option.internal.TRACING = true; break;
				case "-debugging": Option.internal.DEBUGGING = true; break;		// Set by EditorMenues - doDebugAction

				// Lexer Trace Options
				case "-trace-lexer": Option.internal.TRACE_LEXER = 1; break;
				case "-trace-new-lextoken": Option.internal.TRACE_NEW_LEXTOKEN = 1; break;

				// Parser Trace Options
				case "-trace-Parse": Option.internal.TRACE_PARSE = true; break;
				case "-print-syntax-tree": Option.internal.PRINT_SYNTAX_TREE = 1; break;
				case "-trace-attribute-output": Option.internal.TRACE_ATTRIBUTE_OUTPUT = true; break;
				case "-trace-attribute-input": Option.internal.TRACE_ATTRIBUTE_INPUT = true; break;

				// Checker Trace Options
				case "-trace-checker": Option.internal.TRACE_CHECKER = true; break;
				case "-trace-checker-output": Option.internal.TRACE_CHECKER_OUTPUT = true; break;
				case "-trace-find-meaning": Option.internal.TRACE_FIND_MEANING = 1; break;
				
				// Semantic Token Trace Options
				case "-trace-new-semtoken": Option.internal.TRACE_NEW_SEMTOKEN = 1; break;
				case "-trace-verify-token": Option.internal.TRACE_VERIFY_TOKEN = 1; break;
				case "-lexer-verifyer": Option.LEX_VERIFY = true; break;
				case "-change-verifyer": Option.CHG_VERIFY = true; break;
				

				// Java Coder Options
				case "-trace-coding": Option.internal.TRACE_CODING = true; break;         // Only when .java output
				case "-generate-line-calls": Option.internal.GNERATE_LINE_CALLS = true; break;   // Only when .java output

				// Byte code engineering Options
				case "-trace-bytecode-output": Option.internal.TRACE_BYTECODE_OUTPUT = true; break;
				case "-trace-repaired-instruction-list": Option.internal.LIST_REPAIRED_INSTRUCTION_LIST = true; break;
				case "-trace-repairing": Option.internal.TRACE_REPAIRING = true; break;
				case "-list-input-instructuion-list": Option.internal.LIST_INPUT_INSTRUCTION_LIST = true; break;
				case "-trace-repairing-input": Option.internal.TRACE_REPAIRING_INPUT = true; break;
				case "-trace-repairing-output": Option.internal.TRACE_REPAIRING_OUTPUT = true; break;

				
//				default: Util.println("Simula ERROR: Unknown option " + arg);
			}
		}
	}

//	/// Kalles av 'run' før coding og exec
//	public static void decodeArguments2(DocumentManager documentManager, String[] argv) {
////		Util.println("Option.decodeArguments2: ");
//
//		// Parse command line arguments.
//		for(int i=0;i<argv.length;i++) {
//			String arg=argv[i];
//			if (arg.charAt(0) == '-') { // command line option
//				if (arg.equalsIgnoreCase("--debugRun")) i++; // Skip it
//				else if (arg.equalsIgnoreCase("-compilerMode")) Option.setCompilerMode(documentManager, argv[++i]);
//				else if (arg.equalsIgnoreCase("-noexec")) DocumentManager.noExecution = true;
////				else if (arg.equalsIgnoreCase("-noextension")) SimulaCompiler.EXTENSIONS=false;
//				else if (arg.equalsIgnoreCase("-noPopup")) DocumentManager.noPopup = true;
//				else if (arg.equalsIgnoreCase("-nowarn")) DocumentManager.WARNINGS = false;
//				else if (arg.equalsIgnoreCase("-verbose")) DocumentManager.verbose = true;
////				else if (arg.equalsIgnoreCase("-version")) printVersion();
////				else if (arg.equalsIgnoreCase("-select")) setSelectors(argv[++i]);				
//				else if (arg.equalsIgnoreCase("-keepJava")) Option.internal.keepJava = new File(argv[++i]);
//
//				else if (arg.equalsIgnoreCase("-simulaRtsLib")) DocumentManager.simulaRtsLib = new File(argv[++i]);
//				else if (arg.equalsIgnoreCase("-output")) DocumentManager.jarFileDir = new File(argv[++i]);
//				else if (arg.equalsIgnoreCase("-extLib")) DocumentManager.extLib = new File(argv[++i]);
//				
//				// Special RT Options
////				else if (arg.equalsIgnoreCase("-source")) Option.SOURCE_FILE=argv[++i];
////				else if (arg.equalsIgnoreCase("-sourceFileDir")) sourceFileDir=argv[++i];
//				else if (arg.equalsIgnoreCase("-runtimeUserDir")) Option.internal.RUNTIME_USER_DIR=argv[++i];
//				else {
////					Util.println("Simula ERROR: Unknown option " + arg);
////					help();
//				}
//			} else Util.IERR(arg);
//		}
////		Util.IERR("STOP HER INTILL VIDERE");
//	}

	
	/// Initiate Compiler options.
	public static void InitCompilerOptions() {
		Option.editorUIScale = "1.0";
//		Option.selectedTheme = Palette.themeNames[0];
//		CompilerMode compilerMode=CompilerMode.viaJavaSource;
//		DocumentManager.compilerMode = DocumentManager.CompilerMode.directClassFiles;
		DocumentManager.CaseSensitive = false;
		DocumentManager.verbose = false;
		DocumentManager.noExecution = false;
		DocumentManager.WARNINGS = true;
		DocumentManager.EXTENSIONS = true;
		
		Option.internal.InitCompilerOptions();
	}

	/// Editor Utility: Set Compiler Mode.
	/// @param id the mode String.
	public static void setCompilerMode(String id) {
		if(id.equals("viaJavaSource")) {
			DocumentManager.compileViaJavaSource = true;
		} else if(id.equals("directClassFiles")) {
			DocumentManager.compileViaJavaSource = false;
		}
	}


}