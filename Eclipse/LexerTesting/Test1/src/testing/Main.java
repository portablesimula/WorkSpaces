package testing;

import java.io.FileInputStream;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.util.CharTable;
import com.intellij.util.diff.FlyweightCapableTreeStructure;

import simula.compiler.utilities.Global;
import simula.lang.SimulaLanguage;
import simula.lexer.SimulaLexer;
import simula.parser.SimPsiBuilder;
import simula.parser.SimulaParser;
import simula.parser.SimulaParserDefinition;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lang.impl.PsiBuilderFactoryImpl;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.lang.impl.PsiBuilderImpl.MyTreeStructure;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;

public class Main {
	static CharSequence buffer;
	static int startOffset;
	static int endOffset;
	static int initialState;

	public static void main(String[] argv) {
		System.out.println("*** BEGIN main ");
		Global.sourceLineNumber = 1;
//		tester1();
//		try { tester2(); } catch (IOException e) { e.printStackTrace(); }
		tester3();
//		tester4();
	}
	
	static void tester1() {
		System.out.println("*** BEGIN Lexer tester 1 ");
		Lexer lexer = new SimulaLexer();
		LexerTester tester = new LexerTester(lexer);
//		buffer = "abra ca dab";
		
//		buffer = "begin\r\n"
//				+ "   outtext(\"Hello World!\");\r\n"
//				+ "--   outimage;\r\n"
//				+ "end; ";
		
//		buffer = "begin\r\n   outtext(\"Hello World!\");\r\n--   outimage;\r\nend; ";
		tester.doSetText(TEST_TEXT);
	}

//	public static String TEST_TEXT = "begin\r\n   iii := 444;\r\n--   outimage;\r\nend; ";
	public static String TEST_TEXT = "begin\r\n   iii := 444;\r\n--   outimage;\r\n   jjj := 555;\r\nend; ";
//	public static String TEST_TEXT = "begin\r\n   iii := 444;\r\n--   outimage;\r\n   outtext()\r\nend med en kommentar ! ;\r\nEtter final end";
//	public static String TEST_TEXT = "begin\r\n   outtext(\"Hello World!\");\r\n--   outimage;\r\nend; ";
//	public static String TEST_TEXT = "begin\r\n   outtext(\"Hello \"\r\ncomment Kommentar;\r\n\" World!\");\r\n--   outimage;\r\nend; ";

	
	static void tester2() throws IOException {
		System.out.println("*** BEGIN Lexer tester 2 ");
		Lexer lexer = new SimulaLexer();
		LexerTester tester = new LexerTester(lexer);
		String fileName = "C:/Users/omyhr/Simula/Simula-2.0/samples/HexDump.sim";
		FileInputStream file = new FileInputStream(fileName);
		byte[] bytes = file.readAllBytes();
		
//		 char data[] = {'a', 'b', 'c'};
		TEST_TEXT = new String(bytes);
		tester.doSetText(TEST_TEXT);
	}

	
	public static final IFileElementType FILE = new IFileElementType(SimulaLanguage.INSTANCE);

	static void tester3() {
		System.out.println("*** BEGIN tester 3 ");
		ParserDefinition parserDefinition = new SimulaParserDefinition();
		Lexer lexer = new SimulaLexer();
//		String text = "your code to parse";
//		String text = "begin\r\n   outtext(\"Hello World!\");\r\n--   outimage;\r\nend; ";

		// Use the factory to get an instance (which will be a PsiBuilderImpl)
//		PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(parserDefinition, lexer, text);
		PsiBuilderImpl builder = (PsiBuilderImpl) new PsiBuilderFactoryImpl().createBuilder(parserDefinition, lexer, TEST_TEXT);
		System.out.println("Main.tester3: builder="+builder);
		
//		SimulaParser parser = new SimulaParser(builder, TEST_TEXT);
		Global.initiate();
		SimulaParser parser = new SimulaParser();
		IElementType root = FILE;
	    ASTNode tree = parser.parse(root, builder);
//		ASTNode tree = builder.getTreeBuilt();
		System.out.println("Main.tester3: AST-tree: "+tree);
	    printAST(tree, 4);
	}

	static void tester4() {
		System.out.println("*** BEGIN tester 4 ");
		SimpleParsingTest test = new SimpleParsingTest();
		test.doSetup();
		ParserDefinition parserDefinition = new SimulaParserDefinition();
		test.configureFromParserDefinition(parserDefinition, "sim");
		String buffer = "begin\r\n   outtext(\"Hello World!\");\r\n--   outimage;\r\nend; ";
		test.testParsingTestData(buffer);
	}
	
	static void tester5() {
			System.out.println("*** BEGIN tester 5 ");
		Lexer lexer = new SimulaLexer();
		ParserDefinition parserDefinition = new SimulaParserDefinition();

		Project project = null;
		PsiFile containingFile = null;
		CharTable charTable = null;
		CharSequence text = null;
		ASTNode originalTree = null;
//		CharSequence lastCommittedText;
		MyTreeStructure parentLightTree = null;
//		Object parentCachingNode;
		
//		PsiBuilderImpl psiBuilder = new PsiBuilderImpl(project, containingFile, parserDefinition, lexer, charTable, text, originalTree, lastCommittedText, parentLightTree, parentCachingNode);
		PsiBuilderImpl psiBuilder = new PsiBuilderImpl(project, containingFile, parserDefinition, lexer, charTable, text, originalTree, parentLightTree);
		ASTNode tree = psiBuilder.getTreeBuilt();
	}
	
	public static void printAST(ASTNode node, int indent) {
	    // Print current node info
	    String indentation = " ".repeat(indent);
	    System.out.println(indentation + node.getElementType() + " (" + node.getTextRange() + ")" + node.getText().replace("\r", "\\r").replace("\n", "\\n"));
	    
	    // Recurse through children
	    ASTNode child = node.getFirstChildNode();
	    System.out.println("Main.printAST: child="+child);
	    while (child != null) {
	        printAST(child, indent + 2);
	        child = child.getTreeNext();
	    }
	}

	
}
