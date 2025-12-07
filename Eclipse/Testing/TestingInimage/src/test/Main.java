package test;

import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import test.jar.adHoc02;
import test.rts.RTS_RTObject;
import test.rts.RTS_UTIL;

public class Main {
	
	JPanel panel;

	public static void main(String[] args) {
//		testPrompt();
	
//        RTS_UTIL.BPRG("adHoc02", args);
//        RTS_UTIL.RUN_STM(new adHoc02(RTS_RTObject._CTX));

//		Util.listJarFile(new File("C:/SPORT/Test.jar"));
//		Util.listJarFile(new File("C:/users/omyhr/IdeaProjects/Samples/bin/Delfil.jar"));
		Util.listJarFile(new File("C:/GitHub/EclipseWorkSpaces/SimulaCompiler2/Simula/src/bat/bin/Delfil.jar"));
//		Util.doListClassFile("C:/SPORT/Test/module-info.class");
	}


	public static void testPrompt() {
		System.out.print("Enter text: ");
		int c;
		try {
			c = System.in.read();
			System.out.println("Got a CHAR: "+c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
