package simula.lexer;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import simula.lang.SimulaLanguage;

public class TextConst extends IElementType {
	@NonNls @NotNull public final String value;

	public TextConst(@NonNls @NotNull String debugName, @NonNls @NotNull String value) {
		super(debugName, SimulaLanguage.INSTANCE);
		this.value = value;
	}

}
