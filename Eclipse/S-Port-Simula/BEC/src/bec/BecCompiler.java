/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec;

import bec.descriptor.Display;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.Terminal;
import bec.util.Util;

/// This is an implementation of a S-Code Back-end Compiler (BEC).
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/BecCompiler.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public final class BecCompiler {
	
	/// Main method 
	/// @param argv the arguments
	public static void main(String[] argv) {
		String scodeSource = null;
		// Parse command line arguments.
		for(int i=0;i<argv.length;i++) {
			String arg=argv[i];
			if (arg.charAt(0) == '-') { // command line option
				if (arg.equalsIgnoreCase("-help")) help();
				else if (arg.equalsIgnoreCase("-inputTrace")) Option.SCODE_INPUT_TRACE = true;
				else if (arg.equalsIgnoreCase("-traceSVM_CODE")) Option.PRINT_GENERATED_SVM_CODE = true;
				else if (arg.equalsIgnoreCase("-traceSVM_DATA")) Option.PRINT_GENERATED_SVM_DATA = true;
				else if (arg.equalsIgnoreCase("-verbose")) Option.verbose = true;
				else if (arg.equalsIgnoreCase("-execVerbose")) Option.execVerbose = true;
				else if (arg.equalsIgnoreCase("-nopopup")) Option.nopopup = true;
				else if (arg.equalsIgnoreCase("-execTrace")) Option.EXEC_TRACE = 1;
				else if (arg.equalsIgnoreCase("-callTrace")) Option.CALL_TRACE_LEVEL = 2;
				else if (arg.equalsIgnoreCase("-dumpsAtExit")) Option.DUMPS_AT_EXIT = true;
				else {
					Util.ERROR("Unknown option " + arg);
					help();
				}
			} else if(scodeSource==null) scodeSource = arg;
			else Util.ERROR("multiple input files specified");
		}
		
		if(scodeSource==null) {
			Util.ERROR("no input file specified");
			help();
		}
		if(! Option.nopopup) Global.console = new Terminal("Runtime Console");
		
		try {
			new BecCompiler(scodeSource);
		} catch(Throwable e) {
			Util.println("BecCompiler.main: BEC GOT Exception: " + e.getClass().getSimpleName() + "  " + e.getMessage());
			Thread.dumpStack();
			printStackTrace();
			if(Global.console != null) {
				Util.println("BecCompiler.main: Program will terminate in aprox. 30 secods");
				try { Thread.sleep(30 * 1000); } catch (InterruptedException e2) {}
			}
			System.exit(-1);
		}
	}

	/// Print synopsis of standard options
	private static void help() {
		IO.println("");
		IO.println("Usage: java -jar CommonBEC.jar  [options]  ScodeFile ");
		IO.println("");
		IO.println("possible options include:");
		IO.println("  -verbose  Output messages about what the compiler is doing");
		IO.println("  -execVerbose Output messages about what the executor is doing");
		IO.println("  -help        Print this synopsis of standard options");
		IO.println("  -inputTrace  Produce input Scode trace");
		IO.println("  -listing     Produce pretty Scode listing");
		IO.println("  -execTrace   Produce instruction trace during execution");
		IO.println("  -callTrace   Produce routine call trace during execution");
		IO.println("  -dumpsAtExit Produce certain dumps at en of execution");
		IO.println("");
		IO.println("sourceFile ::= S-Code Source File");

		IO.println("BecCompiler.help - Exit: ");
		Thread.dumpStack();
		System.exit(0);
	}


	/// S-program ::= program program_head:string
	/// 						 program_body
	///               endprogram
	/// 
	/// 	program_body 
	/// 		::= interface_module
	/// 		::= macro_definition_module
	/// 		::= <module_definition>*
	/// 		::= main <local_quantity>* < program_element >*
	///
	/// @param scodeSource source code filename
	public BecCompiler(final String scodeSource) {
		if(Option.verbose) Util.println("BEC: Start BecCompiler with " + scodeSource);
		Global.init(scodeSource);
		Display.init();
		Scode.init();
		Type.init();

		Scode.inputInstr();
		if(Scode.curinstr == Sinstr.S_PROGRAM) {
	  		Global.progIdent = Scode.inString();
			Scode.inputInstr();
			if(Scode.curinstr == Sinstr.S_GLOBAL) {
				new InterfaceModule();
				Scode.inputInstr();
			}
			while(Scode.curinstr == Sinstr.S_MODULE) {
				new ModuleDefinition();
				Scode.inputInstr();
				Util.println("*** WARNING: SCode file written to " + scodeSource);
			}
			if(Scode.curinstr == Sinstr.S_MAIN) {
				new MainProgram();
			}
		} else Util.IERR("Illegal S-Program");
		
		if(Option.verbose) {
			Util.println("DONE: BecCompiler: " + scodeSource);
		}
	}
	
	/// Utility: printStackTrace
	private static void printStackTrace() {
		StackTraceElement[] elts = Thread.currentThread().getStackTrace();
		for(StackTraceElement elt:elts) {
			Util.println(""+elt);
		}
	}

}
