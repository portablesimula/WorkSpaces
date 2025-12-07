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

public final class FECmpTESTS {

	// ***************************************************************
	// *** COMPILE SIMPLE TESTS TO S-CODE
	// ***************************************************************
	public static void main(String[] args) {
		long startTimeMs = System.currentTimeMillis( );
		Vector<String> names=new Vector<String>();
		
//		names.add("ENVIR0");
//		names.add("MODL01");
//		names.add("MODL02");
//		names.add("TEST6");
//		names.add("TEST00");
//		names.add("TEST01");
//		names.add("TEST02");
	
//		names.add("ENVIR");
//		names.add("MODL1");
//		names.add("MAIN");
//		names.add("TEST1");
//		names.add("TEST2");
//		names.add("TEST3");
//		names.add("TEST4");
//		names.add("TEST5");
//		names.add("TEST6");
//		names.add("TEST7");
//		names.add("TEST8");
//		names.add("TEST9");
//		names.add("TEST10");
//		names.add("TEST11");
//		names.add("TEST12");
//		names.add("TEST13");
//		names.add("TEST14");


		// Set Compile Time Options and tracing.
//		Option.INLINE_TESTING=true;
	    Option.verbose = true;
//		Option.WARNINGS=true;

		// Overall TRACING Options
//		Option.TRACING=true;
//		Option.BREAKING=true; 

		// Scanner Trace Options
		Option.SOURCE_LISTING=true;
//		Option.TRACE_SELECTION=true;
//		Option.TRACE_SCAN=true;
//		Option.TRACE_MACRO_SCAN=true;
//		Option.TRACE_MACRO_EXPAND=true;
//		Option.TRACE_COMMENTS=true;

		// Parser Trace Options
//		Option.TRACE_PARSE=true;
//		Option.TRACE_PARSE_BREIF=true;
		Option.TRACE_ATTRIBUTE_OUTPUT=true;
//		Option.TRACE_ATTRIBUTE_INPUT=true;

		// Checker Trace Options
//		Option.TRACE_CHECKER=true;
//		Option.TRACE_CHECKER_OUTPUT=true;
		//Option.TRACE_FIND=2;
		
		// Coder Trace Options
//		Option.TRACE_FIND_MEANING=true;
		Option.TRACE_CODING=1;
		
		//Option.TRACE_REPAIRING=true;

		// Runtime Trace Options
//		Option.TRACE_LOOM=false;//true;

//		File userDir=new File("C:/WorkSpaces/SPort-Backend/SimulettaFEC");
//		File userDir=new File("C:/WorkSpaces/SimulettaFECinJava/SimulettaFEC");
		File userDir=new File("C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulettaFEC");
		Global.packetName="simulettaTestPrograms";
		
//		Global.simulaRtsLib=     "C:/Simuletta/Attrs/FEC/simulaRTS";
//		Global.simulettaTESTLib= "C:/Simuletta/Attrs/FEC/simulettaTestPrograms";
		
		Global.simulaRtsLib     = "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/Attrs";
		Global.simulettaTESTLib = "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulettaFEC/src/simulettaTestPrograms";

//		Global.outputDir=new File("C:/Simuletta");
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

		if(Option.verbose) IO.println("\nBEGIN COMPILE TESTS TO S-CODE");
		for(String name:names) {
			String fileName = userDir+"/src/"+Global.packetName+"/sml/"+name+".sml";
//			File outputFile=new File(userDir+"/src/"+Global.packetName+"/scd/"+name);
//			File outputFile=new File(Global.outputDir+"/SCode/"+Global.packetName+'/'+name+".scd");
			File outputFile=new File(Global.outputDir+"/"+Global.packetName+"/SCode/"+name+".scd");

			//outputFile.getParentFile().mkdirs();
			try { outputFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }


			SimulettaCompiler compiler = new SimulettaCompiler(fileName,outputFile);
			compiler.doCompile();
			if(Option.verbose) {
				IO.println("    SCode Output: "+outputFile);
				IO.println("    Attrs Output: "+Global.outputAttributeFile);
			}
		}
		if(Option.verbose) {
			IO.println("--- END COMPILE SIMPLE TESTS TO S-CODE");
			long timeUsed  = System.currentTimeMillis( ) - startTimeMs;
			IO.println("\nElapsed Time: Approximately " + timeUsed/1000 + " sec.");
		}
	}


}
