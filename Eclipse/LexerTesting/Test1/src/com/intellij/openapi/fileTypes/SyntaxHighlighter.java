package com.intellij.openapi.fileTypes;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.KeyedFactoryEPBean;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface SyntaxHighlighter {
    @Internal
    ExtensionPointName<KeyedFactoryEPBean> EP_NAME = ExtensionPointName.create("com.intellij.syntaxHighlighter");
    /** @deprecated */
    @Deprecated(
        forRemoval = true
    )
    SyntaxHighlighterProvider PROVIDER = (SyntaxHighlighterProvider)(new FileTypeExtensionFactory(SyntaxHighlighterProvider.class, EP_NAME)).get();

    @NotNull Lexer getHighlightingLexer();

    TextAttributesKey @NotNull [] getTokenHighlights(IElementType var1);
}
