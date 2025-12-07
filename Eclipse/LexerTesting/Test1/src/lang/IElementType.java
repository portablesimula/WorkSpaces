package lang;

/**
 * Interface for token types returned from lexical analysis and for types
 * of nodes in the AST tree. All used element types are added to a registry which
 * can be enumerated or accessed by index.
 *
 * @see lang.lexer.intellij.lexer.Lexer#getTokenType()
 * @see com.intellij.lang.ASTNode#getElementType()
 */
public class IElementType {
  public static final IElementType[] EMPTY_ARRAY = new IElementType[0];
//  public static final ArrayFactory<IElementType> ARRAY_FACTORY = count -> count == 0 ? EMPTY_ARRAY : new IElementType[count];

  /**
   * Default enumeration predicate which matches all token types.
   *
   * @see #enumerate(Predicate)
   */
  //public static final Predicate TRUE = type -> true;

  public static final short FIRST_TOKEN_INDEX = 1;
  //private static final short MAX_INDEXED_TYPES = 15000;

  private static short size; // guarded by lock
  //private static volatile IElementType [] ourRegistry = EMPTY_ARRAY; // writes are guarded by lock
  private static final Object lock = new String("registry lock");

//  static {
//    IElementType[] init = new IElementType[137];
//    // have to start from one for some obscure compatibility reasons
//    init[0] = new IElementType("NULL", Language.ANY, false);
//    push(init);
//  }

//  static IElementType [] push(IElementType [] types) {
//    synchronized (lock) {
//      IElementType[] oldRegistry = ourRegistry;
//      ourRegistry = types;
//      size = (short)ContainerUtil.skipNulls(Arrays.asList(ourRegistry)).size();
//      return oldRegistry;
//    }
//  }

  
//  public static void unregisterElementTypes(ClassLoader loader, PluginDescriptor pluginDescriptor) {
//    for (int i = 0; i < ourRegistry.length; i++) {
//      IElementType type = ourRegistry[i];
//      if (type != null && type.getClass().getClassLoader() == loader) {
//        ourRegistry[i] = TombstoneElementType.create(type, pluginDescriptor);
//      }
//    }
//  }

  
//  public static void unregisterElementTypes(Language language, PluginDescriptor pluginDescriptor) {
//    if (language == Language.ANY) {
//      throw new IllegalArgumentException("Trying to unregister Language.ANY");
//    }
//    for (int i = 0; i < ourRegistry.length; i++) {
//      IElementType type = ourRegistry[i];
//      if (type != null && type.getLanguage().equals(language)) {
//        ourRegistry[i] = TombstoneElementType.create(type, pluginDescriptor);
//      }
//    }
//  }

  private final short myIndex;
  private final String myDebugName;
  private final Language myLanguage;

  /**
   * Creates and registers a new element type for the specified language.
   *
   * @param debugName the name of the element type, used for debugging purposes.
   * @param language  the language with which the element type is associated.
   */
  public IElementType(String debugName, Language language) {
    this(debugName, language, true);

//    if (!(this instanceof IFileElementType)) {
//      LoadingState.COMPONENTS_REGISTERED.checkOccurred();
//    }
  }

  /**
   * Allows constructing element types for some temporary purposes without registering them.
   * This is not default behavior and not recommended. A lot of other functionality (e.g. {@link TokenSet}) won't work with such element types.
   * Please use {@link #IElementType(String, Language)} unless you know what you're doing.
   */
  protected IElementType(String debugName, Language language, boolean register) {
    this.myIndex = 0;
	myDebugName = debugName;
    myLanguage = language == null ? Language.ANY : language;
//    if (register) {
//      synchronized (lock) {
//        //noinspection AssignmentToStaticFieldFromInstanceMethod
//        myIndex = size++;
//
//        IElementType[] newRegistry = myIndex >= ourRegistry.length
//                                     ? ArrayUtil.realloc(ourRegistry, ourRegistry.length * 3 / 2 + 1, ARRAY_FACTORY)
//                                     : ourRegistry;
//        newRegistry[myIndex] = this;
//        //noinspection AssignmentToStaticFieldFromInstanceMethod
//        ourRegistry = newRegistry;
//      }
//
//      checkSizeDoesNotExceedLimit();
//    }
//    else {
//      myIndex = -1;
//    }
  }

  /**
   * Returns the language associated with the element type.
   *
   * @return the associated language.
   */
  public Language getLanguage() {
    return myLanguage;
  }

  /**
   * Returns the index of the element type in the table of all registered element
   * types.
   *
   * @return the element type index.
   */
  public final short getIndex() {
    return myIndex;
  }

  @Override
  public int hashCode() {
    return myIndex >= 0 ? myIndex : super.hashCode();
  }

  @Override
  public String toString() {
    return getDebugName();
  }

  /**
   * Don't use it directly. Override or call {@link IElementType#toString()}.
   * Note, it should be used only for testing and logging purposes.
   */
  
  public String getDebugName() {
    return myDebugName;
  }

  /**
   * Controls whitespace balancing behavior of PsiBuilder.
   * <p>By default, empty composite elements (containing no children) are bounded to the right (previous) neighbour, forming following tree:
   * <pre>
   *  [previous_element]
   *  [whitespace]
   *  [empty_element]
   *    &lt;empty&gt;
   *  [next_element]
   * </pre>
   * <p>Left-bound elements are bounded to the left (next) neighbour instead:
   * <pre>
   *  [previous_element]
   *  [empty_element]
   *    &lt;empty&gt;
   *  [whitespace]
   *  [next_element]
   * </pre>
   * <p>See com.intellij.lang.impl.PsiBuilderImpl.prepareLightTree() for details.
   * @return true if empty elements of this type should be bound to the left.
   */
  public boolean isLeftBound() {
    return false;
  }

  /**
   * Returns the element type registered at the specified index.
   *
   * @param idx the index for which the element type should be returned.
   * @return the element type at the specified index.
   * @throws IndexOutOfBoundsException if the index is out of registered elements' range.
   */
//  public static IElementType find(short idx) {
//    // volatile read; array always grows, never shrinks, never overwritten
//    IElementType type = ourRegistry[idx];
//    if (type instanceof TombstoneElementType) {
//      throw new IllegalArgumentException("Trying to access element type from unloaded plugin: " + type);
//    }
//    if (type == null) {
//      throw new IndexOutOfBoundsException("Element type index " + idx + " is out of range (0.." + (size - 1) + ")");
//    }
//    return type;
//  }

  /**
   * Predicate for matching element types.
   *
   * @see IElementType#enumerate(Predicate)
   */
  @FunctionalInterface
  public interface Predicate {
    boolean matches(IElementType type);
  }

  //@TestOnly
  static short getAllocatedTypesCount() {
    synchronized (lock) {
      return size;
    }
  }

  /**
   * Enumerates all registered token types which match the specified predicate.
   *
   * @param p the predicate which should be matched by the element types.
   * @return the array of matching element types.
   */
//  public static IElementType [] enumerate(Predicate p) {
//    List<IElementType> matches = new ArrayList<>();
//    for (IElementType value : ourRegistry) {
//      if (value != null && p.matches(value)) {
//        matches.add(value);
//      }
//    }
//    return matches.toArray(new IElementType[0]);
//  }

  /**
   * todo IJPL-562 mark experimental?
   *
   * Map all registered token types that match the specified predicate.
   *
   * @param p the predicate which should be matched by the element types.
   * @return the list of matching element types.
   */
  
//  public static <R> List<R> mapNotNull(Function<IElementType, ? extends R> p) {
//    List<R> matches = new ArrayList<>();
//    for (IElementType value : ourRegistry) {
//      if (value != null) {
//        R result = p.apply(value);
//        if (result != null) {
//          matches.add(result);
//        }
//      }
//    }
//    return matches;
//  }

//  private void checkSizeDoesNotExceedLimit() {
//    if (myIndex != MAX_INDEXED_TYPES) {
//      return;
//    }
//
//    ApplicationManager.getApplication().executeOnPooledThread(() -> {
//      int length = MAX_INDEXED_TYPES;
//      IElementType[] registrySnapshot = new IElementType[length];
//      synchronized (lock) {
//        System.arraycopy(ourRegistry, 0, registrySnapshot, 0, length);
//      }
//
//      Map<Language, List<IElementType>> byLang = Stream.of(registrySnapshot)
//        .filter(Objects::nonNull)
//        .collect(Collectors.groupingBy(ie -> ie.myLanguage));
//
//      Map.Entry<Language, List<IElementType>> max = Collections.max(byLang.entrySet(), Comparator.comparingInt(e -> e.getValue().size()));
//
//      List<IElementType> maxTypes = max.getValue();
//      Language maxLanguage = max.getKey();
//      String first300ElementTypes = StringUtil.first(StringUtil.join(maxTypes, ", "), 300, true);
//
//      Logger.getInstance(IElementType.class)
//        .error("Too many element types registered. Out of (short) range. Most of element types (" + maxTypes.size() + ")" +
//               " were registered for '" + maxLanguage + "': " + first300ElementTypes);
//    });
//  }

//  private static final class TombstoneElementType extends IElementType {
//    private TombstoneElementType(String debugName) {
//      super(debugName, Language.ANY);
//    }
//    private static TombstoneElementType create(IElementType type, PluginDescriptor pluginDescriptor) {
//      return new TombstoneElementType("tombstone of " + type +" ("+type.getClass()+") belonged to unloaded "+pluginDescriptor);
//    }
//  }
}