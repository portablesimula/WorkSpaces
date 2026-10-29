/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.lsp.utilities;

import java.io.File;
import java.util.Stack;

import simula.lsp.SimulaLanguageServer;
import simula.lsp.SimulaTextDocumentService;
import simula.lsp.builder.JavaSourceFileCoder;
import simula.lsp.syntaxClass.declaration.DeclarationScope;

/// Global Variables.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/utilities/Global.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class LspGlobal {
    
//	public static boolean TRACE_LEXER = false;
//	public static boolean TRACE_COMMENTS = false;
	public static boolean INLINE_CONNECTED = false; // Set by: Simula main before Connect(client) is called

	// ===============================================================================================
    public static String serverName = "SimulaLanguageServer";
    public static String serverVersion = "1.0";

	public static SimulaLanguageServer simulaLanguageServer;
	public static SimulaTextDocumentService getSimulaTextDocumentService() {
		return (SimulaTextDocumentService) simulaLanguageServer.getTextDocumentService();
	}

	
	public static ConsolePanel debugConsole;

	/// The current source line number.
	public static int sourceLineNumber;
	
	/// Next available Object Sequence Number.
	public static int Object_SEQU;
	
	/// Current Java output Module. Maintained by JavaModule during Java Coding
	public static JavaSourceFileCoder currentJavaFileCoder;

	/// Default constructor.
	LspGlobal() {}

	/// Initiate Global variables.
	public static void initiate() {
		simulaLanguageServer = new SimulaLanguageServer();
		Object_SEQU = 8001;
//		includeQueue = null;
		ClassHierarchy.init();
//		debugConsole = new ConsolePanel("Simula LSP Server - Debug Console");
//		debugConsole.popup();
//		debugConsole.write("Started !");
//    	Util.println("Global.initiate completed");
	}

	/// The declaration scope stack.
	private static Stack<DeclarationScope> scopeStack = new Stack<DeclarationScope>();
	
	/// Current declaration scope.
	/// Maintained during Checking and Coding
	private static DeclarationScope currentScope = null; // Current Scope. Maintained during Checking and Coding

	/// Returns the current scope.
	/// @return the current scope
	public static DeclarationScope getCurrentScope() {
		return (currentScope);
	}

	/// During Parsing: Set current scope.
	/// @param scope the new scope
	public static void setScope(DeclarationScope scope) {
		currentScope = scope;
	}

	/// During Checking and Coding: Enter declaration scope.
	/// @param scope the new current scope
	public static void enterScope(DeclarationScope scope) {
		scopeStack.push(currentScope);
		currentScope = scope;
	}

	/// During Checking and Coding: Exit declaration scope.
	public static void exitScope() {
		currentScope = scopeStack.pop();
	}

	/// Returns a temp file directory.
	/// @param subDir the wanted sub-directory name
	/// @return a temp file directory
	public static File getTempFileDir(String subDir) {
		String tmp = System.getProperty("java.io.tmpdir");
		File tempFileDir = new File(tmp, subDir);
		tempFileDir.mkdirs();
		setAccessRWX(tempFileDir);
		return (tempFileDir);
	}

	/// Utility: Set read-write-execute access on a directory
	/// @param dir the directory
	private static void setAccessRWX(File dir) {
		dir.setReadable(true, false); // Readable for all users
		dir.setWritable(true, false); // Writable for all users
		dir.setExecutable(true, false); // Executable for all users
	}


}