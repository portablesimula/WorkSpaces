package simula.core.builder.util;

public class LexRange {
	  /**
	   * The range's start position
	   */
	  
	  private LexPosition start;

	  /**
	   * The range's end position
	   */
	  
	  private LexPosition end;

	  public LexRange() {
	  }

	  public LexRange( final LexPosition start,  final LexPosition end) {
	    this.start = start;
	    this.end = end;
	  }

	  /**
	   * The range's start position
	   */
	  
	  public LexPosition getStart() {
	    return this.start;
	  }

	  /**
	   * The range's start position
	   */
	  public void setStart( final LexPosition start) {
	    this.start = start;
	  }

	  /**
	   * The range's end position
	   */
	  
	  public LexPosition getEnd() {
	    return this.end;
	  }

	  /**
	   * The range's end position
	   */
	  public void setEnd( final LexPosition end) {
	    this.end = end;
	  }

	  @Override
	  public String toString() {
		    return "SimRange[start:" + start +", end:" + end +']';
	  }

	  @Override
	  public boolean equals(final Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    LexRange other = (LexRange) obj;
	    if (this.start == null) {
	      if (other.start != null)
	        return false;
	    } else if (!this.start.equals(other.start))
	      return false;
	    if (this.end == null) {
	      if (other.end != null)
	        return false;
	    } else if (!this.end.equals(other.end))
	      return false;
	    return true;
	  }

	  @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((this.start== null) ? 0 : this.start.hashCode());
	    return prime * result + ((this.end== null) ? 0 : this.end.hashCode());
	  }
	}
