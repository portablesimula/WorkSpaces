/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package make.java;

import java.io.File;
import java.io.IOException;
import simula.compiler.SourceModule;
import simula.Option;
import simula.compiler.REMOVE_SimulaCompiler;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.Util;
import simula.editor.RTOption;

/**
 * Run inline compiler tests.
 * @author Øystein Myhre Andersen
 *
 */
public final class RunSimulaSample {

	public static void main(String[] args) {
		//System.setProperty("file.encoding","UTF-8");

		// *** SIMULA PROGRAMMER TIL RELEASE TESTING
//		String name="AnimationTest.sim";
//		String name="Atkins.sim";
//		String name="Dates.sim";
//		String name="DatesTest.sim";
//		String name="FittingRoom.sim";
//		String name="HelloWorld.sim";
//		String name="HexDump.sim";
//		String name="InfectionDisease.sim";
//		String name="JensensDevice.sim";
//		String name="LiftSimulation.sim";
//		String name="NormalDrawing.sim";
//		String name="PrimeUnder.sim";
//		String name="Quine.sim";
//		String name="Sudoku.sim";
//		String name="SudokuPuzzle.sim";
		String name="TQueens.sim";

		// Set Compiler Options.
//		Option.compilerMode = SimulaCompiler.CompilerMode.viaJavaSource;
		Option.compilerMode = SimulaCompiler.CompilerMode.directClassFiles;
//		Option.compilerMode = SimulaCompiler.CompilerMode.simulaClassLoader;
//		Option.verbose=true;
//		Option.EXTENSIONS=false;
//		Option.CaseSensitive=true;
//		Option.noExecution=true;
//		Option.WARNINGS=false;

		// Set internal test, debug options.
		Option.internal.INLINE_TESTING=true;
		Option.noPopup = true;
		Option.internal.TESTING_STACK_SIZE = true;
//		Option.internal.TRACING=false;
//		Option.internal.TRACE_ATTRIBUTE_OUTPUT=true;
//		Option.internal.TRACE_ATTRIBUTE_INPUT=true;

//		File simulaHome=new File("C:/GitHub/WorkSpaces/Eclipse/SimulaProjects/TestBatch");
		File simulaDir=new File("C:/GitHub/WorkSpaces/Eclipse/SimulaProjects/Simula");
		File userDir=new File("C:/GitHub/WorkSpaces/Eclipse/SimulaProjects/SimulaTestBatch");
				
		CoreGlobal.packetName="simulaSamples";
//		Option.internal.keepJava=userDir; // Generated .java Source is then found in Eclipse Package simulaTestPrograms
		CoreGlobal.simulaRtsLib=new File(simulaDir,"bin"); // To use Eclipse Project's simula.runtime
//		Option.outputDir=new File("C:/GitHub/WorkSpaces/Eclipse/SimulaProjects/Simula/src/simulaTestPrograms/samples/simula/bin");
//		Global.outputDir=new File("C:/GitHub/WorkSpaces/Eclipse/SimulaProjects/TestBatch/src/simulaTestPrograms/samples/simula/bin");
		CoreGlobal.outputDir=new File("C:/GitHub/WorkSpaces/Eclipse/SimulaProjects/CompilerTests/bin/simulaTestPrograms");
			
			
		// Set RunTime Options and tracing.
		RTOption.VERBOSE = false;
		RTOption.VERBOSE = true;
//		RTOption.USE_CONSOLE=true;
//		RTOption.BLOCK_TRACING = true;
//		RTOption.GOTO_TRACING = false;
//		RTOption.QPS_TRACING = false;
//		RTOption.SML_TRACING = false;

		String fileName=userDir+"/src/"+CoreGlobal.packetName + '/' + name;
		Option.internal.RUNTIME_USER_DIR=new File(fileName).getParent();

		try {
			CoreGlobal.initiate();
	    	new SourceModule(new File(fileName));
			new REMOVE_SimulaCompiler(fileName).doCompile(CoreGlobal.currentModule.getSyntaxTree());
		} catch (IOException e) {
			Util.generalError("can't open " + fileName + ", reason: " + e);
		}
	}


}
