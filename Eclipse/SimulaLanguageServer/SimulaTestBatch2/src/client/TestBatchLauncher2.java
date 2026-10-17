package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TestBatchLauncher2 {
	
	public static void run(String fileName, Vector<String> argv, Vector<String> argv2) {
        try {
            String jarPath = "C:/Users/omyhr/Simula/SimulaLspServer.jar"; 
            List<String> command = new ArrayList<>();
            command.add("java");
            command.add("-jar");
            command.add(jarPath);
            command.add("--debugRun");
            command.add(fileName);
            for(String arg:argv2) {
            	command.add(arg);
            }
            
            StringBuilder cmd = new StringBuilder();
            for(String c:command) {
            	cmd.append(c).append(' ');
            }
            IO.println("TestBatchLauncher2.run: " + cmd);

            // 2. Opprett ProcessBuilder
            ProcessBuilder pb = new ProcessBuilder(command);

            // 3. (Valgfritt) Send programmets output direkte til din egen konsoll
            // Dette hindrer også at prosessen "henger" fordi IO-bufferet blir fullt
            pb.inheritIO();

            // 4. Start prosessen
            System.out.println("Starter MyProgram.jar...");
            Process process = pb.start();

            // 5. Vent til prosessen terminerer og hent exit-koden
            int exitCode = process.waitFor();
            
            System.out.println("Prosessen avsluttet med exit-kode: " + exitCode);

        } catch (IOException e) {
            System.err.println("Klarte ikke å starte prosessen: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Tråden ble avbrutt mens den ventet: " + e.getMessage());
            Thread.currentThread().interrupt(); // Gjenopprett avbrutt-status
        }
    }
	

}
