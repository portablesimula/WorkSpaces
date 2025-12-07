package tester;

public abstract class ImmutableCharSequence implements CharSequence {

	  ////@Contract(pure = true)
	  public static CharSequence asImmutable(final CharSequence cs) {
	    return isImmutable(cs) ? cs : cs.toString();
	  }

	  private static boolean isImmutable(final CharSequence cs) {
	    return cs instanceof ImmutableCharSequence ||
	           cs instanceof CharSequenceSubSequence && isImmutable(((CharSequenceSubSequence)cs).getBaseSequence());
	  }

	  //@Contract(pure = true)
	  public abstract ImmutableCharSequence concat(CharSequence sequence);

	  //@Contract(pure = true)
	  public abstract ImmutableCharSequence insert(int index, CharSequence seq);

	  //@Contract(pure = true)
	  public abstract ImmutableCharSequence delete(int start, int end);

	  //@Contract(pure = true)
	  public abstract ImmutableCharSequence subtext(int start, int end);

	  //@Contract(pure = true)
	  public ImmutableCharSequence replace(int start, int end, CharSequence seq) {
	    return delete(start, end).insert(start, seq);
	  }

	  @Override
	  public abstract String toString();
	}
