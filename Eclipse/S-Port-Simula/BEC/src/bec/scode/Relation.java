/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.scode;

import java.io.IOException;

import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.value.Value;

/// Relation.
/// 
///		relation ::= ?lt | ?le | ?eq | ?ge | ?gt | ?ne
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/scode/Relation.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class Relation {
	
	/// The S-Code relation
	public int relation;

	/// Construct a new Relation
	/// @param relation the S-Code relation
	public Relation(final int relation) {
		this.relation = relation;
	}

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Finally: Create a Relation object.
	/// @return a Relation object
	public static Relation ofScode() {
		Scode.inputInstr();
		Relation rel = new Relation(Scode.curinstr);
		switch(rel.relation) {
			case Sinstr.S_LT, Sinstr.S_LE, Sinstr.S_EQ,
			     Sinstr.S_GE, Sinstr.S_GT, Sinstr.S_NE: break; // OK
			default: Util.IERR("Illegal Relation: " + rel.relation);
		}
		return rel;
	}
	
	/// Returns the 'not' Relation
	/// @return the 'not' Relation
	public Relation not() {
		switch(this.relation) {
		case Sinstr.S_LT: return new Relation(Sinstr.S_GE);
		case Sinstr.S_LE: return new Relation(Sinstr.S_GT);
		case Sinstr.S_EQ: return new Relation(Sinstr.S_NE);
		case Sinstr.S_GE: return new Relation(Sinstr.S_LT);
		case Sinstr.S_GT: return new Relation(Sinstr.S_LE);
		case Sinstr.S_NE: return new Relation(Sinstr.S_EQ);
		}
		Util.IERR("Illegal Relation: " + this);
		return this;
	}
	
	/// Returns the 'reversed' Relation
	/// @return the 'reversed' Relation
	public Relation rev() {
		switch(this.relation) {
		case Sinstr.S_LT: return new Relation(Sinstr.S_GT); // lhs <  rhs   ==   rhs >  lhs
		case Sinstr.S_LE: return new Relation(Sinstr.S_GE); // lhs <= rhs   ==   rhs >= lhs
		case Sinstr.S_EQ: return new Relation(Sinstr.S_EQ); // lhs == rhs   ==   rhs == lhs
		case Sinstr.S_GE: return new Relation(Sinstr.S_LE); // lhs >= rhs   ==   rhs <= lhs
		case Sinstr.S_GT: return new Relation(Sinstr.S_LT); // lhs >  rhs   ==   rhs <  lhs
		case Sinstr.S_NE: return new Relation(Sinstr.S_NE); // lhs != rhs   ==   rhs != lhs
		}
		Util.IERR("Illegal Relation: " + this);
		return this;
	}
	
	/// Compare two Values according to this relation.
	/// @param lhs the left hand Value
	/// @param rhs the right hand Value
	/// @return true if the relation holds
	public boolean compare(final Value lhs, final Value rhs) {
		boolean res = false;
		if(lhs != null) {
			res = lhs.compare(relation, rhs);
		} else if(rhs != null) {
			res = rhs.compare(rev().relation, lhs);
		} else {
			switch(relation) {
				case Sinstr.S_LT: res = /* 0 < 0  */ false; break;
				case Sinstr.S_LE: res = /* 0 <= 0 */ true; break;
				case Sinstr.S_EQ: res = /* 0 == 0 */ true; break;
				case Sinstr.S_GE: res = /* 0 >= 0 */ true; break;
				case Sinstr.S_GT: res = /* 0 > 0  */ false; break;
				case Sinstr.S_NE: res = /* 0 != 0 */ false; break;
			}
		}
		return res;
	}
	
	/// Compare two integer values according to the given relation.
	/// @param LHS the left hand value
	/// @param relation one of: LT, LE, EQ, GE, GT, NE
	/// @param RHS the right hand value
	/// @return true if the relation holds
	public static boolean compare(final int LHS, final int relation, final int RHS) {
		boolean res = false;
		switch(relation) {
			case Sinstr.S_LT: res = LHS <  RHS; break;
			case Sinstr.S_LE: res = LHS <= RHS; break;
			case Sinstr.S_EQ: res = LHS == RHS; break;
			case Sinstr.S_GE: res = LHS >= RHS; break;
			case Sinstr.S_GT: res = LHS >  RHS; break;
			case Sinstr.S_NE: res = LHS != RHS; break;
		}
		return res;		
	}
	
	@Override
	public String toString() {
		return Sinstr.edInstr(relation);
	}
	
	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	/// Writes a Relation to the given output.
	/// @param oupt the output stream
	/// @throws IOException if IOException occur
	public void write(final AttributeOutputStream oupt) throws IOException {
		oupt.writeShort(relation);
	}

	/// Reads a Relation from the given input.
	/// @param inpt the input stream
	/// @return the Relation read
	/// @throws IOException if IOException occur
	public static Relation read(final AttributeInputStream inpt) throws IOException {
		return new Relation(inpt.readShort());
	}

}
