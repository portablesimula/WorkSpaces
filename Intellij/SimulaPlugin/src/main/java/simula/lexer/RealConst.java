package simula.lexer;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import simula.lang.SimulaLanguage;

public class RealConst extends IElementType {
	@NonNls @NotNull public final double value;

	public RealConst(@NonNls @NotNull String debugName, @NonNls @NotNull double value) {
		super(debugName, SimulaLanguage.INSTANCE);
		this.value = value;
	}

}
