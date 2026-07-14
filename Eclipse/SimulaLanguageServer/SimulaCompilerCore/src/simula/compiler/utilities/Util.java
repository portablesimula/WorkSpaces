/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.utilities;

import java.io.File;
import java.io.InputStream;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.constantpool.ConstantPoolBuilder;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.util.Vector;

import simula.Option;
import simula.builder.SimulaBuilder;
import simula.compiler.syntaxClass.SyntaxElement;
import simula.lsp.util.SimPosition;
import simula.lsp.util.SimRange;
import simula.token.Identifier;
import simula.token.LexToken;

/// A set of all static Utility Methods
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/utilities/Util.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class Util { 
	/// Default constructor.
	Util(){}

	
	/// Utility: get Java ID
	/// @return the Java ID string
	public static String getJavaID() {
		String javaID="Java version "+System.getProperty("java.version");
        return(javaID);
	}

	
	public static String calledFrom(int startIndex, int endIndex) {
		StackTraceElement[] elt = Thread.currentThread().getStackTrace();
		StringBuilder sb = new StringBuilder();
		int n = Math.min(elt.length, endIndex);
		String sep ="";
		for(int i=startIndex;i<n;i++) {
			String methodName = elt[i].getMethodName();
//			String className = elt[i].getClassName();
			String fileName = elt[i].getFileName();
			String className = fileName.replace(".java", "");
			int line = elt[i].getLineNumber();
			
			String ref = "(" + fileName + ':' + line + ')';
			
//			sb.append(sep).append(className).append('.').append(methodName).append("[line ").append(line).append(']').append(elt[i]); sep=",  ";
			sb.append(sep).append(className).append('.').append(methodName).append(ref); sep=",  ";
//			sb.append(sep).append(elt[i]); sep=",";
		}
		return sb.toString();
	}

	/// Number of error messages.
	public static int nError;

//	/// Print a error message.
//	/// @param msg the message
//	public static void error(final String msg) {
//		String err = edLINE(": OLD_Error: " + msg);
//		nError++;
//		printError(err);
//	}

	/// Print a error message.
	/// @param msg the message
	public static void generalWarning(final String msg) {
		LOG.error("General Error: " + msg);
//		simBuilder.addDiagnostic(diagnostic); // TODO: DETTE MÅ RETTES - 
	}

	/// Print a error message.
	/// @param msg the message
	public static void generalWarning(final int lineNumber, final String msg) {
		LOG.error("Line " + lineNumber + ": General Error: " + msg);
//		simBuilder.addDiagnostic(diagnostic); // TODO: DETTE MÅ RETTES - 
	}

	/// Print a warning message.
	/// @param msg the message
	public static void warning(final SimulaBuilder simBuilder, final String msg) {
		warning(simBuilder, simBuilder.getPrevParserToken(), msg);
	}

	/// Print a warning message.
	/// @param msg the message
	public static void warning(final SimulaBuilder simBuilder, final LexToken token, final String msg) {
        if(Option.LEX_VERIFY) {
        // SJEKK AT SimPosition er inne på linja !!!
        if(token.keyWord == KeyWord.NEWLINE) // CRLF or LF ==> ERROR
        	Util.IERR("Util.warning: Warning not inside text line");
        }
        SimPosition start = new SimPosition(token.lineNumber, token.column);
        SimPosition end = new SimPosition(token.lineNumber, token.column + token.length);
		SimulaDiagnostic diagnostic = new SimulaDiagnostic(SimulaDiagnostic.Severity.Warning, new SimRange(start, end), msg);
		
		LOG.warning(diagnostic.toString());
		simBuilder.addDiagnostic(diagnostic);
	}

	/// Print a warning message.
	/// @param msg the message
	public static void warning(final SyntaxElement elt, final String msg) {
        LexToken first = elt.getFirstLexToken();
        LexToken last = elt.getLastLexToken();
        
        SimPosition start = new SimPosition(first.lineNumber, first.column);
        SimPosition end = new SimPosition(last.lineNumber, last.column + last.length);
		SimulaDiagnostic diagnostic = new SimulaDiagnostic(SimulaDiagnostic.Severity.Warning, new SimRange(start, end), msg);

		LOG.warning(diagnostic.toString());
		elt.simBuilder.addDiagnostic(diagnostic);
	}


	/// Report an error message to the SimulaCoreClient.
	/// @param msg the message
	public static void generalError(final String msg) {
		CoreGlobal.simulaCoreClient.error("General Error: " + msg);
	}

	/// Report an error message to the SimulaCoreClient.
	/// @param msg the message
	public static void generalError(final int lineNumber, final String msg) {
		CoreGlobal.simulaCoreClient.error("Line " + lineNumber + ": General Error: " + msg);
	}
	
	/// Print a error message.
	/// @param msg the message
	public static void syntaxError(final SimulaBuilder simBuilder, final String msg) {
		syntaxError(simBuilder, simBuilder.getPrevParserToken(), msg);
	}
	
	public static void syntaxError(final SimulaBuilder simBuilder, final LexToken token, final String msg) {
        if(Option.LEX_VERIFY) {
        // SJEKK AT SimPosition er inne på linja !!!
        if(token.keyWord == KeyWord.NEWLINE) // CRLF or LF ==> ERROR
        	Util.IERR("Util.warning: Error not inside text line");
        }
        SimPosition start = new SimPosition(token.lineNumber, token.column);
        SimPosition end = new SimPosition(token.lineNumber, token.column + token.length);
		SimulaDiagnostic diagnostic = new SimulaDiagnostic(SimulaDiagnostic.Severity.Error, new SimRange(start, end), msg);
		
		LOG.error(diagnostic.toString());
		
//		Util.IERR("Util.SyntaxError: ");
		
		simBuilder.addDiagnostic(diagnostic);
	}
	
	public static void semanticError(final SyntaxElement elt, final String msg) {
        LexToken first = elt.getFirstLexToken();
        LexToken last = elt.getLastLexToken();
        
        SimPosition start = new SimPosition(first.lineNumber, first.column);
        SimPosition end = new SimPosition(last.lineNumber, last.column + last.length);
		SimulaDiagnostic diagnostic = new SimulaDiagnostic(SimulaDiagnostic.Severity.Error, new SimRange(start, end), msg);
		
		LOG.error(diagnostic.toString());
		elt.simBuilder.addDiagnostic(diagnostic);
	}
	
	/// Error during Code generation:
	public static void codingError(final SyntaxElement elt, final String msg) {
        LexToken first = elt.getFirstLexToken();
        LexToken last = elt.getLastLexToken();
        
        SimPosition start = new SimPosition(first.lineNumber, first.column);
        SimPosition end = new SimPosition(last.lineNumber, last.column + last.length);
		SimulaDiagnostic diagnostic = new SimulaDiagnostic(SimulaDiagnostic.Severity.Error, new SimRange(start, end), msg);
		
		LOG.error(diagnostic.toString());
		elt.simBuilder.addDiagnostic(diagnostic);
	}

	/// Exit with Thread.dumpStack
	public static void STOP() {
		Thread.dumpStack();
		System.exit(-1);;
	}

	/// Print the internal error message: IMPOSSIBLE.
	public static void IERR() {
		IERR("IMPOSSIBLE");
	}

	/// Print a internal error message.
	/// @param msg the message
	public static void IERR(final String msg) {
		LOG.error("ERROR: Internal error - " + msg);
		Thread.dumpStack();
		FORCED_EXIT();
	}

	/// Perform FORCED EXIT.
	private static void FORCED_EXIT() {
		IO.println("FORCED EXIT");
		System.exit(-1);
	}

	/// Print a internal error message.
	/// @param msg the message
	/// @param e any Throwable
	public static void IERR(final String msg,final Throwable e) {
		LOG.error("ERROR: Internal error - " + msg +"\nCaused by:");
		e.printStackTrace();
		FORCED_EXIT();
	}
	
//	/// Edit a line with source line number etc.
//	/// @param s the line string
//	/// @return the resulting string
//	private static String edLINE(String s) {		
//		String line = "LINE " + Global.sourceLineNumber + s;
//		if(Global.insertName!=null) line = Global.insertName + ':' + line;
//		if(Global.getCurrentScope() != null) {
//			if(Global.getCurrentScope().sourceFileName!=null) {
//				String sourceName = getBaseName(Global.getCurrentScope().sourceFileName);
//				line = sourceName + ':' + line;
//			}
//		}
//		return(line);
//	}
	
	/// Return the base name part of an URI
	/// @param fileName a File Name.
	/// @return the base name part of an URI
	public static String getBaseName(final String uri) {
    	File file = new File(uri);
    	String fileName = file.getName();
		int p=fileName.lastIndexOf(".");
		return (p > 0)? fileName.substring(0, p) : fileName;
	}

	/// Utility method: TRACE
	/// @param msg the message to print
	public static void TRACE(final String msg) {
		if (Option.internal.TRACING)
			println("TRACE " + CoreGlobal.sourceLineNumber + ": " + msg);
	}

	/// Utility method: TRACE_OUTPUT
	/// @param msg the message to print
	public static void TRACE_OUTPUT(final String msg) {
		if (Option.internal.TRACE_ATTRIBUTE_OUTPUT)
			Util.println("ATTR OUTPUT: " + msg);
	}

	/// Utility method: TRACE_INPUT
	/// @param msg the message to print
	public static void TRACE_INPUT(final String msg) {
		if (Option.internal.TRACE_ATTRIBUTE_INPUT)
			Util.println("ATTR INPUT: " + msg);
	}

	/// Utility method: ASSERT
	/// @param test this test must be true
	/// @param msg the message when test = false
	public static void ASSERT(final boolean test, final String msg) {
		if (!test) {
			IERR("ASSERT(" + msg + ") -- FAILED");
		}
	}

	/// Print a string.
	/// @param s the string
	public static void println(final String s) {
//		if (Global.console != null) {
//			String u = s.replace('\r', (char) 0);
//			u = u.replace('\n', (char) 0);
//			Global.console.write(u + '\n');
//		} else
			IO.println(s);
	}  

	/// Print a error message.
	/// @param s the message
	public static void printError(final String s) {
		String u = s.replace('\r', (char) 0);
//		if (Global.console != null)	Global.console.writeError(u + '\n');
//		else
			System.err.println(u);
	}  

	/// Print a warning message.
	/// @param s the message
	public static void printWarning(final String s) {
		String u = s.replace('\r', (char) 0);
//		if (Global.console != null)	Global.console.writeWarning(u + '\n');
//		else
			System.err.println(u);
	}  

    //*******************************************************************************
    //*** isJavaIdentifier - Check if 'ident' is a legal Java Identifier
    //*******************************************************************************
	/// Check if 'ident' is a legal Java Identifier.
	/// @param ident the given identifier
	/// @return true if 'ident' is a legal Java Identifier otherwise false
	public static boolean isJavaIdentifier(final String ident) {
		if (ident.length() == 0 || !Character.isJavaIdentifierStart(ident.charAt(0))) {
			return false;
		}
		for (int i = 1; i < ident.length(); i++) {
			if (!Character.isJavaIdentifierPart(ident.charAt(i))) {
				return false;
			}
		}
		return true;
	}

    //*******************************************************************************
    //*** makeJavaIdentifier - Make 'ident' a legal Java Identifier
    //*******************************************************************************
	/// Make 'ident' a legal Java Identifier.
	/// @param ident the given identifier
	/// @return the resulting Java identifier
	public static String makeJavaIdentifier(final String ident) {
		StringBuilder sb=new StringBuilder();
		char c=ident.charAt(0);
		if (ident.length() == 0 || !Character.isJavaIdentifierStart(c)) c='_';
		sb.append(c);
		
		for (int i = 1; i < ident.length(); i++) {
			c=ident.charAt(i);
			if (!Character.isJavaIdentifierPart(c)) c='_';
			sb.append(c);
		}
		return(sb.toString());
	}
  
    //*******************************************************************************
    //*** 
    //*******************************************************************************
	/// Returns true if the two specified strings are equal to one another.
	/// @param s1 argument string
	/// @param s2 argument string
	/// @return true if the two specified strings are equal to one another
	public static boolean equals(Identifier id1,Identifier id2) {
		return equals(id1.value, id2.value);
	}
	  
    //*******************************************************************************
    //*** 
    //*******************************************************************************
	/// Returns true if the two specified strings are equal to one another.
	/// @param s1 argument string
	/// @param s2 argument string
	/// @return true if the two specified strings are equal to one another
	public static boolean equals(String s1,String s2) {
		if(Option.CaseSensitive)
			 return(s1.equals(s2));			
		else return(s1.equalsIgnoreCase(s2));
	}

    //*******************************************************************************
    //*** IPOW - Integer Power: b ** x
    //*******************************************************************************
	/// Utility: Integer Power: b ** x
	/// @param base argument base
	/// @param x argument x
	/// @return Returns the value of 'base' raised to the power of 'x'
	public static int IPOW(final SimulaBuilder simBuilder, final long base, long x) {
		if (x == 0) {
			if (base == 0)
				syntaxError(simBuilder, "Exponentiation: " + base + " ** " + x + "  Result is undefined.");
			return (1); // any ** 0 ==> 1
		} else if (x < 0)
			syntaxError(simBuilder, "Exponentiation: " + base + " ** " + x + "  Result is undefined.");
		else if (base == 0)
			return (0); // 0 ** non_zero ==> 0
		
		long res=(long) Math.pow((double)base,(double)x);
		if(res > Integer.MAX_VALUE || res < Integer.MIN_VALUE)
			syntaxError(simBuilder, "Arithmetic overflow: "+base+" ** "+x+" ==> "+res
					+" which is outside integer value range["+Integer.MIN_VALUE+':'+Integer.MAX_VALUE+']');
		return((int)res);
	}
  

	// ***************************************************************
	// *** LIST .class file
	// ***************************************************************
	/// Print a .class file listing.
	/// @param classFileName the .class file name
	public static void doListClassFile(final String classFileName) {
		IO.println("\n\n******** BEGIN List ClassFile: "+classFileName + " *****************************************************");
		try {
			execute("javap", "-c", "-l", "-p", "-s", "-verbose", classFileName);
		} catch (Exception e) {
			Util.IERR("Impossible", e);
		}
		IO.println("******** ENDOF List ClassFile: "+classFileName + " *****************************************************\n\n");
	}

	// ***************************************************************
	// *** EXECUTE OS COMMAND
	// ***************************************************************
	/// Execute OS Command
	/// @param cmd command vector
	/// @return return value from the OS
	public static int execute(final Vector<String> cmd) {
		String[] cmds = new String[cmd.size()];
		cmd.copyInto(cmds);
		return (execute(cmds));
	}

	/// Execute an OS command
	/// @param cmdarray command array
	/// @return exit value
	public static int execute(final String... cmdarray) {
		if (Option.verbose) {
			String line = "";
			for (int i = 0; i < cmdarray.length; i++)
				line = line + " " + cmdarray[i];
//			IO.println("Util.execute: " + line);
			Util.println("Execute: " + line);
		}
		ProcessBuilder processBuilder = new ProcessBuilder(cmdarray);
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();		
			InputStream output = process.getInputStream();  // Process' output
//			if (Global.console != null) {
//				while (process.isAlive()) {
//					while (output.available() > 0) {
//						Global.console.write("" + (char) output.read());
//					}
//				}
//			} else {
				while (process.isAlive()) {
					while (output.available() > 0) {
						System.out.append((char) output.read());
					}
				}
//			}
			return (process.exitValue());

		} catch(Exception e) {
			throw new RuntimeException("Process Execution failed: " + cmdarray[0], e);
		}
	}
  
	/// Build invoke Simula Runtime Error.
	/// @param mss the error message.
	/// @param codeBuilder the codeBuilder to use.
	public static void buildSimulaRuntimeError(String mss,CodeBuilder codeBuilder) {
		ConstantPoolBuilder pool=codeBuilder.constantPool();
		ClassDesc CD = ClassDesc.of("simula.runtime.RTS_SimulaRuntimeError");
		codeBuilder
			.new_(CD)
			.dup()
			.ldc(pool.stringEntry(mss))
			.invokespecial(CD, "<init>", MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)V"))
			.athrow();		
	}

	/// Build line number method call.
	/// @param codeBuilder the codeBuilder to use.
	/// @param lineNumber the line number
	public static void buildLineNumber(CodeBuilder codeBuilder, int lineNumber) {
		if(lineNumber > 0) codeBuilder.lineNumber(lineNumber);
	}
  
}
