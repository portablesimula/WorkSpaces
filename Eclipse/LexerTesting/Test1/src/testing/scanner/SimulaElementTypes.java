package testing.scanner;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;

public interface SimulaElementTypes {
	IElementType NUMBER = new IElementType("NUMBER", Language.ANY);
	IElementType TEGN = new IElementType("TEGN", Language.ANY);
	IElementType KEYWORD = new IElementType("KEYWORD", Language.ANY);
	IElementType COMMENT = new IElementType("COMMENT", Language.ANY);
	IElementType IDENTIFIER = new IElementType("IDENTIFIER", Language.ANY);
	IElementType TEXTCONST = new IElementType("TEXTCONST", Language.ANY);
	IElementType STRING = new IElementType("STRING", Language.ANY);


}
