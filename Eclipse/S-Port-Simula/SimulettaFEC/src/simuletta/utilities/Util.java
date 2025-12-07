/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.utilities;

import java.io.File;
import java.io.IOException;

import simuletta.compiler.Global;
import simuletta.compiler.parsing.MacroScanner;

/**
 * A set of all static Utility Methods
 * 
 * @author Øystein Myhre Andersen
 *
 */
public final class Util { 

	public static void createFile(File file) {
		try { file.getParentFile().mkdirs();
			  file.createNewFile();
		} catch (IOException e1) {
			ERROR("Unable to create file: "+file);
			e1.printStackTrace(); }
	}
	
	public static String edIndent(final int indent) {
		int i=indent; String s="";
		while((i--)>0) s=s+"    ";
		return(s);  
	}
	
	// ******   ERROR HANDLING   ******

	public static void WARNING(final String msg) {
		if (Option.WARNINGS) println(edLine("WARNING",msg));
	}
	
	public static void ERROR(final String msg) {
		Global.nError++; printError(edLine("Error",msg));
		println("STACK-TRACE");	Thread.dumpStack(); // TEMP
		FORCED_EXIT(); // TEMP
	}

	public static void IERR() { IERR(null,null);	}
	public static void IERR(final String msg) { IERR(msg,null);	}
	public static void IERR(final String msg,final Throwable e) {
		Global.nError++; printError(edLine("Internal Error",msg));
		if(e!=null)e.printStackTrace();
		FORCED_EXIT();
	}

	private static String edLine(String title,String msg) {
		String moduleId="";
		try { moduleId=Global.currentModule.identifier.toUpperCase() + ':';
		} catch(Exception e) {}
		return(moduleId+"Line "+(Global.sourceLineNumber-1)+" "+title+": "+msg);
	}

	public static void FATAL_ERROR(final String msg) {
		Global.nError++; printError(edLine("FATAL ERROR",msg));
		println("STACK-TRACE"); Thread.dumpStack();
		FORCED_EXIT();
	}

	public static void NOTIMPL(final String msg) {
		Global.nError++; printError(edLine("NOT IMPLEMENTED",msg));
		println("STACK-TRACE"); Thread.dumpStack(); FORCED_EXIT();
	}

	public static void STOP() {
		printError(edLine("STOP",null));
		println("STACK-TRACE"); Thread.dumpStack(); FORCED_EXIT();
	}

	public static void TRACE(final String msg) { TRACE("TRACE", msg); }
	public static void TRACE(final String id,final String msg) {
		if (Option.TRACING)
			println(id + " " + Global.sourceLineNumber + ": " + edMiniStack() + ": " + msg);
	}
	
	public static String edStack() { return(edStack(3,8)); }
	public static String edMiniStack() { return(edStack(4,7)); }
	
	public static String edStack(int lower,int upper) {
		Thread cur=Thread.currentThread();
		StackTraceElement[] elts=cur.getStackTrace();
		StringBuilder s=new StringBuilder();
		String sep=""; int n=0;
		for(StackTraceElement elt:elts) {
			if(n++ > lower && n < upper) {
				String className=elt.getClassName();
				int p=className.lastIndexOf('.');
				String classIdent=className.substring(p+1);
			s.append(sep).append(classIdent).append('.').append(elt.getMethodName());
			s.append('[').append(elt.getLineNumber()).append(']');
			sep=",";
			}
		}
		return(s.toString());
	}

	public static void TRACE_OUTPUT(final String msg) {
		if (Option.TRACE_ATTRIBUTE_OUTPUT)
			Util.println("ATTR OUTPUT: " + msg);
	}

	public static void TRACE_INPUT(final String msg) {
		if (Option.TRACE_ATTRIBUTE_INPUT)
			Util.println("ATTR INPUT: " + msg);
	}

	public static void NOT_IMPLEMENTED(final String s) {
		System.err.println("*** NOT IMPLEMENTED: " + s);
		BREAK("Press [ENTER] Continue or [Q] for a Stack-Trace");
		FORCED_EXIT();
	}

	public static void FORCED_EXIT() {
		IO.println("FORCED EXIT");	Thread.dumpStack();
		System.exit(-1);
	}

	public static void ASSERT(final boolean test, final String msg) {
		if (!test) {
			println(edLine("ASSERT(" + msg + ")","FAILED"));
			FORCED_EXIT();
		}
	}

	public static void BREAK(final String title) {
		BREAK("BREAK", title);
	}

	public static void BREAK(final String id, final String title) {
		if (Option.BREAKING) {
			try { IO.println(edLine(" "+id + " " + Global.sourceLineNumber + ": " +edMiniStack() + ": " + title + ": <",null));
				  char c=(char) System.in.read();
				  if (c == 'Q' || c == 'q') { println("STACK-TRACE");	Thread.dumpStack(); }
				  while (System.in.available() > 0) System.in.read();
			} catch (Exception e) {
				e.printStackTrace();
				//System.exit(-1);
				FORCED_EXIT();
			}
		}
	}

	public static void println(final String s) {
		IO.println(s);
	}  

	private static void printError(final String s) {
		System.err.println(s);
		if(MacroScanner.currentMacroScanner!=null)
			System.err.println("NOTE Error while expanding "+MacroScanner.currentMacroScanner.macroDefinition);
	}  

	
}
