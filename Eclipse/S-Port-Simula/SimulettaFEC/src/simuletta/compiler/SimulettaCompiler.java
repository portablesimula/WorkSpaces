/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler;

import static simuletta.compiler.Global.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.InlineRoutine;
import simuletta.compiler.declaration.scope.InsertedModule;
import simuletta.compiler.parsing.Parser;
import simuletta.compiler.statement.Statement;
import simuletta.utilities.Option;
import simuletta.utilities.Tag;
import simuletta.utilities.Util;

public class SimulettaCompiler {
	//      -----------------------------------------------------------------
	//      ---                                                           ---
	//      ---                 P O R T A B L E     S I M U L A           ---
	//      ---                                                           ---
	//      ---              S I M U L E T T A    C O M P I L E R         ---
	//      ---                                                           ---
	//      -----------------------------------------------------------------
	public final String inputFileName;
	public final File outputFile;
	
	public SimulettaCompiler(String inputFileName,File outputFile) {
		this.inputFileName=inputFileName;
		this.outputFile=outputFile;
	}

	public void doCompile() {
		Global.INIT();
		File file=new File(inputFileName);
		String name=file.getName();
		int i=name.indexOf('.');
		if(i!=0) name=name.substring(0,i);
		Parser.open(name,getReader());
		currentModule.parse();
		Parser.close();
		//currentModule.print("(END PARSING)",4);
		declareInlineRoutines();

		Tag.checkPredefinedEmpty();


		if (Option.verbose)	Util.println("******************** Pass2 Checking ********************");
		currentModule.checkDeclarationList();
		for(Declaration dcl:currentModule.declarationList) dcl.doChecking();
		//currentModule.print("(END CHECKING DECLARATIONS)",4);
		for (Statement stm : currentModule.statements) stm.doChecking();
		//currentModule.print("(END CHECKING STATEMENTS)",4);

		if (Option.verbose)	Util.println("******************** Pass3 S-Coding ********************");
		currentModule.doSCoding(outputFile);

		if (Option.verbose)	{
			Util.println("********************************** BEGIN LISTING OF MODULES "+modset.size()+" **********************************");
			for(InsertedModule m:modset) {
				Util.println(m.toString());
			}
			Util.println("********************************** ENDOF LISTING OF MODULES "+modset.size()+" **********************************");
		}

		if(Option.verbose || nError != 0) {
			StringBuilder s=new StringBuilder();
			s.append("End Simuletta: ").append(Global.sourceLineNumber).append(" lines, ");
			if(nError != 0) s.append(nError); else s.append("no");
			s.append(" errors.");
			IO.println(s.toString()); // symboltable.close;
		}
		if(nError > 0) IO.println("Program Terminated due to " + nError + " Errors");	            
	}

	private InputStreamReader getReader() {
		InputStreamReader reader = null;
		try { reader=new InputStreamReader(new FileInputStream(inputFileName));
		} catch (IOException e) { Util.ERROR("can't open " + inputFileName+", reason: "+e); }
		if (!inputFileName.toLowerCase().endsWith(".sml"))
			Util.WARNING("Simuletta source file should, by convention be extended by .sml: " + inputFileName);
		File inputFile = new File(inputFileName);
		// Create Output File Path
		String name = inputFile.getName();
		Global.sourceFileName=name;
		Global.sourceName = name.substring(0, name.length() - 4);
		Global.sourceFileDir=inputFile.getParentFile();
		if(Option.TRACING) Util.println("Compiling: \""+inputFileName+"\"");
		return(reader);
	}

	//title ******   I n i t i a l i z a t i o n   ******

	private static void declareInlineRoutines() {
	      def_inlineRoutine("zeroarea",InlineRoutine.Kind.ZEROAREA);
	      def_inlineRoutine("initarea",InlineRoutine.Kind.INITAREA);
	      def_inlineRoutine("dinitarea",InlineRoutine.Kind.DINITAREA);
	      def_inlineRoutine("init_pointer",InlineRoutine.Kind.INITO);
	      def_inlineRoutine("set_pointer",InlineRoutine.Kind.SETO);
	      def_inlineRoutine("max_temps",InlineRoutine.Kind.MAXTEMPS);
	      def_inlineRoutine("length_temps",InlineRoutine.Kind.PUSHLEN);
	      def_inlineRoutine("get_pointer",InlineRoutine.Kind.GETO);

//		  def_inlineRoutine("set_display",SystemFunction.Kind.SETOBJ);
//	      def_inlineRoutine("display",SystemFunction.Kind.GETOBJ);
//	      def_inlineRoutine("rec_size",SystemFunction.Kind.DSIZE);
//	      def_inlineRoutine("conv_ref",SystemFunction.Kind.REF);
//	      def_inlineRoutine("conv_field",InlineRoutine.Kind.FIELD);
	      
//	      currentModule.printDeclarationList();
//	      Util.STOP();
	}
	
    private static void def_inlineRoutine(String ident,InlineRoutine.Kind kind) {
    	currentModule.addIfNotPresent(new InlineRoutine(ident,kind));
    }
	
	
}

