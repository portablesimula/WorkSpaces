/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package make;

import java.util.Vector;

import bec.BecCompiler;
import bec.Option;

public final class RunFull_SML_TestBatch {
	private static long startTimeMs = System.currentTimeMillis();

//	private static final String rtsSCodeDir = "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/";
	private static final String smlSCodeDir = "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulettaTestBatch/SCode/";
//                                             C:\GitHub\EclipseWorkSpaces\S-Port-Simula\FILES\simulettaTestBatch\SCode
//	Global.scodeSource = "C:/Simuletta/SCode/simulaRTS/RT.scd";

	public static void main(String[] args) {
		Option.INLINE_TESTING = true;
		
//		SVM_CALLSYS.RUNTIME_VERBOSE = true;
//		Global.verbose = true;
//		Global.SCODE_INPUT_TRACE = true;
//		Global.PRINT_GENERATED_SVM_CODE = true;
//		Option.EXEC_TRACE = 1;
//		Global.CALL_TRACE_LEVEL = 2;
//		Global.DUMPS_AT_EXIT = true;
		
		// Set Compiler Options.
//		Option.EXTENSIONS=false;
//		Option.noExecution=true;

		Vector<String> names = new Vector<String>();
		
//		names.add("adHoc00.scd");
//		names.add("adHoc01.scd");
//		names.add("adHoc02.scd");
//		names.add("adHoc03.scd");
//		names.add("adHoc04.scd");
//		names.add("adHoc05.scd");
//			names.add("adHoc06.scd"); 
//			names.add("adHoc07.scd");
//			names.add("adHoc08.scd");
//		names.add("adHoc09.scd");
//		names.add("adHoc10.scd");
		
		names.add("smltst00.scd"); // Standard layout of test programs
		names.add("smltst01.scd"); // Simple Assignments
		names.add("smltst02.scd"); // Test Case Statement
		names.add("smltst03.scd"); // Test Assert Statement
		names.add("smltst04.scd"); // Test Parameter Transmission
		names.add("smltst05.scd"); // Test CALL_TOS
		names.add("smltst06.scd"); // Records with Variant(ALT)
		names.add("smltst07.scd"); // Constant and initial values
		names.add("smltst08.scd"); // Arithmetic Operators ( + - * / rem)
		names.add("smltst09.scd"); // Boolean Operators ( and, or, xor, not )
		names.add("smltst10.scd"); // Arithmetic Relations ( < , <= , = , >= , > , <> )
		names.add("smltst11.scd"); // Boolean Relations ( = , <> )
		names.add("smltst12.scd"); // Size Relations ( < , <= , = , >= , > , <> )
		names.add("smltst13.scd"); // General Reference Expression
		names.add("smltst14.scd"); // Object Reference and Size Expression
		names.add("smltst15.scd"); // Type Conversion
		names.add("smltst16.scd"); // Type Conversion between name,ref and field
		names.add("smltst17.scd"); // If-Statements
		names.add("smltst18.scd"); // Goto-Statements
		names.add("smltst19.scd"); // Remote Access
		names.add("smltst20.scd"); // Repeat-Statements
		names.add("smltst21.scd"); // Call: Profile(Body)(a,b,c)
		names.add("smltst22.scd"); // Exit and Non-Local Goto
		names.add("smltst23.scd"); // Export treatment
		names.add("smltst24.scd"); // Zero_Area
		names.add("smltst25.scd"); // Editing Utilities
		names.add("smltst26.scd"); // Complex Variables
		names.add("smltst27.scd"); // SYSTEM Const and Variables
		names.add("smltst28.scd"); // Structured Constants
		names.add("smltst29.scd"); // Object Address Relations ( < , <= , = , >= , > , <> )
		names.add("smltst30.scd"); // More Constants
		names.add("smltst31.scd"); // CALL, EXIT, RETURN and GOTO
		names.add("smltst32.scd"); // More Structured Constants
		names.add("smltst33.scd"); // GOTO fixup'address
		names.add("smltst34.scd"); // Text quant relations ( =, <> )
		names.add("smltst35.scd"); // RT'sizes: string, ptp, ...
		names.add("smltst36.scd"); // RT'sizes: entity, ...
		names.add("smltst37.scd"); // RT'sizes: quantities and simob
//		names.add("smltst38.scd"); // INITO, GETO, SETO
		names.add("smltst39.scd"); // Boolean Operators IMP and EQV
		names.add("smltst40.scd"); // Fixup Object Address
		names.add("smltst41.scd"); // Shift opr: LSHIFTL, RSHIFTL, LSHIFTA, RSHIFTA
		names.add("smltst42.scd"); // Test routine MOVEIN
		names.add("smltst43.scd"); // Test name(infix) parameter
		names.add("smltst44.scd"); // Test Routine MODULO

		for (String name : names) {
			String fileName = smlSCodeDir + name;
//			try {
				new BecCompiler(fileName);
//				IO.println("RETURN FROM: BecCompiler");
//			} catch (Exception e) { Util.IERR("Compiler Error: " + e); }
		}

		IO.println("\n--- END OF SportBEC TESTBATCH.scd");
		long timeUsed = System.currentTimeMillis() - startTimeMs;
		IO.println("\nElapsed Time: Approximately " + timeUsed / 1000 + " sec.");
	}

}
