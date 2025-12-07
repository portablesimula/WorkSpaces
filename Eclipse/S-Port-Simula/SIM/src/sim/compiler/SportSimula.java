package sim.compiler;

import java.io.File;

import sim.editor.SPortEditor;

import static sim.compiler.Global.*;

public class SportSimula {

//	private final static String RELEASE_HOME  = "C:/SPORT";
//	private final static String SportSIM_ROOT = "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SIM";
//	private final static String COMPILER_BIN  = SportSIM_ROOT+"/bin";

	/**
	 * Print synopsis of standard options
	 */
	private static void help() {
//		IO.println(Global.sPortReleaseID+" See: https://github.com/portablesimula");
		IO.println("");
		IO.println("Usage: java -jar simula.jar  [options]  sourceFile ");
		IO.println("");
		IO.println("possible options include:");
		IO.println("  -help                      Print this synopsis of standard options");
		IO.println("  -caseSensitive             Source file is case sensitive.");	
		IO.println("  -compilerMode modeString   Simula Compiler mode *) see below.");	
		IO.println("  -noexec                    Don't execute generated .jar file");
		IO.println("  -nowarn                    Generate no warnings");
		IO.println("  -noextension               Disable all language extensions");
		IO.println("                             In other words, follow the Simula Standard literally");
		IO.println("  -verbose                   Output messages about what the compiler is doing");

		IO.println("  -FEC:select characters     First, all selectors are reset.");
		IO.println("                             Then, for each character, the corresponding selector is set");
		IO.println("  -sport                     Enable all S-PORT extensions");
		
		IO.println("SportSimula.help - Exit: ");
		Thread.dumpStack();
		System.exit(0);
	}

	public static void main(String[] argv) {
		try {
			if(argv.length == 0) {
				SPortEditor editor=new SPortEditor();
				editor.start();				
			} else {
				Global.initiate();
//				IO.println("RUN SPORT SIM Compiler.jar  argv.length="+argv.length);
//				String cmdLine="";
//				for(int i=0;i<argv.length;i++) cmdLine=cmdLine+" "+argv[i];
//		        IO.println("SPortSimula.main: command ="+cmdLine);
		        decodeArgv(argv);
				
//				Util.IERR("NOT IMPL");
//				Option.verbose = true;
				
		        File sourceFile = new File(sourceFileName);
				String name = sourceFile.getName();
				int pos = name.indexOf('.');
				if(pos > 0) name = name.substring(0, pos);
				File sCodeFile=new File(Global.getTempFileDir("sim/tmp/"), name + ".scd");
				sCodeFile.getParentFile().mkdirs();
				sCodeFileName = sCodeFile.toString();
				if(Option.verbose) Util.println("\n\nEditorMenues.doStartRunning: CALL FEC: Output ==> sCodeFileName = "+sCodeFileName);
				
				int execCode = SimulaFEC.callSimulaFEC();
				if(Option.verbose) IO.println("RETURN FROM FEC: ExitCode = "+execCode+"\n\n");
				
				if(execCode == 0) CommonBEC.callBEC();
				
			}		

		} catch(Exception e) { e.printStackTrace(); }
	}

	private static void decodeArgv(String[] argv) {
		// Parse command line arguments.
		for(int i=0;i<argv.length;i++) {
			String arg=argv[i];
			if (arg.charAt(0) == '-') { // command line option
				if (arg.equalsIgnoreCase("-help")) help();
				else if (arg.equalsIgnoreCase("-verbose")) Option.verbose=true;
				else if (arg.equalsIgnoreCase("-caseSensitive")) Option.CaseSensitive=true;
				else if (arg.equalsIgnoreCase("-nopopup")) Option.nopopup=true;
				else if (arg.equalsIgnoreCase("-simdir")) 	Global.simdir = new File(argv[++i]);

				else if (arg.equalsIgnoreCase("-FEC:Listing")) Option.fecListing = true;
				else if (arg.equalsIgnoreCase("-FEC:SCodeTrace")) Option.fecSCodeTrace = true;
//				else if (arg.equalsIgnoreCase("-FEC:TraceLevel")) Option.fecTraceLevel = true;
				
				else if (arg.equalsIgnoreCase("-BEC:SCodeTrace")) Option.becSCodeTrace = true;
				else if (arg.equalsIgnoreCase("-BEC:TraceSVM_CODE")) Option.becTraceSVM_CODE = true;
				else if (arg.equalsIgnoreCase("-BEC:TraceSVM_DATA")) Option.becTraceSVM_DATA = true;

				else if (arg.equalsIgnoreCase("-execTrace")) Option.execTrace=1;
				else if (arg.equalsIgnoreCase("-callTrace")) Option.callTrace=1;
				else if (arg.equalsIgnoreCase("-dumpsAtExit")) Option.dumpsAtExit=true;

				else {
					IO.println("ERROR: Unknown option " + arg);
					help();
				}
			} else if(sourceFileName==null) sourceFileName = arg;
			else {
				IO.println("ERROR: multiple input files specified");
				help();
			}
		}
		if(sourceFileName==null) {
			IO.println("ERROR: no input file specified");
			help();
		}
	}

}
