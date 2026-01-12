package simula.lang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import simula.lang.SimulaLanguage;

//import lang.SimulaLanguage;

//import com.intellij.extapi.psi.PsiFileBase;
//import com.intellij.openapi.fileTypes.FileType;
//import com.intellij.psi.FileViewProvider;
//import org.jetbrains.annotations.NotNull;
//import simula.plugin.extensions.lang.SimulaFileType;
//import simula.plugin.extensions.lang.SimulaLanguage;

public class SimulaLanguageFile extends PsiFileBase {
    public SimulaLanguageFile(FileViewProvider viewProvider) {
        // Pass your language instance and the viewProvider to the super constructor
        super(viewProvider, SimulaLanguage.INSTANCE);
    }

    public FileType getFileType() {
        return SimulaFileType.INSTANCE;
    }

    public String toString() {
        return "Simula Language File";
    }
}
