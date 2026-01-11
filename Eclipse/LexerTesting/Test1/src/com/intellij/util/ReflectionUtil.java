package com.intellij.util;

import com.intellij.openapi.diagnostic.ControlFlowException;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.DifferenceFilter;
import com.intellij.util.lang.CompoundRuntimeException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public final class ReflectionUtil {
    private static final Logger LOG = Logger.getInstance(ReflectionUtil.class);
    private static final Object unsafe;

    private ReflectionUtil() {
    }

    public static @NotNull List<Field> collectFields(@NotNull Class<?> clazz) {
        List<Field> result = new ArrayList();

        for(Class<?> c : JBIterableClassTraverser.classTraverser(clazz)) {
            Collections.addAll(result, c.getDeclaredFields());
        }

        return result;
    }

    public static @NotNull Field findField(@NotNull Class<?> clazz, @Nullable Class<?> type, @NotNull @NonNls String name) throws NoSuchFieldException {
        Field result = findFieldInHierarchy(clazz, (field) -> name.equals(field.getName()) && (type == null || field.getType().equals(type)));
        if (result != null) {
            return result;
        } else {
            throw new NoSuchFieldException("Class: " + clazz + " name: " + name + " type: " + type);
        }
    }

    public static @NotNull Field findAssignableField(@NotNull Class<?> clazz, @Nullable("null means any type") Class<?> fieldType, @NotNull @NonNls String fieldName) throws NoSuchFieldException {
        Field result = findFieldInHierarchy(clazz, (field) -> fieldName.equals(field.getName()) && (fieldType == null || fieldType.isAssignableFrom(field.getType())));
        if (result != null) {
            return result;
        } else {
            throw new NoSuchFieldException("Class: " + clazz + " fieldName: " + fieldName + " fieldType: " + fieldType);
        }
    }

    public static @Nullable Field findFieldInHierarchy(@NotNull Class<?> rootClass, @NotNull Predicate<? super Field> checker) {
        for(Class<?> aClass = rootClass; aClass != null; aClass = aClass.getSuperclass()) {
            for(Field field : aClass.getDeclaredFields()) {
                if (checker.test(field)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        }

        return processInterfaces(rootClass.getInterfaces(), new HashSet(), checker);
    }

    private static @Nullable Field processInterfaces(Class<?> @NotNull [] interfaces, @NotNull Set<? super Class<?>> visited, @NotNull Predicate<? super Field> checker) {
        if (interfaces == null) {
            $$$reportNull$$$0(12);
        }

        for(Class<?> anInterface : interfaces) {
            if (visited.add(anInterface)) {
                for(Field field : anInterface.getDeclaredFields()) {
                    if (checker.test(field)) {
                        field.setAccessible(true);
                        return field;
                    }
                }

                Field field = processInterfaces(anInterface.getInterfaces(), visited, checker);
                if (field != null) {
                    return field;
                }
            }
        }

        return null;
    }

    public static void resetField(@NotNull Class<?> clazz, @Nullable("null means of any type") Class<?> type, @NotNull @NonNls String name) {
        try {
            resetField((Object)null, (Field)findField(clazz, type, name));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetField(@NotNull Object object, @NotNull @NonNls String name) {
        try {
            resetField(object, findField(object.getClass(), (Class)null, name));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetField(@Nullable Object object, @NotNull Field field) {
        field.setAccessible(true);
        Class<?> type = field.getType();

        try {
            if (type.isPrimitive()) {
                if (Boolean.TYPE.equals(type)) {
                    field.set(object, Boolean.FALSE);
                } else if (Integer.TYPE.equals(type)) {
                    field.set(object, 0);
                } else if (Double.TYPE.equals(type)) {
                    field.set(object, (double)0.0F);
                } else if (Float.TYPE.equals(type)) {
                    field.set(object, 0.0F);
                }
            } else {
                field.set(object, (Object)null);
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static @Nullable Method findMethod(@NotNull Collection<Method> methods, @NonNls @NotNull String name, @NotNull Class<?>... parameters) {
        if (parameters == null) {
            $$$reportNull$$$0(20);
        }

        for(Method method : methods) {
            if (parameters.length == method.getParameterCount() && name.equals(method.getName()) && Arrays.equals(parameters, method.getParameterTypes())) {
                return makeAccessible(method);
            }
        }

        return null;
    }

    private static Method makeAccessible(Method method) {
        method.setAccessible(true);
        return method;
    }

    public static @Nullable Method getMethod(@NotNull Class<?> aClass, @NonNls @NotNull String name, @NotNull Class<?>... parameters) {
        if (parameters == null) {
            $$$reportNull$$$0(23);
        }

        try {
            return makeAccessible(aClass.getMethod(name, parameters));
        } catch (NoSuchMethodException var4) {
            return null;
        }
    }

    /** @deprecated */
    @Deprecated
    public static @Nullable Method getDeclaredMethod(@NotNull Class<?> aClass, @NonNls @NotNull String name, @NotNull Class<?>... parameters) {
        if (parameters == null) {
            $$$reportNull$$$0(26);
        }

        try {
            return makeAccessible(aClass.getDeclaredMethod(name, parameters));
        } catch (NoSuchMethodException var4) {
            return null;
        }
    }

    /** @deprecated */
    @Deprecated
    public static @Nullable Field getDeclaredField(@NotNull Class<?> aClass, @NonNls @NotNull String name) {
        return findFieldInHierarchy(aClass, (field) -> name.equals(field.getName()));
    }

    public static @NotNull List<Method> getClassPublicMethods(@NotNull Class<?> aClass) {
        return filterRealMethods(aClass.getMethods());
    }

    public static @NotNull List<Method> getClassDeclaredMethods(@NotNull Class<?> aClass) {
        return filterRealMethods(aClass.getDeclaredMethods());
    }

    private static @NotNull List<Method> filterRealMethods(Method @NotNull [] methods) {
        if (methods == null) {
            $$$reportNull$$$0(31);
        }

        List<Method> result = new ArrayList();

        for(Method method : methods) {
            if (!method.isSynthetic()) {
                result.add(method);
            }
        }

        return result;
    }

    public static @Nullable Class<?> getMethodDeclaringClass(@NotNull Class<?> instanceClass, @NonNls @NotNull String methodName, @NotNull Class<?>... parameters) {
        if (parameters == null) {
            $$$reportNull$$$0(35);
        }

        try {
            return instanceClass.getMethod(methodName, parameters).getDeclaringClass();
        } catch (NoSuchMethodException var5) {
            while(instanceClass != null) {
                try {
                    return instanceClass.getDeclaredMethod(methodName, parameters).getDeclaringClass();
                } catch (NoSuchMethodException var4) {
                    instanceClass = instanceClass.getSuperclass();
                }
            }

            return null;
        }
    }

    public static <T> T getField(@NotNull Class<?> objectClass, @Nullable Object object, @Nullable("null means any type") Class<T> fieldType, @NotNull @NonNls String fieldName) {
        try {
            Field field = findAssignableField(objectClass, fieldType, fieldName);
            return (T)getFieldValue(field, object);
        } catch (NoSuchFieldException e) {
            LOG.debug(e);
            return null;
        }
    }

    public static <T> T getStaticFieldValue(@NotNull Class<?> objectClass, @Nullable("null means any type") Class<T> fieldType, @NotNull @NonNls String fieldName) {
        try {
            Field field = findAssignableField(objectClass, fieldType, fieldName);
            if (isInstanceField(field)) {
                throw new IllegalArgumentException("Field " + objectClass + "." + fieldName + " is not static");
            } else {
                return (T)getFieldValue(field, (Object)null);
            }
        } catch (NoSuchFieldException e) {
            LOG.debug(e);
            return null;
        }
    }

    public static <T> @Nullable T getFieldValue(@NotNull Field field, @Nullable Object object) {
        try {
            return (T)field.get(object);
        } catch (IllegalAccessException e) {
            LOG.debug(e);
            return null;
        }
    }

    public static boolean isInstanceField(@NotNull Field field) {
        return !Modifier.isStatic(field.getModifiers());
    }

    public static <T> boolean setField(@NotNull Class<?> objectClass, Object object, @Nullable("null means any type") Class<T> fieldType, @NotNull @NonNls String fieldName, T value) {
        try {
            Field field = findAssignableField(objectClass, fieldType, fieldName);
            field.set(object, value);
            return true;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOG.debug(e);
            return false;
        }
    }

    public static <T> @NotNull Constructor<T> getDefaultConstructor(@NotNull Class<T> aClass) {
        Constructor var10000;
        try {
            Constructor<T> constructor = aClass.getConstructor();
            constructor.setAccessible(true);
            var10000 = constructor;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No default constructor in " + aClass, e);
        }

        return var10000;
    }

    public static <T> @NotNull T newInstance(@NotNull Class<T> aClass) {
        return (T)newInstance(aClass, true);
    }

    public static <T> @NotNull T newInstance(@NotNull Class<T> aClass, boolean isKotlinDataClassesSupported) {
        Object var10000;
        try {
            Constructor<T> constructor = aClass.getDeclaredConstructor();

            try {
                constructor.setAccessible(true);
            } catch (SecurityException var4) {
            }

            var10000 = constructor.newInstance();
        } catch (Exception var5) {
            if (var5 instanceof InvocationTargetException) {
                Throwable targetException = ((InvocationTargetException)var5).getTargetException();
                if (targetException instanceof ControlFlowException && targetException instanceof RuntimeException) {
                    throw (RuntimeException)targetException;
                }
            }

            if (isKotlinDataClassesSupported) {
                T t = (T)createAsDataClass(aClass);
                if (t != null) {
                    return t;
                }
            }

            ExceptionUtilRt.rethrowUnchecked(var5);
            throw new RuntimeException(var5);
        }

        return (T)var10000;
    }

    private static <T> @Nullable T createAsDataClass(@NotNull Class<T> aClass) {
        for(Annotation annotation : aClass.getAnnotations()) {
            String name = annotation.annotationType().getName();
            if (name.equals("kotlin.Metadata") || name.equals("kotlin.jvm.internal.KotlinClass")) {
                List<Exception> exceptions = null;
                Constructor<?>[] constructors = aClass.getDeclaredConstructors();
                List<Constructor<?>> defaultCtors = new SmartList();

                label102:
                for(Constructor<?> constructor : constructors) {
                    try {
                        try {
                            constructor.setAccessible(true);
                        } catch (Throwable var19) {
                        }

                        if (constructor.getParameterCount() == 0) {
                            return (T)constructor.newInstance();
                        }

                        Class<?>[] parameterTypes = constructor.getParameterTypes();

                        for(Class<?> type : parameterTypes) {
                            if (type.getName().equals("kotlin.jvm.internal.DefaultConstructorMarker")) {
                                defaultCtors.add(constructor);
                                continue label102;
                            }
                        }

                        return (T)constructor.newInstance();
                    } catch (Exception e) {
                        if (exceptions == null) {
                            exceptions = new SmartList();
                        }

                        exceptions.add(new Exception("Failed to call constructor: " + constructor.toString(), e));
                    }
                }

                for(Constructor<?> constructor : defaultCtors) {
                    try {
                        try {
                            constructor.setAccessible(true);
                        } catch (Throwable var18) {
                        }

                        return (T)constructor.newInstance();
                    } catch (Exception e) {
                        if (exceptions == null) {
                            exceptions = new SmartList();
                        }

                        exceptions.add(new Exception("Failed to call constructor: " + constructor.toString(), e));
                    }
                }

                if (exceptions != null) {
                    if (exceptions.size() == 1) {
                        ExceptionUtil.rethrow((Throwable)exceptions.get(0));
                    } else {
                        ExceptionUtil.rethrow(new CompoundRuntimeException(exceptions));
                    }
                }
            }
        }

        return null;
    }

    public static <T> @NotNull T createInstance(@NotNull Constructor<T> constructor, @NotNull Object... args) {
        if (args == null) {
            $$$reportNull$$$0(52);
        }

        Object var10000;
        try {
            var10000 = constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return (T)var10000;
    }

    public static @Nullable Class<?> getGrandCallerClass() {
        int stackFrameCount = 3;
        return getCallerClass(stackFrameCount + 1);
    }

    public static Class<?> getCallerClass(int stackFrameCount) {
        Class<?> callerClass = findCallerClass(stackFrameCount);

        for(int depth = stackFrameCount + 1; callerClass != null && callerClass.getClassLoader() == null; ++depth) {
            callerClass = findCallerClass(depth);
        }

        if (callerClass == null) {
            callerClass = findCallerClass(stackFrameCount - 1);
        }

        return callerClass;
    }

    public static void copyFields(Field @NotNull [] fields, @NotNull Object from, @NotNull Object to) {
        if (fields == null) {
            $$$reportNull$$$0(56);
        }

        copyFields(fields, from, to, (DifferenceFilter)null);
    }

    public static void copyFields(Field @NotNull [] fields, @NotNull Object from, @NotNull Object to, @Nullable DifferenceFilter<?> diffFilter) {
        if (fields == null) {
            $$$reportNull$$$0(59);
        }

        Set<Field> sourceFields = new HashSet(Arrays.asList(from.getClass().getFields()));

        for(Field field : fields) {
            if (sourceFields.contains(field) && isPublic(field) && !isFinal(field)) {
                try {
                    if (diffFilter == null || diffFilter.test(field)) {
                        copyFieldValue(from, to, field);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static <T> boolean comparePublicNonFinalFields(@NotNull T first, @NotNull T second) {
        Class<?> defaultClass = first.getClass();
        Field[] fields = defaultClass.getDeclaredFields();
        if (defaultClass != second.getClass()) {
            fields = (Field[])ArrayUtil.mergeArrays(fields, second.getClass().getDeclaredFields());
        }

        for(Field field : fields) {
            if (isPublic(field) && !isFinal(field)) {
                field.setAccessible(true);

                try {
                    if (!Comparing.equal(field.get(second), field.get(first))) {
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return true;
    }

    public static void copyFieldValue(@NotNull Object from, @NotNull Object to, @NotNull Field field) throws IllegalAccessException {
        if (field == null) {
            $$$reportNull$$$0(64);
        }

        Class<?> fieldType = field.getType();
        if (!fieldType.isPrimitive() && !fieldType.equals(String.class) && !fieldType.isEnum()) {
            throw new RuntimeException("Field '" + field.getName() + "' not copied: unsupported type: " + field.getType());
        } else {
            field.set(to, field.get(from));
        }
    }

    private static boolean isPublic(@NotNull Field field) {
        return (field.getModifiers() & 1) != 0;
    }

    private static boolean isFinal(@NotNull Field field) {
        return (field.getModifiers() & 16) != 0;
    }

    public static @NotNull Class<?> forName(@NotNull String fqn) {
        Class var10000;
        try {
            var10000 = Class.forName(fqn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return var10000;
    }

    public static @NotNull Class<?> boxType(@NotNull Class<?> type) {
        if (!type.isPrimitive()) {
            return type;
        } else if (type == Boolean.TYPE) {
            return Boolean.class;
        } else if (type == Byte.TYPE) {
            return Byte.class;
        } else if (type == Short.TYPE) {
            return Short.class;
        } else if (type == Integer.TYPE) {
            return Integer.class;
        } else if (type == Long.TYPE) {
            return Long.class;
        } else if (type == Float.TYPE) {
            return Float.class;
        } else if (type == Double.TYPE) {
            return Double.class;
        } else {
            return type == Character.TYPE ? Character.class : type;
        }
    }

    public static <T, V> @NotNull Field getTheOnlyVolatileInstanceFieldOfClass(@NotNull Class<T> ownerClass, @NotNull Class<V> fieldType) {
        Field[] declaredFields = ownerClass.getDeclaredFields();
        Field found = null;

        for(Field field : declaredFields) {
            int modifiers = field.getModifiers();
            if (!BitUtil.isSet(modifiers, 8) && BitUtil.isSet(modifiers, 64) && fieldType.isAssignableFrom(field.getType())) {
                if (found != null) {
                    throw new IllegalArgumentException("Two fields of " + fieldType + " found in the " + ownerClass + ": " + found + " and " + field);
                }

                found = field;
            }
        }

        return found;
    }

    /** @deprecated */
    @Deprecated
    @Internal
    @ScheduledForRemoval
    public static @NotNull Object getUnsafe() {
        return unsafe;
    }

    public static Class<?> findCallerClass(int framesToSkip) {
        return ReflectionUtilRt.findCallerClass(framesToSkip + 1);
    }

    public static boolean isAssignable(@NotNull Class<?> ancestor, @NotNull Class<?> descendant) {
        return ancestor == descendant || ancestor.isAssignableFrom(descendant);
    }

    public static String dumpFields(@NotNull Class<?> objectClass, @Nullable Object object, String... fieldNames) {
        List<String> chunks = new SmartList();

        for(String fieldName : fieldNames) {
            chunks.add(fieldName + "=" + getField(objectClass, object, (Class)null, fieldName));
        }

        return String.join("; ", chunks);
    }

    public static <T> @NotNull T proxy(@NotNull Class<? extends T> superInterface, @NotNull InvocationHandler handler) {
        return (T)superInterface.cast(Proxy.newProxyInstance(superInterface.getClassLoader(), new Class[]{superInterface}, handler));
    }

    public static <T> @NotNull T proxy(@Nullable ClassLoader loader, @NotNull Class<? extends T> superInterface, @NotNull InvocationHandler handler) {
        return (T)superInterface.cast(Proxy.newProxyInstance(loader, new Class[]{superInterface}, handler));
    }

    static {
        Class<?> unsafeClass;
        try {
            unsafeClass = Class.forName("sun.misc.Unsafe");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        unsafe = getStaticFieldValue(unsafeClass, unsafeClass, "theUnsafe");
        if (unsafe == null) {
            throw new RuntimeException("Could not find 'theUnsafe' field in the Unsafe class");
        }
    }
}
