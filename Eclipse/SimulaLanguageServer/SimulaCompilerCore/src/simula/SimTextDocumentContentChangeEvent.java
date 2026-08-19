package simula;

import simula.core.builder.util.LexRange;

public class SimTextDocumentContentChangeEvent {
	
	/// The range of the document that changed.
	private LexRange range;

	/// The new text of the range/document.
	private String text;

	public SimTextDocumentContentChangeEvent() {
	}

	public SimTextDocumentContentChangeEvent(final String text) {
		this.text = text;
	}

	public SimTextDocumentContentChangeEvent(final LexRange range, final String text) {
		this(text);
		this.range = range;
	}

	/// The range of the document that changed.
	public LexRange getRange() {
		return this.range;
	}

	/// The range of the document that changed.
	public void setRange(final LexRange range) {
		this.range = range;
	}

	/// The new text of the range/document.
	public String getText() {
		return this.text;
	}

	/// The new text of the range/document.
	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Change[range:" + range + ", text:" + text + ']';
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimTextDocumentContentChangeEvent other = (SimTextDocumentContentChangeEvent) obj;
		if (this.range == null) {
			if (other.range != null)
				return false;
		} else if (!this.range.equals(other.range))
			return false;
//			if (this.rangeLength == null) {
//				if (other.rangeLength != null)
//					return false;
//			} else if (!this.rangeLength.equals(other.rangeLength))
//				return false;
		if (this.text == null) {
			if (other.text != null)
				return false;
		} else if (!this.text.equals(other.text))
			return false;
		return true;
	}
}
