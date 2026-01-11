package simula.lexer;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import simula.lang.SimulaLanguage;

public class IntegerConst extends IElementType {
	@NonNls @NotNull public final long value;

	public IntegerConst(@NonNls @NotNull String debugName, @NonNls @NotNull long value) {
		super(debugName, SimulaLanguage.INSTANCE);
		this.value = value;
	}

}
