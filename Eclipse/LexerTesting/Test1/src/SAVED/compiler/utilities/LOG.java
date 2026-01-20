package simula.compiler.utilities;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.intellij.diagnostic.PluginException;
import com.intellij.openapi.diagnostic.Attachment;
import com.intellij.psi.tree.IElementType;

public class LOG {

	public static void assertTrue(boolean b) {
		if(!b) System.out.println("LOG REPORTS(1) ASSERTION FAILED: ");
	}

	public static void assertTrue(boolean b, @NotNull IElementType contentElementType) {
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

	public static void aerror(String msg, Attachment e) {
		System.out.println("LOG REPORTS(3) ERROR: "+msg);
	}

	public static void error(String msg, @NotNull PluginException byClass, Attachment attachment) {
		System.out.println("LOG REPORTS(4) ERROR: "+msg);
	}


	public static void warn(Throwable e) {
		System.out.println("LOG REPORTS(1) WARNING: "+e);
	}


	public static void warn(String msg, RuntimeException runtimeException) {
		System.out.println("LOG REPORTS(2) WARNING: "+msg);
	}


	public static boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return true;
	}


}
