/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package make.java;

import java.io.File;
import java.util.Vector;

import client.TestBatchLauncher;
import simula.lsp.utilities.Option;
import simula.runtime.RTS_Option;

/**
 * Simula Test Batch donated by Simula as.
 * 
 * @author Several from Norwegian Computing Center.
 * @author Several from Simula as.
 * @author Øystein Myhre Andersen
 *
 */
public final class RunErrorTest {
	
	private static final File rtsDir=new File("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaRuntimeSystem");
	private static final String userDir = "C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch";
	private static final String sourceDir = userDir+"/src/simulaErrors/";


	public static void main(String[] argv) {

		Vector<String> args = new Vector<>();
//		args.add("-caseSensitive");
//		args.add("-noextension");
		args.add("-noPopup");
//		args.add("-nowarn");
//		args.add("-verbose");
//		args.add("-select");

		args.add("-compilerMode"); args.add("viaJavaSource");
//		args.add("-compilerMode"); args.add("directClassFiles");
		
//		args.add("-noexec");
		args.add("-keepJava"); args.add(userDir); // Generated .java Source is then found in Eclipse Package simulaTestBatch
		args.add("-simulaRtsLib"); args.add(new File(rtsDir,"bin").toString()); // To use Eclipse Project's simula.runtime
//		args.add("-extLib"); args.add("C:/GitHub/WorkSpaces/Eclipse/SimulaProjects/Simula/src/simulaTestBatch/sim/bin");
		
		setOptions();
		
		Vector<String> names=new Vector<String>();
		
//		names.add("expression/AdHoc00");

		// *** SIMULA ERROR TEST BATCH TIL EKSEKVERING
//		names.add("TypeError");
//		names.add("HiddenProtected_Errors");
//		names.add("ClassDeclaration_Errors");
//		names.add("Declaration_Errors");
//		names.add("Parameter_Errors");
//		names.add("PrefixedBlock_Errors");
//		names.add("ProcedureDeclaration_Errors");

//		names.add("expression/ArithmeticExpression_Errors");
//		names.add("expression/AssignmentOperation_Errors");
//		names.add("expression/BooleanExpression_Errors");
		names.add("expression/ConditionalExpression_Errors");

		
//		names.add("ObjectRelation_Errors");

				
		for(String name:names) {
			String fileName = sourceDir + name + ".sim";
			TestBatchLauncher.run(fileName, args);
		}
	}

	private static void setOptions() {
		// Set internal test, debug options.
		
		Option.internal.INLINE_TESTING=true;
//		Option.noPopup = true;
		Option.internal.TESTING_STACK_SIZE = true;
//		Option.internal.DEBUGGING=true;
//		Option.internal.LIST_GENERATED_CLASS_FILES=true;

		// Overall TRACING Options
//		Option.internal.TRACING=true;

		// Scanner Trace Options
//		Option.internal.TRACE_LEXER = 2;
//		Option.internal.TRACE_NEW_LEXTOKEN = 2;
//		Option.TRACE_COMMENTS=true;

		// Parser Trace Options
//		Option.internal.TRACE_PARSE=true;
//		Option.internal.PRINT_SYNTAX_TREE=1;
//		Option.internal.TRACE_ATTRIBUTE_OUTPUT=true;
//		Option.internal.TRACE_ATTRIBUTE_INPUT=true;

		// Checker Trace Options
//		Option.internal.TRACE_FIND_MEANING=4;
//		Option.internal.TRACE_CHECKER=true;
//		Option.internal.TRACE_CHECKER_OUTPUT=true;
//		Option.internal.PRINT_SYNTAX_TREE = 4;

		// .java Coder Trace Options
//		Option.TRACE_CODING=true;
//		Option.GNERATE_LINE_CALLS=true;

		// Byte code engineering Options
//		Option.LIST_INPUT_INSTRUCTION_LIST=true;
//		Option.LIST_REPAIRED_INSTRUCTION_LIST=true;
//		Option.internal.TRACE_REPAIRING_INPUT=true;
//		Option.internal.TRACE_REPAIRING=true;
//		Option.internal.TRACE_REPAIRING_OUTPUT=true;
		
		// Set RunTime Options and tracing.
//		RTOption.VERBOSE = true;
//		RTOption.USE_CONSOLE=true;
//		RTOption.BLOCK_TRACING = true;
		RTS_Option.BLOCK_TRACING = true;
//		RTOption.GOTO_TRACING = true;
//		RTOption.QPS_TRACING = true;
//		RTOption.SML_TRACING = true;

	}
	
	
}
