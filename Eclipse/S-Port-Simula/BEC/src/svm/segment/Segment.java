/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.segment;

import java.io.IOException;

import bec.Global;
import bec.descriptor.Kind;
import bec.scode.Relation;
import bec.scode.Sinstr;
import bec.util.AttributeOutputStream;
import bec.util.Util;

/// Segment.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/segment/Segment.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public abstract class Segment {
	
	/// Segment ident
	public String ident;
	
	/// K_SEG_DATA, K_SEG_CONST, K_SEG_CODE
	protected int segmentKind;
	
	/// Segment Sequence number
	private int sequ;
	
	/// Used to generate Segment Sequence numbers
	private static int SEQU = 0x10000;

	/// Segment constructor.
	/// @param ident the Segment ident
	/// @param segmentKind K_SEG_DATA, K_SEG_CONST, K_SEG_CODE
	public Segment(final String ident, final int segmentKind) {
		if(Global.SEGMAP.get(ident) != null) Util.IERR("Segment allready defined: " + ident);
		this.ident = ident.toUpperCase();
		Global.SEGMAP.put(this.ident, this);
		this.segmentKind = segmentKind;
		this.sequ = SEQU++;
	}

	/// Lookup Segment by its ident
	/// @param ident the Segment ident
	/// @return the Segment found
	public static Segment lookup(final String ident) {
		Segment seg = Global.SEGMAP.get(ident);
		if(seg == null) {
			Util.IERR("Can't find Segment \"" + ident + '"');
		}
		return seg;
	}

	/// Compare two Segment addresses according to the given relation.
	/// @param LHSegID the left hand Segment ident
	/// @param lhSegOfst the left hand Segment address offset
	/// @param relation one of: LT, LE, EQ, GE, GT, NE
	/// @param RHSegID the right hand Segment ident
	/// @param rhSegOfst the right hand Segment address offset
	/// @return true if the relation holds
	public static boolean compare(final String LHSegID, final int lhSegOfst, final int relation, final String RHSegID, final int rhSegOfst) {
		if(equals(LHSegID, RHSegID)) {
			return Relation.compare(lhSegOfst, relation, rhSegOfst);
		} else {
			// IO.println("Segment.compare: " + LHSegID + ":" + lhSegOfst + "  " + Sinstr.edInstr(relation) + "  " + RHSegID + ":" + rhSegOfst);
			Segment LHSeg = Global.SEGMAP.get(LHSegID);
			Segment RHSeg = Global.SEGMAP.get(RHSegID);
			int LHS = lhSegOfst + ((LHSeg == null)?0:LHSeg.sequ);
			int RHS = lhSegOfst + ((RHSeg == null)?0:RHSeg.sequ);
			boolean res = false;
			switch(relation) {
				case Sinstr.S_LT: res = LHS <  RHS; break;
				case Sinstr.S_LE: res = LHS <= RHS; break;
				case Sinstr.S_EQ: res = LHS == RHS; break;
				case Sinstr.S_GE: res = LHS >= RHS; break;
				case Sinstr.S_GT: res = LHS >  RHS; break;
				case Sinstr.S_NE: res = LHS != RHS; break;
				//default: Util.IERR("");
			}
			return res;		
		}
	}
	
	/// Returns true if the two specified Strings are equal to one another. 
	/// @param s1 one String to be tested for equality
	/// @param s2 the other String to be tested for equality
	/// @return true if the two Strings are equal
	private static boolean equals(final String s1, final String s2) {
		if(s1 == null) return s2 == null;
		return s1.equals(s2);
	}
	
//	public static void listAll() {
//		for(Segment seg:Global.SEGMAP.values()) {
//			IO.println("   " + seg);
//		}
//	}

	/// Utility: Segment dump
	/// @param title the printout title
	public abstract void dump(final String title);
	
	/// Utility: Segment dump
	/// @param title the printout title
	/// @param from Segment index
	/// @param to Segment index
	public abstract void dump(final String title, final int from, final int to);
	
//	public static void dumpAll(String title) {
//		for(Segment seg:Global.SEGMAP.values()) {
//			seg.dump(title);
//		}
//	}
	
	@Override
	public String toString() {
		return Kind.edKind(segmentKind) + ':' + segmentKind + " \"" + ident + '"';
	}


	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	/// Writes a Segment to the given output.
	/// @param oupt the output stream
	/// @throws IOException if IOException occur
	public void write(final AttributeOutputStream oupt) throws IOException {
		Util.IERR("Method 'write' needs a redefinition in "+this.getClass().getSimpleName());
	}

}
