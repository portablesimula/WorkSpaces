package simula.lsp.coder;

import java.io.IOException;

import simula.lsp.DocumentManager;
import simula.lsp.builder.JavaSourceFileCoder;
import simula.lsp.transform.ClassFileTransform;
import simula.lsp.utilities.Option;
import simula.lsp.utilities.Util;

public class ByteCodeEngineering {
	
	/// Possible doByteCodeEngineering reintroducing labels and goto.
	/// @throws IOException if something went wrong.
	static void doByteCodeEngineering(final SimulaCoder simCoder) throws IOException {
		if (Option.internal.keepJava == null) {
			if (Option.internal.TRACE_BYTECODE_OUTPUT) {
				Util.println("------------  LIST ByteCode Before Engineering  ------------");
				for (JavaSourceFileCoder javaClass : simCoder.javaSourceFileCoders) {
					String classFile = javaClass.getClassOutputFileName(simCoder);
					Util.doListClassFile(classFile);
				}
			}
			for (JavaSourceFileCoder javaClass : simCoder.javaSourceFileCoders) {
				if (javaClass.mustDoByteCodeEngineering) {
					String classFileName = javaClass.getClassOutputFileName(simCoder);
					ClassFileTransform.doRepairSingleByteCode(classFileName,classFileName);
					if(DocumentManager.verbose) Util.println("SimulaCompiler.doByteCodeEngineering: " + simCoder.documentManager.sourceName + ": Class File " + classFileName + " is repaired");
				}
			}
			if (Option.internal.TRACE_BYTECODE_OUTPUT) {
				Util.println("------------  LIST ByteCode After Engineering  ------------");
				for (JavaSourceFileCoder javaClass : simCoder.javaSourceFileCoders) {
					String classFile = javaClass.getClassOutputFileName(simCoder);
					Util.doListClassFile(classFile);
				}
			}
		} else {
			Util.generalWarning("Option.internal.keepJava set: No ByteCode Engineering is performed");
		}
	}

}
