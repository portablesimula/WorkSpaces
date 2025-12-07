/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.compileTimeStack.ProfileItem;
import bec.Global;
import bec.compileTimeStack.AddressItem;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.CTStackItem;
import bec.descriptor.Display;
import bec.descriptor.ProfileDescr;
import bec.descriptor.RoutineDescr;
import bec.descriptor.Variable;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.Util;
import svm.instruction.SVM_CALL;
import svm.instruction.SVM_CALL_SYS;
import svm.instruction.SVM_LOADC;
import svm.instruction.SVM_PRECALL;

/// S-INSTRUCTION: CALL.
/// <pre>
/// call_instruction
/// 		::= connect_profile < parameter_eval >*
/// 				connect_routine
/// 
/// 		connect_profile
/// 			::= precall profile:tag
/// 			::= asscall profile:tag
/// 			::= repcall n:byte profile:tag
/// 
/// 		connect_routine ::= call body:tag | < instruction >+ call-tos
/// 
/// 		parameter_eval
/// 			::= < instruction >+ asspar
/// 			::= < instruction >+ assrep n:byte
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/CALL.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class CALL extends Instruction {
	
	/** Default Constructor */ public CALL() {} 

	/// Treat a complete Call Instruction including parameters stacking and
	/// finally emit a SVM_CALL instruction.
	/// @param nParStacked number of parameters stacked
	public static void ofScode(int nParStacked) {
		int profileTag = Scode.inTag();
		Scode.inputInstr();
		
		ProfileDescr spec = (ProfileDescr) Display.get(profileTag);
		if(spec == null) Util.IERR(""+Scode.edTag(profileTag));
		ProfileItem pitem = new ProfileItem(spec);
		pitem.nasspar = nParStacked;
		
		int nParSlots = 0;
		for(int i=0;i<nParStacked;i++) {
			FETCH.doFetch();
			CTStackItem par = CTStack.pop();
			nParSlots = nParSlots + par.type.size();
		}
		
		CTStack.push(pitem);
	    
		if(spec.pKind == 0) {
			int exportSize = (spec.getExport() == null)? 0 : spec.getExport().type.size();
			int importSize = spec.frameSize-exportSize-1;
			Global.PSEG.emit(new SVM_PRECALL(spec.getSimpleName(), nParSlots, exportSize, importSize));
		}
		
		boolean CALL_TOS = false;
		
		LOOP:while(Scode.curinstr != Sinstr.S_CALL) {
			Instruction.inInstructions();
			if(Scode.curinstr == Sinstr.S_ASSPAR) {
				Scode.inputInstr();
				putPar(pitem,1);
//		      	Global.PSEG.emit(new SVM_NOOP());
			}
			else if(Scode.curinstr == Sinstr.S_ASSREP) {
				int nRep = Scode.inByte();
				Scode.inputInstr();
				putPar(pitem,nRep);
//		      	Global.PSEG.emit(new SVM_NOOP());
			}
			else if(Scode.curinstr == Sinstr.S_CALL_TOS) {
				CALL_TOS = true;
				break LOOP;
			}
			else Util.IERR("Syntax error in call Instruction");
		}
	    //---------  Final Actions  ---------
	    if(pitem.nasspar != pitem.spc.params.size())
	    	Util.IERR("Wrong number of Parameters: got " + pitem.nasspar + ", required" + +pitem.spc.params.size());
//	    ---------  Call Routine  ---------
	    if(CALL_TOS) {
	    	Global.PSEG.emit(SVM_CALL.ofTOS(spec.returSlot));
	    	CTStack.pop();
	    } else {
			int bodyTag = Scode.inTag();
	    	if(spec.pKind > 0) {
	    		Global.PSEG.emit(new SVM_CALL_SYS(spec.pKind));
	    	} else {
	    		RoutineDescr rut = (RoutineDescr) Display.get(bodyTag);
	    		if(rut == null) Util.IERR("Unknown Routine: " + Scode.edTag(bodyTag));
	    		Global.PSEG.emit(new SVM_CALL(rut.getAddress(), spec.returSlot));
	    	}
	    }
//		Global.PSEG.emit(new SVM_NOOP());
	    if(! (CTStack.TOS() instanceof ProfileItem)) {
			CTStack.dumpStack("CALL.ofScode: ");
	    	Util.IERR("CALL.ofScode: Missing ProfileItem on TOS");
	    }
	    CTStack.pop();
		
		// Routines return values on the RT-Stack
		Variable export = spec.getExport();
		if(export != null) {
			Type returnType = export.type;
			CTStack.pushTempItem(returnType);
		}
	}
	
	/// Utility: treat and put parameter
	/// @param pItm the Profile item
	/// @param nrep the repetition count
	private static void putPar(final ProfileItem pItm, final int nrep) {
		Variable param = (Variable) pItm.spc.params.get(pItm.nasspar).getMeaning();
		Type parType = param.type;
		int repCount = param.repCount;
		if(nrep>repCount) Util.IERR("Too many values in repeated param: Got "+nrep+", expect "+repCount);
		pItm.nasspar = pItm.nasspar+1;
		CTStackItem tos = CTStack.TOS();
		Type st = tos.type;
		
		//--- First: Treat TOS ---
		if(st != parType) {
			CONVERT.doConvert(parType);
		} else if(tos instanceof AddressItem) {
			tos.type = st;
		} else {
			CONVERT.doConvert(parType);
		}
		
		if(CTStack.TOS() instanceof AddressItem) FETCH.doFetch();
		CTStack.pop();
		
		if(nrep > 1) { // Then: Treat rest of rep-par ---
			for(int i=nrep-1;i>0;i--) {
				CTStackItem TOS = CTStack.pop();
				if(TOS instanceof AddressItem) Util.IERR("MODE mismatch below TOS");
				if(TOS.type != parType) {
					if(TOS.type.tag == Tag.TAG_INT && parType.tag == Tag.TAG_SINT) ; // OK
					else if(TOS.type.tag == Tag.TAG_SINT && parType.tag == Tag.TAG_INT) ; // OK
					else Util.IERR("TYPE mismatch below TOS -- ASSREP: TOS.type="+TOS.type+"  parType="+parType);
				}
			}
		}
		
		if(repCount > nrep) {
			int parSize = parType.size();
			int n = parSize * (repCount - nrep);
			for(int i=0;i<n;i++) {
				Global.PSEG.emit(new SVM_LOADC(Type.T_INT, null));
				
			}
		}
	}

}
