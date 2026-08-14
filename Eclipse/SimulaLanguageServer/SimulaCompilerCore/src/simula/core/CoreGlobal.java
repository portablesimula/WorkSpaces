/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Stack;
import java.util.jar.JarFile;

import simula.SimulaCoreClient;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.syntaxClass.declaration.DeclarationScope;
import simula.core.utilities.ClassHierarchy;

/// Global Variables.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/utilities/Global.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class CoreGlobal {
    
	public static boolean TRACE_LEXER = false;
	public static boolean TRACE_COMMENTS = false;
	
	public static SimulaCoreClient simulaCoreClient;

	// ===============================================================================================
	
//	/// The current Charset.
//	public static Charset _CHARSET = Charset.defaultCharset();

	/// The current source line number.
	public static int sourceLineNumber;
	
	/// Next available Object Sequence Number.
	public static int Object_SEQU;

//	/// Packet name used in generated .java files.
//	/// NOTE: Must be a single identifier.
//	public static String packetName = "simprog";
	
	/// Current Java output Module. Maintained by JavaModule during Java Coding
	public static JavaSourceFileCoder currentJavaFileCoder;

//	/// The Jar files queued for later inclusion.
//	/// See: JarFileBuilder for details.
//	public static LinkedList<JarFile> includeQueue;

	/// Default constructor.
	CoreGlobal() {}

	/// Initiate Global variables.
	public static void initiate() {
		Object_SEQU = 8001;
//		includeQueue = null;
		ClassHierarchy.init();
//    	IO.println("Global.initiate completed");
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