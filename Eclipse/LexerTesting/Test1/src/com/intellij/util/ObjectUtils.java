package com.intellij.util;

import com.intellij.openapi.util.NotNullFactory;
import com.intellij.util.containers.Convertor;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public final class ObjectUtils {
    public static final Object NULL = sentinel("ObjectUtils.NULL");

    private ObjectUtils() {
    }

    public static @NotNull Object sentinel(@NotNull @NonNls String name) {
        return new Sentinel(name);
    }

    public static <T> @NotNull T sentinel(@NotNull String name, @NotNull Class<T> ofInterface) {
        if (!ofInterface.isInterface()) {
            throw new IllegalArgumentException("Expected interface but got: " + ofInterface);
        } else {
            return (T)ReflectionUtil.proxy(ofInterface, (__, method, args) -> {
                if ("toString".equals(method.getName()) && args.length == 0) {
                    return name;
                } else {
                    throw new AbstractMethodError();
                }
            });
        }
    }

    /** @deprecated */
    @Deprecated
    public static <T> @NotNull T assertNotNull(@Nullable T t) {
        return (T)Objects.requireNonNull(t);
    }

    public static <T> void assertAllElementsNotNull(T @NotNull [] array) {
        if (array == null) {
            $$$reportNull$$$0(5);
        }

        int i = ArrayUtil.indexOfIdentity(array, (Object)null);
        if (i != -1) {
            throw new NullPointerException("Element [" + i + "] is null");
        }
    }

    @Contract(
        value = "!null, _ -> !null; _, !null -> !null; null, null -> null",
        pure = true
    )
    public static <T> T chooseNotNull(@Nullable T t1, @Nullable T t2) {
        return (T)(t1 == null ? t2 : t1);
    }

    @Contract(
        value = "!null, _ -> !null; _, !null -> !null; null, null -> null",
        pure = true
    )
    public static <T> T coalesce(@Nullable T t1, @Nullable T t2) {
        return (T)chooseNotNull(t1, t2);
    }

    @Contract(
        value = "!null, _, _ -> !null; _, !null, _ -> !null; _, _, !null -> !null; null,null,null -> null",
        pure = true
    )
    public static <T> T coalesce(@Nullable T t1, @Nullable T t2, @Nullable T t3) {
        return (T)(t1 != null ? t1 : (t2 != null ? t2 : t3));
    }

    public static <T> @Nullable T coalesce(@NotNull Iterable<? extends T> o) {
        for(T t : o) {
            if (t != null) {
                return t;
            }
        }

        return null;
    }

    /** @deprecated */
    @Deprecated
    public static <T> T notNull(@Nullable T value) {
        return (T)Objects.requireNonNull(value);
    }

    @Contract(
        value = "null, _ -> param2; !null, _ -> param1",
        pure = true
    )
    public static <T> @NotNull T notNull(@Nullable T value, @NotNull T defaultValue) {
        Object var10000 = value == null ? defaultValue : value;
        if ((value == null ? defaultValue : value) == null) {
            $$$reportNull$$$0(8);
        }

        return (T)var10000;
    }

    public static <T> @NotNull T notNull(@Nullable T value, @NotNull NotNullFactory<? extends T> defaultValue) {
        return (T)(value == null ? defaultValue.create() : value);
    }

    @Contract(
        value = "null, _ -> null",
        pure = true
    )
    public static <T> @Nullable T tryCast(@Nullable Object obj, @NotNull Class<T> clazz) {
        return (T)(clazz.isInstance(obj) ? clazz.cast(obj) : null);
    }

    public static <T, S> @Nullable S doIfCast(@Nullable Object obj, @NotNull Class<T> clazz, @NotNull Convertor<? super T, ? extends S> convertor) {
        return (S)(clazz.isInstance(obj) ? convertor.convert(obj) : null);
    }

    @Contract("null, _ -> null")
    public static <T, S> @Nullable S doIfNotNull(@Nullable T obj, @NotNull Function<? super T, ? extends S> function) {
        return (S)(obj == null ? null : function.fun(obj));
    }

    /** @deprecated */
    @Deprecated
    @ScheduledForRemoval
    public static <T> void consumeIfNotNull(@Nullable T obj, @NotNull Consumer<? super T> consumer) {
        if (obj != null) {
            consumer.consume(obj);
        }

    }

    /** @deprecated */
    @Deprecated
    public static <T> void consumeIfCast(@Nullable Object obj, @NotNull Class<T> clazz, @NotNull Consumer<? super T> consumer) {
        if (clazz.isInstance(obj)) {
            consumer.consume(obj);
        }

    }

    @Contract("null, _ -> null")
    public static <T> @Nullable T nullizeByCondition(@Nullable T obj, @NotNull Predicate<? super T> condition) {
        return (T)(condition.test(obj) ? null : obj);
    }

    /** @deprecated */
    @Deprecated
    @ScheduledForRemoval
    public static <T> @Nullable T nullizeIfDefaultValue(@Nullable T obj, @NotNull T defaultValue) {
        return (T)(obj == defaultValue ? null : obj);
    }

    public static int binarySearch(int fromIndex, int toIndex, @NotNull IntUnaryOperator indexComparator) {
        int low = fromIndex;
        int high = toIndex - 1;

        while(low <= high) {
            int mid = low + high >>> 1;
            int cmp = indexComparator.applyAsInt(mid);
            if (cmp < 0) {
                low = mid + 1;
            } else {
                if (cmp <= 0) {
                    return mid;
                }

                high = mid - 1;
            }
        }

        return -(low + 1);
    }

    public static @NotNull String objectInfo(@Nullable Object o) {
        return o != null ? o + " (" + o.getClass().getName() + ")" : "null";
    }

    private static final class Sentinel {
        private final String myName;

        Sentinel(@NotNull String name) {
            this.myName = name;
        }

        public String toString() {
            return this.myName;
        }
    }
}
