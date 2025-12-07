package bec.inlineTest;

import bec.BecCompiler;
import bec.Global;
import java.util.Vector;

/// RunMake RTS
public class RunMake_RTS {
	
	/** Default Constructor */ public RunMake_RTS() {} 

	/// Main
	/// @param argv arguments
	public static void main(String[] argv) {
		Vector<String> names=new Vector<String>();
		
		// ============================================
		//  BACKEND COMPILE RTS FROM SCODE TO SVM-CODE
		// ============================================
		
		names.add("RT");	// SCode ==> C:/SPORT/RTS/RT.svm
		names.add("SYSR");	// SCode ==> C:/SPORT/RTS/SYSR.svm
		names.add("KNWN");	// SCode ==> C:/SPORT/RTS/KNWN.svm
		names.add("UTIL");	// SCode ==> C:/SPORT/RTS/UTIL.svm
		names.add("STRG");	// SCode ==> C:/SPORT/RTS/STRG.svm
		names.add("CENT");	// SCode ==> C:/SPORT/RTS/CENT.svm
		names.add("CINT");	// SCode ==> C:/SPORT/RTS/CINT.svm
		names.add("ARR");	// SCode ==> C:/SPORT/RTS/ARR.svm
		names.add("FORM");	// SCode ==> C:/SPORT/RTS/FORM.svm
		names.add("LIBR");	// SCode ==> C:/SPORT/RTS/LIBR.svm
		names.add("FIL");	// SCode ==> C:/SPORT/RTS/FIL.svm
		names.add("SMST");	// SCode ==> C:/SPORT/RTS/SMST.svm
		names.add("SML");	// SCode ==> C:/SPORT/RTS/SML.svm
		names.add("EDIT");	// SCode ==> C:/SPORT/RTS/EDIT.svm
		names.add("MNTR");	// SCode ==> C:/SPORT/RTS/MNTR.svm

		Global.outputDIR = "C:/SPORT/RTS/";
//		Option.verbose = true;
//		Option.traceMode = 4;
//		Option.SCODE_INPUT_TRACE = true;
//		Option.TRACE_ALLOC_FRAME = true;
//		Option.PRINT_GENERATED_SVM_CODE = true;
//		Option.PRINT_GENERATED_SVM_DATA = true;
//		Option.ATTR_INPUT_TRACE = true;
//		Option.ATTR_OUTPUT_TRACE = true;
//		Option.ATTR_INPUT_DUMP = true;
//		Option.ATTR_OUTPUT_DUMP = true;
//		Option.SEGMENT_INPUT_DUMP = true;
//		Option.SEGMENT_OUTPUT_DUMP = true;

		for(String name:names) {
			String fileName = "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/"+name+".scd";
			new BecCompiler(fileName);
		}
	}

}
