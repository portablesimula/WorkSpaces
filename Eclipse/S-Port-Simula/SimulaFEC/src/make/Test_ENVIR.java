package make;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class Test_ENVIR {

	
	public static void main(String[] argv) {
//		compile("ENVIR-PART1.sim");
		compile("ENVIR.sim");
		
		System.out.append("END TEST");
	}
	

	private static void compile(String name) {
		String fileName = "C:\\GitHub\\EclipseWorkSpaces\\S-Port-Simula\\SimulaFEC\\src\\fec\\source\\"+name;
		
		Vector<String> cmds = new Vector<String>();
		cmds.add("java");
		cmds.add("-jar");
		cmds.add("C:\\Users\\omyhr\\Simula\\Simula-2.0\\simula.jar");
//		cmds.add("-verbose");
		cmds.add("-noexec");
//		cmds.add("-nowarn");
		cmds.add("-sport");
		cmds.add("-select"); cmds.add("ZDTW");
//		cmds.add("-select"); cmds.add("ZDW");
		cmds.add(fileName);

		try {
			exec(cmds);
		} catch (IOException e) {
			IO.println("Test_ENVIR.compile - Exit: ");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// ***************************************************************
	// *** EXECUTE OS COMMAND
	// ***************************************************************
	public static int exec(final Vector<String> cmd) throws IOException {
		String[] cmds = new String[cmd.size()];
		cmd.copyInto(cmds);
		return (exec(cmds));
	}

	public static int exec(String... cmd) throws IOException {
		String line="";
		for(int i=0;i<cmd.length;i++) line=line+" "+cmd[i];
        IO.println("MakeCompiler.execute: command="+line);
		ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();		
			InputStream output = process.getInputStream();  // Process' output
			while (process.isAlive()) {
				while (output.available() > 0)
					System.out.append((char) output.read());
			}
			return (process.exitValue());

		} catch(Exception e) {
			IO.println("ERROR: "+e);
			throw new RuntimeException("Process Execution failed: " + line, e);
		}
	}



}
