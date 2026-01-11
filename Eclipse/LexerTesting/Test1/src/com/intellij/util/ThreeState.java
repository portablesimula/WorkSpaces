// Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public enum ThreeState {
    YES, NO, UNSURE;

    /**
     * Combine two different ThreeState values yielding UNSURE if values are different
     * and itself if values are the same.
     *
     * @param other other value to combine with this value
     * @return a result of combination of two ThreeState values
     */
    @NotNull
    public ThreeState merge(@Nullable ThreeState other) {
        return this == other ? this : UNSURE;
    }

    public boolean toBoolean() {
        if (this == UNSURE) {
            throw new IllegalStateException("Must be or YES, or NO");
        }
        return this == YES;
    }

    /**
     * @param other state to compare with
     * @return true if the state is at least the same positive as the supplied one
     */
    public boolean isAtLeast(@NotNull ThreeState other) {
        switch (other) {
            case YES:
                return this == YES;
            case UNSURE:
                return this != NO;
            case NO:
                return true;
            default:
                throw new IllegalArgumentException("Unknown state: " + other);
        }
    }

    @NotNull
    public static ThreeState fromBoolean(boolean value) {
        return value ? YES : NO;
    }

    /**
     * @return `YES` if the given states contain `YES`, otherwise `UNSURE` if the given states contain `UNSURE`, otherwise `NO`
     */
    @NotNull
    public static ThreeState mostPositive(@NotNull Iterable<ThreeState> states) {
        ThreeState result = NO;
        for (ThreeState state : states) {
            switch (state) {
                case YES:
                    return YES;
                case UNSURE:
                    result = UNSURE;
                    break;
                default:
                    // Fortsett til neste
                    break;
            }
        }
        return result;
    }

    /**
     * @return `UNSURE` if `states` contains different values, the single value otherwise
     * @throws IllegalArgumentException if `states` is empty
     */
    @NotNull
    public static ThreeState merge(@NotNull Iterable<ThreeState> states) {
        ThreeState result = null;
        for (ThreeState state : states) {
            if (state == UNSURE) {
                return UNSURE;
            }
            if (result == null) {
                result = state;
            } else if (result != state) {
                return UNSURE;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Argument should not be empty");
        }
        return result;
    }
}
