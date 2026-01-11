package com.intellij.openapi.editor.colors;

import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.options.FontSize;
import com.intellij.openapi.options.Scheme;
import com.intellij.openapi.options.SchemeMetaInfo;
import com.intellij.openapi.util.NlsSafe;
import java.awt.Color;
import java.awt.Font;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface EditorColorsScheme extends Cloneable, TextAttributesScheme, Scheme, SchemeMetaInfo {
    static @NonNls @NotNull String getDefaultSchemeName() {
        return EditorColorsManager.getDefaultSchemeName();
    }

    void setName(String var1);

    void setAttributes(@NotNull TextAttributesKey var1, TextAttributes var2);

    default TextAttributes getAttributes(@Nullable TextAttributesKey key, boolean useDefaults) {
        return this.getAttributes(key);
    }

    @NotNull Color getDefaultBackground();

    @NotNull Color getDefaultForeground();

    @Nullable Color getColor(ColorKey var1);

    void setColor(ColorKey var1, Color var2);

    @NotNull FontPreferences getFontPreferences();

    void setFontPreferences(@NotNull FontPreferences var1);

    @NlsSafe String getEditorFontName();

    void setEditorFontName(String var1);

    int getEditorFontSize();

    default float getEditorFontSize2D() {
        return (float)this.getEditorFontSize();
    }

    void setEditorFontSize(int var1);

    default void setEditorFontSize(float fontSize) {
        this.setEditorFontSize((int)((double)fontSize + (double)0.5F));
    }

    /** @deprecated */
    @Deprecated(
        forRemoval = true
    )
    default FontSize getQuickDocFontSize() {
        return FontSize.SMALL;
    }

    /** @deprecated */
    @Deprecated(
        forRemoval = true
    )
    default void setQuickDocFontSize(@NotNull FontSize fontSize) {
    }

    @NotNull Font getFont(EditorFontType var1);

    float getLineSpacing();

    void setLineSpacing(float var1);

    boolean isUseLigatures();

    void setUseLigatures(boolean var1);

    Object clone();

    @NotNull FontPreferences getConsoleFontPreferences();

    void setConsoleFontPreferences(@NotNull FontPreferences var1);

    default void setUseEditorFontPreferencesInConsole() {
    }

    default boolean isUseEditorFontPreferencesInConsole() {
        return false;
    }

    default void setUseAppFontPreferencesInEditor() {
    }

    default boolean isUseAppFontPreferencesInEditor() {
        return false;
    }

    @NlsSafe String getConsoleFontName();

    void setConsoleFontName(String var1);

    int getConsoleFontSize();

    default float getConsoleFontSize2D() {
        return (float)this.getConsoleFontSize();
    }

    void setConsoleFontSize(int var1);

    default void setConsoleFontSize(float fontSize) {
        this.setConsoleFontSize((int)((double)fontSize + (double)0.5F));
    }

    float getConsoleLineSpacing();

    void setConsoleLineSpacing(float var1);

    void readExternal(Element var1);

    @Internal
    boolean isReadOnly();
}
