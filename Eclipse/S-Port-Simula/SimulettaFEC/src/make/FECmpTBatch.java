/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package make;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import simuletta.compiler.Global;
import simuletta.compiler.SimulettaCompiler;
import simuletta.utilities.Option;

public final class FECmpTBatch {

	// ***************************************************************
	// *** COMPILE TESTBATCH TO S-CODE
	// ***************************************************************
	public static void main(String[] args) {
		long startTimeMs = System.currentTimeMillis( );
		Vector<String> names=new Vector<String>();
		
//		names.add("adHoc00");
//		names.add("adHoc01");
//		names.add("adHoc02");
//		names.add("adHoc03");
//		names.add("adHoc04");
//		names.add("adHoc05");
//		names.add("adHoc06");
//		names.add("adHoc07");
//		names.add("adHoc08");
//		names.add("adHoc09");
//		names.add("adHoc10");
//		names.add("adHoc11");
//		names.add("adHoc12");
//		names.add("adHoc13");
//		names.add("adHoc14");
//		names.add("adHoc15");
		
		names.add("smltst00"); // Standard layout of test programs
		names.add("smltst01"); // Simple Assignments
		names.add("smltst02"); // Test Case Statement
		names.add("smltst03"); // Test Assert Statement
		names.add("smltst04"); // Test Parameter Transmission
		names.add("smltst05"); // Test CALL_TOS
		names.add("smltst06"); // Records with Variant(ALT)
		names.add("smltst07"); // Constant and initial values
		names.add("smltst08"); // Arithmetic Operators ( + - * / rem)
		names.add("smltst09"); // Boolean Operators ( and, or, xor, not )
		names.add("smltst10"); // Arithmetic Relations ( < , <= , = , >= , > , <> )
		names.add("smltst11"); // Boolean Relations ( = , <> )
		names.add("smltst12"); // Size Relations ( < , <= , = , >= , > , <> )
		names.add("smltst13"); // General Reference Expression
		names.add("smltst14"); // Object Reference and Size Expression
		names.add("smltst15"); // Type Conversion
		names.add("smltst16"); // Type Conversion between name,ref and field
		names.add("smltst17"); // If-Statements
		names.add("smltst18"); // Goto-Statements
		names.add("smltst19"); // Remote Access
		names.add("smltst20"); // Repeat-Statements
		names.add("smltst21"); // Call: Profile(Body)(a,b,c)
		names.add("smltst22"); // Exit and Non-Local Goto
		names.add("smltst23"); // Export treatment
		names.add("smltst24"); // Zero_Area
		names.add("smltst25"); // Editing Utilities
		names.add("smltst26"); // Complex Variables
		names.add("smltst27"); // SYSTEM Const and Variables
		names.add("smltst28"); // Structured Constants
		names.add("smltst29"); // Object Address Relations ( < , <= , = , >= , > , <> )
		names.add("smltst30"); // More Constants
		names.add("smltst31"); // CALL, EXIT, RETURN and GOTO
		names.add("smltst32"); // More Structured Constants
		names.add("smltst33"); // GOTO fixup'address
		names.add("smltst34"); // Text quant relations ( =, <> )
		names.add("smltst35"); // RT'sizes: string, ptp, ...
		names.add("smltst36"); // RT'sizes: entity, ...
		names.add("smltst37"); // RT'sizes: quantities and simob
		names.add("smltst38"); // INITO, GETO, SETO
		names.add("smltst39"); // Boolean Operators IMP and EQV
		names.add("smltst40"); // Fixup Object Address
		names.add("smltst41"); // Shift opr: LSHIFTL, RSHIFTL, LSHIFTA, RSHIFTA
		names.add("smltst42"); // Test routine MOVEIN
		names.add("smltst43"); // Test name(infix) parameter
		names.add("smltst44"); // Test Routine MODULO

		// Set Compile Time Options and tracing
//		Option.INLINE_TESTING=true;
//	    Option.verbose = true;
//		Option.WARNINGS=true;

		// Overall TRACING Options
//		Option.TRACING=true;
//		Option.BREAKING=true; 

		// Scanner Trace Options
//		Option.SOURCE_LISTING=true;
//		Option.TRACE_SELECTION=true;
//		Option.TRACE_SCAN=true;
//		Option.TRACE_MACRO_SCAN=true;
//		Option.TRACE_MACRO_EXPAND=true;
//		Option.TRACE_COMMENTS=true;

		// Parser Trace Options
//		Option.TRACE_PARSE=true;
//		Option.TRACE_PARSE_BREIF=true;
//		Option.TRACE_ATTRIBUTE_OUTPUT=true;
//		Option.TRACE_ATTRIBUTE_INPUT=true;

		// Checker Trace Options
//		Option.TRACE_CHECKER=true;
//		Option.TRACE_CHECKER_OUTPUT=true;
		//Option.TRACE_FIND=2;
		
		// Coder Trace Options
//		Option.TRACE_FIND_MEANING=true;
//		Option.TRACE_CODING=1;
		
//		File userDir=new File("C:/WorkSpaces/SimulettaFECinJava/SimulettaFEC");
		File userDir=new File("C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulettaFEC");

		Global.packetName="simulettaTestBatch";
		
//		Global.simulaRtsLib="C:/Simuletta/Attrs/FEC/simulaRTS";
//		Global.outputDir=new File("C:/Simuletta");
		
		Global.simulaRtsLib     = "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/Attrs";
		Global.outputDir=new File("C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES");

		
		// Set RunTime Options and tracing.
//		RTOption.VERBOSE = false;//true;
//		RTOption.DEBUGGING = true;//false;//true;
//		RTOption.USE_CONSOLE=false;
//		RTOption.CODE_STEP_TRACING = false;
//		RTOption.BLOCK_TRACING = false;
//		RTOption.GOTO_TRACING = false;
//		RTOption.THREAD_TRACING = false;
//		RTOption.QPS_TRACING = false;
//		RTOption.SML_TRACING = false;
//		RTOption.LOOM_TRACING = false;
//		RTOption.USE_VIRTUAL_THREAD=true;

		if(Option.verbose) IO.println("\nBEGIN COMPILE SIMULETTA TESTBATCH");
		for(String name:names) {
			String fileName = userDir+"/src/"+Global.packetName+"/sml/"+name+".sml";
//			File outputFile=new File(Global.outputDir+"/SCode/"+Global.packetName+'/'+name+".scd");
			File outputFile=new File(Global.outputDir+"/"+Global.packetName+"/SCode/"+name+".scd");

			//outputFile.getParentFile().mkdirs();
			IO.println("    COMPILE "+fileName);
			IO.println("    OUTPUT  "+outputFile);
			try { outputFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); System.exit(0); }


			SimulettaCompiler compiler = new SimulettaCompiler(fileName,outputFile);
			compiler.doCompile();
			if(Option.verbose) {
				IO.println("    SCode Output: "+outputFile);
				IO.println("    Attrs Output: "+Global.outputAttributeFile);
			}
		}
		if(Option.verbose) {
			IO.println("--- END COMPILE TESTBATCH TO S-CODE");
			long timeUsed  = System.currentTimeMillis( ) - startTimeMs;
			IO.println("\nElapsed Time: Approximately " + timeUsed/1000 + " sec.");
		}
	}


}
