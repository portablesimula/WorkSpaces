/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.descriptor;

/// Object Kind Codes.
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/descriptor/Kind.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class Kind {

	/** Default Constructor */ public Kind() {} 

//	---------     O b j e c t   K i n d   C o d e s      ---------
//
	/** Object kind code */ /** Object kind code */ public static final int K_Module=1;

	/** Object kind code */ /** Object kind code */ public static final int K_SEG_DATA  = 2;
	/** Object kind code */ /** Object kind code */ public static final int K_SEG_CONST = 3;
	/** Object kind code */ /** Object kind code */ public static final int K_SEG_CODE  = 4;

	/** Object kind code */ public static final int K_EndModule=5;
	/** Object kind code */ public static final int K_RECTYPES=6;
//	 --- Descriptors ---
	/** Object kind code */ public static final int K_RecordDescr=10;
	/** Object kind code */ public static final int K_TypeRecord=11;
	/** Object kind code */ public static final int K_Attribute=12;
	/** Object kind code */ public static final int K_Import=13;
	/** Object kind code */ public static final int K_Export=14;
	/** Object kind code */ public static final int K_LocalVar=15;
	/** Object kind code */ public static final int K_GlobalVar=16;
	/** Object kind code */ public static final int K_ExternVar=17;
	/** Object kind code */ public static final int K_ProfileDescr=18;
	/** Object kind code */ public static final int K_IntRoutine=19;
	/** Object kind code */ public static final int K_ExtRoutine=20;
	/** Object kind code */ public static final int K_IntLabel=21;
	/** Object kind code */ public static final int K_ExtLabel=22;
	/** Object kind code */ public static final int K_SwitchDescr=23;
//	 --- Stack Items ---
	/** Object kind code */ public static final int K_ProfileItem=24;
	/** Object kind code */ public static final int K_Address=25;
	/** Object kind code */ public static final int K_Temp=26;
	/** Object kind code */ public static final int K_Coonst=27;
	/** Object kind code */ public static final int K_Result=28;
//	 --- Others ---
	/** Object kind code */ public static final int K_BSEG=32;
	/** Object kind code */ public static final int K_Exit=33;
	/** Object kind code */ public static final int K_Retur=34;

	/** Object kind code */ public static final int K_Max=35;  // Max value of object kind codes  + 1
	
	/** Object kind code */ public static final int qDCL=40;
	/** Object kind code */ public static final int qSTM=41;

	/// Edit object kind
	/// @param kind the object kind code
	/// @return the edited string
	public static String edKind(int kind) {
		switch(kind) {
			case K_Module:			return "Module";
			case K_RECTYPES:		return "RECTYPES";
			case K_SEG_DATA:		return "SEG_DATA";
			case K_SEG_CONST:		return "SEG_CONST";
			case K_SEG_CODE:		return "SEG_CODE";
			case K_Coonst:			return "Coonst";
			case K_RecordDescr:		return "RecordDescr";
			case K_Attribute:		return "Attribute";
			case K_GlobalVar:		return "GlobalVar";
			case K_LocalVar:		return "LocalVar";
			case K_ProfileDescr:	return "ProfileDescr";
			case K_Import:			return "Import";
			case K_Export:			return "Export";
			case K_Exit:			return "Exit";
			case K_Retur:			return "Retur";
			case K_IntRoutine:		return "IntRoutine";
			case K_IntLabel:		return "IntLabel";
			case K_EndModule:		return "EndModule";
		}
		return "Kind:" + kind;
	}


}
