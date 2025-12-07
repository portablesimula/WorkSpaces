package lang.highlighter;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

public final class ArrayUtil {
	  public static final char[] EMPTY_CHAR_ARRAY = ArrayUtilRt.EMPTY_CHAR_ARRAY;
	  public static final byte[] EMPTY_BYTE_ARRAY = ArrayUtilRt.EMPTY_BYTE_ARRAY;
	  public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
	  public static final int[] EMPTY_INT_ARRAY = ArrayUtilRt.EMPTY_INT_ARRAY;
	  public static final Object[] EMPTY_OBJECT_ARRAY = ArrayUtilRt.EMPTY_OBJECT_ARRAY;
	  public static final String[] EMPTY_STRING_ARRAY = ArrayUtilRt.EMPTY_STRING_ARRAY;
	  public static final Class[] EMPTY_CLASS_ARRAY = ArrayUtilRt.EMPTY_CLASS_ARRAY;
	  public static final long[] EMPTY_LONG_ARRAY = ArrayUtilRt.EMPTY_LONG_ARRAY;
	  public static final File[] EMPTY_FILE_ARRAY = ArrayUtilRt.EMPTY_FILE_ARRAY;
	  public static final Runnable[] EMPTY_RUNNABLE_ARRAY = new Runnable[0];
	  /**
	   * @deprecated use {@link com.intellij.openapi.util.text.Strings#EMPTY_CHAR_SEQUENCE} instead
	   */
	  @SuppressWarnings("StringOperationCanBeSimplified") @Deprecated
	  public static final CharSequence EMPTY_CHAR_SEQUENCE = new String();

	  public static final ArrayFactory<String> STRING_ARRAY_FACTORY = ArrayUtil::newStringArray;
	  public static final ArrayFactory<Object> OBJECT_ARRAY_FACTORY = ArrayUtil::newObjectArray;

	  private ArrayUtil() { }

	  //@Contract(pure=true)
	  public static byte [] realloc(byte [] array, int newSize) {
	    if (newSize == 0) {
	      return EMPTY_BYTE_ARRAY;
	    }

	    return array.length == newSize ? array : Arrays.copyOf(array, newSize);
	  }
	  //@Contract(pure=true)
	  public static double [] realloc(double [] array, int newSize) {
	    if (newSize == 0) {
	      return EMPTY_DOUBLE_ARRAY;
	    }

	    return array.length == newSize ? array : Arrays.copyOf(array, newSize);
	  }

	  //@Contract(pure=true)
	  public static boolean [] realloc(boolean [] array, int newSize) {
	    if (newSize == 0) {
	      return ArrayUtilRt.EMPTY_BOOLEAN_ARRAY;
	    }

	    return array.length == newSize ? array : Arrays.copyOf(array, newSize);
	  }

	  //@Contract(pure=true)
	  public static short [] realloc(short [] array, int newSize) {
	    if (newSize == 0) {
	      return ArrayUtilRt.EMPTY_SHORT_ARRAY;
	    }

	    return array.length == newSize ? array : Arrays.copyOf(array, newSize);
	  }

	  //@Contract(pure=true)
	  public static long [] realloc(long [] array, int newSize) {
	    if (newSize == 0) {
	      return EMPTY_LONG_ARRAY;
	    }

	    return array.length == newSize ? array : Arrays.copyOf(array, newSize);
	  }

	  //@Contract(pure=true)
	  public static int [] realloc(int [] array, int newSize) {
	    if (newSize == 0) {
	      return EMPTY_INT_ARRAY;
	    }

	    return array.length == newSize ? array : Arrays.copyOf(array, newSize);
	  }

	  //@Contract(pure=true)
	  public static <T> T [] realloc(T [] array, int newSize, ArrayFactory<? extends T> factory) {
	    int oldSize = array.length;
	    if (oldSize == newSize) {
	      return array;
	    }

	    T[] result = factory.create(newSize);
	    if (newSize == 0) {
	      return result;
	    }

	    System.arraycopy(array, 0, result, 0, Math.min(oldSize, newSize));
	    return result;
	  }

	  //@Contract(pure=true)
	  public static long [] append(long [] array, long value) {
	    array = realloc(array, array.length + 1);
	    array[array.length - 1] = value;
	    return array;
	  }
	  //@Contract(pure=true)
	  public static int [] append(int [] array, int value) {
	    array = realloc(array, array.length + 1);
	    array[array.length - 1] = value;
	    return array;
	  }

	  //@Contract(pure=true)
	  public static <T> T [] insert(T [] array, int index, T value) {
	    T[] result = newArray(getComponentType(array), array.length + 1);
	    System.arraycopy(array, 0, result, 0, index);
	    result[index] = value;
	    System.arraycopy(array, index, result, index + 1, array.length - index);
	    return result;
	  }

	  //@Contract(pure=true)
	  public static int [] insert(int [] array, int index, int value) {
	    int[] result = new int[array.length + 1];
	    System.arraycopy(array, 0, result, 0, index);
	    result[index] = value;
	    System.arraycopy(array, index, result, index+1, array.length - index);
	    return result;
	  }
	  //@Contract(pure=true)
	  public static long [] insert(long [] array, int index, long value) {
	    long[] result = new long[array.length + 1];
	    System.arraycopy(array, 0, result, 0, index);
	    result[index] = value;
	    System.arraycopy(array, index, result, index+1, array.length - index);
	    return result;
	  }
	  //@Contract(pure=true)
	  public static char [] insert(char [] array, int index, char value) {
	    char[] result = new char[array.length + 1];
	    System.arraycopy(array, 0, result, 0, index);
	    result[index] = value;
	    System.arraycopy(array, index, result, index+1, array.length - index);
	    return result;
	  }

	  //@Contract(pure=true)
	  public static byte [] append(byte [] array, byte value) {
	    array = realloc(array, array.length + 1);
	    array[array.length - 1] = value;
	    return array;
	  }
	  //@Contract(pure=true)
	  public static boolean [] append(boolean [] array, boolean value) {
	    array = realloc(array, array.length + 1);
	    array[array.length - 1] = value;
	    return array;
	  }

	  //@Contract(pure=true)
	  public static char [] realloc(char [] array, int newSize) {
	    if (newSize == 0) {
	      return ArrayUtilRt.EMPTY_CHAR_ARRAY;
	    }

	    int oldSize = array.length;
	    return oldSize == newSize ? array : Arrays.copyOf(array, newSize);
	  }

	  //@Contract(pure=true)
	  public static <T> T [] toObjectArray(Collection<? extends T> collection, Class<T> aClass) {
	    T[] array = newArray(aClass, collection.size());
	    return collection.toArray(array);
	  }

	  //@Contract(pure=true)
	  public static <T> T [] toObjectArray(Class<T> aClass, Object ... source) {
	    T[] array = newArray(aClass, source.length);
	    //noinspection SuspiciousSystemArraycopy
	    System.arraycopy(source, 0, array, 0, array.length);
	    return array;
	  }

	  //@Contract(pure=true)
	  public static Object [] toObjectArray(Collection<?> collection) {
	    return collection.toArray(ArrayUtilRt.EMPTY_OBJECT_ARRAY);
	  }

	  //@Contract(pure=true)
	  public static int [] toIntArray(Collection<Integer> list) {
	    int[] ret = newIntArray(list.size());
	    int i = 0;
	    for (Integer e : list) {
	      ret[i++] = e;
	    }
	    return ret;
	  }

	  //@Contract(pure=true)
	  public static <T> T [] mergeArrays(T [] a1, T [] a2) {
	    if (a1.length == 0) {
	      return a2;
	    }
	    if (a2.length == 0) {
	      return a1;
	    }

	    Class<T> class1 = getComponentType(a1);
	    Class<T> class2 = getComponentType(a2);
	    Class<T> aClass = class1.isAssignableFrom(class2) ? class1 : class2;

	    T[] result = newArray(aClass, a1.length + a2.length);
	    System.arraycopy(a1, 0, result, 0, a1.length);
	    System.arraycopy(a2, 0, result, a1.length, a2.length);
	    return result;
	  }

	  //@Contract(pure=true)
	  public static <T> T [] mergeCollections(Collection<? extends T> c1, Collection<? extends T> c2, ArrayFactory<? extends T> factory) {
	    T[] res = factory.create(c1.size() + c2.size());

	    int i = 0;

	    for (T t : c1) {
	      res[i++] = t;
	    }

	    for (T t : c2) {
	      res[i++] = t;
	    }

	    return res;
	  }

	  //@Contract(pure=true)
	  public static <T> T [] mergeArrays(T [] a1, T [] a2, ArrayFactory<? extends T> factory) {
	    if (a1.length == 0) {
	      return a2;
	    }
	    if (a2.length == 0) {
	      return a1;
	    }
	    T[] result = factory.create(a1.length + a2.length);
	    System.arraycopy(a1, 0, result, 0, a1.length);
	    System.arraycopy(a2, 0, result, a1.length, a2.length);
	    return result;
	  }

	  //@Contract(pure=true)
	  public static String [] mergeArrays(String [] a1, String ... a2) {
	    return mergeArrays(a1, a2, STRING_ARRAY_FACTORY);
	  }

	  //@Contract(pure=true)
	  public static int [] mergeArrays(int [] a1, int [] a2) {
	    if (a1.length == 0) {
	      return a2;
	    }
	    if (a2.length == 0) {
	      return a1;
	    }
	    int[] result = new int[a1.length + a2.length];
	    System.arraycopy(a1, 0, result, 0, a1.length);
	    System.arraycopy(a2, 0, result, a1.length, a2.length);
	    return result;
	  }

	  //@Contract(pure=true)
	  public static byte [] mergeArrays(byte [] a1, byte [] a2) {
	    if (a1.length == 0) {
	      return a2;
	    }
	    if (a2.length == 0) {
	      return a1;
	    }
	    byte[] result = new byte[a1.length + a2.length];
	    System.arraycopy(a1, 0, result, 0, a1.length);
	    System.arraycopy(a2, 0, result, a1.length, a2.length);
	    return result;
	  }

	  //@Contract(pure=true)
	  public static int[] intersection(int [] a1, int [] a2) {
	    if (a1.length == 0) {
	      return a2;
	    }
	    if (a2.length == 0) {
	      return a1;
	    }
	    IntSet result = new IntOpenHashSet(a1);
	    result.retainAll(new IntArrayList(a2));
	    return result.toIntArray();
	  }

	  /**
	   * Allocates new array of size {@code array.length + collection.size()} and copies elements of {@code array} and
	   * {@code collection} to it.
	   *
	   * @param array      source array
	   * @param collection source collection
	   * @param factory    array factory used to create destination array of type {@code T}
	   * @return destination array
	   */
	  //@Contract(pure=true)
	  public static <T> T [] mergeArrayAndCollection(T [] array,
	                                                          Collection<? extends T> collection,
	                                                          ArrayFactory<? extends T> factory) {
	    if (collection.isEmpty()) {
	      return array;
	    }

	    T[] array2;
	    try {
	      T[] a = factory.create(collection.size());
	      array2 = collection.toArray(a);
	    }
	    catch (ArrayStoreException e) {
	      throw new RuntimeException("Bad elements in collection: " + collection, e);
	    }

	    if (array.length == 0) {
	      return array2;
	    }

	    T[] result = factory.create(array.length + collection.size());
	    System.arraycopy(array, 0, result, 0, array.length);
	    System.arraycopy(array2, 0, result, array.length, array2.length);
	    return result;
	  }

	  /**
	   * Appends {@code element} to the {@code src} array. As you can
	   * imagine the appended element will be the last one in the returned result.
	   *
	   * @param src     array to which the {@code element} should be appended.
	   * @param element object to be appended to the end of {@code src} array.
	   * @return new array
	   */
	  //@Contract(pure=true)
	  public static <T> T [] append(T [] src, T element) {
	    return append(src, element, getComponentType(src));
	  }

	  //@Contract(pure=true)
	  public static <T> T [] prepend(T element, T [] array) {
	    return prepend(element, array, getComponentType(array));
	  }

	  //@Contract(pure=true)
	  public static <T> T [] prepend(T element, T [] array, Class<T> type) {
	    int length = array.length;
	    T[] result = newArray(type, length + 1);
	    System.arraycopy(array, 0, result, 1, length);
	    result[0] = element;
	    return result;
	  }

	  //@Contract(pure=true)
	  public static <T> T [] prepend(T element, T [] src, ArrayFactory<? extends T> factory) {
	    int length = src.length;
	    T[] result = factory.create(length + 1);
	    System.arraycopy(src, 0, result, 1, length);
	    result[0] = element;
	    return result;
	  }

	  //@Contract(pure=true)
	  public static byte [] prepend(byte element, byte [] array) {
	    int length = array.length;
	    byte[] result = new byte[length + 1];
	    result[0] = element;
	    System.arraycopy(array, 0, result, 1, length);
	    return result;
	  }

	  //@Contract(pure=true)
	  public static <T> T [] append(T [] src, T element, ArrayFactory<? extends T> factory) {
	    int length = src.length;
	    T[] result = factory.create(length + 1);
	    System.arraycopy(src, 0, result, 0, length);
	    result[length] = element;
	    return result;
	  }

	  //@Contract(pure=true)
	  public static <T> T [] append(T [] src, T element, Class<T> componentType) {
	    int length = src.length;
	    T[] result = newArray(componentType, length + 1);
	    System.arraycopy(src, 0, result, 0, length);
	    result[length] = element;
	    return result;
	  }

	  /**
	   * Removes element with index {@code idx} from array {@code src}.
	   *
	   * @param src array.
	   * @param idx index of element to be removed.
	   * @return modified array.
	   */
	  //@Contract(pure=true)
	  public static <T> T [] remove(T [] src, int idx) {
	    int length = src.length;
	    if (idx < 0 || idx >= length) {
	      throw new IllegalArgumentException("invalid index: " + idx);
	    }
	    Class<T> type = getComponentType(src);
	    T[] result = newArray(type, length - 1);
	    System.arraycopy(src, 0, result, 0, idx);
	    System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
	    return result;
	  }

	  public static <T> T [] newArray(Class<T> type, int length) {
	    //noinspection unchecked
	    return (T[])Array.newInstance(type, length);
	  }

	  //@Contract(pure=true)
	  public static <T> T [] remove(T [] src, int idx, ArrayFactory<? extends T> factory) {
	    int length = src.length;
	    if (idx < 0 || idx >= length) {
	      throw new IllegalArgumentException("invalid index: " + idx);
	    }
	    T[] result = factory.create(length - 1);
	    System.arraycopy(src, 0, result, 0, idx);
	    System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
	    return result;
	  }

	  //@Contract(pure=true)
	  public static <T> T [] remove(T [] src, T element) {
	    int index = ArrayUtilRt.find(src, element);
	    return index == -1 ? src : remove(src, index);
	  }

	  //@Contract(pure=true)
	  public static <T> T [] remove(T [] src, T element, ArrayFactory<? extends T> factory) {
	    int idx = find(src, element);
	    if (idx == -1) return src;

	    return remove(src, idx, factory);
	  }

	  //@Contract(pure=true)
	  public static int [] remove(int [] src, int idx) {
	    int length = src.length;
	    if (idx < 0 || idx >= length) {
	      throw new IllegalArgumentException("invalid index: " + idx);
	    }
	    int[] result = newIntArray(src.length - 1);
	    System.arraycopy(src, 0, result, 0, idx);
	    System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
	    return result;
	  }
	  //@Contract(pure=true)
	  public static short [] remove(short [] src, int idx) {
	    int length = src.length;
	    if (idx < 0 || idx >= length) {
	      throw new IllegalArgumentException("invalid index: " + idx);
	    }
	    short[] result = src.length == 1 ? ArrayUtilRt.EMPTY_SHORT_ARRAY : new short[src.length - 1];
	    System.arraycopy(src, 0, result, 0, idx);
	    System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
	    return result;
	  }

	  //@Contract(pure=true)
	  public static int find(int [] src, int obj) {
	    return indexOf(src, obj);
	  }

	  //@Contract(pure=true)
	  public static <T> int find(T [] src, T obj) {
	    return ArrayUtilRt.find(src, obj);
	  }

	  //@Contract(pure=true)
	  public static boolean startsWith(byte [] array, byte [] prefix) {
	    //noinspection ArrayEquality
	    if (array == prefix) {
	      return true;
	    }
	    int length = prefix.length;
	    if (array.length < length) {
	      return false;
	    }

	    for (int i = 0; i < length; i++) {
	      if (array[i] != prefix[i]) {
	        return false;
	      }
	    }

	    return true;
	  }

	  //@Contract(pure=true)
	  public static <E> boolean startsWith(E [] array, E [] subArray) {
	    //noinspection ArrayEquality
	    if (array == subArray) {
	      return true;
	    }
	    int length = subArray.length;
	    if (array.length < length) {
	      return false;
	    }

	    for (int i = 0; i < length; i++) {
	      if (!Comparing.equal(array[i], subArray[i])) {
	        return false;
	      }
	    }

	    return true;
	  }

	  //@Contract(pure=true)
	  public static boolean startsWith(byte [] array, int start, byte [] subArray) {
	    int length = subArray.length;
	    if (array.length - start < length) {
	      return false;
	    }

	    for (int i = 0; i < length; i++) {
	      if (array[start + i] != subArray[i]) {
	        return false;
	      }
	    }

	    return true;
	  }

	  //@Contract(pure=true)
	  public static <T> T [] reverseArray(T [] array) {
	    T[] newArray = array.clone();
	    for (int i = 0; i < array.length; i++) {
	      newArray[array.length - i - 1] = array[i];
	    }
	    return newArray;
	  }

	  //@Contract(pure=true)
	  public static int [] reverseArray(int [] array) {
	    int[] newArray = array.clone();
	    for (int i = 0; i < array.length; i++) {
	      newArray[array.length - i - 1] = array[i];
	    }
	    return newArray;
	  }

	  //@Contract(pure=true)
	  public static int lexicographicCompare(String [] obj1, String [] obj2) {
	    for (int i = 0; i < Math.max(obj1.length, obj2.length); i++) {
	      String o1 = i < obj1.length ? obj1[i] : null;
	      String o2 = i < obj2.length ? obj2[i] : null;
	      if (o1 == null) return -1;
	      if (o2 == null) return 1;
	      int res = o1.compareToIgnoreCase(o2);
	      if (res != 0) return res;
	    }
	    return 0;
	  }

	  //@Contract(pure=true)
	  public static int lexicographicCompare(int [] obj1, int [] obj2) {
	    for (int i = 0; i < Math.min(obj1.length, obj2.length); i++) {
	      int res = Integer.compare(obj1[i], obj2[i]);
	      if (res != 0) return res;
	    }
	    return Integer.compare(obj1.length, obj2.length);
	  }

	  //must be Comparables
	  //@Contract(pure=true)
	  public static <T extends Comparable<T>> int lexicographicCompare(T [] obj1, T [] obj2) {
	    for (int i = 0; i < Math.max(obj1.length, obj2.length); i++) {
	      T o1 = i < obj1.length ? obj1[i] : null;
	      T o2 = i < obj2.length ? obj2[i] : null;
	      if (o1 == null) return -1;
	      if (o2 == null) return 1;
	      int res = o1.compareTo(o2);
	      if (res != 0) return res;
	    }
	    return 0;
	  }

	  public static <T> void swap(T [] array, int i1, int i2) {
	    T t = array[i1];
	    array[i1] = array[i2];
	    array[i2] = t;
	  }

	  public static void swap(int [] array, int i1, int i2) {
	    int t = array[i1];
	    array[i1] = array[i2];
	    array[i2] = t;
	  }

	  public static void swap(boolean [] array, int i1, int i2) {
	    boolean t = array[i1];
	    array[i1] = array[i2];
	    array[i2] = t;
	  }

	  public static void swap(char [] array, int i1, int i2) {
	    char t = array[i1];
	    array[i1] = array[i2];
	    array[i2] = t;
	  }

	  public static <T> void rotateLeft(T [] array, int i1, int i2) {
	    T t = array[i1];
	    System.arraycopy(array, i1 + 1, array, i1, i2 - i1);
	    array[i2] = t;
	  }

	  public static <T> void rotateRight(T [] array, int i1, int i2) {
	    T t = array[i2];
	    System.arraycopy(array, i1, array, i1 + 1, i2 - i1);
	    array[i1] = t;
	  }

	  /**
	   * @param objects array to search in
	   * @param object an object to search
	   * @return an index of the first occurrence of an {@code object} inside the {@code objects} array, or -1, if no object is found.
	   * Objects are compared using {@link Object#equals(Object)}.
	   */
	  //@Contract(pure=true)
	  public static int indexOf(Object [] objects, Object object) {
	    return ArrayUtilRt.indexOf(objects, object, 0, objects.length);
	  }

	  //@Contract(pure=true)
	  public static <T> int indexOf(List<? extends T> objects, T object, BiPredicate<? super T, ? super T> predicate) {
	    for (int i = 0; i < objects.size(); i++) {
	      if (predicate.test(objects.get(i), object)) {
	        return i;
	      }
	    }
	    return -1;
	  }

	  /**
	   * @param objects array to search in
	   * @param object an object to search
	   * @param comparator a predicate which returns true for equal objects
	   * @param <T> type of the objects in the array
	   * @return an index of the first occurrence of an {@code object} inside the {@code objects} array, according to the comparator.
	   * Returns -1, if no object is found.
	   */
	  //@Contract(pure=true)
	  public static <T> int indexOf(T [] objects, T object, BiPredicate<? super T, ? super T> comparator) {
	    for (int i = 0; i < objects.length; i++) {
	      if (comparator.test(objects[i], object)) {
	        return i;
	      }
	    }
	    return -1;
	  }

	  //@Contract(pure=true)
	  public static int indexOf(long [] ints, long value) {
	    for (int i = 0; i < ints.length; i++) {
	      if (ints[i] == value) return i;
	    }
	    return -1;
	  }

	  //@Contract(pure=true)
	  public static int indexOf(int [] ints, int value) {
	    for (int i = 0; i < ints.length; i++) {
	      if (ints[i] == value) return i;
	    }
	    return -1;
	  }

	  //@Contract(pure = true)
	  public static int indexOf(byte [] array, byte [] pattern, int startIndex) {
	    for (int i = startIndex; i <= array.length - pattern.length; i++) {
	      if (startsWith(array, i, pattern)) {
	        return i;
	      }
	    }
	    return -1;
	  }

	  //@Contract(pure=true)
	  public static <T> int lastIndexOf(T [] src, T obj) {
	    for (int i = src.length - 1; i >= 0; i--) {
	      T o = src[i];
	      if (o == null) {
	        if (obj == null) {
	          return i;
	        }
	      }
	      else {
	        if (o.equals(obj)) {
	          return i;
	        }
	      }
	    }
	    return -1;
	  }

	  //@Contract(pure=true)
	  public static int lastIndexOf(int [] src, int obj) {
	    for (int i = src.length - 1; i >= 0; i--) {
	      int o = src[i];
	      if (o == obj) {
	        return i;
	      }
	    }
	    return -1;
	  }

	  //@Contract(pure=true)
	  public static int lastIndexOfNot(int [] src, int obj) {
	    for (int i = src.length - 1; i >= 0; i--) {
	      int o = src[i];
	      if (o != obj) {
	        return i;
	      }
	    }
	    return -1;
	  }

	  @SafeVarargs
	  //@Contract(pure=true)
	  public static <T> boolean contains(T o, T ... objects) {
	    return indexOf(objects, o) >= 0;
	  }

	  //@Contract(pure = true)
	  public static boolean contains(String s, String ... strings) {
	    return indexOf(strings, s) >= 0;
	  }

	  //@Contract(pure=true)
	  public static int [] newIntArray(int count) {
	    return count == 0 ? ArrayUtilRt.EMPTY_INT_ARRAY : new int[count];
	  }
	  //@Contract(pure=true)
	  public static byte [] newByteArray(int count) {
	    return count == 0 ? ArrayUtilRt.EMPTY_BYTE_ARRAY : new byte[count];
	  }

	  //@Contract(pure=true)
	  public static long [] newLongArray(int count) {
	    return count == 0 ? EMPTY_LONG_ARRAY : new long[count];
	  }

	  //@Contract(pure=true)
	  public static String [] newStringArray(int count) {
	    return count == 0 ? ArrayUtilRt.EMPTY_STRING_ARRAY : new String[count];
	  }

	  //@Contract(pure=true)
	  public static Object [] newObjectArray(int count) {
	    return count == 0 ? ArrayUtilRt.EMPTY_OBJECT_ARRAY : new Object[count];
	  }

	  //@Contract(pure=true)
	  public static <E> E [] ensureExactSize(int count, E [] sample) {
	    if (count == sample.length) return sample;
	    return newArray(getComponentType(sample), count);
	  }

	  /**
	   * Returns the first element of the given array, or null if the array is null or empty.
	   *
	   * @param array the array from which to retrieve the first element, may be null.
	   * @return the first element of the array, or null if the array is null or empty.
	   */
	  //@Contract(value = "null -> null", pure = true)
	  public static <T> T getFirstElement(T [] array) {
	    return array != null && array.length > 0 ? array[0] : null;
	  }

	  /**
	   * Returns the last element of the provided array.
	   *
	   * @param array the array from which the last element is requested. It can be null.
	   * @return the last element of the array, or null if the array is null or empty.
	   */
	  //@Contract(value = "null -> null", pure=true)
	  public static <T> T getLastElement(T [] array) {
	    return array != null && array.length > 0 ? array[array.length - 1] : null;
	  }

	  /**
	   * Returns the last element of the given array. If the array is null or empty,
	   * the default value is returned.
	   *
	   * @param array the array from which to get the last element
	   * @param defaultValue the value to be returned if the array is null or empty
	   * @return the last element of the array, or the default value if the array is null or empty
	   */
	  //@Contract(pure=true)
	  public static int getLastElement(int [] array, int defaultValue) {
	    return array == null || array.length == 0 ? defaultValue : array[array.length - 1];
	  }

	  /**
	   * Checks if the given array is null or has no elements.
	   *
	   * @param array the array to check
	   * @return true if the array is null or empty, false otherwise
	   */
	  //@Contract(value = "null -> true", pure=true)
	  public static <T> boolean isEmpty(T [] array) {
	    return array == null || array.length == 0;
	  }

	  //@Contract(pure=true)
	  public static String [] toStringArray(Collection<String> collection) {
	    return ArrayUtilRt.toStringArray(collection);
	  }

	  public static <T> void copy(Collection<? extends T> src, T [] dst, int dstOffset) {
	    int i = dstOffset;
	    for (T t : src) {
	      dst[i++] = t;
	    }
	  }

	  //@Contract(value = "null -> null; !null -> !null", pure = true)
	  public static <T> T [] copyOf(T [] original) {
	    if (original == null) return null;
	    return original.clone();
	  }

	  //@Contract(value = "null -> null; !null -> !null", pure = true)
	  public static boolean [] copyOf(boolean [] original) {
	    if (original == null) return null;
	    return original.length == 0 ? ArrayUtilRt.EMPTY_BOOLEAN_ARRAY : original.clone();
	  }

	  //@Contract(value = "null -> null; !null -> !null", pure = true)
	  public static int [] copyOf(int [] original) {
	    if (original == null) return null;
	    return original.length == 0 ? ArrayUtilRt.EMPTY_INT_ARRAY : original.clone();
	  }

	  //@Contract(value = "null -> null; !null -> !null", pure = true)
	  public static byte [] copyOf(byte [] original) {
	    if (original == null) return null;
	    return original.length == 0 ? ArrayUtilRt.EMPTY_BYTE_ARRAY : original.clone();
	  }

	  //@Contract(pure = true)
	  public static <T> T [] stripTrailingNulls(T [] array) {
	    return array.length != 0 && array[array.length-1] == null ? Arrays.copyOf(array, trailingNullsIndex(array)) : array;
	  }

	  private static <T> int trailingNullsIndex(T [] array) {
	    for (int i = array.length - 1; i >= 0; i--) {
	      if (array[i] != null) {
	        return i + 1;
	      }
	    }
	    return 0;
	  }

	  // calculates average of the median values in the selected part of the array. E.g. for part=3 returns average in the middle third.
	  public static long averageAmongMedians(long [] time, int part) {
	    assert part >= 1;
	    int n = time.length;
	    Arrays.sort(time);
	    long total = 0;
	    int start = n / 2 - n / part / 2;
	    int end = n / 2 + n / part / 2;
	    for (int i = start; i < end; i++) {
	      total += time[i];
	    }
	    int middlePartLength = end - start;
	    return middlePartLength == 0 ? 0 : total / middlePartLength;
	  }

	  public static long averageAmongMedians(int [] time, int part) {
	    assert part >= 1;
	    int n = time.length;
	    Arrays.sort(time);
	    long total = 0;
	    int start = n / 2 - n / part / 2;
	    int end = n / 2 + n / part / 2;
	    for (int i = start; i < end; i++) {
	      total += time[i];
	    }
	    int middlePartLength = end - start;
	    return middlePartLength == 0 ? 0 : total / middlePartLength;
	  }

	  //@Contract(pure = true)
	  public static int min(int [] values) {
	    int min = Integer.MAX_VALUE;
	    for (int value : values) {
	      if (value < min) min = value;
	    }
	    return min;
	  }

	  //@Contract(pure = true)
	  public static int max(int [] values) {
	    int max = Integer.MIN_VALUE;
	    for (int value : values) {
	      if (value > max) max = value;
	    }
	    return max;
	  }
	  //@Contract(pure = true)
	  public static double max(double [] values) {
	    double max = Double.NEGATIVE_INFINITY;
	    for (double value : values) {
	      if (value > max) max = value;
	    }
	    return max;
	  }

	  //@Contract(pure = true)
	  public static int[] mergeSortedArrays(int [] a1, int [] a2, boolean mergeEqualItems) {
	    int newSize = a1.length + a2.length;
	    if (newSize == 0) return ArrayUtilRt.EMPTY_INT_ARRAY;
	    int[] r = new int[newSize];
	    int o = 0;
	    int index1 = 0;
	    int index2 = 0;
	    while (index1 < a1.length || index2 < a2.length) {
	      int e;
	      if (index1 >= a1.length) {
	        e = a2[index2++];
	      }
	      else if (index2 >= a2.length) {
	        e = a1[index1++];
	      }
	      else {
	        int element1 = a1[index1];
	        int element2 = a2[index2];
	        if (element1 == element2) {
	          index1++;
	          index2++;
	          if (mergeEqualItems) {
	            e = element1;
	          }
	          else {
	            r[o++] = element1;
	            e = element2;
	          }
	        }
	        else if (element1 < element2) {
	          e = element1;
	          index1++;
	        }
	        else {
	          e = element2;
	          index2++;
	        }
	      }
	      r[o++] = e;
	    }

	    return o == newSize ? r : Arrays.copyOf(r, o);
	  }

	  public static <T> Class<T> getComponentType(T [] collection) {
	    //noinspection unchecked
	    return (Class<T>)collection.getClass().getComponentType();
	  }

	  //@Contract(pure=true)
	  public static <T> int indexOfIdentity(T [] array, T element) {
	    for (int i = 0; i < array.length; i++) {
	      if (array[i] == element) {
	        return i;
	      }
	    }
	    return -1;
	  }

	  /**
	   * Checks the equality of two arrays, according to a custom condition. Like {@code Arrays#equals} but doesn't
	   * require a comparator.
	   *
	   * @param arr1 first array
	   * @param arr2 second array
	   * @param equalityCondition BiPredicate that returns true if two elements are considered to be equal. Must return true
	   *                          if both arguments are the same object.
	   * @return true if both arrays are equal in terms of equalityCondition
	   * @param <T> type of array elements
	   */
	  public static <T> boolean areEqual(T [] arr1, T [] arr2, BiPredicate<? super T, ? super T> equalityCondition) {
	    if (arr1 == arr2) return true;
	    if (arr1.length != arr2.length) return false;
	    for (int i = 0; i < arr1.length; i++) {
	      if (!equalityCondition.test(arr1[i], arr2[i])) return false;
	    }
	    return true;
	  }
	}
