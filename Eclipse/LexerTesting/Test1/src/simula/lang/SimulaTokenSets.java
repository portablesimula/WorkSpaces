package simula.lang;

import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

public interface SimulaTokenSets {
//    // Standard whitespace and comment tokens
//    TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
////   TokenSet COMMENTS = TokenSet.create(SimpleTypes.COMMENT);
//
//    // Tokens that represent string literals
//    TokenSet STRING_LITERALS = TokenSet.create(SimpleTypes.VALUE);
	
//	private void test() {
//		TokenSet whiteSpace = TokenSet.WHITE_SPACE;
//	}

//	IElementType[] types = {
//            SimulaTypes.COMMENT,      // Single-line comment
//            SimulaTypes.BLOCK_COMMENT // Multi-line comment
//			};

//    public static final TokenSet COMMENTS = TokenSet.create(types);

    public static final TokenSet COMMENTS = TokenSet.create(
            SimulaTypes.COMMENT,      // Single-line comment
            SimulaTypes.BLOCK_COMMENT // Multi-line comment
    );
    
 // Example: Creating a set of all whitespace tokens
//    public static final TokenSet COMN55 = TokenSet.create(new IElementType[] { TokenType.WHITE_SPACE, TokenType.BAD_CHARACTER });

}
