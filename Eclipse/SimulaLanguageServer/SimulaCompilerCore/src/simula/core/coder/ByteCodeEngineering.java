package simula.core.coder;

import java.io.IOException;

import simula.Option;
import simula.core.CoreGlobal2;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.transform.ClassFileTransform;
import simula.core.utilities.Util;

public class ByteCodeEngineering {
	
	/// Possible doByteCodeEngineering reintroducing labels and goto.
	/// @throws IOException if something went wrong.
	static void doByteCodeEngineering(final SimulaCoder simCoder) throws IOException {
		if (Option.internal.keepJava == null) {
			if (Option.internal.TRACE_BYTECODE_OUTPUT) {
				IO.println("------------  LIST ByteCode Before Engineering  ------------");
				for (JavaSourceFileCoder javaClass : simCoder.javaSourceFileCoders) {
					String classFile = javaClass.getClassOutputFileName(simCoder);
					Util.doListClassFile(classFile);
				}
			}
			for (JavaSourceFileCoder javaClass : simCoder.javaSourceFileCoders) {
				if (javaClass.mustDoByteCodeEngineering) {
					String classFileName = javaClass.getClassOutputFileName(simCoder);
					ClassFileTransform.doRepairSingleByteCode(classFileName,classFileName);
					if(CoreGlobal2.verbose) IO.println("SimulaCompiler.doByteCodeEngineering: " + simCoder.documentManager.sourceName + ": Class File " + classFileName + " is repaired");
				}
			}
			if (Option.internal.TRACE_BYTECODE_OUTPUT) {
				IO.println("------------  LIST ByteCode After Engineering  ------------");
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
