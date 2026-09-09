/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.declaration;

import simula.core.DocumentManager;
import simula.core.builder.util.Identifier;
import simula.core.DocumentManager;
import simula.core.syntaxClass.OverLoad;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.expression.Constant;
import simula.core.syntaxClass.statement.InlineStatement;
import simula.core.syntaxClass.statement.Statement;
import simula.core.utilities.LOG;
import simula.core.utilities.Meaning;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.ObjectList;
import simula.core.utilities.Util;

import java.lang.constant.ClassDesc;

import simula.Option;

/// Standard Class.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/declaration/StandardClass.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class StandardClass extends ClassDeclaration {
	public String edJavaClassName() {
		return (identifierValue());
	}

	/// The type text.
	public static StandardClass typeText;

	/// The Standard Class ENVIRONMENT.
	public static StandardClass ENVIRONMENT;

	/// The Standard Class BASICIO.
	public static StandardClass BASICIO;

	/// The Standard Class CLASS.
	static StandardClass CLASS;

	/// The Standard Class Infile.
	public static StandardClass Infile;

	/// The Standard Class Printfile.
	public static StandardClass Printfile;

	/// The Standard Class CatchingErrors.
	public static StandardClass CatchingErrors;
	
	private static Type ref_RTObject = Type.Ref("RTObject");

	private static Type ref_File = Type.Ref("File");
	private static Type ref_Infile = Type.Ref("Infile");
	private static Type ref_Printfile = Type.Ref("Printfile"); 
	
	private static Type ref_Link = Type.Ref("Link");  
	private static Type ref_Linkage = Type.Ref("Linkage");  
	private static Type ref_Head = Type.Ref("Head");  
	
	private static Type ref_MAIN_PROGRAM = Type.Ref("MAIN_PROGRAM");
	private static Type ref_EVENT_NOTICE = Type.Ref("EVENT_NOTICE");
	private static Type ref_Process = Type.Ref("Process");
	
	private static Type ref_TextElement = Type.Ref("TextElement");  
	private static Type ref_ShapeElement = Type.Ref("ShapeElement");  


	/// Method to initiate all standard classes.
	public static void INITIATE(final DocumentManager documentManager) {
		initTypeText(documentManager);
		initUNIVERSE(documentManager);
		initRTObject(documentManager);
		initENVIRONMENT(documentManager);
		initBASICIO(documentManager);
		initCLASS(documentManager);
		initFile(documentManager);
			initImagefile(documentManager);
				initInfile(documentManager);
				initOutfile(documentManager);
				initDirectfile(documentManager);
				initPrintfile(documentManager);
			initBytefile(documentManager);
				initInbytefile(documentManager);
				initOutbytefile(documentManager);
				initDirectbytefile(documentManager);
		initSimset(documentManager);
			initLinkage(documentManager);
			initHead(documentManager);
			initLink(documentManager);
		initSimulation(documentManager);
			initEVENT_NOTICE(documentManager);
			initProcess(documentManager);
			initMAIN_PROGRAM(documentManager);
			
		if(DocumentManager.EXTENSIONS) {
			initCatchingErrors(documentManager);
			initDEC_Lib(documentManager);
			initDrawing(documentManager);
				initShapeElement(documentManager);
				initTextElement(documentManager);
		}
		LOG.info("StandardClass: INITIATE DONE");
	}


	// ******************************************************************
	// *** The Type TXT
	// ******************************************************************
	/// Initiate the The Type Text.
	private static void initTypeText(final DocumentManager documentManager) {
		String[] mtd = { "(Lsimula/runtime/RTS_TXT;FI)V", "(Lsimula/runtime/RTS_TXT;DI)V" };
		typeText = new StandardClass(documentManager, "TXT");
		typeText.isContextFree=true;
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"constant");  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"start");  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"length");  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"main");  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"pos");  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setpos",parameter(documentManager, "i",Type.Integer));  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"more");  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Character,"getchar");  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"putchar",parameter(documentManager, "c",Type.Character));  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"sub",parameter(documentManager, "i",Type.Integer),parameter(documentManager, "n",Type.Integer));  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"strip");  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"getint");  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.LongReal,"getreal");  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"getfrac");  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"putint",parameter(documentManager, "i",Type.Integer));  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"putfrac",parameter(documentManager, "i",Type.Integer),parameter(documentManager, "n",Type.Integer));  
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,mtd,null,"putfix", parameter(documentManager, "r",new OverLoad(Type.Real,Type.LongReal)),parameter(documentManager, "n",Type.Integer)); 
		typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,mtd,null,"putreal",parameter(documentManager, "r",new OverLoad(Type.Real,Type.LongReal)),parameter(documentManager, "n",Type.Integer)); 
		// **************************************
		// *** Additional Text Procedures ***
		// **************************************
		if(DocumentManager.EXTENSIONS) {
			typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"trim"); 
			typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Character,"loadChar",parameter(documentManager, "i",Type.Integer)); 
			typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"storeChar",parameter(documentManager, "c",Type.Character),parameter(documentManager, "i",Type.Integer)); 
			typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"startsWith",parameter(documentManager, "t",Type.Text)); 
			typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"endsWith",parameter(documentManager, "t",Type.Text)); 
			typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"indexOf",parameter(documentManager, "c",Type.Character)); 
			typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"lastIndexOf",parameter(documentManager, "c",Type.Character)); 
			typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"replace",parameter(documentManager, "old",Type.Character),parameter(documentManager, "new",Type.Character)); 
			typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"replaceText",parameter(documentManager, "old",Type.Text),parameter(documentManager, "new",Type.Text)); 
			typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"toLowerCase"); 
			typeText.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"toUpperCase"); 
		}
	}

	// ******************************************************************
	// *** The Standard Class UNIVERSE
	// ******************************************************************
	/// The Standard Class UNIVERSE.
	private static StandardClass UNIVERSE;
	
	/// Initiate the Standard Class UNIVERSE
	private static void initUNIVERSE(final DocumentManager documentManager) {
		UNIVERSE = new StandardClass(documentManager, "UNIVERSE");
		UNIVERSE.isContextFree = true;
		UNIVERSE.declaredIn = null;
	}
	  
	// ******************************************************************
	// *** The Standard Class RTObject - Prefix to all classes
	// ******************************************************************
	/// Initiate the Standard Class RTObject.
	private static void initRTObject(final DocumentManager documentManager) {
		StandardClass RTObject = new StandardClass(documentManager, "RTObject");
		ref_RTObject.setQual(RTObject);
		UNIVERSE.addStandardClass(RTObject); // Declared in UNIVERSE
		RTObject.isContextFree = true;
		RTObject.addStandardProcedure(documentManager, ObjectKind.MemberMethod, Type.Text, "objectTraceIdentifier");
		RTObject.addStandardProcedure(documentManager, ObjectKind.MemberMethod, null, "detach"); // Nødvendig for å kompilere Simuletta
	}
	  
	// ******************************************************************
	// *** The Standard Class ENVIRONMENT
	// ******************************************************************
	/// Initiate the Standard Class ENVIRONMENT.
	private static void initENVIRONMENT(final DocumentManager documentManager) {
		ENVIRONMENT = new StandardClass(documentManager, "RTObject","ENVIRONMENT");
		UNIVERSE.addStandardClass(ENVIRONMENT); // Declared in UNIVERSE
		ENVIRONMENT.isContextFree=true; // This class is a Context i.e. all members are static

		//	    Environmental enquiries ................................. 9.6
		//	    Procedure sourceline.
		//	    Constants  maxrank, maxint, minint, maxreal, minreal,
		//	      maxlongreal, minlongreal, simulaid.

		ENVIRONMENT.addStandardAttribute(documentManager, Type.LongReal,"maxlongreal",Double.MAX_VALUE);  
		ENVIRONMENT.addStandardAttribute(documentManager, Type.LongReal,"minlongreal",-Double.MAX_VALUE);  
//		ENVIRONMENT.addStandardAttribute(documentManager, Type.LongReal,"minlongreal",Double.MIN_VALUE);  
		ENVIRONMENT.addStandardAttribute(documentManager, Type.Real,"maxreal",Float.MAX_VALUE);  
		ENVIRONMENT.addStandardAttribute(documentManager, Type.Real,"minreal",-Float.MAX_VALUE);  
//		ENVIRONMENT.addStandardAttribute(documentManager, Type.Real,"minreal",Float.MIN_VALUE);  
		ENVIRONMENT.addStandardAttribute(documentManager, Type.Integer,"maxrank",255);  
		ENVIRONMENT.addStandardAttribute(documentManager, Type.Integer,"maxint",Integer.MAX_VALUE);  
		ENVIRONMENT.addStandardAttribute(documentManager, Type.Integer,"minint",Integer.MIN_VALUE);  
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"simulaid");
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"sourceline");

		//	    Basic operations ........................................ 9.1
		//	    Procedures mod, rem, abs, sign, entier,
		//	      addepsilon, subepsilon.

		String[] mtd = { "(F)F", "(D)D" };

		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"mod",parameter(documentManager, "i",Type.Integer),parameter(documentManager, "j",Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"rem",parameter(documentManager, "i",Type.Integer),parameter(documentManager, "j",Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"abs",parameter(documentManager, "e",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"sign",parameter(documentManager, "e",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"entier",parameter(documentManager, "e",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,mtd,new OverLoad(Type.Real,Type.LongReal),"addepsilon",parameter(documentManager, "e",new OverLoad(Type.Real,Type.LongReal)));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,mtd,new OverLoad(Type.Real,Type.LongReal),"subepsilon",parameter(documentManager, "e",new OverLoad(Type.Real,Type.LongReal)));

		//	    Text utilities .......................................... 9.2
		//	    Procedures copy, blanks, char, isochar, rank, isorank,
		//	      digit, letter, lowten, decimalmark, upcase, lowcase.

		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"copy",parameter(documentManager, "T",Parameter.Mode.value,Type.Text));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"blanks",parameter(documentManager, "n",Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Character,"Char",parameter(documentManager, "n",Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Character,"_char",parameter(documentManager, "n",Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Character,"isochar",parameter(documentManager, "n",Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"rank",parameter(documentManager, "c",Type.Character));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"isorank",parameter(documentManager, "c",Type.Character));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Boolean,"digit",parameter(documentManager, "c",Type.Character));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Boolean,"letter",parameter(documentManager, "c",Type.Character));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Character,"lowten",parameter(documentManager, "c",Type.Character));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Character,"decimalmark",parameter(documentManager, "c",Type.Character));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"upcase",parameter(documentManager, "t",Type.Text));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"lowcase",parameter(documentManager, "t",Type.Text));

		//	    Scheduling .............................................. 9.3
		//	    Procedures call (7.3.2), resume (7.3.3).

		//	    Mathematical functions .................................. 9.4
		//	    Procedures sqrt, sin, cos, tan, cotan, arcsin, arccos,
		//	      arctan, arctan2, sinh, cosh, tanh, ln, log10, exp.

		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"sqrt",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"sin",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"cos",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"tan",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"cotan",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"arcsin",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"arccos",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"arctan",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"arctan2",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"sinh",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"cosh",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"tanh",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"ln",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"log10",parameter(documentManager, "x",Type.LongReal));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"exp",parameter(documentManager, "x",Type.LongReal));

		//	    Extremum functions ...................................... 9.5
		//	    Procedures max, min.

		String[] mtd2 = { "(II)I", "(IF)F", "(ID)D", "(FI)F", "(FF)F", "(FD)D", "(DI)D", "(DF)D", "(DD)D",
				          "(CC)C", "(Lsimula/runtime/RTS_TXT;Lsimula/runtime/RTS_TXT;)Lsimula/runtime/RTS_TXT;" };

		OverLoad types = new OverLoad(Type.Integer,Type.Real,Type.LongReal,Type.Character,Type.Text);
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,mtd2,types,"min",parameter(documentManager, "x",types),parameter(documentManager, "y",types));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,mtd2,types,"max",parameter(documentManager, "x",types),parameter(documentManager, "y",types));

		//	    Error control ........................................... 9.7
		//	    Procedure error.

		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"error",parameter(documentManager, "msg",Type.Text));

		// Array quantities ........................................ 9.8
		//	    Procedures upperbound, lowerbound.

		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"upperbound",parameter(documentManager, "a",null,Parameter.Kind.Array),parameter(documentManager, "i",Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"lowerbound",parameter(documentManager, "a",null,Parameter.Kind.Array),parameter(documentManager, "i",Type.Integer));

		// Random drawing .......................................... 9.9
		//	    Procedures draw, randint, uniform, normal, negexp,
		//	      Poisson, Erlang, discrete, linear, histd.

		String[] mtdx = { "SPECIAL", "SPECIAL" };

		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Boolean,"draw",parameter(documentManager, "a",Type.LongReal),parameter(documentManager, "U",Parameter.Mode.name,Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"randint",parameter(documentManager, "a",Type.Integer),parameter(documentManager, "b",Type.Integer),parameter(documentManager, "U",Parameter.Mode.name,Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"uniform",parameter(documentManager, "a",Type.LongReal),parameter(documentManager, "b",Type.LongReal),parameter(documentManager, "U",Parameter.Mode.name,Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"normal",parameter(documentManager, "a",Type.LongReal),parameter(documentManager, "b",Type.LongReal),parameter(documentManager, "U",Parameter.Mode.name,Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"negexp",parameter(documentManager, "a",Type.LongReal),parameter(documentManager, "U",Parameter.Mode.name,Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"Poisson",parameter(documentManager, "a",Type.LongReal),parameter(documentManager, "U",Parameter.Mode.name,Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"Erlang",parameter(documentManager, "a",Type.LongReal),parameter(documentManager, "b",Type.LongReal),parameter(documentManager, "U",Parameter.Mode.name,Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,mtdx,Type.Integer,"discrete",parameter(documentManager, "A",new OverLoad(Type.Real,Type.LongReal),Parameter.Kind.Array,1),parameter(documentManager, "U",Parameter.Mode.name,Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,mtdx,Type.LongReal,"linear",parameter(documentManager, "A",new OverLoad(Type.Real,Type.LongReal),Parameter.Kind.Array,1),parameter(documentManager, "B",new OverLoad(Type.Real,Type.LongReal),Parameter.Kind.Array,1),parameter(documentManager, "U",Parameter.Mode.name,Type.Integer));
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,mtdx,Type.Integer,"histd",parameter(documentManager, "A",new OverLoad(Type.Real,Type.LongReal),Parameter.Kind.Array,1),parameter(documentManager, "U",Parameter.Mode.name,Type.Integer));

		//	    Calendar and timing utilities ........................... 9.10
		//	    Procedures datetime, cputime, clocktime.

		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"datetime");
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"cputime");
		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"clocktime");

		//	    Miscellaneous utilities ................................. 9.11
		//	    Procedure histo.

		ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"histo",parameter(documentManager, "A",Type.Real,Parameter.Kind.Array,1),parameter(documentManager, "B",Type.Real,Parameter.Kind.Array,1)
				,parameter(documentManager, "c",Type.Real),parameter(documentManager, "d",Type.Real));
		//	    ENVIRONMENT.addStandardProcedure(documentManager, BlockKind.ContextFreeMethod,Type.Text,"objectTraceIdentifier");

		// **************************************
		// *** Additional Standard Procedures ***
		// **************************************
		if(DocumentManager.EXTENSIONS) {
			
			String[] mtd4 = { "(I)Lsimula/runtime/RTS_TXT;", "(F)Lsimula/runtime/RTS_TXT;","(D)Lsimula/runtime/RTS_TXT;","(Z)Lsimula/runtime/RTS_TXT;","(C)Lsimula/runtime/RTS_TXT;" };
			String[] mtd5 = { "(FI)Lsimula/runtime/RTS_TXT;", "(DI)Lsimula/runtime/RTS_TXT;" };
			String[] mtd6 = { "(F)Lsimula/runtime/RTS_TXT;", "(D)Lsimula/runtime/RTS_TXT;" };

			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"waitSomeTime"
				,parameter(documentManager, "millies",Type.Integer)); 
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"printThreadList"
				,parameter(documentManager, "withStackTrace",Type.Boolean));
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"printStaticChain");
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,mtd4,Type.Text,"edit"
				,parameter(documentManager, "x",new OverLoad(Type.Integer,Type.Real,Type.LongReal,Type.Boolean,Type.Character)));
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,mtd5,Type.Text,"edfix"
				,parameter(documentManager, "x",new OverLoad(Type.Real,Type.LongReal)),parameter(documentManager, "n",Type.Integer));
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,mtd6,Type.Text,"edtime"
				,parameter(documentManager, "x",new OverLoad(Type.Real,Type.LongReal)));
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"argv",parameter(documentManager, "index",Type.Integer)); 
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"exit",parameter(documentManager, "status",Type.Integer)); 
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"hash",parameter(documentManager, "t",Type.Text));
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"DEFEXCEPTION",parameter(documentManager, "erh",Parameter.Kind.Procedure,Parameter.Mode.value,null));
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"prompt",parameter(documentManager, "title",Type.Text),parameter(documentManager, "msg",Type.Text));
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Boolean,"confirmDialog",parameter(documentManager, "title",Type.Text),parameter(documentManager, "msg",Type.Text));
			ENVIRONMENT.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"fileChooser",parameter(documentManager, "title",Type.Text),parameter(documentManager, "dir",Type.Text));
		}
	}

	// ******************************************************************
	// *** The Standard Class BASICIO
	// ******************************************************************
	/// Initiate the Standard Class BASICIO.
	/// <pre>
	///  ENVIRONMENT class BASICIO (INPUT_LINELENGTH, OUTPUT_LINELENGTH);
	///  integer INPUT_LINELENGTH, OUTPUT_LINELENGTH;
	///  begin ref (Infile) SYSIN; ref (Printfile) SYSOUT;
	///        ref (Infile)    procedure sysin;   sysin  :- SYSIN;
	///        ref (Printfile) procedure sysout;  sysout :- SYSOUT;
	/// 
	///        procedure terminate_program;
	///        begin ... ;  goto STOP  end terminate_program;
	/// 
	///            class File 
	///       File class Imagefile
	///       File class Bytefile
	///  Imagefile class Infile
	///  Imagefile class Outfile
	///  Imagefile class Directfile
	///    Outfile class Printfile
	///   Bytefile class Inbytefile
	///   Bytefile class Outbytefile 
	///   Bytefile class Directbytefile
	/// 
	///        SYSIN  :- new Infile("...");    ! Implementation-defined
	///        SYSOUT :- new Printfile("..."); ! files names;
	///        SYSIN.open(blanks(INPUT_LINELENGTH));
	///        SYSOUT.open(blanks(OUTPUT_LINELENGTH));
	///        inner;
	///  STOP: SYSIN.close;
	///        SYSOUT.close
	///  end BASICIO;
	/// </pre>
	private static void initBASICIO(final DocumentManager documentManager) {
		BASICIO = new StandardClass(documentManager, "RTObject","BASICIO");
		ENVIRONMENT.addStandardClass(BASICIO); // Declared in ENVIRONMENT
		BASICIO.isContextFree=true;
		BASICIO.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,ref_Infile,"sysin");  
		BASICIO.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,ref_Printfile,"sysout");  
		BASICIO.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"terminate_program");  
		BASICIO.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"call",parameter(documentManager, "obj",ref_RTObject));
		BASICIO.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"resume",parameter(documentManager, "obj",ref_RTObject));
	}


	// ******************************************************************
	// *** The Standard Class CLASS
	// ******************************************************************
	/// Initiate the Standard Class CLASS.
	/// 
	/// Simula Stadard States: Fictituous outermost prefix
	/// Any class that has no (textually given) prefix is by definition
	/// prefixed by a fictitious class whose only attribute is:
	/// <pre>
	/// 	          procedure detach; ... ;  (see 7.3.1)
	/// </pre>
	/// Thus every class object or instance of a prefixed block has this attribute.

	private static void initCLASS(final DocumentManager documentManager) {
		CLASS = new StandardClass(documentManager, "RTObject","CLASS");
		ENVIRONMENT.addStandardClass(CLASS);  // Declared in ENVIRONMENT
		CLASS.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"detach");
	}

	// ******************************************************************
	// *** The Standard Class File
	// ******************************************************************
	/// The Standard Class File.
	private static StandardClass File;
	
	/// Initiate the Standard Class File.
	/// <pre>
	///  class File(FILENAME); value FILENAME; text FILENAME;
	///  begin
	///     Boolean OPEN_;
	///     text procedure filename; filename:=copy(FILENAME);
	///     Boolean procedure isopen; isopen:=OPEN_;
	///     Boolean procedure setaccess(mode);  text mode; ... ;
	///  
	///     if FILENAME = notext then error("Illegal File Name");
	///  end File;      
	/// </pre>
	private static void initFile(final DocumentManager documentManager) {
		File = new StandardClass(documentManager, "CLASS","File",parameter(documentManager, "FILENAME_",Type.Text));
		ref_File.setQual(File);
		BASICIO.addStandardClass(File);  // Declared in BASICIO
		File.addStandardAttribute(documentManager, Type.Boolean,"OPEN_");  
		File.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"filename");
		File.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"isopen");
		File.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"setaccess",parameter(documentManager, "mode",Type.Text));  
	}  

	// ******************************************************************
	// *** The Standard File Class Imagefile
	// ******************************************************************
	/// The Standard Class Imagefile.
	private static StandardClass Imagefile;
	
	/// Initiate the Standard Class Imagefile.
	/// <pre>
	///  File class Imagefile;
	///  begin text image;
	///     procedure setpos(i);  integer i;  image.setpos(i);
	///     integer procedure pos;     pos    := image.pos;
	///     Boolean procedure more;    more   := image.more;
	///     integer procedure length;  length := image.length;
	///  end Imagefile;
	/// </pre>
	private static void initImagefile(final DocumentManager documentManager) {
		Imagefile = new StandardClass(documentManager, "File","Imagefile");
		BASICIO.addStandardClass(Imagefile);  // Declared in BASICIO
		Imagefile.addStandardAttribute(documentManager, Type.Text,"image");  
		Imagefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setpos",parameter(documentManager, "i",Type.Integer));  
		Imagefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"pos");  
		Imagefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"more");  
		Imagefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"length");   
	}

	// ******************************************************************
	// *** The Standard Imagefile Class Infile
	// ******************************************************************
	/// Initiate the Standard Class Infile.
	/// <pre>
	///  Imagefile class Infile;
	///  begin Boolean ENDFILE;
	///     Boolean procedure endfile;  endfile:= ENDFILE;
	///     Boolean procedure open(fileimage); text fileimage;
	///     Boolean procedure close;
	///     procedure inimage;
	///     Boolean procedure inrecord;
	///     character procedure inchar;
	///     Boolean procedure lastitem;
	///     text procedure intext(w); integer w;
	///     integer procedure inint;
	///     long real procedure inreal;
	///     integer procedure infrac;
	/// 
	///     ENDFILE:= true
	///     ...
	///  end Infile;
	/// </pre>
	private static void initInfile(final DocumentManager documentManager) {
		Infile = new StandardClass(documentManager, "Imagefile","Infile");
		ref_Infile.setQual(Infile);
		BASICIO.addStandardClass(Infile);  // Declared in BASICIO
		Infile.addStandardAttribute(documentManager, Type.Boolean,"ENDFILE_");  
		Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"endfile");  
		Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"open",parameter(documentManager, "fileimage",Type.Text));  
		Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"close");  
		Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"inimage");  
		Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"inrecord");  
		Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Character,"inchar");  
		Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"lastitem");  
		Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"intext",parameter(documentManager, "w",Type.Integer));  
		Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"inint");  
		Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.LongReal,"inreal");  
		Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"infrac");  
//		if(SimulaCompiler.EXTENSIONS) {
//			Infile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"prompt",parameter(documentManager, "msg",Type.Text));
//		}
	}  

	// ******************************************************************
	// *** The Standard Imagefile Class Outfile
	// ******************************************************************
	/// Initiate the Standard Class Outfile.
	/// <pre>
	///  Imagefile class Outfile;
	///  begin
	///     Boolean procedure open(fileimage);  text fileimage;
	///     Boolean procedure close;
	///     procedure outimage;
	///     procedure outrecord;
	///     procedure breakoutimage;
	///     Boolean procedure checkpoint;
	///     procedure outchar(c); character c;
	///     procedure outtext(t); text t;
	///     text procedure FIELD(w); integer w;
	///     procedure outint(i,w); integer i,w;
	///     procedure outfix(r,n,w); long real r; integer n,w;
	///     procedure outreal(r,n,w); long real r; integer n,w;
	///     procedure outfrac(i,n,w); integer i,n,w;
	/// 
	///    ... ;
	/// end Outfile;
	/// </pre>
	private static void initOutfile(final DocumentManager documentManager) { 
		String[] mtd = { "(FII)V", "(DII)V" };

		StandardClass Outfile = new StandardClass(documentManager, "Imagefile","Outfile");
		BASICIO.addStandardClass(Outfile);  // Declared in BASICIO
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"open",parameter(documentManager, "fileimage",Type.Text));  
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"close");  
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outimage");  
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outrecord");  
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"breakoutimage");  
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"checkpoint");  
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outchar",parameter(documentManager, "c",Type.Character));  
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outtext",parameter(documentManager, "t",Type.Text));  
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"FIELD_",parameter(documentManager, "w",Type.Integer));  
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outint",parameter(documentManager, "i",Type.Integer),parameter(documentManager, "w",Type.Integer));  
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outfrac",parameter(documentManager, "i",Type.Integer),parameter(documentManager, "n",Type.Integer),parameter(documentManager, "w",Type.Integer)); 
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,mtd,null,"outfix", parameter(documentManager, "r",new OverLoad(Type.Real,Type.LongReal)),parameter(documentManager, "n",Type.Integer),parameter(documentManager, "w",Type.Integer)); 
		Outfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,mtd,null,"outreal",parameter(documentManager, "r",new OverLoad(Type.Real,Type.LongReal)),parameter(documentManager, "n",Type.Integer),parameter(documentManager, "w",Type.Integer)); 
	}  

	// ******************************************************************
	// *** The Standard Imagefile Class Directfile
	// ******************************************************************
	/// Initiate the Standard Class Directfile.
	/// <pre>
	///  Imagefile class Directfile;
	///  begin   integer LOC, MAXLOC;  Boolean ENDFILE, LOCKED;
	///     integer procedure location;  location:= LOC;
	///     Boolean procedure endfile;   endfile := ENDFILE;
	///     Boolean procedure locked;    locked  := LOCKED;
	///     Boolean procedure open(fileimage); text fileimage; 
	///     Boolean procedure close;
	///     integer procedure lastloc;
	///     integer procedure maxloc;
	///     procedure locate(i); integer i;
	///     procedure inimage;
	///     procedure outimage;
	///     Boolean procedure deleteimage;
	///     character procedure inchar;
	///     integer procedure lock(t,i,j); real t; integer i,j;
	///     Boolean procedure unlock; 
	///     Boolean procedure checkpoint;
	///     Boolean procedure lastitem;
	///     text procedure intext;
	///     integer procedure inint;
	///     long real procedure inreal;
	///     integer procedure infrac;
	///     procedure outchar(c); character c;
	///     procedure outtext(t); text t;
	///     text procedure FIELD(w); integer w;
	///     procedure outint(i,w); integer i,w;
	///     procedure outfix(r,n,w);  long real r; integer n,w;
	///     procedure outreal(r,n,w); long real r; integer n,w;
	///     procedure outfrac(i,n,w); integer i,n,w;
	/// 
	///     ENDFILE:= true
	///     ...
	///  end Directfile;
	/// </pre>
	private static void initDirectfile(final DocumentManager documentManager) {
		String[] mtd = { "(FII)V", "(DII)V" };

		StandardClass Directfile = new StandardClass(documentManager, "Imagefile","Directfile");
		BASICIO.addStandardClass(Directfile);  // Declared in BASICIO
		Directfile.addStandardAttribute(documentManager, Type.Integer,"LOC_");  
		Directfile.addStandardAttribute(documentManager, Type.Integer,"MAXLOC_");  
		Directfile.addStandardAttribute(documentManager, Type.Boolean,"ENDFILE_");  
		Directfile.addStandardAttribute(documentManager, Type.Boolean,"LOCKED_");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"location");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"endfile");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"locked");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"open",parameter(documentManager, "fileimage",Type.Text));  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"close");      
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"lastloc");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"maxloc");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"locate",parameter(documentManager, "i",Type.Integer));  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"inimage");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outimage");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"deleteimage");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Character,"inchar");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"lock",parameter(documentManager, "t",Type.Real),parameter(documentManager, "i",Type.Integer),parameter(documentManager, "j",Type.Integer));  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"unlock");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"checkpoint");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"lastitem");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"intext",parameter(documentManager, "w",Type.Integer));  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"inint");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.LongReal,"inreal");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"infrac");  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outchar",parameter(documentManager, "c",Type.Character));  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outtext",parameter(documentManager, "t",Type.Text));   
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"FIELD_",parameter(documentManager, "w",Type.Integer));  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outint",parameter(documentManager, "i",Type.Integer),parameter(documentManager, "w",Type.Integer));  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outfrac",parameter(documentManager, "i",Type.Integer),parameter(documentManager, "n",Type.Integer),parameter(documentManager, "w",Type.Integer));  
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,mtd,null,"outfix", parameter(documentManager, "r",new OverLoad(Type.Real,Type.LongReal)),parameter(documentManager, "n",Type.Integer),parameter(documentManager, "w",Type.Integer)); 
		Directfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,mtd,null,"outreal",parameter(documentManager, "r",new OverLoad(Type.Real,Type.LongReal)),parameter(documentManager, "n",Type.Integer),parameter(documentManager, "w",Type.Integer)); 
	}  

	// ******************************************************************
	// *** The Standard Outfile Class Printfile
	// ******************************************************************
	/// Initiate the Standard Class Printfile.
	/// <pre>
	///  Outfile class Printfile;
	///  begin integer LINE, LINES_PER_PAGE, SPACING, PAGE;
	///    integer procedure line; line := LINE;
	///    integer procedure page; page := PAGE;
	///    Boolean procedure open(fileimage); text fileimage; 
	///    Boolean procedure close; 
	///    integer procedure linesperpage(n); integer n; 
	///    procedure spacing(n); integer n; 
	///    procedure eject(n);  integer n; 
	///    procedure outimage;
	///    procedure outrecord;
	/// 
	///    SPACING := 1;
	///    LINES_PER_PAGE := ... ;
	///    ...
	///  end Printfile;
	/// </pre>
	private static void initPrintfile(final DocumentManager documentManager) {
		Printfile = new StandardClass(documentManager, "Outfile","Printfile");
		ref_Printfile.setQual(Printfile);
		BASICIO.addStandardClass(Printfile);  // Declared in BASICIO
		Printfile.addStandardAttribute(documentManager, Type.Integer,"LINE_");  
		Printfile.addStandardAttribute(documentManager, Type.Integer,"LINES_PER_PAGE_");  
		Printfile.addStandardAttribute(documentManager, Type.Integer,"SPACING_");  
		Printfile.addStandardAttribute(documentManager, Type.Integer,"PAGE_");  
		Printfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"line"); 
		Printfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"page");  
		Printfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"open",parameter(documentManager, "fileimage",Type.Text));  
		Printfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"close");  
		Printfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"linesperpage",parameter(documentManager, "n",Type.Integer));  
		Printfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"spacing",parameter(documentManager, "n",Type.Integer));  
		Printfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"eject",parameter(documentManager, "n",Type.Integer));  
		Printfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outimage");  
		Printfile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outrecord");  
	}  

	// ******************************************************************
	// *** The Standard file Class Bytefile
	// ******************************************************************
	/// The Standard Class Bytefile.
	private static StandardClass Bytefile;

	/// Initiate the Standard Class Bytefile.
	/// <pre>
	///  file class Bytefile;
	///  begin short integer BYTESIZE;
	///     short integer procedure bytesize; bytesize := BYTESIZE;
	/// 
	///  end Bytefile;
	/// </pre>
	private static void initBytefile(final DocumentManager documentManager) { 
		Bytefile = new StandardClass(documentManager, "File","Bytefile");
		BASICIO.addStandardClass(Bytefile);  // Declared in BASICIO
		Bytefile.addStandardAttribute(documentManager, Type.Integer,"BYTESIZE_");  
		Bytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"bytesize");  
	}  

	// ******************************************************************
	// *** The Standard Bytefile Class Inbytefile
	// ******************************************************************
	/// Initiate the Standard Class Inbytefile.
	/// <pre>
	///  Bytefile class Inbytefile;
	///  begin Boolean ENDFILE;
	///    Boolean procedure endfile; endfile:= ENDFILE;
	///    Boolean procedure open; 
	///    Boolean procedure close;
	///    short integer procedure inbyte;
	///    text procedure intext(t); text t; 
	/// 
	///    ENDFILE:= true;
	///    ...
	///  end Inbytefile;
	/// </pre>
	private static void initInbytefile(final DocumentManager documentManager) { 
		StandardClass Inbytefile = new StandardClass(documentManager, "Bytefile","Inbytefile");
		BASICIO.addStandardClass(Inbytefile);  // Declared in BASICIO
		Inbytefile.addStandardAttribute(documentManager, Type.Boolean,"ENDFILE_");  
		Inbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"endfile");  
		Inbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"open");
		Inbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"close");  
		Inbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"inbyte");  
		Inbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"intext",parameter(documentManager, "t",Type.Text));
		if(DocumentManager.EXTENSIONS) {
			Inbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"in2byte");  // Extension to Simula Standard
		}
	}  

	// ******************************************************************
	// *** The Standard Bytefile Class Outbytefile
	// ******************************************************************
	/// Initiate the Standard Class Outbytefile.
	/// <pre>
	///  Bytefile class Outbytefile;
	///  begin
	///    Boolean procedure open; 
	///    Boolean procedure close; 
	///    procedure outbyte(x); short integer x; 
	///    procedure outtext(t); text t; 
	///    Boolean procedure checkpoint; 
	/// 
	///  end Outbytefile;
	/// </pre>
	private static void initOutbytefile(final DocumentManager documentManager) { 
		StandardClass Outbytefile = new StandardClass(documentManager, "Bytefile","Outbytefile");
		BASICIO.addStandardClass(Outbytefile);
		Outbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"open");
		Outbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"close");  
		Outbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outbyte",parameter(documentManager, "x",Type.Integer));   
		Outbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outtext",parameter(documentManager, "t",Type.Text));  
		Outbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"checkpoint");  
		if(DocumentManager.EXTENSIONS) {
			Outbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"out2byte",parameter(documentManager, "x",Type.Integer));   			
		}
	}  

	// ******************************************************************
	// *** The Standard Bytefile Class Directbytefile
	// ******************************************************************
	/// Initiate the Standard Class Directbytefile.
	/// <pre>
	///  Bytefile class Directbytefile;
	///  begin integer LOC, MAXLOC;  Boolean LOCKED;
	///    Boolean procedure endfile; endfile:=OPEN and then LOC>lastloc;
	///    integer procedure location; location := LOC;
	///    integer procedure maxloc; maxloc := MAXLOC;
	///    Boolean procedure locked; locked := LOCKED;
	///    Boolean procedure open; 
	///    Boolean procedure close; 
	///    integer procedure lastloc; 
	///    procedure locate(i); integer i; 
	///    short integer procedure inbyte; 
	///    procedure outbyte(x); short integer x; 
	///    Boolean procedure checkpoint; 
	///    integer procedure lock(t,i,j); real t; integer i,j; 
	///    Boolean procedure unlock; 
	///    procedure intext(t); text t; 
	///    procedure outtext(t); text t;
	///     ...
	///  end Directbytefile;
	/// </pre>
	private static void initDirectbytefile(final DocumentManager documentManager) { 
		StandardClass Directbytefile = new StandardClass(documentManager, "Bytefile","Directbytefile");
		BASICIO.addStandardClass(Directbytefile);  // Declared in BASICIO
		Directbytefile.addStandardAttribute(documentManager, Type.Integer,"LOC_");  
		Directbytefile.addStandardAttribute(documentManager, Type.Integer,"MAXLOC_");  
		Directbytefile.addStandardAttribute(documentManager, Type.Boolean,"LOCKED_");  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"endfile");  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"location");  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"maxloc");  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"locked");  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"open");  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"close");      
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"lastloc");  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"locate",parameter(documentManager, "i",Type.Integer));  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"inbyte");  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outbyte",parameter(documentManager, "x",Type.Integer));   
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"out2byte",parameter(documentManager, "x",Type.Integer));   
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"checkpoint");  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"lock",parameter(documentManager, "t",Type.Real),parameter(documentManager, "i",Type.Integer),parameter(documentManager, "j",Type.Integer));  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"unlock");  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Text,"intext",parameter(documentManager, "t",Type.Text));  
		Directbytefile.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"outtext",parameter(documentManager, "t",Type.Text));  
	}  

	// ******************************************************************
	// *** The Standard Class Simset
	// ******************************************************************
	/// The Standard Class Simset.
	private static StandardClass Simset;

	/// Initiate the Standard Class Simset.
	private static void initSimset(final DocumentManager documentManager) { 
		Simset = new StandardClass(documentManager, "CLASS","Simset");
		ENVIRONMENT.addStandardClass(Simset);  // Declared in ENVIRONMENT
	}  

	// ******************************************************************
	// *** The Standard Class Linkage
	// ******************************************************************
	/// The Standard Class Linkage.
	private static StandardClass Linkage;

	/// Initiate the Standard Class Linkage.
	private static void initLinkage(final DocumentManager documentManager) { 
		Linkage = new StandardClass(documentManager, "CLASS","Linkage");
		ref_Linkage.setQual(Linkage);
		Simset.addStandardClass(Linkage);  // Declared in Simset
		Linkage.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_Link,"suc");  
		Linkage.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_Link,"pred");  
		Linkage.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_Linkage,"prev");  
	}  

	// ******************************************************************
	// *** The Standard Linkage Class Head
	// ******************************************************************
	/// Initiate the Standard Class Head.
	private static void initHead(final DocumentManager documentManager) {
		StandardClass Head = new StandardClass(documentManager, "Linkage","Head");
		ref_Head.setQual(Head);
		Simset.addStandardClass(Head);  // Declared in Simset
		Head.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_Link,"first");  
		Head.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_Link,"last");  
		Head.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"empty");  
		Head.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"cardinal");  
		Head.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"clear");  
	}  

	// ******************************************************************
	// *** The Standard Linkage Class Link
	// ******************************************************************
	/// Initiate the Standard Class Link.
	private static void initLink(final DocumentManager documentManager) { 
		StandardClass Link = new StandardClass(documentManager, "Linkage","Link");
		ref_Link.setQual(Link);
		Simset.addStandardClass(Link);  // Declared in Simset
		Link.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"out");  
		Link.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"follow",parameter(documentManager, "X",ref_Linkage));  
		Link.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"precede",parameter(documentManager, "X",ref_Linkage));  
		Link.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"into",parameter(documentManager, "S",ref_Head));  
	}  

	// ******************************************************************
	// *** The Standard Class Simulation
	// ******************************************************************
	/// The Standard Class Simulation.
	private static StandardClass Simulation;

	/// Initiate the Standard Class Simulation.
	private static void initSimulation(final DocumentManager documentManager) { 
		Simulation = new StandardClass(documentManager, "Simset","Simulation");
		ENVIRONMENT.addStandardClass(Simulation);  // Declared in ENVIRONMENT
		Simulation.detachUsed=true;
		Simulation.addStandardAttribute(documentManager, ref_Head,"SQS");  
		Simulation.addStandardAttribute(documentManager, ref_MAIN_PROGRAM, "main");  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.LongReal,"time");  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_EVENT_NOTICE,"FIRSTEV");  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_Process,"current");  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"hold",parameter(documentManager, "T",Type.LongReal));  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"passivate");  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"wait",parameter(documentManager, "S",ref_Head));  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"cancel",parameter(documentManager, "x",ref_Process));  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"accum",parameter(documentManager, "a",Parameter.Mode.name,Type.LongReal),parameter(documentManager, "b",Parameter.Mode.name,Type.LongReal)
				,parameter(documentManager, "c",Parameter.Mode.name,Type.LongReal),parameter(documentManager, "d",Type.LongReal));    
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"ActivateDirect"
				,parameter(documentManager, "REAC",Type.Boolean)
				,parameter(documentManager, "X",ref_Process)
				);  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"ActivateAt"
				,parameter(documentManager, "REAC",Type.Boolean)
				,parameter(documentManager, "X",ref_Process)
				,parameter(documentManager, "T",Type.LongReal)
				,parameter(documentManager, "PRIO",Type.Boolean)
				);  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"ActivateDelay"
				,parameter(documentManager, "REAC",Type.Boolean)
				,parameter(documentManager, "X",ref_Process)
				,parameter(documentManager, "T",Type.LongReal)
				,parameter(documentManager, "PRIO",Type.Boolean)
				);  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"ActivateBefore"
				,parameter(documentManager, "REAC",Type.Boolean)
				,parameter(documentManager, "X",ref_Process)
				,parameter(documentManager, "Y",ref_Process)
				);  
		Simulation.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"ActivateAfter"
				,parameter(documentManager, "REAC",Type.Boolean)
				,parameter(documentManager, "X",ref_Process)
				,parameter(documentManager, "Y",ref_Process)
				);  
	}  

	// ******************************************************************
	// *** The Standard Link Class EVENT_NOTICE
	// ******************************************************************
	/// Initiate the Standard Class EVENT_NOTICE.
	private static void initEVENT_NOTICE(final DocumentManager documentManager) { 
		StandardClass EVENT_NOTICE = new StandardClass(documentManager, "Link","EVENT_NOTICE");
		ref_EVENT_NOTICE.setQual(EVENT_NOTICE);
		Simulation.addStandardClass(EVENT_NOTICE);  // Declared in Simulation
		//	    ref(EVENT_NOTICE) procedure suc;
		//	    ref(EVENT_NOTICE) procedure pred;
		//	    procedure RANK(BEFORE_); Boolean BEFORE_;
		EVENT_NOTICE.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_EVENT_NOTICE,"suc");  
		EVENT_NOTICE.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_EVENT_NOTICE,"pred");  
		EVENT_NOTICE.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"RANK",parameter(documentManager, "BEFORET",Type.Boolean));  
	}  

	// ******************************************************************
	// *** The Standard Link Class Process
	// ******************************************************************
	/// The Standard Class Process.
	private static StandardClass Process;

	/// Initiate the Standard Class Process.
	private static void initProcess(final DocumentManager documentManager) { 
		Process = new StandardClass(documentManager, "Link","Process");
		ref_Process.setQual(Process);
		Simulation.addStandardClass(Process);  // Declared in Simulation
		Process.detachUsed=true;
		Process.statements1=new ObjectList<Statement>();
		Process.statements1.add(new InlineStatement(documentManager, "detach")); // Statements before inner 
		Process.statements.add(new InlineStatement(documentManager, "terminate")); // Statements after inner 				
		//	    ref(EVENT_NOTICE) EVENT;
		//	    Boolean TERMINATED_;
		//	    Boolean procedure idle;
		//	    Boolean procedure terminated;
		//	    real procedure evtime;
		//	    ref(Process) procedure nextev;
		Process.addStandardAttribute(documentManager, ref_EVENT_NOTICE,"EVENT");  
		Process.addStandardAttribute(documentManager, Type.Boolean,"TERMINATED_");  
		Process.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"idle");  
		Process.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Boolean,"terminated");  
		Process.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.LongReal,"evtime");  
		Process.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_Process,"nextev");  
	}  


	// ******************************************************************
	// *** The Standard Process Class MAIN_PROGRAM
	// ******************************************************************
	/// Initiate the Standard Class MAIN_PROGRAM.
	private static void initMAIN_PROGRAM(final DocumentManager documentManager) { 
		StandardClass MAIN_PROGRAM = new StandardClass(documentManager, "Process","MAIN_PROGRAM");
		ref_MAIN_PROGRAM.setQual(MAIN_PROGRAM);
		Simulation.addStandardClass(MAIN_PROGRAM);   // Declared in Simulation
		//	    Process class MAIN_PROGRAM;
		//	    begin
		//	       L: detach; goto L
		//	    end MAIN_PROGRAM;
	}  


	// ******************************************************************
	// *** The Standard Class CatchingErrors  NOTE: if(SimulaCompiler.EXTENSIONS)
	// ******************************************************************
	/// Initiate the Standard Class CatchingErrors.
	private static void initCatchingErrors(final DocumentManager documentManager) { 
		CatchingErrors = new StandardClass(documentManager, "CLASS","CatchingErrors");
		ENVIRONMENT.addStandardClass(CatchingErrors);  // Declared in ENVIRONMENT

//		IO.println("StandardClass.initCatchingErrors: ENVIRONMENT.declarationList: " + ENVIRONMENT.declarationList);
//		ENVIRONMENT.declarationList.print("StandardClass.initCatchingErrors: ");
		
		CatchingErrors.virtualSpecList.add(new VirtualSpecification(documentManager, new Identifier("onError"), null, VirtualSpecification.Kind.Procedure,CatchingErrors.prefixLevel(),null));
		CatchingErrors.statements1=new ObjectList<Statement>();
		CatchingErrors.statements1.add(new InlineStatement(documentManager, "try")); // Statements before inner 
		CatchingErrors.statements.add(new InlineStatement(documentManager, "catch")); // Statements after inner 				
	}  

	
	// ******************************************************************
	// *** The Standard Class DEC_Lib   - as defined in DEC handbook III    NOTE: if(SimulaCompiler.EXTENSIONS)
	// ******************************************************************
	/// Initiate the Standard Class DEC_Lib.
	private static void initDEC_Lib(final DocumentManager documentManager) { 
		StandardClass DEC_Lib = new StandardClass(documentManager, "CLASS","DEC_Lib");
		ENVIRONMENT.addStandardClass(DEC_Lib);  // Declared in ENVIRONMENT.
		DEC_Lib.isContextFree=true; // This class is a Context i.e. all members are static
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"abort",parameter(documentManager, "mess",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Boolean,"change",parameter(documentManager, "m",Parameter.Mode.name,Type.Text),parameter(documentManager, "o",Type.Text),parameter(documentManager, "n",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"checkextension",parameter(documentManager, "fileName",Type.Text),parameter(documentManager, "defaultextension",Parameter.Mode.value,Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"checkfrac",parameter(documentManager, "t",Parameter.Mode.name,Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"checkint",parameter(documentManager, "t",Parameter.Mode.name,Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"checkreal",parameter(documentManager, "t",Parameter.Mode.name,Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"compress",parameter(documentManager, "t",Type.Text),parameter(documentManager, "c",Type.Character));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"conc",parameter(documentManager, "t1",Type.Text),parameter(documentManager, "t2",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"conc2",parameter(documentManager, "t1",Type.Text),parameter(documentManager, "t2",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"conc3",parameter(documentManager, "t1",Type.Text),parameter(documentManager, "t2",Type.Text),parameter(documentManager, "t3",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"conc4",parameter(documentManager, "t1",Type.Text),parameter(documentManager, "t2",Type.Text),parameter(documentManager, "t3",Type.Text),parameter(documentManager, "t4",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"conc5",parameter(documentManager, "t1",Type.Text),parameter(documentManager, "t2",Type.Text),parameter(documentManager, "t3",Type.Text),parameter(documentManager, "t4",Type.Text),parameter(documentManager, "t5",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"cptime");  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"dayno");  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"daytime");  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"depchar",parameter(documentManager, "t",Type.Text),parameter(documentManager, "p",Type.Integer),parameter(documentManager, "c",Type.Character));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"enterdebug",parameter(documentManager, "maycontinue",Type.Boolean));
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,null,"exit",parameter(documentManager, "code",Type.Integer));
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Character,"fetchar",parameter(documentManager, "t",Type.Text),parameter(documentManager, "p",Type.Integer));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Character,"findtrigger",parameter(documentManager, "master",Parameter.Mode.name,Type.Text),parameter(documentManager, "triggers",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"from",parameter(documentManager, "t",Type.Text),parameter(documentManager, "p",Type.Integer));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"front",parameter(documentManager, "t",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Boolean,"frontcompare",parameter(documentManager, "string",Type.Text),parameter(documentManager, "config",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"frontstrip",parameter(documentManager, "t",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"getitem",parameter(documentManager, "tt",Parameter.Mode.name,Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"hash",parameter(documentManager, "t",Type.Text),parameter(documentManager, "n",Type.Integer));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"initem",parameter(documentManager, "f",ref_File));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"linecount",parameter(documentManager, "pf",ref_Printfile));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Character,"insinglechar");  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Character,"lowc",parameter(documentManager, "c",Type.Character));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"maketext",parameter(documentManager, "c",Type.Character),parameter(documentManager, "n",Type.Integer));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Boolean,"puttext",parameter(documentManager, "ot",Parameter.Mode.name,Type.Text),parameter(documentManager, "nt",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"rest",parameter(documentManager, "t",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Character,"scanchar",parameter(documentManager, "t",Parameter.Mode.name,Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"scanfrac",parameter(documentManager, "tt",Parameter.Mode.name,Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"scanint",parameter(documentManager, "tt",Parameter.Mode.name,Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.LongReal,"scanreal",parameter(documentManager, "tt",Parameter.Mode.name,Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"scanto",parameter(documentManager, "t",Parameter.Mode.name,Type.Text),parameter(documentManager, "c",Type.Character));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"search",parameter(documentManager, "t1",Type.Text),parameter(documentManager, "t2",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"skip",parameter(documentManager, "t",Parameter.Mode.name,Type.Text),parameter(documentManager, "c",Type.Character));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Integer,"startpos",parameter(documentManager, "t",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"today");  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"tsub",parameter(documentManager, "t",Type.Text),parameter(documentManager, "p",Type.Integer),parameter(documentManager, "l",Type.Integer));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Character,"upc",parameter(documentManager, "c",Type.Character));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Boolean,"upcompare",parameter(documentManager, "master",Type.Text),parameter(documentManager, "test",Type.Text));  
		DEC_Lib.addStandardProcedure(documentManager, ObjectKind.ContextFreeMethod,Type.Text,"upto",parameter(documentManager, "t",Type.Text),parameter(documentManager, "p",Type.Integer));  
	}  

	// ******************************************************************
	// *** The Standard Class Drawing    NOTE: if(SimulaCompiler.EXTENSIONS)
	// ******************************************************************
	/// The Standard Class Drawing.
	private static StandardClass Drawing;

	/// Initiate the Standard Class Drawing.
	private static void initDrawing(final DocumentManager documentManager) {
		Drawing = new StandardClass(documentManager, "Simset","Drawing",parameter(documentManager, "Title",Type.Text),parameter(documentManager, "width",Type.Integer),parameter(documentManager, "height",Type.Integer)); 
		ENVIRONMENT.addStandardClass(Drawing);  // Declared in ENVIRONMENT
		Drawing.addStandardAttribute(documentManager, Type.Integer,"white",    0xffffff); // Color white:      R=255, G=255, B=255.
		Drawing.addStandardAttribute(documentManager, Type.Integer,"lightGray",0xc0c0c0); // Color light gray: R=192, G=192, B=192.  
		Drawing.addStandardAttribute(documentManager, Type.Integer,"gray",     0x808080); // Color gray:       R=128, G=128, B=128. 
		Drawing.addStandardAttribute(documentManager, Type.Integer,"darkGray", 0x404040); // Color dark gray:  R=64,  G=64,  B=64.
		Drawing.addStandardAttribute(documentManager, Type.Integer,"black",    0x000000); // Color black:      R=0,   G=0,   B=0.  
		Drawing.addStandardAttribute(documentManager, Type.Integer,"red",      0xff0000); // Color red:        R=255, G=0,   B=0. 
		Drawing.addStandardAttribute(documentManager, Type.Integer,"pink",     0xffafaf); // Color pink:       R=255, G=175, B=175.
		Drawing.addStandardAttribute(documentManager, Type.Integer,"orange",   0xffc800); // Color orange:     R=255, G=200, B=0. 
		Drawing.addStandardAttribute(documentManager, Type.Integer,"yellow",   0xffff00); // Color yellow:     R=255, G=255, B=0. 
		Drawing.addStandardAttribute(documentManager, Type.Integer,"green",    0x00ff00); // Color green:      R=0,   G=255, B=0.
		Drawing.addStandardAttribute(documentManager, Type.Integer,"magenta",  0xff00ff); // Color magenta:    R=255, G=0,   B=255.
		Drawing.addStandardAttribute(documentManager, Type.Integer,"cyan",     0x00ffff); // Color cyan:       R=0,   G=255, B=255.
		Drawing.addStandardAttribute(documentManager, Type.Integer,"blue",     0x0000ff); // Color blue:       R=0,   G=0,   B=255.
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Integer,"color",parameter(documentManager, "r",Type.Integer),parameter(documentManager, "g",Type.Integer),parameter(documentManager, "b",Type.Integer));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setBackgroundColor",parameter(documentManager, "color",Type.Integer));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setDrawColor",parameter(documentManager, "color",Type.Integer));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setFillColor",parameter(documentManager, "color",Type.Integer));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setStroke",parameter(documentManager, "width",Type.Real));  

		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_Head,"renderingSet");  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setFontStylePlain");  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setFontStyleBold");  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setFontStyleItalic");  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setFontStyleBoldItalic");  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setFontSize",parameter(documentManager, "size",Type.Real));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Real,"getFontSize");  

		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_TextElement,"drawText",parameter(documentManager, "t",Type.Text),parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_ShapeElement,"drawLine",parameter(documentManager, "x1",Type.LongReal),parameter(documentManager, "y1",Type.LongReal),parameter(documentManager, "x2",Type.LongReal),parameter(documentManager, "y2",Type.LongReal));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_ShapeElement,"drawEllipse",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal),parameter(documentManager, "height",Type.LongReal));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_ShapeElement,"drawRectangle",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal),parameter(documentManager, "height",Type.LongReal));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_ShapeElement,"drawRoundRectangle",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal)
				,parameter(documentManager, "height",Type.LongReal),parameter(documentManager, "arcw",Type.LongReal),parameter(documentManager, "arch",Type.LongReal));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_ShapeElement,"fillEllipse",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal),parameter(documentManager, "height",Type.LongReal));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_ShapeElement,"fillRectangle",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal),parameter(documentManager, "height",Type.LongReal));  
		Drawing.addStandardProcedure(documentManager, ObjectKind.MemberMethod,ref_ShapeElement,"fillRoundRectangle",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal)
				,parameter(documentManager, "height",Type.LongReal),parameter(documentManager, "arcw",Type.LongReal),parameter(documentManager, "arch",Type.LongReal));  
	}

	// ******************************************************************
	// *** The Standard Link Class ShapeElement    NOTE: if(SimulaCompiler.EXTENSIONS)
	// ******************************************************************
	/// Initiate the Standard Class ShapeElement.
	private static void initShapeElement(final DocumentManager documentManager) {
		StandardClass ShapeElement = new StandardClass(documentManager, "Link","ShapeElement");
		ref_ShapeElement.setQual(ShapeElement);
		Drawing.addStandardClass(ShapeElement);  // Declared in Drawing
		ShapeElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setColor",parameter(documentManager, "color",Type.Integer));  
		ShapeElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"drawLine",parameter(documentManager, "x1",Type.LongReal),parameter(documentManager, "y1",Type.LongReal),parameter(documentManager, "x2",Type.LongReal),parameter(documentManager, "y2",Type.LongReal));  
		ShapeElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"drawEllipse",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal),parameter(documentManager, "height",Type.LongReal));  
		ShapeElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"drawRectangle",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal),parameter(documentManager, "height",Type.LongReal));  
		ShapeElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"drawRoundRectangle",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal)
				,parameter(documentManager, "height",Type.LongReal),parameter(documentManager, "arcw",Type.LongReal),parameter(documentManager, "arch",Type.LongReal));  
		ShapeElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"fillEllipse",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal),parameter(documentManager, "height",Type.LongReal));  
		ShapeElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"fillRectangle",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal),parameter(documentManager, "height",Type.LongReal));  
		ShapeElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"fillRoundRectangle",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "width",Type.LongReal)
				,parameter(documentManager, "height",Type.LongReal),parameter(documentManager, "arcw",Type.LongReal),parameter(documentManager, "arch",Type.LongReal));  
		ShapeElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"instantMoveTo",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal));  
		ShapeElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"moveTo",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "speed",Type.LongReal));  
	}

	// ******************************************************************
	// *** The Standard Link Class TextElement    NOTE: if(SimulaCompiler.EXTENSIONS)
	// ******************************************************************
	/// Initiate the Standard Class TextElement.
	private static void initTextElement(final DocumentManager documentManager) {
		StandardClass TextElement = new StandardClass(documentManager, "Link","TextElement",parameter(documentManager, "txt",Type.Text),parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal));  
		ref_TextElement.setQual(TextElement);
		Drawing.addStandardClass(TextElement);  // Declared in Drawing
		TextElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setColor",parameter(documentManager, "color",Type.Integer));  
		TextElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setStroke",parameter(documentManager, "width",Type.Real));  
		TextElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setFontStylePlain");  
		TextElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setFontStyleBold");  
		TextElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setFontStyleItalic");  
		TextElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setFontStyleBoldItalic");  
		TextElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setFontSize",parameter(documentManager, "size",Type.Real));  
		TextElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,Type.Real,"getFontSize");  

		TextElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"setText",parameter(documentManager, "t",Type.Text));  
		TextElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"instantMoveTo",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal));  
		TextElement.addStandardProcedure(documentManager, ObjectKind.MemberMethod,null,"moveTo",parameter(documentManager, "x",Type.LongReal),parameter(documentManager, "y",Type.LongReal),parameter(documentManager, "speed",Type.LongReal));  
	}





	// ******************************************************************
	// *** Constructors
	// ******************************************************************

	/// Create a new StandardClass.
	/// @param className the class's name
	private StandardClass(final DocumentManager documentManager, final String className) {
		super(documentManager, new Identifier(className));
		this.externalIdent = "RTS_"+className;
		this.declarationKind=ObjectKind.StandardClass;
		this.type=Type.Ref(className);
		SET_SEMANTICS_CHECKED();
	}

	/// Create a new StandardClass.
	/// @param prefix prefix class-identifier
	/// @param className the class's name
	private StandardClass(final DocumentManager documentManager, final String prefix, final String className) {
		this(documentManager, className);
		this.prefix = new Identifier(prefix);
		SET_SEMANTICS_CHECKED();
	}

	/// Create a new StandardClass.
	/// @param prefix prefix class-identifier
	/// @param className the class's name
	/// @param param the parameters
	private StandardClass(final DocumentManager documentManager, final String prefix, final String className, final Parameter... param) {
		this(documentManager, prefix,className);
		for(int i=0;i<param.length;i++) param[i].into(parameterList);
		SET_SEMANTICS_CHECKED();
	}
	
	@Override
	public int getRTBlockLevel() {
		return 0;
	}

	// ******************************************************************
	// *** Lookup Meaning
	// ******************************************************************

	@Override
	public Meaning findVisibleAttributeMeaning(Identifier ident) {
		if(Option.internal.TRACE_FIND_MEANING > 1)
			LOG.trace("StandardClass.findVisibleAttributeMeaning: BEGIN Search "+identifierValue()+" for "+ident.value+" ================================== "+identifierValue()+" ==================================");
		for(Declaration declaration:declarationList) {
			if(Option.internal.TRACE_FIND_MEANING > 2) LOG.trace("StandardClass.findVisibleAttributeMeaning: Checking Local "+declaration.identifierValue());
			if(Util.equals(ident, declaration.identifier)) {
				return(new Meaning(declaration,this));
			}
		}
		if(Option.internal.TRACE_FIND_MEANING > 1)
			LOG.trace("StandardClass.findVisibleAttributeMeaning: ENDOF Search "+identifierValue()+" for "+ident.value+" ========= NOT FOUND ============== "+identifierValue()+" ==================================");
		if(prefix != null) {
			ClassDeclaration prfx=getPrefixClass();
			if(prfx!=null) return(prfx.findVisibleAttributeMeaning(ident));
		}
		return(null);
	}

	@Override
	public Meaning findRemoteAttributeMeaning(Identifier ident) {
//		IO.println("StandardClass.findRemoteAttributeMeaning: " + identifierValue() + ", TRY FIND: "+ident.value);
		for(Declaration declaration:declarationList) {
//			IO.println("StandardClass.findRemoteAttributeMeaning: CHECK: " + declaration.identifierValue() + ", find: "+ident);
			if(Util.equals(ident, declaration.identifier)) {
				return(new Meaning(declaration,this));
			}
		}
		ClassDeclaration prfx=getPrefixClass();
		if(prfx!=null) return(prfx.findRemoteAttributeMeaning(ident));
		return(null);
	}

	// ******************************************************************
	// *** Parameter Specifications
	// ******************************************************************

	/// Create a new Parameter.
	/// @param ident the identifier
	/// @param type  the type
	/// @return the newly created Parameter
	private static Parameter parameter(final DocumentManager documentManager, String ident, Type type)	{
		return new Parameter(documentManager, new Identifier(ident), type,Parameter.Kind.Simple); }

	/// Create a new Parameter.
	/// @param ident the identifier
	/// @param type  the type
	/// @param kind  the parameter kind
	/// @return the newly created Parameter
	private static Parameter parameter(final DocumentManager documentManager, String ident, Type type, int kind)	{
		return new Parameter(documentManager, new Identifier(ident), type, kind); }

	/// Create a new Parameter.
	/// @param ident the identifier
	/// @param type  the type
	/// @param kind  the parameter kind
	/// @param nDim  number of dimensions for arrays
	/// @return the newly created Parameter
	private static Parameter parameter(final DocumentManager documentManager, String ident, Type type, int kind, int nDim)	{
		return new Parameter(documentManager, new Identifier(ident), type, kind, nDim); }

	/// Create a new Parameter.
	/// @param ident the identifier
	/// @param mode  the mode
	/// @param type  the type
	/// @return the newly created Parameter
	private static Parameter parameter(final DocumentManager documentManager, String ident,int mode,Type type) {
		Parameter spec = new Parameter(documentManager, new Identifier(ident),type,Parameter.Kind.Simple);
		spec.setMode(mode); return(spec);
	}

	/// Create a new Parameter.
	/// @param ident the identifier
	/// @param kind  the parameter kind
	/// @param mode  the mode
	/// @param type  the type
	/// @return the newly created Parameter
	private static Parameter parameter(final DocumentManager documentManager, String ident,int kind, int mode,Type type) {
		Parameter spec=new Parameter(documentManager, new Identifier(ident),type,kind);
		spec.setMode(mode); return(spec);
	}


	// ******************************************************************
	// *** Add Class / Attribute / Procedure
	// ******************************************************************

	/// Add a StandardClass.
	/// @param standardClass the StandardClass
	private void addStandardClass(StandardClass standardClass) {
		standardClass.declaredIn = this;
		((ClassDeclaration)standardClass.declaredIn).hasLocalClasses=true;
		declarationList.add(standardClass);
	}

	/// Create and add a new standard attribute.
	/// @param type the attribute type
	/// @param ident the attribute identifier
	private void addStandardAttribute(final DocumentManager documentManager, Type type, String ident) {
		declarationList.add(new SimpleVariableDeclaration(documentManager, type, new Identifier(ident))); }

	/// Create and add a new constant standard attribute.
	/// @param type the attribute type
	/// @param ident the attribute identifier
	/// @param value the constant integer value
	private void addStandardAttribute(final DocumentManager documentManager, Type type, String ident, Number value) {
		declarationList.add(new SimpleVariableDeclaration(documentManager, type, new Identifier(ident), true, new Constant(documentManager, type,value))); }

	/// Create and add a new StandardProcedure.
	/// @param kind the declaration kind
	/// @param type the procedure's type
	/// @param ident the procedure identifier
	private void addStandardProcedure(final DocumentManager documentManager, int kind, Type type, String ident) {
		declarationList.add(new StandardProcedure(documentManager, this, kind, type, ident)); }

	/// Create and add a new StandardProcedure.
	/// @param kind the declaration kind
	/// @param type the procedure's type
	/// @param ident the procedure identifier
	/// @param param the parameters
	private void addStandardProcedure(final DocumentManager documentManager, int kind, Type type, String ident, Parameter... param) {
		declarationList.add(new StandardProcedure(documentManager, this, kind, type, ident, param)); }

	/// Create and add a new StandardProcedure.
	/// @param kind the declaration kind
	/// @param mtdSet the set of Method Type Descriptors
	/// @param type the procedure's type
	/// @param ident the procedure identifier
	/// @param param the parameters
	private void addStandardProcedure(final DocumentManager documentManager, int kind, String[] mtdSet, Type type, String ident, Parameter... param) {
		declarationList.add(new StandardProcedure(documentManager, this, kind, mtdSet, type, ident, param)); }

	// ***********************************************************************************************
	// *** ClassFile coding Utility: getClassDesc   -- Defined in DeclarationScope
	// ***********************************************************************************************
	@Override
	public ClassDesc getClassDesc() {
		return(ClassDesc.of("simula.runtime." + this.externalIdent));
	}

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
//	/// Default constructor used by Attribute File I/O
//	public StandardClass(final DocumentManager documentManager) {
//		super(documentManager, null);
//	}


}


