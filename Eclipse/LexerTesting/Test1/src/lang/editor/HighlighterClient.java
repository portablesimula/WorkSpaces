package lang.editor;

import javax.swing.text.Document;

public interface HighlighterClient {
	  Project getProject();

	  void repaint(int start, int end);

	  Document getDocument();
	}
