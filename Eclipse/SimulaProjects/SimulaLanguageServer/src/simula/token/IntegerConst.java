package simula.token;

import simula.compiler.utilities.KeyWord;
import simula.psi.LexToken;

public class IntegerConst extends LexToken {
	public final long value;

	public IntegerConst(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, long value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.INTEGERKONST);
		this.value = value;
	}

//	/// Returns the style for this PsiElement.
//	/// @return the style for this PsiElement
//	@Override
//	public Style getStyle(final PsiTextPanel psiText) {
//		return psiText.styleConstant;
//	}

}
