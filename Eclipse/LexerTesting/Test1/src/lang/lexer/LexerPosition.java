package lang.lexer;

public interface LexerPosition {
	  /**
	   * Returns the current offset of the lexer.
	   * @return the lexer offset
	   */
	  int getOffset();
	  int getState();
	}
