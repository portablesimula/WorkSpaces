package com.intellij.openapi.editor;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.util.text.CharArrayUtil;
import java.beans.PropertyChangeListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public interface Document extends UserDataHolder {
    Document[] EMPTY_ARRAY = new Document[0];
    @NonNls String PROP_WRITABLE = "writable";

    @Contract(
        pure = true
    )
    default @NotNull @NlsSafe String getText() {
        return this.getImmutableCharSequence().toString();
    }

    @Contract(
        pure = true
    )
    default @NotNull @NlsSafe String getText(@NotNull TextRange range) {
        return range.substring(this.getText());
    }

    @Contract(
        pure = true
    )
    default @NotNull @NlsSafe CharSequence getCharsSequence() {
        return this.getImmutableCharSequence();
    }

    @Contract(
        pure = true
    )
    @NotNull @NlsSafe CharSequence getImmutableCharSequence();

    /** @deprecated */
    @Deprecated
    @ScheduledForRemoval
    default char @NotNull [] getChars() {
        char[] var10000 = CharArrayUtil.fromSequence(this.getImmutableCharSequence());
        if (var10000 == null) {
            $$$reportNull$$$0(4);
        }

        return var10000;
    }

    @Contract(
        pure = true
    )
    default int getTextLength() {
        return this.getImmutableCharSequence().length();
    }

    @Contract(
        pure = true
    )
    int getLineCount();

    @Contract(
        pure = true
    )
    int getLineNumber(int var1);

    @Contract(
        pure = true
    )
    int getLineStartOffset(int var1);

    @Contract(
        pure = true
    )
    int getLineEndOffset(int var1);

    default boolean isLineModified(int line) {
        return false;
    }

    void insertString(int var1, @NonNls @NotNull CharSequence var2);

    void deleteString(int var1, int var2);

    void replaceString(int var1, int var2, @NlsSafe @NotNull CharSequence var3);

    @Contract(
        pure = true
    )
    boolean isWritable();

    @Contract(
        pure = true
    )
    long getModificationStamp();

    default void fireReadOnlyModificationAttempt() {
    }

    default void addDocumentListener(@NotNull DocumentListener listener) {
    }

    default void addDocumentListener(@NotNull DocumentListener listener, @NotNull Disposable parentDisposable) {
    }

    default void removeDocumentListener(@NotNull DocumentListener listener) {
    }

    default @NotNull RangeMarker createRangeMarker(int startOffset, int endOffset) {
        return this.createRangeMarker(startOffset, endOffset, false);
    }

    @NotNull RangeMarker createRangeMarker(int var1, int var2, boolean var3);

    default void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    default void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    default void setReadOnly(boolean isReadOnly) {
    }

    @NotNull RangeMarker createGuardedBlock(int var1, int var2);

    default void removeGuardedBlock(@NotNull RangeMarker block) {
    }

    default @Nullable RangeMarker getOffsetGuard(int offset) {
        return this.getRangeGuard(offset, offset);
    }

    default @Nullable RangeMarker getRangeGuard(int start, int end) {
        return null;
    }

    default void startGuardedBlockChecking() {
    }

    default void stopGuardedBlockChecking() {
    }

    default void setCyclicBufferSize(int bufferSize) {
    }

    void setText(@NotNull CharSequence var1);

    default @NotNull RangeMarker createRangeMarker(@NotNull TextRange textRange) {
        return this.createRangeMarker(textRange.getStartOffset(), textRange.getEndOffset());
    }

    @Contract(
        pure = true
    )
    default int getLineSeparatorLength(int line) {
        return 0;
    }

    default boolean isInBulkUpdate() {
        return false;
    }

    /** @deprecated */
    @Deprecated
    default void setInBulkUpdate(boolean value) {
    }
}
