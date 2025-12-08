package simula.plugin.extensions.highlighterExtension.highlighter;

// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import simula.plugin.extensions.highlighterExtension.lexer.SimulaElementTypes;
import simula.plugin.extensions.highlighterExtension.lexer.SimulaLexer;
import simula.plugin.extensions.lang.psi.SimulaTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SimulaSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey SEPARATOR =
            createTextAttributesKey("SIMPLE_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey KEY =
            createTextAttributesKey("SIMPLE_KEY", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey VALUE =
            createTextAttributesKey("SIMPLE_VALUE", DefaultLanguageHighlighterColors.STRING);
//    public static final TextAttributesKey COMMENT =
//            createTextAttributesKey("SIMPLE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("SIMPLE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

//    IElementType NUMBER = new IElementType("NUMBER", Language.ANY);
//    IElementType TEGN = new IElementType("TEGN", Language.ANY);
//    IElementType KEYWORD = new IElementType("KEYWORD", Language.ANY);
//    IElementType COMMENT = new IElementType("COMMENT", Language.ANY);
//    IElementType IDENTIFIER = new IElementType("IDENTIFIER", Language.ANY);
//    IElementType TEXTCONST = new IElementType("TEXTCONST", Language.ANY);
//    IElementType STRING = new IElementType("STRING", Language.ANY);

    public static final TextAttributesKey NUMBER =
            createTextAttributesKey("SIMULA_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey TEGN =
            createTextAttributesKey("SIMULA_TEGN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("SIMULA_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("SIMULA_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey IDENTIFIER =
            createTextAttributesKey("SIMULA_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey TEXTCONST =
            createTextAttributesKey("SIMULA_TEXTCONST", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey STRING =
            createTextAttributesKey("SIMULA_STRING", DefaultLanguageHighlighterColors.STRING);


    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] SEPARATOR_KEYS = new TextAttributesKey[]{SEPARATOR};
    private static final TextAttributesKey[] KEY_KEYS = new TextAttributesKey[]{KEY};
    private static final TextAttributesKey[] VALUE_KEYS = new TextAttributesKey[]{VALUE};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEY};

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new SimulaLexer();
//        return new SimpleManualLexer();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(SimulaTypes.SEPARATOR)) {
            return SEPARATOR_KEYS;
        }
        if (tokenType.equals(SimulaElementTypes.KEYWORD)) {
            return KEY_KEYS;
        }
        if (tokenType.equals(SimulaTypes.VALUE)) {
            return VALUE_KEYS;
        }
        if (tokenType.equals(SimulaElementTypes.COMMENT)) {
            return COMMENT_KEYS;
        }
        if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        }
        return EMPTY_KEYS;
    }

}