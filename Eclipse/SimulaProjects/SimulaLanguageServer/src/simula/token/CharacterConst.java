package simula.token;

import simula.compiler.utilities.KeyWord;
import simula.lsp.compiler.TokenManager;
import simula.psi.LexToken;

public class CharacterConst extends LexToken {
	public final Character value;

	public CharacterConst(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.CHARACTERKONST);
		this.value = Character.valueOf((char) value);
	}

//	/// Returns the style for this PsiElement.
//	/// @return the style for this PsiElement
//	@Override
//	public Style getStyle(final PsiTextPanel psiText) {
//		return psiText.styleConstant;
//	}

	
	public int getLspTokenType() {
		return TokenManager.STRING.index; 
	}

}
