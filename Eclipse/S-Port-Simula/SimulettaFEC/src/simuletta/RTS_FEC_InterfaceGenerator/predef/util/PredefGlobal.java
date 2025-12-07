/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.RTS_FEC_InterfaceGenerator.predef.util;

import java.io.File;


/**
 * Global Variables 
 * 
 * @author Øystein Myhre Andersen
 *
 */
public final class PredefGlobal {
    public static final String simulettaReleaseID="Simuletta-1.0";

	public static int    sourceLineNumber;
	public static String currentSourceLine;
	public static File   sourceFileDir; 
	public static String sourceFileName;
	public static String sourceName;
	public static String simulaRtsLib;        // The simula runtime system
	public static String simulettaTESTLib;    // The simuletta TEST runtime system
	public static File   attributeFile;       // Where to write AttributeFiles
	public static File   outputDir;           // The Directory containing output AttributeFiles and SCode 
	public static int    nError;
	
//	public static String packetName; // E.g. "simulaRTS"; // NOTE: Must be a single identifier

//	public static Vector<InsertedModule> modset;  // The set of all inserted modules
//	public static Vector<Declaration> bodyList;   // See: RoutineBody.doSCodeDeclaration
//	public static ProgramModule currentModule;    // Current ProgramModule
//	public static DeclarationScope currentScope;  // Current Scope. Maintained during Checking and Coding
//	public static boolean allVisible;             // Set by Compiler Directive  %VISIBLE and %HIDDEN
//	public static SCodeFile sCode;                // Set when SCoding starts: new SCodeFile();
//	public static boolean duringMacroDefinition;

	public static void INIT() {
//	    Tag.INIT();
//	    ProgramModule.INIT();
//		modset=new Vector<InsertedModule>();
//		bodyList=new Vector<Declaration>();
//		currentModule=new ProgramModule(null);
//		currentScope=null;
//		allVisible=false;
//		sCode=null;
//		duringMacroDefinition=false;
		nError=0;
	}	

	//%title ***   c a t e g    c o d e s   ***

	    public static final int C_unspec=  0;  // corresponds to RTS 'm_ref';
	    public static final int C_value =  1;  // corresponds to RTS 'm_value';
	    public static final int C_name  =  2;  // corresponds to RTS 'm_name';
	    public static final int C_local =  3;  // corresponds to RTS 'm_local';
	    public static final int C_extnal=  4;  // corresponds to RTS 'm_extr';
	    public static final int C_unknwn=  5;
	    public static final int C_virt  =  6;
	    public static final int C_block =  7;

	    public static final int C_max   =  7;  // NB *** must be < 8;

	    public static String C_code(int k) {
	        if(k ==C_unknwn) return("default");
	        if(k ==C_local ) return("local");
	        if(k ==C_value ) return("value");
	        if(k ==C_name  ) return("name");
	        if(k ==C_unspec) return("unspec");
	        if(k ==C_virt  ) return("virt");
	        if(k ==C_extnal) return("extnal");
	        if(k ==C_block ) return("block");
	                         return("illegal");
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


	//%title ***   c l a s s i f i c    c o d e s   ***

	     public static final int Clf000 =0;  //Normal userdefined declaration, used when it comes from a usual external declaration.;
	     public static final int Clf001 =1;  //External procedure with binding;
	     public static final int Clf002 =2;  //parameters to external procedure with binding.

//	                        The rest is only used in system attr. files;

	     public static final int Clf003 =3;  //Is used when it is of no specific interest.;
	     public static final int Clf004 =4;  //Used for system classes.;
//	                         --  Procedures implemented by routines:
	     public static final int Clf005 =5;  //Used for procedure which is attribute of text
//	                        ( accessed as <text expr>.<procedure> ). Should
//	                        be called as a routine with an extra first GADDR
//	                        parameter (address of text variable).  If the
//	                        <text expr> is not a variable, this routine
//	                        should not be called. Instead its successor in
//	                        the declaration list of 'class _text' should be
//	                        called. This is assumed to be classified with U;
	     public static final int Clf006 =6;  //Used for procedure which is attribute of text
//	                        ( accessed as <text expr>.<procedure> ).  Should
//	                        be called as a routine with an extra first text
//	                        quantity parameter (as a value).;
	     public static final int Clf007 =7;  //Used for procedures local in classes that must
//	                        have a reference to the object as an extra first
//	                        parameter.;
	     public static final int Clf008 =8;  //- as '7', but the routine may lead to garbage
//	                        collection.  Thus SAVE-RESTORE must possibly
//	                        enclose the call.  Except for 'setacces', these
//	                        are all of type text, and the text reference is
//	                        delivered on TOS. If the procedure is remotely
//	                        accessed, the extra first parameter has to be
//	                        kept in TMP.PNT during a possible save.;
	     public static final int Clf009 =9;  //Used for class process of SIMULATION.
//	                        Enables checking of encloser as simulation block;
	     public static final int Clf010 =10;  //Used for procedures that are translated to routines with no special treatment.;

	     public static final int Clf011 =11;  //- as '10', but the routine may lead to garbage
//	                        collection.  Thus SAVE-RESTORE must possibly
//	                        enclose the call.  These are all procedures of
//	                        type text, and the text reference is delivered
//	                        on TOS.;
	     public static final int Clf012 =12;  //Used for procedure in class file which is an
//	                        operation on the image. Image of the actual file
//	                        should be given as extra first parameter, as a
//	                        GADDR (corresponding to '5' above).;
	     public static final int Clf013 =13;  //Used for standard procedures which should be
//	                        translated to a direct fetch from a RTS variable (max/min functions, simulaid).;
	     public static final int Clf014 =14;  //Used for type procedures local in classes that
//	                        should be translated to fetching a value
//	                        directely from an attribute (certain attributes
//	                        of FILE and SIMULATION). Is translated to
//	                        code for OADDR of the enclosure followed by a
//	                        REMOTEV.;
	     public static final int Clf015 =15;  //Used for type procedures local in classes that
//	                        should be translated to fetching a value from an
//	                        attribute through one level of indirection.  Is
//	                        translated to code for OADDR followed by two
//	                        REMOTEV's.  Currently used for Simulation'time
//	                        only.;
//	                        -- Classification of parameters:
	     public static final int Clf016 =16;  //Used for parameters to procedures that must be
//	                        translated to usual parameters to routines.;
	//%                       === Special classification codes ===
	//%                       These codes enable the compiler to recognize
	//%                       inline coded procedures, etc.
	     public static final int Clf017 =17;  //- as '16', but parameter checking suppressed;
	     public static final int Clf018 =18;  //Used for parameters to routines class. > 19;
	     public static final int Clf019 =19;  //Used for text parameters of loadchar/storechar
//	                         - prepare for (S-code) index;
//	         ----- Inline coded procedures:
	     public static final int Clf020 =20;  //rem;
	     public static final int Clf021 =21;  //int abs;
	     public static final int Clf022 =22;  //real abs;
	     public static final int Clf023 =23;  //lreal abs;
	     public static final int Clf024 =24;  //int sign;
	     public static final int Clf025 =25;  //real sign;
	     public static final int Clf026 =26;  //lreal sign;
	     public static final int Clf027 =27;  //char;
	     public static final int Clf028 =28;  //isochar;
	     public static final int Clf029 =29;  //rank;
	     public static final int Clf030 =30;  //isorank;
	     public static final int Clf031 =31;  //loadchar;
	     public static final int Clf032 =32;  //storechar;
	     public static final int Clf033 =33;  //sint min;
	     public static final int Clf034 =34;  //int min;
	     public static final int Clf035 =35;  //real min;
	     public static final int Clf036 =36;  //lreal min;
	     public static final int Clf037 =37;  //sint max;
	     public static final int Clf038 =38;  //int max;
	     public static final int Clf039 =39;  //rela max;
	     public static final int Clf040 =40;  //lreal max;
	     public static final int Clf041 =41;  //sourceline;
	     public static final int Clf042 =42;  //text'more;
	     public static final int Clf043 =43;  //imagefile'more;
	     public static final int Clf044 =44;  //text'pos;
	     public static final int Clf045 =45;  //imagefile'pos;
	     public static final int Clf046 =46;  //text'length;
	     public static final int Clf047 =47;  //imagefile'length;
	     public static final int Clf048 =48;  //text'start;

	     public static final int Clfmax =48;

	     public static String Cl_code(int k) {
	    	 if(k<=Clfmax) return(""+k);
	    	 return("illegal");
	     }


}