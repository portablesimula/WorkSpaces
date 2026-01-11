package com.intellij.openapi.util;

import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.IntObjectMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

@NonNls
public class Key<T> {
    private static final AtomicInteger ourKeysCounter = new AtomicInteger();
    private static final IntObjectMap<Key<?>> allKeys = ContainerUtil.createIntKeyWeakValueMap();
    private final int myIndex;
    private final String myName;

    public Key(@NonNls @NotNull String name) {
        this.myIndex = ourKeysCounter.getAndIncrement();
        this.myName = name;
        synchronized(allKeys) {
            allKeys.put(this.myIndex, this);
        }
    }

    public final int hashCode() {
        return this.myIndex;
    }

    public final boolean equals(Object obj) {
        return obj == this;
    }

    public String toString() {
        return this.myName;
    }

    public static <T> @NotNull Key<T> create(@NonNls @NotNull String name) {
        return new Key<T>(name);
    }

    @Contract("null -> null")
    public @UnknownNullability T get(@Nullable UserDataHolder holder) {
        return (T)(holder == null ? null : holder.getUserData(this));
    }

    @Contract("_, !null -> !null")
    public T get(@Nullable UserDataHolder holder, T defaultValue) {
        T t = (T)this.get(holder);
        return (T)(t == null ? defaultValue : t);
    }

    public @NotNull T getRequired(@NotNull UserDataHolder holder) {
        return (T)Objects.requireNonNull(holder.getUserData(this));
    }

    public boolean isIn(@Nullable UserDataHolder holder) {
        return this.get(holder) != null;
    }

    public void set(@Nullable UserDataHolder holder, @Nullable T value) {
        if (holder != null) {
            holder.putUserData(this, value);
        }

    }

    public static <T> @Nullable("can become null if the key has been gc-ed") Key<T> getKeyByIndex(int index) {
        synchronized(allKeys) {
            return (Key)allKeys.get(index);
        }
    }

    /** @deprecated */
    @Deprecated
    public static @Nullable Key<?> findKeyByName(@NotNull String name) {
        synchronized(allKeys) {
            for(Key<?> key : allKeys.values()) {
                if (name.equals(key.myName)) {
                    return key;
                }
            }

            return null;
        }
    }
}
