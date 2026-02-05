package simula.parser;

import simula.compiler.utilities.Util;

public interface REMOVED_Marker {// extends Marker {

	static class IElementType {}

	IElementType getCurrentLexerToken();

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
	REMOVED_Marker precede();

	void drop();

	void rollbackTo();

	void done(IElementType var1);

	default void doneBefore(IElementType type, REMOVED_Marker before) {
		if (before == null) Util.IERR();
		this.doneBefore(type, (REMOVED_Marker)before);
	}

	default void doneBefore(IElementType type, REMOVED_Marker before, String errorMessage) {
		if (before == null) Util.IERR();
		this.doneBefore(type, (REMOVED_Marker)before, errorMessage);
	}

	void collapse(IElementType var1);

	void error(String var1);

	default void errorBefore(String message, REMOVED_Marker before) {
		if (before == null) Util.IERR();
		this.errorBefore(message, (REMOVED_Marker)before);
	}

	void setCustomEdgeTokenBinders(WhitespacesAndCommentsBinder var1, WhitespacesAndCommentsBinder var2);


}
