package simula.lang;

import com.intellij.psi.tree.TokenSet;

public interface SimulaTokenSets {
//    // Standard whitespace and comment tokens
//    TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
////   TokenSet COMMENTS = TokenSet.create(SimpleTypes.COMMENT);
//
//    // Tokens that represent string literals
//    TokenSet STRING_LITERALS = TokenSet.create(SimpleTypes.VALUE);

    public static final TokenSet COMMENTS = TokenSet.create(
            SimulaTypes.COMMENT,      // Single-line comment
            SimulaTypes.BLOCK_COMMENT // Multi-line comment
    );
}
