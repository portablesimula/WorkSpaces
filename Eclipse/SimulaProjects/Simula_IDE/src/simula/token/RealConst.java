package simula.token;

import javax.swing.text.Style;
import simula.compiler.utilities.KeyWord;
import simula.editor.PsiTextPanel;
import simula.psi.LexToken;

public class RealConst extends LexToken {
	public final float value;

	public RealConst(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, float value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.REALKONST);
		this.value = value;
	}

	/// Returns the style for this PsiElement.
	/// @return the style for this PsiElement
	@Override
	public Style getStyle(final PsiTextPanel psiText) {
		return psiText.styleConstant;
	}

}
