package simula.compiler.syntaxClass;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SyntaxClass extends IElementType {
    public SyntaxClass(@NonNls @NotNull String debugName, @Nullable Language language) {
        super(debugName, language);
    }
}
