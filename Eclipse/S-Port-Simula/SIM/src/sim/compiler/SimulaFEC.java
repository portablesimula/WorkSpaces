package sim.compiler;

import java.io.IOException;
import java.util.Vector;

import static sim.compiler.Global.*;

public abstract class SimulaFEC {
	
	private static boolean TESTING = false; // Remove when TESTING = false
	private static boolean fecTerminated;   // Remove when TESTING = false
	private static int exitCode;            // Remove when TESTING = false

	// ****************************************************************
	// *** SimulaEditor: Main Entry for TESTING ONLY
	// ****************************************************************
	public static void main(String[] args) {
		Option.verbose = true;
		Option.fecListing = true;
//		String name = "HelloWorld";
		String name = "Sudoku";
		sCodeFileName = "src/sim/samplePrograms/scode/"+name+".scd";
		sourceFileName = "src/sim/samplePrograms/"+name+".sim";
		callSimulaFEC();
	}

	/// Called from EditorMenues 'run'
	public static int callSimulaFEC() {
		Vector<String> cmds = new Vector<String>();
		cmds.add("java");
		cmds.add("-jar");
//		cmds.add("C:\\SPORT\\SimulaFEC.jar");
		cmds.add(Global.simdir + "\\SimulaFEC.jar");
		cmds.add("-nopopup");
		if(Option.verbose) cmds.add("-verbose");
//		if(Option.fecTraceLevel > 0) { cmds.add("-SPORT:trace"); cmds.add(""+Option.fecTraceLevel); }
		if(Option.fecListing) cmds.add("-SPORT:listing");
		if(Option.fecSCodeTrace) cmds.add("-SPORT:traceScode");
		if(selectors != null) {	cmds.add("-SPORT:select"); cmds.add(selectors); }
		cmds.add("-SPORT:SCodeFile"); cmds.add(sCodeFileName);
		cmds.add(sourceFileName);

		if(Option.verbose) IO.println("BEGIN SIMULA FEC ==> \"" + sCodeFileName + '"');
		if(TESTING) {
			Runnable task = new Runnable() {
				public void run() {
					try {
						exitCode = Util.exec(cmds);
						fecTerminated = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			fecTerminated = false;
			new Thread(task).start();
			while(! fecTerminated) Thread.yield();
			IO.println("SimulaFEC.callSimulaFEC: exitCode=" + exitCode);
			return exitCode;
		} else {
			try {
				return Util.exec(cmds);
			} catch (IOException e) {
				IO.println("SimulaFEC.callFEC - Exit: ");
				e.printStackTrace();
				System.exit(0);
				return -1;
			}
			
		}
	}

}
