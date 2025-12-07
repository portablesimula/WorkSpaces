package lang.editor;

import javax.swing.event.DocumentListener;

public interface EditorHighlighter extends DocumentListener {
	  
	  HighlighterIterator createIterator(int startOffset);

	  default void setText(CharSequence text) {
	  }

	  void setEditor(HighlighterClient editor);

	  default void setColorScheme(EditorColorsScheme scheme) {
	  }
	}