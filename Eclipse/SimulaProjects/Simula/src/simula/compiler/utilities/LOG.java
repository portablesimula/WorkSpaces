package simula.compiler.utilities;

import simula.compiler.syntaxClass.SyntaxClass;

public class LOG {

	public static void assertTrue(boolean b) {
		if(!b) System.out.println("LOG REPORTS(1) ASSERTION FAILED: ");
	}

	public static void assertTrue(boolean b, SyntaxClass contentElementType) {
		if(!b) System.out.println("LOG REPORTS(2) ASSERTION FAILED: ");
	}
	
	public static void println(String msg) {
		System.out.println(msg);
	}
	
	public static void error(String msg) {
		System.out.println("LOG REPORTS(1) ERROR: "+msg);
		Thread.dumpStack();
	}

	public static void error(String msg, Throwable e) {
		System.out.println("LOG REPORTS(2) ERROR: "+msg);
	}

//	public static void aerror(String msg, Attachment e) {
//		System.out.println("LOG REPORTS(3) ERROR: "+msg);
//	}
//
//	public static void error(String msg, PluginException byClass, Attachment attachment) {
//		System.out.println("LOG REPORTS(4) ERROR: "+msg);
//	}


	public static void warn(Throwable e) {
		System.out.println("LOG REPORTS(1) WARNING: "+e);
	}


	public static void warn(String msg, Throwable runtimeException) {
		System.out.println("LOG REPORTS(2) WARNING: "+msg);
	}


	public static boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public static void assertTrue(boolean b, String string) {
		// TODO Auto-generated method stub
		
	}


}
