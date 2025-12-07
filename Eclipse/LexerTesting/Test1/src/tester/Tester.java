package tester;

import lang.IElementType;
import lang.editor.HighlighterClient;
import lang.editor.SegmentArrayWithData;
import lang.lexer.Lexer;

public class Tester {

	  private HighlighterClient myEditor;
	  private Lexer myLexer;
	  private int myInitialState;
	  protected CharSequence myText;
	  private SegmentArrayWithData mySegments;
	  
	  public Tester(Lexer lexer) {
		  myLexer = lexer;
	  }


	  protected interface TokenProcessor {
	    void addToken(int tokenIndex, int startOffset, int endOffset, int data, IElementType tokenType);

	    default void finish() {}
	  }

	  protected SegmentArrayWithData createSegments() {
		    return new SegmentArrayWithData(createStorage());
		  }

	  /**
	   * Defines how to pack/unpack and store highlighting states.
	   * <p>
	   * By default a editor highlighter uses {@link ShortBasedStorage} implementation which
	   * serializes information about element type to be highlighted with elements' indices ({@link IElementType#getIndex()})
	   * and deserializes ids back to {@link IElementType} using element types registry {@link IElementType#find(short)}.
	   * <p>
	   * If you need to store more information during syntax highlighting or
	   * if your element types cannot be restored from {@link IElementType#getIndex()},
	   * you can implement you own storage and override this method.
	   * <p>
	   * As an example, see {@link org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateHighlightingLexer},
	   * that lexes files with unregistered (without index) element types and its
	   * data storage ({@link org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateLexerDataStorage}
	   * serializes/deserializes them to/from strings. The storage is created in {@link org.jetbrains.plugins.textmate.language.syntax.highlighting.TextMateEditorHighlighterProvider.TextMateLexerEditorHighlighter}
	   *
	   * @return data storage for highlighter states
	   */
	  protected DataStorage createStorage() {
	    return myLexer instanceof RestartableLexer ? new IntBasedStorage() : new ShortBasedStorage();
	  }

	  protected TokenProcessor createTokenProcessor(int startIndex, SegmentArrayWithData segments, CharSequence myText) {
		    return (tokenIndex, startOffset, endOffset, data, tokenType) -> segments.setElementAt(tokenIndex, startOffset, endOffset, data);
		  }

	  private boolean canRestart(int lexerState) {
		    if (myLexer instanceof RestartableLexer) {
		      return ((RestartableLexer)myLexer).isRestartableState(lexerState);
		    }
		    return lexerState == myInitialState;
		  }

	  public void doSetText(CharSequence text) {
//		    if (Comparing.equal(myText, text)) return;
		    text = ImmutableCharSequence.asImmutable(text);

		    SegmentArrayWithData tempSegments = createSegments();
		    TokenProcessor processor = createTokenProcessor(0, tempSegments, text);
		    int textLength = text.length();
		    Lexer lexerWrapper = new ValidatingLexerWrapper(myLexer);

		    lexerWrapper.start(text, 0, textLength,
		                       myLexer instanceof RestartableLexer ? ((RestartableLexer)myLexer).getStartState() : myInitialState);
		    int i = 0;
		    while (true) {
		      IElementType tokenType = lexerWrapper.getTokenType();
		      if (tokenType == null) break;

		      int state = lexerWrapper.getState();
		      int data = tempSegments.packData(tokenType, state, canRestart(state));
		      processor.addToken(i, lexerWrapper.getTokenStart(), lexerWrapper.getTokenEnd(), data, tokenType);
		      i++;
		      if (i % 1024 == 0) {
		        ProgressManager.checkCanceled();
		      }
		      lexerWrapper.advance();
		    }

		    myText = text;
		    mySegments = tempSegments;
		    processor.finish();

		    if (textLength > 0 && (mySegments.mySegmentCount == 0 || mySegments.myEnds[mySegments.mySegmentCount - 1] != textLength)) {
		    	IO.println("\nTester.doSetText: =========== ERROR ==========");
		    	IO.println("Tester.doSetText: textLength="+textLength);
		    	IO.println("Tester.doSetText: mySegments.mySegmentCount="+mySegments.mySegmentCount);
		    	IO.println("Tester.doSetText: mySegments.myEnds[mySegments.mySegmentCount - 1]="+mySegments.myEnds[mySegments.mySegmentCount - 1]);
		      throw new IllegalStateException("Unexpected termination offset for lexer " + myLexer);
		    }

		    if (myEditor != null && !ApplicationManager.getApplication().isHeadlessEnvironment()) {
		    	throw new RuntimeException("NOT IMPL");
//		    	UIUtil.invokeLaterIfNeeded(() -> myEditor.repaint(0, textLength));
		    }
		  }

}
