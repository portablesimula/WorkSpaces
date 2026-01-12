package simula.lang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class SimulaLanguageFile extends PsiFileBase {
    public SimulaLanguageFile(@NotNull FileViewProvider viewProvider) {
        // Pass your language instance and the viewProvider to the super constructor
        super(viewProvider, SimulaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return SimulaFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Simula Language File";

    }
}
