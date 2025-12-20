package simula.plugin.extensions.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import simula.plugin.extensions.highlighterExtension.lexer.SimulaIcons;
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
        return "Simula File";
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
