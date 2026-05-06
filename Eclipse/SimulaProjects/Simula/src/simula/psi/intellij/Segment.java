package simula.psi.intellij;

public interface Segment {

	int getStartOffset();

	int getEndOffset();

	/**
	* Checks if the given offset is contained within the range 
	* (unlike {@link #containsOffset(int)}, offset at the end of the range is considered to be outside).
	*
	* @param offset the offset to check
	* @return true if the given offset is within the range, false otherwise
	* @see #containsOffset(int) 
	*/
	boolean contains(int offset);

}
