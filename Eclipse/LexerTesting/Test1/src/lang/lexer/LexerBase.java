package lang.lexer;

public abstract class LexerBase extends Lexer {
	  @Override
	  public LexerPosition getCurrentPosition() {
	    final int offset = getTokenStart();
	    final int intState = getState();
	    return new LexerPositionImpl(offset, intState);
	  }

	  @Override
	  public void restore(LexerPosition position) {
	    start(getBufferSequence(), position.getOffset(), getBufferEnd(), position.getState());
	  }
	}
