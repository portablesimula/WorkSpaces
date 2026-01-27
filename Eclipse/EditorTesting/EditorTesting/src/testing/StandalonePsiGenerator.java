package testing;

import simula.psi.LexToken;
import simula.psi.REMOVED_PsiElement;
import simula.psi.SimulaLexer;

public class StandalonePsiGenerator {
    public static void main(String[] args) {
        String code = "var x = 10 + 5";
//        SimpleLexer lexer = new SimpleLexer(code);
        SimulaLexer lexer = new SimulaLexer();
		CharSequence buffer = code;
		int startOffset = 0;
		int endOffset = buffer.length();
		int initialState = 0;
	    lexer.start(buffer, startOffset, endOffset, initialState);
        
        // Root of the tree
        ExpressionPsi root = new ExpressionPsi();
        
//        String token;
//        while ((token = lexer.nextToken()) != null) {
//            // In a real PSI, the parser decides which node class to instantiate
//            root.addChild(new IdentifierPsi(token));
//        }
        LexToken token;
        while ((token = lexer.nextToken()) != null) {
            // In a real PSI, the parser decides which node class to instantiate
            root.addChild(new IdentifierPsi(token));
        }

        // Print the tree structure
        printTree(root, 0);
    }

    private static void printTree(REMOVED_PsiElement element, int depth) {
        System.out.println("  ".repeat(depth) + element.getClass().getSimpleName() + ": [" + element.getText() + "]");
        for (REMOVED_PsiElement child : element.getChildren()) {
            printTree(child, depth + 1);
        }
    }
}
