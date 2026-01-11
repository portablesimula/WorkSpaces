package simula.compiler.syntaxClass;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Declaration extends SyntaxClass {
    public Declaration(@NotNull String debugName, @Nullable Language language) {
        super(debugName, language);
    }
}
