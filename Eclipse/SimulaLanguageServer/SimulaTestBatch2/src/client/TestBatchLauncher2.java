package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TestBatchLauncher2 {
	
	public static void run(String fileName, Vector<String> args) {
		try {
			String jarPath = "C:/Users/omyhr/Simula/SimulaLspServer.jar"; 
			List<String> command = new ArrayList<>();
			command.add("java");
			command.add("-jar");
			command.add(jarPath);
			command.add("--debugRun");
			command.add(fileName);
			for(String arg:args) {
				command.add(arg);
			}

//			StringBuilder cmd = new StringBuilder();
//			for(String c:command) {
//				cmd.append(c).append(' ');
//			}
//			IO.println("TestBatchLauncher2.run: " + cmd);

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.inheritIO();
			Process process = pb.start();

			@SuppressWarnings("unused")
			int exitCode = process.waitFor();
		} catch (Exception e) {
			System.err.println("Klarte ikke å starte prosessen: " + e.getMessage());
		}
	}


}
