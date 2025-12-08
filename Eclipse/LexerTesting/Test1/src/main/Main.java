package main;

import java.io.FileInputStream;
import java.io.IOException;

import lang.IElementType;
import lang.lexer.Lexer;
import lang.scanner.SimpleManualLexer;
import lang.scanner.SimulaLexer;
import tester.Tester;

public class Main {
	static CharSequence buffer;
	static int startOffset;
	static int endOffset;
	static int initialState;

	public static void main(String[] argv) {
//		tester1();
//		tester2();
		try {
			tester3();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void tester1() {
		Lexer lexer = new SimpleManualLexer();
		buffer = "abra ca dab";
		endOffset = buffer.length();
		lexer.start(buffer, startOffset, endOffset, initialState);
		
		while(true) {
			IElementType type = lexer.getTokenType();
			int start = lexer.getTokenStart();
			int end = lexer.getTokenEnd();
			
			// NEW TOKEN
			
			lexer.advance();
			
		}
	}
	
	static void tester2() {
//		Lexer lexer = new SimpleManualLexer();
		Lexer lexer = new SimulaLexer();
		Tester tester = new Tester(lexer);
//		buffer = "abra ca dab";
		
//		buffer = "begin\r\n"
//				+ "   outtext(\"Hello World!\");\r\n"
//				+ "--   outimage;\r\n"
//				+ "end; ";
		
		buffer = "begin\r\n   outtext(\"Hello World!\");\r\n--   outimage;\r\nend; ";
		tester.doSetText(buffer);
	}

	
	static void tester3() throws IOException {
		Lexer lexer = new SimulaLexer();
		Tester tester = new Tester(lexer);
		String fileName = "C:/Users/omyhr/Simula/Simula-2.0/samples/HexDump.sim";
		FileInputStream file = new FileInputStream(fileName);
		byte[] bytes = file.readAllBytes();
		
//		 char data[] = {'a', 'b', 'c'};
	    buffer = new String(bytes);
		tester.doSetText(buffer);
	}

}
