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

public final class FECmpRTS {

	// ***********************************************************************************************
	// *** COMPILE RTS TO S-CODE in C:\GitHub\EclipseWorkSpaces\S-Port-Simula\FILES\simulaRTS\SCode
	// ***********************************************************************************************
	public static void main(String[] args) {
		long startTimeMs = System.currentTimeMillis( );
		Vector<String> names=new Vector<String>();
		names.add("RT");   // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/RT.scd
		names.add("SYSR"); // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/SYSR.scd
		names.add("KNWN"); // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/KNWN.scd
		names.add("UTIL"); // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/UTIL.scd
		
		names.add("STRG"); // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/STRG.scd
		names.add("CENT"); // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/CENT.scd
		names.add("CINT"); // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/CINT.scd
		names.add("ARR");  // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/ARR.scd
		names.add("FORM"); // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/FORM.scd
		names.add("LIBR"); // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/LIBR.scd
		names.add("FIL");  // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/FIL.scd
		names.add("SMST"); // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/SMST.scd
		names.add("SML");  // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/SML.scd
		names.add("EDIT"); // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/EDIT.scd
		names.add("MNTR"); // ==> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode/MNTR.scd

//		names.add("RTS$INTERFACE"); // Will call RTS_FEC_InterfaceGenerator  which creates the files:
									//
		                // NEI      // - "C:/Simuletta/Attrs/BEC/simulaRTS/RTS$INTERFACE.dat"
									// - "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulettaFEC/src/simulaRTS/sml/RTS$INTERFACE.sml"
									// ====>
									// - "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/RTSINIT.ini"
									//    C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FEC/src/fec/source/RTSINIT.ini
		
									// - "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/RTS-FEC-INTERFACE1.def"
		                            // - "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/RTS-FEC-INTERFACE2.def"
		
						// NEI		// - "C:/WorkSpaces/SPort-System/S_Port/attrs/FEC/PREDEF.atr
						// NEI		// - "C:/SPORT/FILES/Attrs/FEC/PREDEF.atr"
									// - "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/Attrs/FEC/PREDEF.atr"


	    // Selection Switches: +M  Myhres debug dumps
	    //                     +C: storage checking incl.gettextInfo
	    //                     +D: debug/trace output
		//					   -X: Code for SIMOB
		Option.SELECT="DM";
//		Option.SELECT="CDM";
//		Option.SELECT="CDMX";
//		Option.SELECT="CD";
		
		// Set Compile Time Options and tracing.BEGIN COMPILE
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
		
		//Option.TRACE_REPAIRING=true;

		// Runtime Trace Options
//		Option.TRACE_LOOM=false;//true;

//		File userDir=new File("C:/WorkSpaces/SPort-Backend/SimulettaFEC");
//		File userDir=new File("C:/WorkSpaces/SimulettaFECinJava/SimulettaFEC");
		File userDir=new File("C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulettaFEC");

		Global.packetName="simulaRTS";
		
//		Global.simulaRtsLib="C:/Simuletta/Attrs/FEC/simulaRTS";
//		Global.outputDir=new File("C:/Simuletta");
		
		Global.simulaRtsLib="C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/Attrs";
		Global.outputDir=new File("C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES");
		
		
		// Set RunTime Options and tracing.C:/Simuletta/Attrs/FEC/simulaRTS
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

		//if(Option.verbose)
			IO.println("\nBEGIN COMPILE RTS TO S-CODE");
		for(String name:names) {
			String fileName = userDir+"/src/"+Global.packetName+"/sml/"+name+".sml";
//			File outputFile=new File(userDir+"/src/"+Global.packetName+"/scd/"+name+".scd");
//			File outputFile=new File(Global.outputDir+"/SCode/"+Global.packetName+'/'+name+".scd");
			File outputFile=new File(Global.outputDir+"/"+Global.packetName+"/SCode/"+name+".scd");

			//outputFile.getParentFile().mkdirs();
			IO.println("    COMPILE "+fileName);
			IO.println("    OUTPUT  "+outputFile);
			try { outputFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }


			SimulettaCompiler compiler = new SimulettaCompiler(fileName,outputFile);
			compiler.doCompile();
			if(Option.verbose) {
				IO.println("    SCode Output: "+outputFile);
				IO.println("    Attrs Output: "+Global.outputAttributeFile);
			}
		}
		//if(Option.verbose) {
			IO.println("--- END COMPILE RTS TO S-CODE");
			long timeUsed  = System.currentTimeMillis( ) - startTimeMs;
			IO.println("\nElapsed Time: Approximately " + timeUsed/1000 + " sec.");
		//}
	}


}
