package simula.lang;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import simula.lexer.Identifier;
import simula.lexer.SimulaElementTypes;
import simula.lexer.KeyWordToken;

public class SimulaParser implements PsiParser {
    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        System.out.println("SimulaParser.parse was called");
//        if(true) throw new RuntimeException("SimulaParser.parse was called");

        System.out.println("SimulaParser.parse: root="+root);

        // 1. Begin the root marker for the entire file
        final PsiBuilder.Marker rootMarker = builder.mark();
        System.out.println("SimulaParser.parse: rootMarker="+rootMarker);
        System.out.println("SimulaParser.parse: builder.eof="+builder.eof());

        // 2. Iterate through the token stream
        while (!builder.eof()) {
//            IElementType tokenType = builder.getTokenType();
//            System.out.println("SimulaParser.parse: tokenType="+tokenType.getClass().getSimpleName() + "  " + tokenType);
//
//            // Example logic: if we see a KEY token, try to parse a full property
//            if (tokenType == SimpleTypes.KEY) {
//                parseProperty(builder);
//            } else {
//                // Skip unknown or whitespace tokens automatically handled by lexer
//                builder.advanceLexer();
//            }

            parseStatement(builder);
        }



        // 3. Close the root marker and return the tree
        rootMarker.done(root);
        return builder.getTreeBuilt();
    }

    private void parseStatement(PsiBuilder builder) {
        IElementType tokenType = builder.getTokenType();

        if (tokenType == KeyWordToken.BEGIN) {
            parseBlock(builder);
//        } else if (tokenType == SimulaElementTypes.IDENTIFIER) {
        } else if (tokenType instanceof Identifier) {
            parseAssignment(builder);
        } else {
            // Error handling or consuming unknown tokens
            builder.advanceLexer();
        }
    }

    private void parseBlock(PsiBuilder builder) {
        PsiBuilder.Marker blockMarker = builder.mark();
        builder.advanceLexer(); // consume BEGIN

        while (!builder.eof() && builder.getTokenType() != KeyWordToken.END) {
            parseStatement(builder);
        }

        if (builder.getTokenType() == KeyWordToken.END) {
            builder.advanceLexer(); // consume END
            blockMarker.done(SimulaElementTypes.BLOCK_ELEMENT);
        } else {
            blockMarker.error("Expected 'END'");
        }
    }

    private void parseAssignment(PsiBuilder builder) {
        PsiBuilder.Marker assignMarker = builder.mark();
        builder.advanceLexer(); // consume identifier

        if (builder.getTokenType() == KeyWordToken.ASSIGNVALUE) {
            builder.advanceLexer();
            // In a real parser, you'd call parseExpression(builder) here
            consumeUntilSemicolon(builder);
            assignMarker.done(SimulaElementTypes.ASSIGNMENT_STATEMENT);
        } else {
            assignMarker.drop(); // Not an assignment, backtrack or handle error
        }
    }

    private void consumeUntilSemicolon(PsiBuilder builder) {
        while (!builder.eof() && builder.getTokenType() != KeyWordToken.SEMICOLON) {
            builder.advanceLexer();
        }
        if (builder.getTokenType() == KeyWordToken.SEMICOLON) {
            builder.advanceLexer();
        }
    }

//    private void parseProperty(PsiBuilder builder) {
//        if(true) throw new RuntimeException("SimulaParser.parseProperty was called");
//        // Start a marker for a specific PSI element (e.g., Property)
//        PsiBuilder.Marker propertyMarker = builder.mark();
//
//        builder.advanceLexer(); // Consume KEY
//
//        if (builder.getTokenType() == SimpleTypes.SEPARATOR) {
//            builder.advanceLexer(); // Consume SEPARATOR (=)
//
//            if (builder.getTokenType() == SimpleTypes.VALUE) {
//                builder.advanceLexer(); // Consume VALUE
//            } else {
//                builder.error("Value expected");
//            }
//        } else {
//            builder.error("Separator expected");
//        }
//
//        // Finalize the marker with the specific IElementType for this node
//        propertyMarker.done(SimpleTypes.PROPERTY);
//    }

}
