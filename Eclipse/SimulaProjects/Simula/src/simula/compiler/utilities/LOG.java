package simula.compiler.utilities;

import simula.compiler.syntaxClass.SyntaxElement;

public class LOG {

	public static void assertTrue(boolean b) {
		if(!b) IO.println("LOG REPORTS(1) ASSERTION FAILED: ");
	}

	public static void assertTrue(boolean b, SyntaxElement contentElementType) {
		if(!b) IO.println("LOG REPORTS(2) ASSERTION FAILED: ");
	}
	
	public static void println(String msg) {
		IO.println(msg);
	}
	
	public static void error(String msg) {
		IO.println("LOG REPORTS(1) ERROR: "+msg);
		Thread.dumpStack();
	}

	public static void error(String msg, Throwable e) {
		IO.println("LOG REPORTS(2) ERROR: "+msg);
	}

//	public static void aerror(String msg, Attachment e) {
//		IO.println("LOG REPORTS(3) ERROR: "+msg);
//	}
//
//	public static void error(String msg, PluginException byClass, Attachment attachment) {
//		IO.println("LOG REPORTS(4) ERROR: "+msg);
//	}


	public static void warn(Throwable e) {
		IO.println("LOG REPORTS(1) WARNING: "+e);
	}


	public static void warn(String msg, Throwable runtimeException) {
		IO.println("LOG REPORTS(2) WARNING: "+msg);
	}


	public static boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public static void assertTrue(boolean b, String string) {
		// TODO Auto-generated method stub
		
	}


}
