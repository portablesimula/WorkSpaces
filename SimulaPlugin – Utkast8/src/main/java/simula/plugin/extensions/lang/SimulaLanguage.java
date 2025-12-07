package simula.plugin.extensions.lang;

import com.intellij.lang.Language;

public class SimulaLanguage extends Language {

    public static final SimulaLanguage INSTANCE = new SimulaLanguage();

    public SimulaLanguage() {
        super("Simula");
    }

}
