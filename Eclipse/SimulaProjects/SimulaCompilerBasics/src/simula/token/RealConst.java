package simula.token;

import simula.compiler.utilities.KeyWord;
import simula.lsp.compiler.TokenManager;
import simula.psi.LexToken;

public class RealConst extends LexToken {
	public final float value;

	public RealConst(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, float value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.REALKONST);
		this.value = value;
	}

//	/// Returns the style for this PsiElement.
//	/// @return the style for this PsiElement
//	@Override
//	public Style getStyle(final PsiTextPanel psiText) {
//		return psiText.styleConstant;
//	}
	
	public int getLspTokenType() {
		return TokenManager.NUMBER.index; 
	}

}
