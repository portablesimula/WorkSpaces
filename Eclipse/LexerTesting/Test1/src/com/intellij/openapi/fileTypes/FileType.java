package com.intellij.openapi.fileTypes;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.NlsContexts.Label;
import com.intellij.openapi.vfs.VirtualFile;
import javax.swing.Icon;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileType {
    FileType[] EMPTY_ARRAY = new FileType[0];

    @NonNls @NotNull String getName();

    default @NotNull @Nls String getDisplayName() {
        return this.getName();
    }

    @Label @NotNull String getDescription();

    @NlsSafe @NotNull String getDefaultExtension();

    Icon getIcon();

    boolean isBinary();

    default boolean isReadOnly() {
        return false;
    }

    default @NonNls @Nullable String getCharset(@NotNull VirtualFile file, byte @NotNull [] content) {
        if (content == null) {
            //$$$reportNull$$$0(2);
        }

        return null;
    }
}
