package simula.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import simula.lexer.SimulaIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class SimulaFileType extends LanguageFileType {

    public static final SimulaFileType INSTANCE = new SimulaFileType();

    private SimulaFileType() {
        super(SimulaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Simula";  // This must match the name in plugin.xml: <fileType>
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Simula language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "sim";
    }

    @Override
    public Icon getIcon() {
        return SimulaIcons.FILE;
    }

}
