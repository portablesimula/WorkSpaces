package com.intellij.util;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public class IncorrectOperationException extends RuntimeException {
    public IncorrectOperationException() {
    }

    public IncorrectOperationException(String message) {
        super(message);
    }

    public IncorrectOperationException(Throwable t) {
        super(t);
    }

    public IncorrectOperationException(String message, Throwable t) {
        super(message, t);
    }

    /** @deprecated */
    @Deprecated
    @ScheduledForRemoval
    public IncorrectOperationException(String message, Exception e) {
        super(message, e);
    }
}
