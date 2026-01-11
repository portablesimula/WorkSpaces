package com.intellij.pom;

import com.intellij.platform.backend.navigation.NavigationRequest;
import com.intellij.platform.backend.navigation.NavigationRequests;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.concurrency.ThreadingAssertions;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.concurrency.annotations.RequiresReadLock;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

public interface Navigatable {
    Navigatable[] EMPTY_NAVIGATABLE_ARRAY = new Navigatable[0];

    @Experimental
    @RequiresReadLock
    @RequiresBackgroundThread
    default @Nullable NavigationRequest navigationRequest() {
        ThreadingAssertions.assertBackgroundThread();
        return NavigationRequests.getInstance().rawNavigationRequest(this);
    }

    default void navigate(boolean requestFocus) {
        throw new IncorrectOperationException("Must not call `navigate(boolean)` if `canNavigate()` returns `false`, or `navigate(boolean)` should be overridden if `canNavigate()` can return `true`.");
    }

    default boolean canNavigate() {
        return false;
    }

    default boolean canNavigateToSource() {
        return false;
    }
}
