package simula.lexer;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import simula.lang.SimulaLanguage;

public class CharacterConst extends IElementType {
	@NonNls @NotNull public final int value;

	public CharacterConst(@NonNls @NotNull String debugName, @NonNls @NotNull int value) {
		super(debugName, SimulaLanguage.INSTANCE);
		this.value = value;
	}

}
