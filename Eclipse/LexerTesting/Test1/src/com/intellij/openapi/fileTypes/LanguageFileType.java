package com.intellij.openapi.fileTypes;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.nio.charset.Charset;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class LanguageFileType implements FileType {
    private final Language myLanguage;
    private final boolean mySecondary;

    protected LanguageFileType(@NotNull Language language) {
        this(language, false);
    }

    protected LanguageFileType(@NotNull Language language, boolean secondary) {
        this.myLanguage = language;
        this.mySecondary = secondary;
    }

    public final @NotNull Language getLanguage() {
        return this.myLanguage;
    }

    public final boolean isBinary() {
        return false;
    }

    public boolean isSecondary() {
        return this.mySecondary;
    }

    /** @deprecated */
    @Deprecated
    public boolean isJVMDebuggingSupported() {
        return false;
    }

    /** @deprecated */
    @Deprecated
    public Charset extractCharsetFromFileContent(@Nullable Project project, @Nullable VirtualFile file, @NotNull String content) {
        return null;
    }

    public Charset extractCharsetFromFileContent(@Nullable Project project, @Nullable VirtualFile file, @NotNull CharSequence content) {
        return this.extractCharsetFromFileContent(project, file, content.toString());
    }

    public @Nls @NotNull String getDisplayName() {
        return this.myLanguage.getDisplayName();
    }
}
