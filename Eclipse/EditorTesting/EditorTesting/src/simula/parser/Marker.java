package simula.parser;

import simula.compiler.utilities.Util;

public interface Marker {// extends Marker {

	static class IElementType {}

	IElementType getTokenType();

	int getStartOffset();

	int getEndOffset();

	default int getTextLength() {
		return this.getEndOffset() - this.getStartOffset();
	}

	default int getStartIndex() {
		throw new UnsupportedOperationException("not implemented for this kind of markers");
	}

	default int getEndIndex() {
		throw new UnsupportedOperationException("not implemented for this kind of markers");
	}

	default String getErrorMessage() {
		return null;
	}

	default boolean isCollapsed() {
		return false;
	}


	//public interface Marker extends Production {
	Marker precede();

	void drop();

	void rollbackTo();

	void done(IElementType var1);

	default void doneBefore(IElementType type, Marker before) {
		if (before == null) Util.IERR();
		this.doneBefore(type, (Marker)before);
	}

	default void doneBefore(IElementType type, Marker before, String errorMessage) {
		if (before == null) Util.IERR();
		this.doneBefore(type, (Marker)before, errorMessage);
	}

	void collapse(IElementType var1);

	void error(String var1);

	default void errorBefore(String message, Marker before) {
		if (before == null) Util.IERR();
		this.errorBefore(message, (Marker)before);
	}

	void setCustomEdgeTokenBinders(WhitespacesAndCommentsBinder var1, WhitespacesAndCommentsBinder var2);


}
