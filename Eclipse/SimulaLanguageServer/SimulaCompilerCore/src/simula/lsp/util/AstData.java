package simula.lsp.util;

import simula.compiler.utilities.Util;

public class AstData {
	String string;
	int firstLineNumber;
	int lastLineNumber;
	
	public AstData(String string, int firstLineNumber, int lastLineNumber) {
		// TODO Auto-generated constructor stub
//		Util.IERR("DENNE MÅ SKRIVES");
		this.string = string;
		this.firstLineNumber = firstLineNumber;
		this.lastLineNumber = lastLineNumber;
	}

}
