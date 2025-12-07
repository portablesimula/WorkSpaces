/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.declaration.scope;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.Vector;

import simuletta.RTS_FEC_InterfaceGenerator.RTS_FEC_InterfaceGenerator;
import simuletta.compiler.Global;
import simuletta.compiler.common.SCodeFile;
import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.LabelDeclaration;
import simuletta.compiler.declaration.Profile;
import simuletta.compiler.declaration.InlineRoutine;
import simuletta.compiler.declaration.VariableDeclaration;
import simuletta.compiler.expression.value.Value;
import simuletta.compiler.parsing.Mnemonic;
import simuletta.compiler.parsing.Parser;
import simuletta.compiler.parsing.SimulettaScanner;
import simuletta.compiler.statement.Statement;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Tag;
import simuletta.utilities.Util;

/**
 * Program Module.
 * 
 * <pre>
 * Syntax:
 *
 * SimulettaProgram ::= InterfaceModule | SubModule | MainProgram
 * 
 * 		InterfaceModule ::= GLOBAL module'identifier < ( info'String ) >?
 * 							BEGIN < < VISIBLE >? declInInterface >*  END
 * 
 * 
 * 		SubModule ::= MODULE module'identifier 
 * 					  BEGIN < < VISIBLE >? declInModule >*  < Statement >*  END
 * 		
 * 		MainProgram ::= BEGIN < declInModule >*  < Statement >*  END
 * 
 * 			declInModule ::= ModuleInclusion
 * 						   | MnemonicDefinition
 *						   | GlobalDeclaration
 *						   | ConstantDeclaration
 *						   | RecordDeclaration
 *						   | RoutineDeclaration
 * 
 * 
 * 			declInInterface ::= MnemonicDefinition
 *							  | GlobalDeclaration
 *							  | ConstantDeclaration
 *							  | RecordDeclaration
 *							  | RoutineDeclaration
 * 
 * 				MnemonicDefinition ::= MacroDefinition
 * 									 | LiteralDefinition
 * 
 * 					LiteralDefinition ::= DEFINE literal'Identifier = LiteralValue 
 * 											< , literal'Identifier = LiteralValue >*
 * 
 * 						LiteralValue ::= BasicSymbol
 * 
 * 				MacroDefinition ::= MACRO macro'Identifier ( parcount'IntegerNumber )
 * 									BEGIN < MacroElement >* ENDMACRO
 * 
 * 					MacroElement ::= any BasicSymbol except ENDMACRO and %
 * 								   | % parnumber'IntegerNumber
 * 
 *  			GlobalDeclaration ::= Type Globalid  < , Globalid >*
 *
 *  			ConstantDeclaration ::= CONST Type Constantid  < , Constantid >*
 * 
 * 				RecordDeclaration ::= RECORD record'identifier  < : prefix'identifier >  < RecordInfo >?
 * 									  BEGIN CommonPart  < VariantPart >*  END
 * 
 *				RoutineDeclaration ::= ProfileDeclaration
 * 									 | BodyDeclaration
 * 									 | SingularRoutine
 * 									 | KnownRoutine
 * 									 | SystemRoutine
 * 									 | ExternalRoutine
 * 
 * 					ProfileDeclaration ::= < GLOBAL < ( identifying'StringValue ) >? >?
 *          			                   PROFILE profile'identifier  ParameterSpecification  END
 * 
 * 
 * SCode:
 * 
 * S-program ::= PROGRAM programHead:string  ProgramBody ENDPROGRAM
 *
 *		ProgramBody	::= interface_module
 *					 |  macro_definition_module
 *					 |  <module_definition>*
 *					 |  MAIN <local_quantity>* < program_element >*
 * 
 *			interface_module ::= global module module_id:string check_code:string
 *									<global_interface>* tag_list
 *									body < init global:tag type repetition_value >*
 *								 endmodule
 *
 *				global_interface ::= record_descriptor
 *								  |  constant_definition < system sid:string >?
 *								  |  global_definition < system sid:string >?
 *								  |  routine_profile
 *								  |  info_setting
 *
 *					global_definition ::= global internal:newtag quantity_descriptor
 *
 *
 *			macro_definition_module ::= macro module module_id:string check_code:string
 *										  <macro_definition>* endmodule
 *
 *
 *			module_definition ::= module module_id:string check_code:string
 *									visible_existing
 *									body <local_quantity>* < program_element >* endmodule
 *
 *				visible_existing ::= <visible>* tag_list ! existing
 *
 *					visible ::= record_descriptor   | routine_profile | routine_specification
 *							 |  label_specification | constant_specification | insert_statement | info_setting
 *
 *					tag_list ::= < tag internal:tag external:number >+
 *
 *
 *			program_element	::= instruction	| label_declaration	| routine_profile | routine_definition | skip_statement
 *							 | if_statement | protect_statement | goto_statement | insert_statement | delete_statement
 *
 *				instruction	::= constant_declaration | record_descriptor | routine_specification | stack_instruction | assign_instruction
 *							 |  addressing_instruction | protect_instruction | temp_control | access_instruction | arithmetic_instruction
 *							 | convert_instruction | jump_instruction | goto_instruction | if_instruction | skip_instruction
 *							 | segment_instruction | call_instruction | area_initialisation | eval_instruction | info_setting | macro_call

 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class ProgramModule extends DeclarationScope {
//	public int lineNumber;    // From SyntaxClass
//	public String identifier; // From Declaration
//	public Tag tag;  		  // From Declaration
//	public boolean global;    // From Declaration
//	public boolean visible;   // From Declaration
//	public Vector<Declaration> declarationList // From DeclarationScope
	public static final String version="Simuletta AtributeFile Layout version 1.0";

	public boolean global;   // Set in pass 1 if compiling a global module ;
	public boolean combined; // Set in pass 1 if compiling the combined global module RTS$;
	private String info;     // Current module's check-code (notext for main)
	
	private	String eltid;

	public final Vector<Statement> statements = new Vector<Statement>();	
	private static Vector<Declaration> constlist;//=new Vector<Declaration>();
	
	
	public boolean isGlobalModule() { return(global); }
	public boolean isSubModule() { return(!(isGlobalModule() || isMainProgram())); }
	public boolean isMainProgram() { return(identifier==null); }
	
    // ***********************************************************************************************
    // *** Constructor
    // ***********************************************************************************************
    public ProgramModule(final String identifier) {
    	super(identifier,false);
    }
    
    public static void INIT() {
    	constlist=new Vector<Declaration>();
    }

	public void parse() {
		enterScope(this);
    	if(Parser.accept(KeyWord.MODULE,KeyWord.GLOBAL)) {
    		//Util.TRACE("Grammar.doParse");
    		currentModule.global=(Parser.prevToken.getKeyWord()==KeyWord.GLOBAL);
    		currentModule.identifier=Parser.expectIdentifier();
    		//Util.TRACE("Grammar.doParse: module_ident="+currentModule.identifier+",");
    		//IO.println("Grammar.doParse: module_ident="+currentModule.identifier+",");
    		if(Parser.accept(KeyWord.BEGPAR)) {
    			currentModule.info=Parser.acceptString();
    			//Util.TRACE("Grammar.doParse: info="+info+",");
    			Parser.expect(KeyWord.ENDPAR);
    		}
    		if(currentModule.info == null) currentModule.info= identifier+':'+datetime();
    	}
    	Parser.expect(KeyWord.BEGIN);
    	Declaration.doParse(currentModule.declarationList);
//    	printDeclarationList();
    	
    	Statement.parseStatements(currentModule.statements);
    	Parser.expect(KeyWord.END);
		exitScope(this);
	}
	
	// ***********************************************************************************************
	// *** doSCodeModuleHead
	// ***********************************************************************************************
	private void doSCodeModuleHead() {
    	sCode.outinst(S_PROGRAM);
    	sCode.outstring(datetime() + "'Simuletta'Java"); sCode.outcode();
    	
    	if(currentModule.isMainProgram()) {
    		sCode.outinst(S_MAIN); eltid="MAIN"; }
    	else {
    		if(currentModule.isGlobalModule()) sCode.outinst(S_GLOBAL);
    		eltid=currentModule.identifier;
    		//if(eltid.length() > 10) eltid=eltid.substring(1,10);
    		sCode.outinst(S_MODULE);
    		sCode.outstring(eltid); sCode.outstring(currentModule.info);
    	}
    	sCode.outcode();
    	
    	if(modset.size()>0) {
    		for(InsertedModule m:modset) {
    			sCode.outinst((m.system)?S_SYSINSERT:S_INSERT);
    			sCode.outstring(m.identifier); sCode.outstring(m.modcode);
    			sCode.outstring("?");	// *** External ident later  TEMP  ***;
    			sCode.outnumber(m.tagbase); sCode.outnumber(m.taglimit);
    			sCode.outcode();
    		}
    	}
	}
    public static String datetime() {
		DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String datim = LocalDateTime.now().format(form);
		return (datim);
	}
	
	
	// ***********************************************************************************************
	// *** doSCoding
	// ***********************************************************************************************
	public void doSCoding(File outputFile) {
		sCode=new SCodeFile("sCode",outputFile);
    	currentModule.doSCodeModuleHead();
    	
    	// Extracted set of visible declarations suitable for output Attr-file
    	Vector<Declaration> visibleDeclarations=new Vector<Declaration>();
    	
    	// localVariable is used to postpone the declaration code to local varables until after any qualification (infix,ref).
    	Vector<VariableDeclaration> localVariable=new Vector<VariableDeclaration>();
    	
    	for(Declaration d:currentModule.declarationList) {
    		//Util.println("DECLARATION: "+d.getClass().getSimpleName()+"  "+d.identifier+"  visible="+d.visible);
    		d.doSCodeSpecCode();
    	}
    	
    	for(Declaration d:currentModule.declarationList) {
    		//Util.println("DECLARATION: "+d.getClass().getSimpleName()+"  "+d.identifier+"  visible="+d.visible);
    		if(d.visible || currentModule.isGlobalModule()) {
    			if(!(d instanceof InlineRoutine)) {
//    				IO.println("ProgramModule.doSCodeModuleHead: doSCodeDeclaration d="+d);
    				if(d.getTag() == null) Util.IERR("Missing tag in visible descriptor: "+d.getClass().getSimpleName()+"   "+d);
    				if(d.getTag().getXtag() < 0) Util.IERR("Missing xtag in visible descriptor: "+d);
    				d.doSCodeDeclaration();
    				visibleDeclarations.add(d);
    			}
    		}
    		else if(d instanceof Record) d.doSCodeDeclaration();
    		else if(d instanceof RoutineDeclaration) d.doSCodeDeclaration();
    		else if(d instanceof Profile) d.doSCodeDeclaration();
     		else if(d instanceof DeclaredBody) d.doSCodeDeclaration();
    		else if(d instanceof LabelDeclaration) d.doSCodeDeclaration();
    		else if(d instanceof VariableDeclaration && !currentModule.isMainProgram()) {
    			d.doSCodeDeclaration();
    			VariableDeclaration var=(VariableDeclaration)d;
//    			if(var.initval==null || !var.read_only )
    				localVariable.add(var);
    		}
//    		else Util.println("Unknown Declaration: d="+d.getClass().getSimpleName()+"  "+d);
    	}
//		IO.println("ProgramModule.doSCoding: visibleDeclarations.size="+visibleDeclarations.size());
//		IO.println("ProgramModule.doSCoding: nxtag="+Tag.nxtag);
    	
    	if(!currentModule.isMainProgram()) {
    		// GlobalModule and SubModule
    		currentModule.doOutputAttributeFile(visibleDeclarations); // NOTE: Will output external tag list. See Tag'Externalization

    		Tag.outputTAGTAB();
    		sCode.outinst(S_BODY);
    		sCode.outcode();

    		if(localVariable.size()>0) {
    			for(VariableDeclaration var:localVariable) {
    				if(var.initval != null) {
    					//ERROR("Illegal with initial value: "+var.initval);
    					var.outConstant();
    				} else {
    					var.doSCodeDeclaration(); // TESTING
    					var.doOutput(S_LOCAL);
    				}
    			}
     		}
    		
            //while not vislist.empty do vislist.first.into(constlist);
//    		while(!visibleDeclarations.isEmpty()) {
//    			Declaration d=visibleDeclarations.remove(0);
//    			//constlist.add(d);
//    			addConstant(d);
//    		}
    		
//    		for(Declaration d:visibleDeclarations) addConstant(d);
//    		visibleDeclarations=null;
    		
    	}

    	if(currentModule.isGlobalModule()) {
       		OutputInitValues();
    	} else {
    		// SubModules and MainProgram.
    		PrepareAndOutputConstants();
    	}
    	
    	
    	for(Declaration d:bodyList) {
    		//Util.println("BODY-STATEMENTS: "+d.getClass().getSimpleName()+"  "+d.identifier+"  visible="+d.visible);
    		if(d instanceof DeclaredBody) ((DeclaredBody)d).doSCodeStatement();
    		else if(d instanceof RoutineBody) ((RoutineBody)d).doSCodeStatement();
    		else if(d instanceof InlineRoutine); // Nothing
    		else FATAL_ERROR("BodyList  d="+d+", QUAL="+d.getClass().getSimpleName()); //IERR();INSERT
    	}

    	if(!currentModule.isGlobalModule()) {
    		for(Statement s:currentModule.statements) s.doSCodeStatement();
    	}
    	if(!currentModule.isMainProgram()) {
    		sCode.outinst(S_ENDMODULE);	sCode.outcode();
    	}

    	//Tag.listTagTable();

    	sCode.outinst(S_ENDPROGRAM); sCode.outcode();
    	sCode.close(); sCode=null;
	}
    
    private static void PrepareAndOutputConstants() {
    	Util.BREAK("PrepareAndOutputConstants");
		// Prepare and output constants for SubModules and MainProgram.
		// All VariableDeclaration.initval has previously been specified (S_CONSTSPEC) in VariableDeclaration.doSCoding
		for(Declaration d:currentModule.declarationList) {
			if(d instanceof VariableDeclaration) {
				VariableDeclaration var=(VariableDeclaration)d;
				if(!var.IS_SCODE_EMITTED()) {
					Util.BREAK("doPrepare: "+var);
					if(var.initval != null) {
						if(Option.TRACE_CODING > 0) Util.BREAK("<= * " + var.identifier);
						if(! var.constSpecWritten) {
							var.constSpecWritten = true;
							var.doOutput(S_CONSTSPEC);
						}
						if(!constlist.contains(d)) constlist.add(d);
					} else d.doSCodeDeclaration();
				}
			}
		}
		for(Declaration d:constlist) {
			if(d instanceof VariableDeclaration) {
    			VariableDeclaration var=(VariableDeclaration)d;
    	    	Util.BREAK("doOutputConstant: "+var);
				var.doOutput(S_CONST); sCode.outcode(+1);
				if(var.initval!=null) {
					Util.BREAK("Initval: QUAL="+var.initval.getClass().getSimpleName());
					for(Value val:var.initval) {
						Util.BREAK("Outconst: QUAL="+val.getClass().getSimpleName());
						Type typ=val.doOutConst();
						if(typ != var.type) {
							Type res=Type.checkCompatible(typ,var.type);
							if(res==null) ERROR("Different types in repeated constant: "+typ+" -- "+var.type+"   VALUE="+val);
						}
					}
				}
				sCode.outcode(-1);
			}
		}
    }

    
    private static void OutputInitValues() {
    	Util.BREAK("OutputInitValues");
		for(Declaration d:currentModule.declarationList) {
			//inspect d when quant do {
			if(d instanceof VariableDeclaration var) {
//				VariableDeclaration var=(VariableDeclaration)d;
				if(var.initval != null && var.kind!=VariableDeclaration.VarKind.GlobalConstant) {
		    		// Output INIT-Statements for Global Module.
		    		//
		    		//  INIT-Statement  ::=   INIT global:tag type repetition_value 
		    		//
		    		// A global variable may be initialised to a given value through the init statement.
		    		// This initialisation must take place before execution of the program proper take place, e.g. in connection with main. 
		    		//
					sCode.outinst(S_INIT); sCode.outtag(var.getTag());
					var.type.toSCode(); //OldType.outtype(var.type);
					for(Value e:var.initval) {
						Type typ=e.doOutConst();//.doSCodingDirect();
						if(typ != var.type) {
							Type res=Type.checkCompatible(typ,var.type);
							if(res==null) ERROR("Missing type conversion: "+typ+" --> "+var.type);
						}
					}
				}
			}
		}
    }

    
	// ***********************************************************************************************
	// *** doOutputAttributeFile
	// ***********************************************************************************************
	public void doOutputAttributeFile(Vector<Declaration> visibleDeclarations) {
		if(combined)
			 try { RTS_FEC_InterfaceGenerator.doGenerateFiles(identifier); } catch (IOException e) { e.printStackTrace(); }
		else try { writeAttrFile(visibleDeclarations); } catch (IOException e) { e.printStackTrace(); }
	}
    	
	private void writeAttrFile(Vector<Declaration> visibleDeclarations) throws IOException {
		if (Option.verbose)	IO.println("************************** BEGIN WRITE ATTRIBUTE FILE: "+identifier+"  lastTag="+Tag.lastTag+" **************************");
//		String relativeAttributeFileName="Attrs/FEC/"+Global.packetName+"/"+Global.sourceName+".atr";
		String relativeAttributeFileName=Global.packetName+"/Attrs/"+Global.sourceName+".atr";
		File attributeFile = new File(Global.outputDir,relativeAttributeFileName);
		if (Option.verbose)	Util.println("ProgramModule.writeModule: \"" + attributeFile+"\"");
    	createFile(attributeFile);
		FileOutputStream fileOutputStream = new FileOutputStream(attributeFile);
		// BufferedOutputStream uses a default buffer size of 512 bytes
//		AttributeOutput oupt = new AttributeOutput(new BufferedOutputStream(fileOutputStream));
		ObjectOutput oupt = new ObjectOutputStream(new BufferedOutputStream(fileOutputStream));
	
		Util.BREAK("Module.writeModule: File="+attributeFile);
		if (Option.verbose)	Util.TRACE("***       Write External " + identifier);

//		oupt.writeString(version);
		oupt.writeObject(version);
		
		//oupt.writeObject(SimulettaScanner.mnemonics);
		Set<String> keys=SimulettaScanner.mnemonics.keySet();
		oupt.writeShort(keys.size());
		for(String key:keys) {
			Mnemonic mne=SimulettaScanner.mnemonics.get(key);
//			IO.println("Mnemonic: "+mne.getClass().getSimpleName()+"   "+mne);
//			mne.write(oupt);
			oupt.writeObject(mne);
		}
		
		//oupt.writeObject(visibleDeclarations);
		oupt.writeShort(visibleDeclarations.size());
//		for(Declaration decl:visibleDeclarations) decl.write(oupt);
		for(Declaration decl:visibleDeclarations) oupt.writeObject(decl);;
		oupt.writeShort(Tag.nxtag);
		
		//oupt.flush();
		oupt.close();	oupt = null;
		if (Option.verbose)	IO.println("************************** END WRITE ATTRIBUTE FILE: "+identifier+"  lastTag="+Tag.lastTag+", nxtag="+Tag.nxtag+" **************************");
		if (Option.verbose)	Util.TRACE("ProgramModule.writeProgramModule: *** ENDOF Generate SimulettaAttributeFile: " + attributeFile);
		Global.outputAttributeFile=attributeFile;
	}

	public void print(final String lead,final int indent) {
		IO.println(Util.edIndent(indent)+"************************** "+lead+" BEGIN PRINT MODULE: "+identifier+"  lastTag="+Tag.lastTag+" **************************");
    	for(Declaration decl:declarationList) {
    		decl.print("",indent);
    	}
		IO.println(Util.edIndent(indent)+"************************** "+lead+" ENDOF PRINT MODULE: "+identifier+"  lastTag="+Tag.lastTag+" **************************");
    }
    
	public String toString() {
		return("Module "+identifier+"  lastTag="+Tag.lastTag);
	}

}
