package simula.plugin.extensions.lang.psi;

// This is a generated file. Not intended for manual editing.

import com.intellij.psi.tree.IElementType;
//import com.simula.lang.psi.impl.SimulaPropertyImpl;

public interface SimulaTypes {

//    IElementType PROPERTY = new SimulaElementType("PROPERTY");

    IElementType COMMENT = new SimulaTokenType("COMMENT");
    IElementType WHITE_SPACES = new SimulaTokenType("WHITE_SPACES");
    IElementType STRING_LITERAL = new SimulaTokenType("STRING_LITERAL");
    IElementType CRLF = new SimulaTokenType("CRLF");
    IElementType KEY = new SimulaTokenType("KEY");
    IElementType SEPARATOR = new SimulaTokenType("SEPARATOR");
    IElementType VALUE = new SimulaTokenType("VALUE");

//    class Factory {
//        public static PsiElement createElement(ASTNode node) {
//            IElementType type = node.getElementType();
//            if (type == PROPERTY) {
//                return new SimulaPropertyImpl(node);
//            }
//            throw new AssertionError("Unknown element type: " + type);
//        }
//    }
}
