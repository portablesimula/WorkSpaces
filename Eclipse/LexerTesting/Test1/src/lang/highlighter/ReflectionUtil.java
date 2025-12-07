package lang.highlighter;

import java.lang.classfile.Annotation;
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

public final class ReflectionUtil {
//	  private static final Logger LOG = Logger.getInstance(ReflectionUtil.class);

	  private ReflectionUtil() { }

	  public static List<Field> collectFields(Class<?> clazz) {
	    List<Field> result = new ArrayList<>();
	    for (Class<?> c : JBIterableClassTraverser.classTraverser(clazz)) {
	      Collections.addAll(result, c.getDeclaredFields());
	    }
	    return result;
	  }

	  public static Field findField(Class<?> clazz, final Class<?> type, final String name) throws NoSuchFieldException {
	    Field result = findFieldInHierarchy(clazz, field -> name.equals(field.getName()) && (type == null || field.getType().equals(type)));
	    if (result != null) return result;

	    throw new NoSuchFieldException("Class: " + clazz + " name: " + name + " type: " + type);
	  }

	  public static Field findAssignableField(Class<?> clazz, final Class<?> fieldType, String fieldName) throws NoSuchFieldException {
	    Field result = findFieldInHierarchy(clazz, field -> fieldName.equals(field.getName()) && (fieldType == null || fieldType.isAssignableFrom(field.getType())));
	    if (result != null) {
	      return result;
	    }
	    throw new NoSuchFieldException("Class: " + clazz + " fieldName: " + fieldName + " fieldType: " + fieldType);
	  }

	  public static Field findFieldInHierarchy(Class<?> rootClass,
	                                                     Predicate<? super Field> checker) {
	    for (Class<?> aClass = rootClass; aClass != null; aClass = aClass.getSuperclass()) {
	      for (Field field : aClass.getDeclaredFields()) {
	        if (checker.test(field)) {
	          field.setAccessible(true);
	          return field;
	        }
	      }
	    }

	    // ok, let's check interfaces
	    return processInterfaces(rootClass.getInterfaces(), new HashSet<>(), checker);
	  }

	  private static Field processInterfaces(Class<?> [] interfaces,
	                                                   Set<? super Class<?>> visited,
	                                                   Predicate<? super Field> checker) {
	    for (Class<?> anInterface : interfaces) {
	      if (!visited.add(anInterface)) {
	        continue;
	      }

	      for (Field field : anInterface.getDeclaredFields()) {
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
	    return null;
	  }

	  public static void resetField(Class<?> clazz, Class<?> type, String name)  {
	    try {
	      resetField(null, findField(clazz, type, name));
	    }
	    catch (NoSuchFieldException e) {
	      throw new RuntimeException(e);
	    }
	  }

	  public static void resetField(Object object, String name) {
	    try {
	      resetField(object, findField(object.getClass(), null, name));
	    }
	    catch (NoSuchFieldException e) {
	      throw new RuntimeException(e);
	    }
	  }

	  public static void resetField(final Object object, Field field) {
	    field.setAccessible(true);
	    Class<?> type = field.getType();
	    try {
	      if (type.isPrimitive()) {
	        if (boolean.class.equals(type)) {
	          field.set(object, Boolean.FALSE);
	        }
	        else if (int.class.equals(type)) {
	          field.set(object, 0);
	        }
	        else if (double.class.equals(type)) {
	          field.set(object, (double)0);
	        }
	        else if (float.class.equals(type)) {
	          field.set(object, (float)0);
	        }
	      }
	      else {
	        field.set(object, null);
	      }
	    }
	    catch (IllegalAccessException e) {
	      throw new RuntimeException(e);
	    }
	  }

	  public static Method findMethod(Collection<Method> methods, String name, Class<?> ... parameters) {
	    for (final Method method : methods) {
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

	  public static Method getMethod(Class<?> aClass, String name, Class<?> ... parameters) {
	    try {
	      return makeAccessible(aClass.getMethod(name, parameters));
	    }
	    catch (NoSuchMethodException e) {
	      return null;
	    }
	  }

	  /**
	   * @deprecated Use {@link java.lang.invoke.MethodHandles} instead and try to avoid using of a closed API.
	   * @see java.lang.invoke.MethodHandles
	   * @see java.lang.invoke.MethodHandles.Lookup#findVirtual
	   * @see java.lang.invoke.MethodHandles.Lookup#findStatic
	   * @see com.jetbrains.internal.JBRApi
	   */
	  @Deprecated
	  public static Method getDeclaredMethod(Class<?> aClass, String name, Class<?> ... parameters) {
	    try {
	      return makeAccessible(aClass.getDeclaredMethod(name, parameters));
	    }
	    catch (NoSuchMethodException e) {
	      return null;
	    }
	  }

	  /**
	   * @deprecated Use {@link java.lang.invoke.MethodHandles} instead and try to avoid using of a closed API.
	   * @see java.lang.invoke.MethodHandles
	   * @see java.lang.invoke.MethodHandles.Lookup#findGetter 
	   * @see java.lang.invoke.MethodHandles.Lookup#findSetter  
	   * @see java.lang.invoke.MethodHandles.Lookup#findStaticGetter   
	   * @see java.lang.invoke.MethodHandles.Lookup#findStaticSetter
	   * @see com.jetbrains.internal.JBRApi
	   */
	  @Deprecated
	  public static Field getDeclaredField(Class<?> aClass, String name) {
	    return findFieldInHierarchy(aClass, field -> name.equals(field.getName()));
	  }

	  public static List<Method> getClassPublicMethods(Class<?> aClass) {
	    return filterRealMethods(aClass.getMethods());
	  }

	  public static List<Method> getClassDeclaredMethods(Class<?> aClass) {
	    return filterRealMethods(aClass.getDeclaredMethods());
	  }

	  private static List<Method> filterRealMethods(Method [] methods) {
	    List<Method> result = new ArrayList<>();
	    for (Method method : methods) {
	      if (!method.isSynthetic()) {
	        result.add(method);
	      }
	    }
	    return result;
	  }

	  public static Class<?> getMethodDeclaringClass(Class<?> instanceClass, String methodName, Class<?> ... parameters) {
	    try {
	      return instanceClass.getMethod(methodName, parameters).getDeclaringClass();
	    }
	    catch (NoSuchMethodException ignore) {
	    }

	    while (instanceClass != null) {
	      try {
	        return instanceClass.getDeclaredMethod(methodName, parameters).getDeclaringClass();
	      }
	      catch (NoSuchMethodException ignored) {
	      }
	      instanceClass = instanceClass.getSuperclass();
	    }
	    return null;
	  }

	  public static <T> T getField(Class<?> objectClass, Object object, Class<T> fieldType, String fieldName) {
	    try {
	      Field field = findAssignableField(objectClass, fieldType, fieldName);
	      return getFieldValue(field, object);
	    }
	    catch (NoSuchFieldException e) {
//	      LOG.debug(e);
	      return null;
	    }
	  }

	  public static <T> T getStaticFieldValue(Class<?> objectClass, Class<T> fieldType, String fieldName) {
	    try {
	      final Field field = findAssignableField(objectClass, fieldType, fieldName);
	      if (isInstanceField(field)) {
	        throw new IllegalArgumentException("Field " + objectClass + "." + fieldName + " is not static");
	      }
	      return getFieldValue(field, null);
	    }
	    catch (NoSuchFieldException e) {
//	      LOG.debug(e);
	      return null;
	    }
	  }

	  public static <T> T getFieldValue(Field field, Object object) {
	    try {
	      //noinspection unchecked
	      return (T)field.get(object);
	    }
	    catch (IllegalAccessException e) {
//	      LOG.debug(e);
	      return null;
	    }
	  }

	  public static boolean isInstanceField(Field field) {
	    return !Modifier.isStatic(field.getModifiers());
	  }

	  // returns true if value was set
	  public static <T> boolean setField(Class<?> objectClass,
	                                     Object object,
	                                     Class<T> fieldType,
	                                     String fieldName,
	                                     T value) {
	    try {
	      final Field field = findAssignableField(objectClass, fieldType, fieldName);
	      field.set(object, value);
	      return true;
	    }
	    catch (NoSuchFieldException | IllegalAccessException e) {
//	      LOG.debug(e);
	      // this 'return' was moved into 'catch' block because otherwise reference to common super-class of these exceptions (ReflectiveOperationException)
	      // which doesn't exist in JDK 1.6 will be added to class-file during instrumentation
	      return false;
	    }
	  }

	  public static <T> Constructor<T> getDefaultConstructor(Class<T> aClass) {
	    try {
	      final Constructor<T> constructor = aClass.getConstructor();
	      constructor.setAccessible(true);
	      return constructor;
	    }
	    catch (NoSuchMethodException e) {
	      throw new RuntimeException("No default constructor in " + aClass, e);
	    }
	  }

	  /**
	   * Handles private classes.
	   */
	  public static <T> T newInstance(Class<T> aClass) {
	    return newInstance(aClass, true);
	  }

	  public static <T> T newInstance(Class<T> aClass, boolean isKotlinDataClassesSupported) {
	    try {
	      Constructor<T> constructor = aClass.getDeclaredConstructor();
	      try {
	        constructor.setAccessible(true);
	      }
	      catch (SecurityException ignored) {
	      }
	      return constructor.newInstance();
	    }
	    catch (Exception e) {
	      //noinspection InstanceofCatchParameter
	      if (e instanceof InvocationTargetException) {
	        Throwable targetException = ((InvocationTargetException)e).getTargetException();
	        // handle ExtensionNotApplicableException also (extends ControlFlowException)
	        if (targetException instanceof ControlFlowException && targetException instanceof RuntimeException) {
	          throw (RuntimeException)targetException;
	        }
	      }

	      if (isKotlinDataClassesSupported) {
	        T t = createAsDataClass(aClass);
	        if (t != null) {
	          return t;
	        }
	      }

	      ExceptionUtilRt.rethrowUnchecked(e);
	      throw new RuntimeException(e);
	    }
	  }

	  private static <T> T createAsDataClass(Class<T> aClass) {
	    // support Kotlin data classes - pass null as default value
	    for (java.lang.annotation.Annotation annotation : aClass.getAnnotations()) {
	      String name = annotation.annotationType().getName();
	      if (!name.equals("kotlin.Metadata") && !name.equals("kotlin.jvm.internal.KotlinClass")) {
	        continue;
	      }

	      List<Exception> exceptions = null;
	      Constructor<?>[] constructors = aClass.getDeclaredConstructors();
	      List<Constructor<?>> defaultCtors = new SmartList<>();
	      ctorLoop:
	      for (Constructor<?> constructor : constructors) {
	        try {
	          try {
	            constructor.setAccessible(true);
	          }
	          catch (Throwable ignored) {
	          }

	          if (constructor.getParameterCount() == 0) {
	            //noinspection unchecked
	            return (T) constructor.newInstance();
	          }

	          Class<?>[] parameterTypes = constructor.getParameterTypes();
	          for (Class<?> type : parameterTypes) {
	            if (type.getName().equals("kotlin.jvm.internal.DefaultConstructorMarker")) {
	              defaultCtors.add(constructor);
	              continue ctorLoop;
	            }
	          }

	          //noinspection unchecked
	          return (T)constructor.newInstance(new Object[parameterTypes.length]);
	        }
	        catch (Exception e) {
	          if (exceptions == null) {
	            exceptions = new SmartList<>();
	          }
	          exceptions.add(new Exception("Failed to call constructor: " + constructor.toString(), e));
	        }
	      }

	      for (Constructor<?> constructor : defaultCtors) {
	        try {
	          try {
	            constructor.setAccessible(true);
	          }
	          catch (Throwable ignored) {
	          }

	          //noinspection unchecked
	          return (T)constructor.newInstance();
	        }
	        catch (Exception e) {
	          if (exceptions == null) {
	            exceptions = new SmartList<>();
	          }
	          exceptions.add(new Exception("Failed to call constructor: " + constructor.toString(), e));
	        }
	      }

	      if (exceptions != null) {
	        if (exceptions.size() == 1) {
	          ExceptionUtil.rethrow(exceptions.get(0));
	        }
	        else {
	          ExceptionUtil.rethrow(new CompoundRuntimeException(exceptions));
	        }
	      }
	    }
	    return null;
	  }

	  public static <T> T createInstance(Constructor<T> constructor, Object ... args) {
	    try {
	      return constructor.newInstance(args);
	    }
	    catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	  }

	  public static Class<?> getGrandCallerClass() {
	    int stackFrameCount = 3;
	    return getCallerClass(stackFrameCount+1);
	  }

	  public static Class<?> getCallerClass(int stackFrameCount) {
	    Class<?> callerClass = findCallerClass(stackFrameCount);
	    for (int depth=stackFrameCount+1; callerClass != null && callerClass.getClassLoader() == null; depth++) { // looks like a system class
	      callerClass = findCallerClass(depth);
	    }
	    if (callerClass == null) {
	      callerClass = findCallerClass(stackFrameCount-1);
	    }
	    return callerClass;
	  }

	  public static void copyFields(Field [] fields, Object from, Object to) {
	    copyFields(fields, from, to, null);
	  }

	  public static void copyFields(Field [] fields, Object from, Object to, DifferenceFilter<?> diffFilter) {
	    //noinspection SSBasedInspection
	    Set<Field> sourceFields = new HashSet<>(Arrays.asList(from.getClass().getFields()));
	    for (Field field : fields) {
	      if (sourceFields.contains(field)) {
	        if (isPublic(field) && !isFinal(field)) {
	          try {
	            if (diffFilter == null || diffFilter.test(field)) {
	              copyFieldValue(from, to, field);
	            }
	          }
	          catch (Exception e) {
	            throw new RuntimeException(e);
	          }
	        }
	      }
	    }
	  }

	  public static <T> boolean comparePublicNonFinalFields(T first, T second) {
	    Class<?> defaultClass = first.getClass();
	    Field[] fields = defaultClass.getDeclaredFields();
	    if (defaultClass != second.getClass()) {
	      fields = ArrayUtil.mergeArrays(fields, second.getClass().getDeclaredFields());
	    }
	    for (Field field : fields) {
	      if (!isPublic(field) || isFinal(field)) {
	        continue;
	      }

	      field.setAccessible(true);
	      try {
	        if (!Comparing.equal(field.get(second), field.get(first))) {
	          return false;
	        }
	      }
	      catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	      }
	    }
	    return true;
	  }

	  public static void copyFieldValue(Object from, Object to, Field field)
	    throws IllegalAccessException {
	    Class<?> fieldType = field.getType();
	    if (fieldType.isPrimitive() || fieldType.equals(String.class) || fieldType.isEnum()) {
	      field.set(to, field.get(from));
	    }
	    else {
	      throw new RuntimeException("Field '" + field.getName()+"' not copied: unsupported type: "+field.getType());
	    }
	  }

	  private static boolean isPublic(Field field) {
	    return (field.getModifiers() & Modifier.PUBLIC) != 0;
	  }

	  private static boolean isFinal(Field field) {
	    return (field.getModifiers() & Modifier.FINAL) != 0;
	  }

	  public static Class<?> forName(String fqn) {
	    try {
	      return Class.forName(fqn);
	    }
	    catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	  }

	  public static Class<?> boxType(Class<?> type) {
	    if (!type.isPrimitive()) return type;
	    if (type == boolean.class) return Boolean.class;
	    if (type == byte.class) return Byte.class;
	    if (type == short.class) return Short.class;
	    if (type == int.class) return Integer.class;
	    if (type == long.class) return Long.class;
	    if (type == float.class) return Float.class;
	    if (type == double.class) return Double.class;
	    if (type == char.class) return Character.class;
	    return type;
	  }

	  public static <T,V> Field getTheOnlyVolatileInstanceFieldOfClass(Class<T> ownerClass, Class<V> fieldType) {
	    Field[] declaredFields = ownerClass.getDeclaredFields();
	    Field found = null;
	    for (Field field : declaredFields) {
	      int modifiers = field.getModifiers();
	      if (BitUtil.isSet(modifiers, Modifier.STATIC) || !BitUtil.isSet(modifiers, Modifier.VOLATILE)) {
	        continue;
	      }
	      if (fieldType.isAssignableFrom(field.getType())) {
	        if (found == null) {
	          found = field;
	        }
	        else {
	          throw new IllegalArgumentException("Two fields of "+fieldType+" found in the "+ownerClass+": "+found + " and "+field);
	        }
	      }
	    }
	    if (found == null) {
	      throw new IllegalArgumentException("No (non-static, non-final) field of "+fieldType+" found in the "+ownerClass);
	    }
	    return found;
	  }

	  private static final Object unsafe;
	  static {
	    Class<?> unsafeClass;
	    try {
	      unsafeClass = Class.forName("sun.misc.Unsafe");
	    }
	    catch (ClassNotFoundException e) {
	      throw new RuntimeException(e);
	    }
	    unsafe = getStaticFieldValue(unsafeClass, unsafeClass, "theUnsafe");
	    if (unsafe == null) {
	      throw new RuntimeException("Could not find 'theUnsafe' field in the Unsafe class");
	    }
	  }

	  /**
	   * @deprecated Use {@link java.lang.invoke.VarHandle} or {@link java.util.concurrent.ConcurrentHashMap} or other standard JDK concurrent facilities
	   */
	  //@ApiStatus.Internal
	  @Deprecated
	  //@ApiStatus.ScheduledForRemoval
	  public static Object getUnsafe() {
	    return unsafe;
	  }

	  /**
	   * Returns the class this method was called 'framesToSkip' frames up the caller hierarchy.
	   * NOTE:
	   * <b>Extremely expensive!
	   * Please consider not using it.
	   * These aren't the droids you're looking for!</b>
	   */
	  public static Class<?> findCallerClass(int framesToSkip) {
	    return ReflectionUtilRt.findCallerClass(framesToSkip + 1);
	  }

	  public static boolean isAssignable(Class<?> ancestor, Class<?> descendant) {
	    return ancestor == descendant || ancestor.isAssignableFrom(descendant);
	  }

	  /**
	   * @return concatenated list of field names and values from the {@code object}.
	   */
	  public static String dumpFields(Class<?> objectClass, Object object, String... fieldNames) {
	    List<String> chunks = new SmartList<>();
	    for (String fieldName : fieldNames) {
	      chunks.add(fieldName + "=" + getField(objectClass, object, null, fieldName));
	    }
	    return String.join("; ", chunks);
	  }

	  /**
	   * A convenience type-safe method to create a {@link Proxy} with a single superinterface using the classloader of the specified
	   * super-interface.
	   * 
	   * @param superInterface super-interface
	   * @param handler invocation handler to handle method calls
	   * @return new proxy instance
	   * @param <T> type of the interface to implement
	   * @see Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler) 
	   */
	  public static <T> T proxy(Class<? extends T> superInterface, InvocationHandler handler) {
	    return superInterface.cast(Proxy.newProxyInstance(superInterface.getClassLoader(), new Class[]{superInterface}, handler));
	  }

	  /**
	   * A convenience type-safe method to create a {@link Proxy} with a single superinterface
	   * 
	   * @param loader classloader to use
	   * @param superInterface super-interface
	   * @param handler invocation handler to handle method calls
	   * @return new proxy instance
	   * @param <T> type of the interface to implement
	   * @see Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler) 
	   */
	  public static <T> T proxy(ClassLoader loader, Class<? extends T> superInterface, InvocationHandler handler) {
	    return superInterface.cast(Proxy.newProxyInstance(loader, new Class[]{superInterface}, handler));
	  }
}
