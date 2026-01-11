package com.intellij.diagnostic;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public enum LoadingState {
    BOOTSTRAP("bootstrap"),
    COMPONENTS_REGISTERED("app component registered"),
    CONFIGURATION_STORE_INITIALIZED("app store initialized"),
    COMPONENTS_LOADED("app component loaded"),
    APP_READY("app ready"),
    APP_STARTED("app started"),
    PROJECT_OPENED("project opened");

    private static final AtomicReference<LoadingState> currentState = new AtomicReference(BOOTSTRAP);
    final String displayName;
    public static BiConsumer<String, Throwable> errorHandler;
    private static boolean CHECK_LOADING_PHASE;
    private static Set<ThrowableWrapper> stackTraces;

    private LoadingState(String displayName) {
        this.displayName = displayName;
    }

    @Internal
    public static void setStrictMode() {
        CHECK_LOADING_PHASE = true;
    }

    public void checkOccurred() {
        if (CHECK_LOADING_PHASE) {
            LoadingState currentState = (LoadingState)LoadingState.currentState.get();
            if (currentState.compareTo(this) < 0 && !isKnownViolator()) {
                this.logStateError(currentState);
            }
        }
    }

    private synchronized void logStateError(@NotNull LoadingState currentState) {
        Throwable t = new Throwable();
        if (stackTraces == null) {
            stackTraces = new HashSet();
        }

        if (stackTraces.add(new ThrowableWrapper(t))) {
            BiConsumer<String, Throwable> errorHandler = LoadingState.errorHandler;
            if (errorHandler != null) {
                errorHandler.accept("Should be called at least in the state " + this + ", the current state is: " + currentState + "\nCurrent violators count: " + stackTraces.size() + "\n\n", t);
            }

        }
    }

    private static boolean isKnownViolator() {
        for(StackTraceElement element : Thread.currentThread().getStackTrace()) {
            String className = element.getClassName();
            if (className.contains("com.intellij.util.indexing.IndexInfrastructure") || className.contains("com.intellij.psi.impl.search.IndexPatternSearcher") || className.contains("com.jetbrains.performancePlugin.ProjectLoaded")) {
                return true;
            }
        }

        return false;
    }

    public boolean isOccurred() {
        return ((LoadingState)currentState.get()).compareTo(this) >= 0;
    }

    @Internal
    public static void setCurrentState(@NotNull LoadingState state) {
        LoadingState old = (LoadingState)currentState.getAndSet(state);
        if (old.compareTo(state) > 0) {
            BiConsumer<String, Throwable> errorHandler = LoadingState.errorHandler;
            if (errorHandler != null) {
                errorHandler.accept("New state " + state + " cannot precede old " + old, new Throwable());
            }
        }

    }

    @Internal
    public static void compareAndSetCurrentState(@NotNull LoadingState expectedState, @NotNull LoadingState newState) {
        currentState.compareAndSet(expectedState, newState);
    }

    @Internal
    public static void setCurrentStateIfAtLeast(@NotNull LoadingState expectedState, @NotNull LoadingState newState) {
        assert newState.compareTo(expectedState) > 0;

        LoadingState current;
        do {
            current = (LoadingState)currentState.get();
            if (current.compareTo(expectedState) < 0) {
                return;
            }

            if (current.compareTo(newState) >= 0) {
                return;
            }
        } while(!currentState.compareAndSet(current, newState));

    }

    private static final class ThrowableWrapper {
        final Throwable throwable;

        private ThrowableWrapper(Throwable throwable) {
            this.throwable = throwable;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof ThrowableWrapper)) {
                return false;
            } else {
                Throwable throwable = ((ThrowableWrapper)obj).throwable;
                return this.throwable == throwable || fingerprint(this.throwable).equals(fingerprint(throwable));
            }
        }

        public int hashCode() {
            return fingerprint(this.throwable).hashCode();
        }

        private static String fingerprint(Throwable throwable) {
            StringBuilder sb = new StringBuilder();

            for(StackTraceElement traceElement : throwable.getStackTrace()) {
                sb.append(traceElement.getClassName()).append(traceElement.getMethodName());
            }

            return sb.toString();
        }
    }
}
