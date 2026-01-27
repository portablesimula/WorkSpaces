package simula.parser;

import simula.editor.PsiBuilder;
import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.syntaxClass.statement.Statement;
import simula.compiler.utilities.Util;

//import lang.SyntaxClass;

//import com.intellij.lang.ASTNode;
//import simula.editor.PsiBuilder;
//import com.intellij.lang.PsiParser;
//import com.intellij.psi.tree.SyntaxClass;
//import org.jetbrains.annotations.NotNull;
//import simula.plugin.extensions.lexer.Identifier;
//import simula.plugin.extensions.lexer.SimulaElementTypes;
//import simula.plugin.extensions.lexer.KeyWord;

public class SimulaParser {// implements PsiParser {
//	SimPsiBuilder simBuilder;
//	String text;
//	
//	public SimulaParser(@NotNull PsiBuilder psiBuilder, String text) {
//		this.text = text;
//    	simBuilder = new  SimPsiBuilder(psiBuilder, text);
//	}
	
//    public ASTNode parse(SyntaxClass root, PsiBuilder psiBuilder) {
    public SyntaxClass parse(SyntaxClass root, PsiBuilder psiBuilder) {
        System.out.println("\nSimulaParser.parse was called +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("SimulaParser.parse: root="+root);
//        psiBuilder.setDebugMode(true);

        // 1. Begin the root marker for the entire file
//        System.out.println("SimulaParser.parse: rootMarker="+rootMarker);
//        System.out.println("SimulaParser.parse: psiBuilder.eof="+psiBuilder.eof());
//        Util.IERR();

        // 2. Iterate through the token stream
//        while (!psiBuilder.eof()) {
        if (!psiBuilder.eof()) {
            System.out.println("\nSimulaParser.parse: NEW ProgramModule(psiBuilder)");
            ProgramModule program = new ProgramModule(psiBuilder);
            psiBuilder.psiRoot.printTree("SimulaParser.parse: END Program");
            return program;
        }

//        if (!psiBuilder.eof()) {
//            final PsiMarker afterEndMarker = psiBuilder.mark();
//        	while (!psiBuilder.eof()) {
//    	        psiBuilder.advanceLexer();
//        	}
//        	afterEndMarker.done(SimulaElementTypes.TEXT_AFTER_FINAL_END);
//        }

        // 3. Close the root marker and return the tree
//        System.out.println("\nSimulaParser.parse: Close the root marker: --------------------------------------------------");
//        rootMarker.done(root);
        
        
//        System.out.println("\nSimulaParser.parse: Return the tree: --------------------------------------------------");
//        return psiBuilder.getTreeBuilt();
//        psiBuilder.printPSI("End Parse");
        return null;
    }


//    private void parseStatement(PsiBuilder psiBuilder) {
//        SyntaxClass tokenType = psiBuilder.getTokenType();
//        System.out.println("SimulaParser.parseStatement: tokenType="+tokenType.getClass().getSimpleName()+" "+tokenType+" "+psiBuilder.getTokenText());
//
//        if (tokenType == KeyWord.BEGIN) {
//            System.out.println("SimulaParser.parseStatement: BEGIN ==> parseBlock");
//            parseBlock(psiBuilder);
////        } else if (tokenType == SimulaElementTypes.IDENTIFIER) {
//        } else if (tokenType instanceof Identifier) {
//            parseAssignment(psiBuilder);
//        } else {
//            // Error handling or consuming unknown tokens
//            psiBuilder.advanceLexer();
//        }
//    }
//
//    private void parseBlock(PsiBuilder psiBuilder) {
//        PsiMarker blockMarker = psiBuilder.mark();
//        psiBuilder.advanceLexer(); // consume BEGIN
//
//        while (!psiBuilder.eof() && psiBuilder.getTokenType() != KeyWord.END) {
//            System.out.println("SimulaParser.parseBlock: NOT END ==> parseStatement");
//            parseStatement(psiBuilder);
//        }
//
//        if (psiBuilder.getTokenType() == KeyWord.END) {
//            psiBuilder.advanceLexer(); // consume END
//            blockMarker.done(SimulaElementTypes.BLOCK_ELEMENT);
//        } else {
//            blockMarker.error("Expected 'END'");
//        }
//    }
//
//    private void parseAssignment(PsiBuilder psiBuilder) {
//        PsiMarker assignMarker = psiBuilder.mark();
//        psiBuilder.advanceLexer(); // consume identifier
//
//        if (psiBuilder.getTokenType() == KeyWord.ASSIGNVALUE) {
//            psiBuilder.advanceLexer();
//            // In a real parser, you'd call parseExpression(psiBuilder) here
//            consumeUntilSemicolon(psiBuilder);
//            assignMarker.done(SimulaElementTypes.ASSIGNMENT_STATEMENT);
//        } else {
//            assignMarker.drop(); // Not an assignment, backtrack or handle error
//        }
//    }
//
//    public static void consumeUntilSemicolon(PsiBuilder psiBuilder) {
//        while (!psiBuilder.eof() && psiBuilder.getTokenType() != KeyWord.SEMICOLON) {
//            psiBuilder.advanceLexer();
//        }
//        if (psiBuilder.getTokenType() == KeyWord.SEMICOLON) {
//            psiBuilder.advanceLexer();
//        }
//    }

//    private void parseProperty(PsiBuilder psiBuilder) {
//        if(true) throw new RuntimeException("SimulaParser.parseProperty was called");
//        // Start a marker for a specific PSI element (e.g., Property)
//        PsiMarker propertyMarker = psiBuilder.mark();
//
//        psiBuilder.advanceLexer(); // Consume KEY
//
//        if (psiBuilder.getTokenType() == SimpleTypes.SEPARATOR) {
//            psiBuilder.advanceLexer(); // Consume SEPARATOR (=)
//
//            if (psiBuilder.getTokenType() == SimpleTypes.VALUE) {
//                psiBuilder.advanceLexer(); // Consume VALUE
//            } else {
//                psiBuilder.error("Value expected");
//            }
//        } else {
//            psiBuilder.error("Separator expected");
//        }
//
//        // Finalize the marker with the specific SyntaxClass for this node
//        propertyMarker.done(SimpleTypes.PROPERTY);
//    }

}
