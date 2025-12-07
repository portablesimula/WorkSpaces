package lang.lexer;

import lang.IElementType;

//import java.lang.System.Logger;

/**
 * Interface for breaking a file into a sequence of tokens.
 *
 * @see LexerBase for certain methods' implementation
 * @see <a href="https://plugins.jetbrains.com/docs/intellij/implementing-lexer.html">Implementing Lexer (IntelliJ Platform Docs)</a>
 * @see RestartableLexer
 */
public abstract class Lexer {
//  private static final Logger LOG = Logger.getInstance(Lexer.class);
//  private static final long LEXER_START_THRESHOLD = 500;

  /**
   * Prepare for lexing character data from {@code buffer} passed. Internal lexer state is supposed to be {@code initialState}. It is guaranteed
   * that the value of initialState is the same as returned by {@link #getState()} method of this lexer at condition {@code startOffset=getTokenStart()}.
   * This method is used to incrementally re-lex changed characters using lexing data acquired from this particular lexer sometime in the past.
   *
   * @param buffer       character data for lexing.
   * @param startOffset  offset to start lexing from
   * @param endOffset    offset to stop lexing at
   * @param initialState the initial state of the lexer.
   */
  public abstract void start(CharSequence buffer, int startOffset, int endOffset, int initialState);

//  private void startMeasured(CharSequence buffer, int startOffset, int endOffset, int initialState) {
//    if (!LOG.isDebugEnabled()) {
//      start(buffer, startOffset, endOffset, initialState);
//      return;
//    }
//    long start = System.currentTimeMillis();
//    start(buffer, startOffset, endOffset, initialState);
//    long startDuration = System.currentTimeMillis() - start;
//    if (startDuration > LEXER_START_THRESHOLD) {
//      LOG.debug("Starting lexer took: ", startDuration,
//                "; at ", startOffset, " - ", endOffset, "; state: ", initialState,
//                "; text: ", StringUtil.shortenTextWithEllipsis(buffer.toString(), 1024, 500)
//      );
//    }
//  }

  public final void start(CharSequence buf, int start, int end) {
//    startMeasured(buf, start, end, 0);
  }

  public final void start(CharSequence buf) {
//    startMeasured(buf, 0, buf.length(), 0);
  }

  public CharSequence getTokenSequence() {
    return getBufferSequence().subSequence(getTokenStart(), getTokenEnd());
  }

  public String getTokenText() {
    return getTokenSequence().toString();
  }

  /**
   * Returns the current state of the lexer.
   *
   * @return the lexer state.
   */
  public abstract int getState();

  /**
   * Returns the token at the current position of the lexer or {@code null} if lexing is finished.
   *
   * @return the current token.
   */
  public abstract IElementType getTokenType();

  /**
   * Returns the start offset of the current token.
   *
   * @return the current token start offset.
   */
  public abstract int getTokenStart();

  /**
   * Returns the end offset of the current token.
   *
   * @return the current token end offset.
   */

  public abstract int getTokenEnd();

  /**
   * Advances the lexer to the next token.
   */
  public abstract void advance();

  /**
   * Returns the current position and state of the lexer.
   *
   * @return the lexer position and state.
   */
  public abstract LexerPosition getCurrentPosition();

  /**
   * Restores the lexer to the specified state and position.
   *
   * @param position the state and position to restore to.
   */
  public abstract void restore(LexerPosition position);

  /**
   * Returns the buffer sequence over which the lexer is running. This method should return the
   * same buffer instance which was passed to the {@code start()} method.
   *
   * @return the lexer buffer.
   */
  public abstract CharSequence getBufferSequence();

  /**
   * Returns the offset at which the lexer will stop lexing. This method should return
   * the length of the buffer or the value passed in the {@code endOffset} parameter
   * to the {@code start()} method.
   *
   * @return the lexing end offset
   */
  public abstract int getBufferEnd();
}
