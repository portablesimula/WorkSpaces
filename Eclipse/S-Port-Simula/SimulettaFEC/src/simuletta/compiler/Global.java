/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler;

import java.io.File;
import java.util.Vector;

import simuletta.compiler.common.SCodeFile;
import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.scope.DeclarationScope;
import simuletta.compiler.declaration.scope.InsertedModule;
import simuletta.compiler.declaration.scope.ProgramModule;
import simuletta.utilities.Tag;


/**
 * Global Variables 
 * 
 * @author Øystein Myhre Andersen
 *
 */
public final class Global {
    public static final String simulettaReleaseID="Simuletta-1.0";

	public static int    sourceLineNumber;
	public static String currentSourceLine;
	public static File   sourceFileDir; 
	public static String sourceFileName;
	public static String sourceName;
	public static String simulaRtsLib;        // The simula runtime system
	public static String simulettaTESTLib;    // The simuletta TEST runtime system
	public static File   outputAttributeFile; // Where to write AttributeFiles
	public static File   outputDir;           // The Directory containing output AttributeFiles and SCode 
	public static int    nError;
	
	public static String packetName; // E.g. "simulaRTS"; // NOTE: Must be a single identifier

	public static Vector<InsertedModule> modset;  // The set of all inserted modules
	public static Vector<Declaration> bodyList;   // See: RoutineBody.doSCodeDeclaration
	public static ProgramModule currentModule;    // Current ProgramModule
	public static DeclarationScope currentScope;  // Current Scope. Maintained during Checking and Coding
	public static boolean allVisible;             // Set by Compiler Directive  %VISIBLE and %HIDDEN
	public static SCodeFile sCode;                // Set when SCoding starts: new SCodeFile();
	public static boolean duringMacroDefinition;

	public static void INIT() {
	    Tag.INIT();
	    ProgramModule.INIT();
		modset=new Vector<InsertedModule>();
		bodyList=new Vector<Declaration>();
		currentModule=new ProgramModule(null);
		currentScope=null;
		allVisible=false;
		sCode=null;
		duringMacroDefinition=false;
		nError=0;
	}	
	
	//%title ***   k i n d    c o d e s   ***

	public static final int K_ident = 0;  //!corresponds to RTS 'k_smp';
	public static final int K_proc  = 1;  //!corresponds to RTS 'k_pro';
	public static final int K_array = 2;  //!corresponds to RTS 'k_arr';
	public static final int K_label = 3;  //!corresponds to RTS 'k_lab';
	public static final int K_switch= 4;  //!corresponds to RTS 'k_swt';
	public static final int K_class = 5;  //!corresponds to RTS 'k_cla';
	public static final int K_rep   = 6;  //!corresponds to RTS 'k_rep';
	public static final int K_record= 7;  //!corresponds to RTS 'k_rec';
	public static final int K_subbl = 8;
	public static final int K_prefbl= 9;
	public static final int K_error = 10;
	public static final int K_labbl = 11;
	public static final int K_unknwn= 12;
	public static final int K_extnal= 13;

//	public static final int K_literal= 14;
//	public static final int K_macro= 15;

	public static final int K_max   = 15;  //!NB *** must be < 16 ;

	public static String K_code(int k) {
		if(k == K_error  ) return("unknwn");
		if(k == K_ident  ) return("ident");
		if(k == K_array  ) return("array");
		if(k == K_rep    ) return("infix array");
		if(k == K_proc   ) return("proc");
		if(k == K_class  ) return("class");
		if(k == K_label  ) return("label");
		if(k == K_switch ) return("switch");
		if(k == K_subbl  ) return("subbl");
		if(k == K_prefbl ) return("prefbl");
		if(k == K_record ) return("record");
		if(k == K_labbl  ) return("labbl");
		if(k == K_unknwn ) return("unknwn");
		if(k == K_extnal ) return("predef");
//		if(k == K_literal) return("literal");
//		if(k == K_macro  ) return("macro");
		return("illegal");
	}
	
}