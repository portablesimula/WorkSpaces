package simula.plugin.extensions.highlighterExtension.lexer;

import com.intellij.psi.tree.IElementType;
import simula.plugin.extensions.lang.SimulaLanguage;

public interface SimulaElementTypes {
	IElementType NUMBER = new IElementType("NUMBER", SimulaLanguage.INSTANCE);
	IElementType TEGN = new IElementType("TEGN", SimulaLanguage.INSTANCE);
	IElementType KEYWORD = new IElementType("KEYWORD", SimulaLanguage.INSTANCE);
	IElementType COMMENT = new IElementType("COMMENT", SimulaLanguage.INSTANCE);
	IElementType IDENTIFIER = new IElementType("IDENTIFIER", SimulaLanguage.INSTANCE);
	IElementType TEXTCONST = new IElementType("TEXTCONST", SimulaLanguage.INSTANCE);
	IElementType STRING = new IElementType("STRING", SimulaLanguage.INSTANCE);


}
