package simula.parser;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import simula.compiler.syntaxClass.statement.Statement;

//import lang.IElementType;

//import com.intellij.lang.ASTNode;
//import com.intellij.lang.PsiBuilder;
//import com.intellij.lang.PsiParser;
//import com.intellij.psi.tree.IElementType;
//import org.jetbrains.annotations.NotNull;
//import simula.plugin.extensions.lexer.Identifier;
//import simula.plugin.extensions.lexer.SimulaElementTypes;
//import simula.plugin.extensions.lexer.KeyWord;

public class SimulaParser implements PsiParser {
	SimPsiBuilder simBuilder;
//	String text;
	
	public SimulaParser(@NotNull PsiBuilder psiBuilder, String text) {
//		this.text = text;
    	simBuilder = new  SimPsiBuilder(psiBuilder, text);
	}
	
    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder psiBuilder) {
        System.out.println("SimulaParser.parse was called");
//        if(true) throw new RuntimeException("SimulaParser.parse was called");

        System.out.println("SimulaParser.parse: root="+root);

        // 1. Begin the root marker for the entire file
        final PsiBuilder.Marker rootMarker = psiBuilder.mark();
        System.out.println("SimulaParser.parse: rootMarker="+rootMarker);
        System.out.println("SimulaParser.parse: psiBuilder.eof="+psiBuilder.eof());

        // 2. Iterate through the token stream
        while (!psiBuilder.eof()) {
//            IElementType tokenType = psiBuilder.getTokenType();
//            System.out.println("SimulaParser.parse: tokenType="+tokenType.getClass().getSimpleName() + "  " + tokenType);
//
//            // Example logic: if we see a KEY token, try to parse a full property
//            if (tokenType == SimpleTypes.KEY) {
//                parseProperty(psiBuilder);
//            } else {
//                // Skip unknown or whitespace tokens automatically handled by lexer
//                psiBuilder.advanceLexer();
//            }

            System.out.println("SimulaParser.parse: CALL parseStatement(psiBuilder)");
//            Statement.parseStatement(psiBuilder);
            Statement.parseStatement(simBuilder);
        }



        // 3. Close the root marker and return the tree
        System.out.println("SimulaParser.parse: Close the root marker and return the tree: ");
        rootMarker.done(root);
        return psiBuilder.getTreeBuilt();
    }

//    private void parseStatement(PsiBuilder psiBuilder) {
//        IElementType tokenType = psiBuilder.getTokenType();
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
//        PsiBuilder.Marker blockMarker = psiBuilder.mark();
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
//        PsiBuilder.Marker assignMarker = psiBuilder.mark();
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
//        PsiBuilder.Marker propertyMarker = psiBuilder.mark();
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
//        // Finalize the marker with the specific IElementType for this node
//        propertyMarker.done(SimpleTypes.PROPERTY);
//    }

}
