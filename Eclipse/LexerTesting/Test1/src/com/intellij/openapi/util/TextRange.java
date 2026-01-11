package com.intellij.openapi.util;

import java.io.Serializable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TextRange implements Segment, Serializable {
    private static final long serialVersionUID = -670091356599757430L;
    public static final TextRange EMPTY_RANGE = new TextRange(0, 0);
    public static final TextRange[] EMPTY_ARRAY = new TextRange[0];
    private final int myStartOffset;
    private final int myEndOffset;

    @Contract(
        pure = true
    )
    public TextRange(int startOffset, int endOffset) {
        this(startOffset, endOffset, true);
    }

    protected TextRange(int startOffset, int endOffset, boolean checkForProperTextRange) {
        this.myStartOffset = startOffset;
        this.myEndOffset = endOffset;
        if (checkForProperTextRange) {
            assertProperRange(this);
        }

    }

    public final int getStartOffset() {
        return this.myStartOffset;
    }

    public final int getEndOffset() {
        return this.myEndOffset;
    }

    public final int getLength() {
        return this.myEndOffset - this.myStartOffset;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TextRange)) {
            return false;
        } else {
            TextRange range = (TextRange)obj;
            return this.myStartOffset == range.myStartOffset && this.myEndOffset == range.myEndOffset;
        }
    }

    public int hashCode() {
        return this.myStartOffset + this.myEndOffset;
    }

    @Contract(
        pure = true
    )
    public boolean contains(@NotNull TextRange range) {
        return this.contains((Segment)range);
    }

    @Contract(
        pure = true
    )
    public boolean contains(@NotNull Segment segment) {
        return this.containsRange(segment.getStartOffset(), segment.getEndOffset());
    }

    @Contract(
        pure = true
    )
    public boolean containsRange(int startOffset, int endOffset) {
        return this.getStartOffset() <= startOffset && endOffset <= this.getEndOffset();
    }

    @Contract(
        pure = true
    )
    public static boolean containsRange(@NotNull Segment outer, @NotNull Segment inner) {
        return outer.getStartOffset() <= inner.getStartOffset() && inner.getEndOffset() <= outer.getEndOffset();
    }

    @Contract(
        pure = true
    )
    public boolean containsOffset(int offset) {
        return this.myStartOffset <= offset && offset <= this.myEndOffset;
    }

    public String toString() {
        return "(" + this.myStartOffset + "," + this.myEndOffset + ")";
    }

    @Contract(
        pure = true
    )
    public boolean contains(int offset) {
        return this.myStartOffset <= offset && offset < this.myEndOffset;
    }

    @Contract(
        pure = true
    )
    public @NotNull String substring(@NotNull String str) {
        return str.substring(this.myStartOffset, this.myEndOffset);
    }

    @Contract(
        pure = true
    )
    public @NotNull CharSequence subSequence(@NotNull CharSequence str) {
        return str.subSequence(this.myStartOffset, this.myEndOffset);
    }

    @Contract(
        pure = true
    )
    public @NotNull TextRange cutOut(@NotNull TextRange subRange) {
        if (subRange.getStartOffset() > this.getLength()) {
            throw new IllegalArgumentException("SubRange: " + subRange + "; this=" + this);
        } else if (subRange.getEndOffset() > this.getLength()) {
            throw new IllegalArgumentException("SubRange: " + subRange + "; this=" + this);
        } else {
            assertProperRange(subRange);
            return new TextRange(this.myStartOffset + subRange.getStartOffset(), Math.min(this.myEndOffset, this.myStartOffset + subRange.getEndOffset()));
        }
    }

    @Contract(
        pure = true
    )
    public @NotNull TextRange shiftRight(int delta) {
        return delta == 0 ? this : new TextRange(this.myStartOffset + delta, this.myEndOffset + delta);
    }

    @Contract(
        pure = true
    )
    public @NotNull TextRange shiftLeft(int delta) {
        return delta == 0 ? this : new TextRange(this.myStartOffset - delta, this.myEndOffset - delta);
    }

    @Contract(
        pure = true
    )
    public @NotNull TextRange grown(int lengthDelta) {
        return lengthDelta == 0 ? this : from(this.myStartOffset, this.getLength() + lengthDelta);
    }

    @Contract(
        pure = true
    )
    public static @NotNull TextRange from(int offset, int length) {
        return create(offset, offset + length);
    }

    @Contract(
        pure = true
    )
    public static @NotNull TextRange create(int startOffset, int endOffset) {
        return new TextRange(startOffset, endOffset);
    }

    @Contract(
        pure = true
    )
    public static @NotNull TextRange create(@NotNull Segment segment) {
        return create(segment.getStartOffset(), segment.getEndOffset());
    }

    @Contract(
        pure = true
    )
    public static boolean areSegmentsEqual(@NotNull Segment segment1, @NotNull Segment segment2) {
        return segment1.getStartOffset() == segment2.getStartOffset() && segment1.getEndOffset() == segment2.getEndOffset();
    }

    @Contract(
        pure = true
    )
    public @NotNull String replace(@NotNull String original, @NotNull String replacement) {
        String beginning = original.substring(0, this.getStartOffset());
        String ending = original.substring(this.getEndOffset());
        return beginning + replacement + ending;
    }

    @Contract(
        pure = true
    )
    public boolean intersects(@NotNull TextRange textRange) {
        return this.intersects((Segment)textRange);
    }

    @Contract(
        pure = true
    )
    public boolean intersects(@NotNull Segment textRange) {
        return this.intersects(textRange.getStartOffset(), textRange.getEndOffset());
    }

    @Contract(
        pure = true
    )
    public boolean intersects(int startOffset, int endOffset) {
        return Math.max(this.myStartOffset, startOffset) <= Math.min(this.myEndOffset, endOffset);
    }

    @Contract(
        pure = true
    )
    public boolean intersectsStrict(@NotNull TextRange textRange) {
        return this.intersectsStrict(textRange.getStartOffset(), textRange.getEndOffset());
    }

    @Contract(
        pure = true
    )
    public boolean intersectsStrict(int startOffset, int endOffset) {
        return Math.max(this.myStartOffset, startOffset) < Math.min(this.myEndOffset, endOffset);
    }

    @Contract(
        pure = true
    )
    public TextRange intersection(@NotNull TextRange range) {
        if (this.equals(range)) {
            return this;
        } else {
            int newStart = Math.max(this.myStartOffset, range.getStartOffset());
            int newEnd = Math.min(this.myEndOffset, range.getEndOffset());
            return isProperRange(newStart, newEnd) ? new TextRange(newStart, newEnd) : null;
        }
    }

    @Contract(
        pure = true
    )
    public boolean isEmpty() {
        return this.myStartOffset >= this.myEndOffset;
    }

    @Contract(
        pure = true
    )
    public @NotNull TextRange union(@NotNull TextRange textRange) {
        return this.equals(textRange) ? this : new TextRange(Math.min(this.myStartOffset, textRange.getStartOffset()), Math.max(this.myEndOffset, textRange.getEndOffset()));
    }

    @Contract(
        pure = true
    )
    public boolean equalsToRange(int startOffset, int endOffset) {
        return startOffset == this.myStartOffset && endOffset == this.myEndOffset;
    }

    @Contract(
        pure = true
    )
    public static @NotNull TextRange allOf(@NotNull String s) {
        return new TextRange(0, s.length());
    }

    public static void assertProperRange(@NotNull Segment range) throws AssertionError {
        assertProperRange(range, "");
    }

    public static void assertProperRange(@NotNull Segment range, @NotNull Object message) throws AssertionError {
        assertProperRange(range.getStartOffset(), range.getEndOffset(), message);
    }

    public static void assertProperRange(int startOffset, int endOffset, @NotNull Object message) {
        if (!isProperRange(startOffset, endOffset)) {
            throw new IllegalArgumentException("Invalid range specified: (" + startOffset + ", " + endOffset + "); " + message);
        }
    }

    public static boolean isProperRange(int startOffset, int endOffset) {
        return startOffset <= endOffset && startOffset >= 0;
    }

    public boolean isProperRange() {
        return isProperRange(this.getStartOffset(), this.getEndOffset());
    }
}
