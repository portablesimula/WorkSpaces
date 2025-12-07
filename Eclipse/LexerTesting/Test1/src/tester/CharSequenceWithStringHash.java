package tester;


/**
 * This interface implementations must have {@code hashCode} values equal to those for String.
 *
 * @see com.intellij.openapi.util.text.StringUtil#stringHashCode(CharSequence)
 */
//@Internal
public interface CharSequenceWithStringHash extends CharSequence {
  @Override
  int hashCode();
}