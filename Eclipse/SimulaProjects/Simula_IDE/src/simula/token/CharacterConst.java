package simula.token;

import javax.swing.text.Style;
import simula.compiler.utilities.KeyWord;
import simula.editor.PsiTextPanel;
import simula.psi.LexToken;

public class CharacterConst extends LexToken {
	public final Character value;

	public CharacterConst(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.CHARACTERKONST);
		this.value = Character.valueOf((char) value);
	}

	/// Returns the style for this PsiElement.
	/// @return the style for this PsiElement
	@Override
	public Style getStyle(final PsiTextPanel psiText) {
		return psiText.styleConstant;
	}

}
