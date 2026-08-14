package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

import simula.SimulaCoreExports;

public class TestBatchLauncher {
//	private static final File simulaDir=new File("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/Simula");

	public static void run(String fileName, Vector<String> argv, Vector<String> argv2) {
//		try {
		// Remove time, date, and headers from Logger output.
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");
		
		SimulaCoreExports.initiate(new TestBatchClient(), argv);
		String uri = fileName;
		int version = 1;
        try {
            String content = Files.readString(Path.of(fileName));
//            content = content.replace("\r\n", "\n");
    	    SimulaCoreExports.didOpen(uri, version, content);
        } catch (IOException e) {
            e.printStackTrace();
        }

//		simulaCompiler.doCompile();
        SimulaCoreExports.run(uri, argv2);
        
//	} catch (IOException e) {
//		Util.generalError("can't open " + fileName + ", reason: " + e);
//	}

	}
}
