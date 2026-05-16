package simula.token;

import javax.swing.text.Style;
import simula.compiler.utilities.KeyWord;
import simula.editor.PsiTextPanel;
import simula.psi.LexToken;

public class LongRealConst extends LexToken {
	public final double value;

	public LongRealConst(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, double value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.LONGREALKONST);
		this.value = value;
	}

	/// Returns the style for this PsiElement.
	/// @return the style for this PsiElement
	@Override
	public Style getStyle(final PsiTextPanel psiText) {
		return psiText.styleConstant;
	}

}
