package lang.editor;

import lang.IElementType;

/**
 * An interface for getting additional information from preceding tokens in {@link RestartableLexer#start(CharSequence, int, int, int, TokenIterator)}
 */
//@ApiStatus.Experimental
public interface TokenIterator {

  /**
   * current token start offset
   */
  int getStartOffset(int index);

  /**
   * current token end offset
   */
  int getEndOffset(int index);

  /**
   * current token type offset
   */
  //@NotNull
  IElementType getType(int index);

  /**
   * current token state
   */
  int getState(int index);

  /**
   * @return number of tokens in document
   */
  int getTokenCount();

  /**
   * @return position on which
   */
  int initialTokenIndex();
}