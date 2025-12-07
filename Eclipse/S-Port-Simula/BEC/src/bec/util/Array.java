/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.util;

import java.util.Vector;

/// Array, an extension of Vector which allows arbitrary indexing while the Array grows.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/util/Array.java"><b>Source File</b></a>.
/// 
/// @param <E> Type of Array elements
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public class Array<E> extends Vector<E> {
	
	/// The initial capacity of the vector
	private static final int initialCapacity = 2500;
	
	/// The amount by which the capacity is increased when the vector grows
	private static final int capacityIncrement = 100;
	
	/// Constructor
	public Array() {
		super(initialCapacity, capacityIncrement);
	}
	
	/// Replaces the element at the specified position in this Array with the specified element.
	/// If necessary, the size of the underlying Vector is increased.
	///
	/// @param index index of the element to replace
	/// @param elt element to be stored at the specified position
	/// @return the element previously at the specified position
	/// @throws ArrayIndexOutOfBoundsException if the index is out of range: index < 0
	public E set(final int index, final E elt) {
		int minSize = index + 1;
		if(size() < minSize) setSize(minSize);
		return super.set(index, elt);
	}
	
	/// Returns the element at the specified position in this Array.
	///
	/// @param index index of the element to return
	/// @return object at the specified index or null if the index is out of range (index < 0 || index >= size())
	public E get(final int index) {
		try {
			return super.get(index);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
}
