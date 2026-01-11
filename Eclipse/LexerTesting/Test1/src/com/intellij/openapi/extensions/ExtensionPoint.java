//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.openapi.extensions;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.extensions.impl.ExtensionComponentAdapter;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface ExtensionPoint<T> {
    /** @deprecated */
    @Deprecated
    void registerExtension(T var1);

    @TestOnly
    void registerExtension(T var1, @NotNull Disposable var2);

    @TestOnly
    void registerExtension(T var1, @NotNull PluginDescriptor var2, @NotNull Disposable var3);

    @TestOnly
    void registerExtension(T var1, @NotNull LoadingOrder var2, @NotNull Disposable var3);

    T @NotNull [] getExtensions();

    @NotNull List<T> getExtensionList();

    int size();

    /** @deprecated */
    @Deprecated
    void unregisterExtension(T var1);

    void unregisterExtension(@NotNull Class<? extends T> var1);

    boolean unregisterExtensions(@NotNull BiPredicate<String, ExtensionComponentAdapter> var1, boolean var2);

    void addExtensionPointListener(@NotNull ExtensionPointListener<T> var1, boolean var2, @Nullable Disposable var3);

    void addChangeListener(@NotNull Runnable var1, @Nullable Disposable var2);

    void addChangeListener(@NotNull CoroutineScope var1, @NotNull Runnable var2);

    @Internal
    void removeExtensionPointListener(@NotNull ExtensionPointListener<T> var1);

    @Internal
    boolean isDynamic();

    @Internal
    @NotNull PluginDescriptor getPluginDescriptor();

    @Internal
    @Experimental
    <K> @Nullable T getByKey(@NotNull K var1, @NotNull Class<?> var2, @NotNull Function<T, @Nullable K> var3);

    public static enum Kind {
        INTERFACE,
        BEAN_CLASS;
    }
}
