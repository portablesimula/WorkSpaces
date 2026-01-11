package simula.lexer;

import com.intellij.psi.tree.IElementType;
import simula.lang.SimulaLanguage;

public interface SimulaElementTypes {
//	IElementType NUMBER = new IElementType("NUMBER", SimulaLanguage.INSTANCE);
	IElementType TEGN = new IElementType("TEGN", SimulaLanguage.INSTANCE);
//	IElementType KEYWORD = new IElementType("KEYWORD", SimulaLanguage.INSTANCE);
//	IElementType COMMENT = new IElementType("COMMENT", SimulaLanguage.INSTANCE);
//	IElementType IDENTIFIER = new IElementType("IDENTIFIER", SimulaLanguage.INSTANCE);
//	IElementType TEXTCONST = new IElementType("TEXTCONST", SimulaLanguage.INSTANCE);
	IElementType STRING = new IElementType("STRING", SimulaLanguage.INSTANCE);

//	public static final IElementType BEGIN = new IElementType("BEGIN", SimulaLanguage.INSTANCE);
//	public static final IElementType END = new IElementType("END", SimulaLanguage.INSTANCE);
//	public static final IElementType IDENTIFIER = new IElementType("BEGIN", SimulaLanguage.INSTANCE);

	public static final IElementType BLOCK_ELEMENT = new IElementType("BLOCK_ELEMENT", SimulaLanguage.INSTANCE);
	public static final IElementType ASSIGNMENT_STATEMENT = new IElementType("ASSIGNMENT_STATEMENT", SimulaLanguage.INSTANCE);

//	public static final IElementType ASSIGN_OP = new IElementType("ASSIGN_OP", SimulaLanguage.INSTANCE);
//	public static final IElementType SEMICOLON = new IElementType("SEMICOLON", SimulaLanguage.INSTANCE);

}
