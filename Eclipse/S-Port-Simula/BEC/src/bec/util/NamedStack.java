/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.util;

import java.util.Stack;

/// Named Stack.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/util/NamedStack.java"><b>Source File</b></a>.
/// 
/// @param <E> Type of stack elements
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public class NamedStack<E> extends Stack<E> {
	
	/// The Stack ident
	private String ident;
	
	/// Create a NamedStack with the given ident.
	/// @param ident the Stack ident
	public NamedStack(final String ident) {
		this.ident = ident;
	}
	
	/// Returns the Stack ident
	/// @return the Stack ident
	public String ident() {
		return ident;
	}

	/// Debug utility: dumpStack
	/// @param title the title of the dump printout
	public void dumpStack(final String title) {
		dumpStack(0, title);
	}
	
	/// Debug utility: dumpStack
	/// @param level the indent level of the dump printout
	/// @param title the title of the dump printout
	public void dumpStack(final int level, final String title) {
		String indent = "";
		for(int i=0;i<level;i++) indent = indent + "      ";
		String lead = indent + title + ": Current Stack " + ident;
		if(this.size() == 0) {
			IO.println(lead + ": **Empty**");				
		} else {
			IO.println(lead);
			lead = indent + "        TOS: ";
			for(int i=this.size()-1;i>=0;i--) {
				E item = this.get(i);
				IO.println(lead + item);
				lead = indent + "             ";					
			}
		}
	}
			
}
