package com.intellij.openapi.util;

import java.util.Comparator;
import org.jetbrains.annotations.Contract;

public interface Segment {
    Segment[] EMPTY_ARRAY = new Segment[0];
    Comparator<Segment> BY_START_OFFSET_THEN_END_OFFSET = (r1, r2) -> {
        int result = r1.getStartOffset() - r2.getStartOffset();
        if (result == 0) {
            result = r1.getEndOffset() - r2.getEndOffset();
        }

        return result;
    };

    @Contract(
        pure = true
    )
    int getStartOffset();

    @Contract(
        pure = true
    )
    int getEndOffset();
}
