package lang.highlighter;

import java.util.Arrays;
import java.util.Map;

import lang.IElementType;

public abstract class SyntaxHighlighterBase implements SyntaxHighlighter {
//	  private static final Logger LOG = Logger.getInstance(SyntaxHighlighterBase.class);

	  /**
	   * @deprecated Use {@link TextAttributesKey#EMPTY_ARRAY} instead
	   */
	  @Deprecated
	  protected static final TextAttributesKey[] EMPTY = TextAttributesKey.EMPTY_ARRAY;

	  public static TextAttributesKey [] pack(TextAttributesKey key) {
	    return key == null ? TextAttributesKey.EMPTY_ARRAY : new TextAttributesKey[]{key};
	  }

	  public static TextAttributesKey [] pack(TextAttributesKey key1, TextAttributesKey key2) {
	    if (key1 == null) return pack(key2);
	    if (key2 == null) return pack(key1);
	    return new TextAttributesKey[]{key1, key2};
	  }

	  public static TextAttributesKey [] pack(TextAttributesKey [] base, TextAttributesKey key) {
	    if (key == null) return base;
	    TextAttributesKey[] result = Arrays.copyOf(base, base.length + 1);
	    result[base.length] = key;
	    return result;
	  }

	  public static TextAttributesKey [] pack(TextAttributesKey key, TextAttributesKey [] base) {
	    if (key == null) return base;
	    TextAttributesKey[] result = new TextAttributesKey[base.length + 1];
	    System.arraycopy(base, 0, result, 1, base.length);
	    result[0] = key;
	    return result;
	  }

	  public static TextAttributesKey [] pack(TextAttributesKey [] base, TextAttributesKey t1, TextAttributesKey t2) {
	    int add = 0;
	    if (t1 != null) add++;
	    if (t2 != null) add++;
	    if (add == 0) return base;
	    TextAttributesKey[] result = Arrays.copyOf(base, base.length + add);
	    add = base.length;
	    if (t1 != null) result[add++] = t1;
	    if (t2 != null) result[add] = t2;
	    return result;
	  }

	  public static void fillMap(Map<? super IElementType, ? super TextAttributesKey> map, TokenSet keys, TextAttributesKey value) {
	    fillMap(map, value, keys.getTypes());
	  }

	  protected static void fillMap(Map<? super IElementType, ? super TextAttributesKey> map, TextAttributesKey value, IElementType ... types) {
	    for (IElementType type : types) {
	      map.put(type, value);
	    }
	  }

	  /**
	   * Tries to update the map by associating given keys with a given value.
	   * Throws error if the map already contains different mapping for one of given keys.
	   */
	  protected static void safeMap(final Map<IElementType, TextAttributesKey> map,
	                                final TokenSet keys,
	                                final TextAttributesKey value) {
	    for (final IElementType type : keys.getTypes()) {
	      safeMap(map, type, value);
	    }
	  }

	  /**
	   * Tries to update the map by associating given key with a given value.
	   * Throws error if the map already contains different mapping for given key.
	   */
	  protected static void safeMap(final Map<IElementType, TextAttributesKey> map,
	                                final IElementType type,
	                                final TextAttributesKey value) {
	    final TextAttributesKey oldVal = map.put(type, value);
//	    if (oldVal != null && !oldVal.equals(value)) {
//	      LOG.error("Remapping highlighting for \"" + type + "\" val: old=" + oldVal + " new=" + value);
//	    }
	  }
	}
