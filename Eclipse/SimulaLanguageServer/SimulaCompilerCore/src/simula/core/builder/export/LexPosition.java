package simula.core.builder.export;

public class LexPosition {
	  
	  /// Line position in a document (zero-based).
	  private int line;

	  /// Character offset on a line in a document (zero-based).
	  private int character;

	  public LexPosition() {
	  }

	  public LexPosition(final int line, final int character) {
	    this.line = line;
	    this.character = character;
	  }

	  /// Line position in a document (zero-based).
	  public int getLine() {
	    return this.line;
	  }

	  /**
	   * Line position in a document (zero-based).
	   */
	  public void setLine(final int line) {
	    this.line = line;
	  }

	  /**
	   * Character offset on a line in a document (zero-based).
	   */
	  public int getCharacter() {
	    return this.character;
	  }

	  /**
	   * Character offset on a line in a document (zero-based).
	   */
	  public void setCharacter(final int character) {
	    this.character = character;
	  }

	  @Override
	  public String toString() {
	    return "SimPosition[line:" + line +", column:" + character +']';
	  }

	  @Override
	  public boolean equals(final Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    LexPosition other = (LexPosition) obj;
	    if (other.line != this.line)
	      return false;
	    if (other.character != this.character)
	      return false;
	    return true;
	  }

	  @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + this.line;
	    return prime * result + this.character;
	  }
	}
