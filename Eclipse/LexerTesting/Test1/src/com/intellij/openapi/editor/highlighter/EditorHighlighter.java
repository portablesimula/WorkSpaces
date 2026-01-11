package com.intellij.openapi.editor.highlighter;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.event.DocumentListener;
import org.jetbrains.annotations.NotNull;

public interface EditorHighlighter extends DocumentListener {
    @NotNull HighlighterIterator createIterator(int var1);

    default void setText(@NotNull CharSequence text) {
    }

    void setEditor(@NotNull HighlighterClient var1);

    default void setColorScheme(@NotNull EditorColorsScheme scheme) {
    }
}
