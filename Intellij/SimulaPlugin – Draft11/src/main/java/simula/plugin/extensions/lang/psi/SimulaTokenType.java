package simula.plugin.extensions.lang.psi;

import com.intellij.psi.tree.IElementType;
import simula.plugin.extensions.lang.SimulaLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class SimulaTokenType extends IElementType {

    public SimulaTokenType(@NotNull @NonNls String debugName) {
        super(debugName, SimulaLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "SimulaTokenType." + super.toString();
    }

}