package com.intellij.openapi.application;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import java.awt.Component;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import testing.util.LOG;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.Obsolete;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public interface Application extends ComponentManager {
    @Obsolete
    default void invokeLaterOnWriteThread(@NotNull Runnable action) {
        this.invokeLater(action, this.getDefaultModalityState());
    }

    @Obsolete
    default void invokeLaterOnWriteThread(@NotNull Runnable action, @NotNull ModalityState modal) {
        this.invokeLater(action, modal, this.getDisposed());
    }

    @Obsolete
    default void invokeLaterOnWriteThread(@NotNull Runnable action, @NotNull ModalityState modal, @NotNull Condition<?> expired) {
        if (expired == null) {
            LOG.error("$$$reportNull$$$0(5)");
        }

        this.invokeLater(action, modal, expired);
    }

    void runReadAction(@NotNull Runnable var1);

    <T> T runReadAction(@NotNull Computable<T> var1);

    <T, E extends Throwable> T runReadAction(@NotNull ThrowableComputable<T, E> var1) throws E;

    void runWriteAction(@NotNull Runnable var1);

    <T> T runWriteAction(@NotNull Computable<T> var1);

    <T, E extends Throwable> T runWriteAction(@NotNull ThrowableComputable<T, E> var1) throws E;

    boolean hasWriteAction(@NotNull Class<?> var1);

    @Experimental
    default <T, E extends Throwable> T runWriteIntentReadAction(@NotNull ThrowableComputable<T, E> computation) throws E {
        this.assertWriteIntentLockAcquired();
        return (T)computation.compute();
    }

    @Obsolete
    void assertReadAccessAllowed();

    @Obsolete
    void assertWriteAccessAllowed();

    @Experimental
    @Obsolete
    void assertReadAccessNotAllowed();

    @Obsolete
    void assertIsDispatchThread();

    @Experimental
    @Obsolete
    void assertIsNonDispatchThread();

    @Experimental
    @Obsolete
    void assertWriteIntentLockAcquired();

    void addApplicationListener(@NotNull ApplicationListener var1, @NotNull Disposable var2);

    /** @deprecated */
    @Deprecated
    @Internal
    @RequiresEdt
    void saveAll();

    void saveSettings();

    boolean holdsReadLock();

    void exit();

    default void exit(boolean force, boolean exitConfirmed, boolean restart, int exitCode) {
        this.exit();
    }

    default void exit(boolean force, boolean exitConfirmed, boolean restart) {
        this.exit();
    }

    @Contract(
        pure = true
    )
    boolean isWriteAccessAllowed();

    @Contract(
        pure = true
    )
    boolean isReadAccessAllowed();

    @Contract(
        pure = true
    )
    boolean isDispatchThread();

    @Experimental
    @Contract(
        pure = true
    )
    boolean isWriteIntentLockAcquired();

    void invokeLater(@NotNull Runnable var1);

    void invokeLater(@NotNull Runnable var1, @NotNull Condition<?> var2);

    void invokeLater(@NotNull Runnable var1, @NotNull ModalityState var2);

    void invokeLater(@NotNull Runnable var1, @NotNull ModalityState var2, @NotNull Condition<?> var3);

    void invokeAndWait(@NotNull Runnable var1, @NotNull ModalityState var2) throws ProcessCanceledException;

    void invokeAndWait(@NotNull Runnable var1) throws ProcessCanceledException;

    /** @deprecated */
    @Deprecated
    @RequiresEdt
    @Internal
    @NotNull ModalityState getCurrentModalityState();

    @RequiresEdt
    @NotNull ModalityState getModalityStateForComponent(@NotNull Component var1);

    @NotNull ModalityState getDefaultModalityState();

    /** @deprecated */
    @Deprecated
    @Internal
    @NotNull ModalityState getNoneModalityState();

    /** @deprecated */
    @Deprecated
    @Internal
    @NotNull ModalityState getAnyModalityState();

    long getStartTime();

    long getIdleTime();

    boolean isUnitTestMode();

    boolean isHeadlessEnvironment();

    boolean isCommandLine();

    @NotNull Future<?> executeOnPooledThread(@NotNull Runnable var1);

    <T> @NotNull Future<T> executeOnPooledThread(@NotNull Callable<T> var1);

    boolean isRestartCapable();

    void restart();

    boolean isActive();

    boolean isInternal();

    boolean isEAP();

    @Internal
    default boolean isExitInProgress() {
        return false;
    }

    @Internal
    boolean isSaveAllowed();

    /** @deprecated */
    @Deprecated
    void addApplicationListener(@NotNull ApplicationListener var1);

    /** @deprecated */
    @Deprecated
    void removeApplicationListener(@NotNull ApplicationListener var1);

    /** @deprecated */
    @Deprecated
    default boolean isDisposeInProgress() {
        return this.isDisposed();
    }

    /** @deprecated */
    @Deprecated
    @NotNull AccessToken acquireReadActionLock();

    /** @deprecated */
    @Deprecated
    @NotNull AccessToken acquireWriteActionLock(@NotNull Class<?> var1);

    /** @deprecated */
    @Deprecated
    @Experimental
    @Contract(
        pure = true
    )
    default boolean isWriteThread() {
        return this.isWriteIntentLockAcquired();
    }

    /** @deprecated */
    @Deprecated
    @ScheduledForRemoval
    @Experimental
    default void assertIsWriteThread() {
        this.assertWriteIntentLockAcquired();
    }

    @Experimental
    @Internal
    default CoroutineContext getLockStateAsCoroutineContext(CoroutineContext context, boolean shared) {
        return EmptyCoroutineContext.INSTANCE;
    }

    @Experimental
    @Internal
    default void returnPermitFromContextElement(CoroutineContext ctx) {
    }

    @Experimental
    @Internal
    default boolean hasLockStateInContext(CoroutineContext context) {
        return false;
    }

    @Internal
    @Obsolete
    default boolean isTopmostReadAccessAllowed() {
        return this.isReadAccessAllowed();
    }

    @Internal
    default @NonNls @Nullable String isLockingProhibited() {
        return null;
    }
}
