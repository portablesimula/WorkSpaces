package lang.editor;

import java.util.Comparator;

import javax.swing.event.DocumentListener;

public interface PrioritizedDocumentListener extends DocumentListener {
	  /**
	   * Comparator that sorts {@link DocumentListener} objects by their {@link PrioritizedDocumentListener#getPriority() priorities} (if any).
	   * <p/>
	   * The rules are:
	   * <pre>
	   * <ul>
	   *   <li>{@link PrioritizedDocumentListener} has more priority than {@link DocumentListener};</li>
	   *   <li>{@link PrioritizedDocumentListener} with lower value returned from {@link #getPriority()} has more priority than another;</li>
	   * </ul>
	   * </pre>
	   */
	  Comparator<? super DocumentListener> COMPARATOR = new Comparator<Object>() {
	    @Override
	    public int compare(Object o1, Object o2) {
	      return Integer.compare(getPriority(o1), getPriority(o2));
	    }

	    private int getPriority(Object o) {
	      if (o instanceof PrioritizedDocumentListener) {
	        return ((PrioritizedDocumentListener)o).getPriority();
	      }
	      return Integer.MAX_VALUE;
	    }
	  };

	  int getPriority();
	}
