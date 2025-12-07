/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.declaration;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.*;
import static simuletta.compiler.expression.Expression.parseValue;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

import simuletta.compiler.declaration.scope.Record;
import simuletta.compiler.expression.value.Value;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Tag;
import simuletta.utilities.Util;

/**
 * <pre>
 * 
 * Syntax:
 * 
 *  VariableDeclaration ::= GlobalDeclaration | ConstantDeclaration | LocalDeclaration | ParameterDeclaration | ...
 *  
 *  	GlobalDeclaration ::= Type Globalid  < , Globalid >*
 *  
 *  		Globalid ::= Identifier < SYSTEM identifier >?  < = Value >?
 *  		           | Identifier < SYSTEM identifier >?  ( repeat'IntegerNumber )  < = ( Value < , Value >* ) >?
 *  
 *  
 *  	ConstantDeclaration ::= CONST Type Constantid  < , Constantid >*
 *  
 *  		Constantid ::= Identifier = Value
 * 						 | Identifier SYSTEM identifier
 *						 | Identifier   ( repeat'IntegerNumber )  < = ( Value < , Value >* ) >?
 *						 | Identifier   ( repeat'IntegerNumber )  SYSTEM identifier
 *  
 *  
 *  	LocalDeclaration ::= Type Localid  < , Localid >*
 *  
 *  		Localid ::= Identifier  ( repeat'IntegerNumber )  < = ( Value < , Value >* >?
 *  
 *  
 *  	ParameterDeclaration ::= Type Parid  < , Parid >*
 *  
 *  		Parid ::= Identifier  ( repeat'IntegerNumber )
 *  
 *   
 * </pre>
 * 
 * @see Type
 * @author Øystein Myhre Andersen
 */
public class VariableDeclaration extends Declaration implements Externalizable {
//	public int lineNumber;    // From SyntaxClass
//	public String identifier; // From Declaration
//	public Tag tag;  		  // From Declaration
//	public boolean global;    // From Declaration
//	public boolean visible;   // From Declaration
	public VarKind kind;
    public enum VarKind { LocalVariable, GlobalConstant, GlobalVariable, SubModuleConstant, SubModuleVariable, MainProgramConstant, MainProgramVariable };

    public Type type;
	public boolean preped;
	public boolean read_only;
	public int count;
	public Vector<Value> initval;
	public String sysid;
	public boolean constSpecWritten;
	
	public VariableDeclaration(final boolean visibleflag,final Type type,final String identifier) {
		super(identifier,visibleflag);
		this.type=type;
    	defTag(visibleflag,identifier);
	}

    public static VariableDeclaration doParse(Type type,boolean visibleflag,boolean constflag) {
    	String identifier=Parser.expectIdentifier();
    	VariableDeclaration quant=new VariableDeclaration(visibleflag,type,identifier);
    	quant.count=1;
    	if(Parser.accept(KeyWord.SYSTEM)) {
    		quant.sysid=Parser.expectString();
    	}
    	if(Parser.accept(KeyWord.BEGPAR)) {
    		quant.count=Parser.expectIntegerConst().intValue();
    		Parser.expect(KeyWord.ENDPAR);
    	}
    	if(Parser.accept(KeyWord.EQ)) {
    		quant.initval=new Vector<Value>();
    		if(Parser.accept(KeyWord.BEGPAR)) {
    			do {
    				Value val=parseValue(constflag);
    				//IO.println("VariableDeclaration.doParse: val="+val);
    				quant.initval.add(val);
    				if(quant.initval.size() > quant.count) ERROR("Too many elts in repetition");
    			} while(Parser.accept(KeyWord.COMMA));

    			Parser.expect(KeyWord.ENDPAR);
     		}
    		else quant.initval.add(parseValue(constflag));

    	}
    	quant.read_only=constflag;
    	
		if(currentScope.isRoutine()) {
			// Quant_in_RoutineBody;
			if(quant.initval != null || quant.read_only) ERROR("Illegal declaration of " + quant.identifier+": initval="+quant.initval+", read_only="+quant.read_only);
			quant.kind=VarKind.LocalVariable;
		}
		else if(currentModule.isGlobalModule()) {
			// Quant_in_GlobalModule;
			if(quant.read_only)	quant.kind=VarKind.GlobalConstant;
			else quant.kind=VarKind.GlobalVariable;
		}
		else if(currentModule.isSubModule()) {
			// Quant_in_SubModule;	
			if(quant.visible) {
				if(quant.initval != null && quant.read_only) quant.kind=VarKind.GlobalConstant;
				else quant.kind=VarKind.GlobalVariable; 
			} else {
				if(quant.initval != null && quant.read_only) quant.kind=VarKind.SubModuleConstant;
				else quant.kind=VarKind.SubModuleVariable; 
			}
		} else {
			// Quant_in_MainProgram;	
			if(quant.initval != null && quant.read_only) quant.kind=VarKind.MainProgramConstant;
			else quant.kind=VarKind.MainProgramVariable;
		}
		Parser.TRACE("Quant.parse: ENDOF NEW QUANT: " + quant + ", currentToken="+Parser.currentToken+", prevToken="+Parser.prevToken);
		//Util.println("Quant.parse: ENDOF NEW QUANT: " + quant + ", currentToken="+Parser.currentToken+", prevToken="+Parser.prevToken);
		currentModule.checkDeclarationList();
		return(quant);
    }

	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		enterLine();//Global.sourceLineNumber = lineNumber;
//			Comn.enterScope(this);
			if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
			type.doChecking();
//			Comn.exitScope();
			SET_SEMANTICS_CHECKED();
		exitLine();
	}
	
	// ***********************************************************************************************
	// *** Coding: prepareSCodeOutput
	// ***********************************************************************************************
	public void prepareSCodeOutput() {
//		IO.println("VariableDeclaration.prepareSCodeOutput: "+this);
		if(type.isInfixType()) {
			Record rec=type.getQualifyingRecord();
			rec.doSCodeDeclaration();
		}
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeSpecCode
	// ***********************************************************************************************
	public void doSCodeSpecCode() {
		ASSERT_SEMANTICS_CHECKED(this);
//		if(IS_SCODE_EMITTED()) return;
//		SET_SCODE_EMITTED();  // TESTING
//		Comn.enterScope(this);
		enterLine();
////			output_S_LINE();
			prepareSCodeOutput();
			if(type.isInfixType()) {
				Record rec=type.getQualifyingRecord();
				if(!rec.sCodeWritten) rec.doSCodeSpecCode();
				if(!rec.sCodeWritten) ERROR("Declaration loop");
			}
			if(! constSpecWritten) {
				constSpecWritten = true;
				switch(kind) {
//					case LocalVariable:       doOutput(S_LOCAL); break;
				
//					case GlobalConstant:      output_S_LINE(); outConstant(); break;
					case GlobalConstant:      doOutput(S_CONSTSPEC); break; // TESTING
				
//					case GlobalVariable:      output_S_LINE(); doOutput(S_GLOBAL); break;
					case SubModuleConstant:	  doOutput(S_CONSTSPEC); break;
					//case SubModuleVariable:   currentModule.localdecl.add(this); break;
					case SubModuleVariable:   break; // Alredy output through 'currentModule.localdecl'
					case MainProgramConstant: doOutput(S_CONSTSPEC); break;
//					case MainProgramVariable: doOutput(S_LOCAL); break;
					default:
				}
			}
		exitLine();
//		Comn.exitScope();
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeDeclaration
	// ***********************************************************************************************
	public void doSCodeDeclaration() {
		ASSERT_SEMANTICS_CHECKED(this);
		if(IS_SCODE_EMITTED()) return;
		SET_SCODE_EMITTED();  // TESTING
//		Comn.enterScope(this);
		enterLine();
//			output_S_LINE();
			prepareSCodeOutput();
//			if(type.isInfixType()) {
//				Record rec=type.getQualifyingRecord();
//				if(!rec.sCodeWritten) rec.doSCodeDeclaration();
//				if(!rec.sCodeWritten) ERROR("Declaration loop");
//			}
			switch(kind) {
				case LocalVariable:       doOutput(S_LOCAL); break;
				
				case GlobalConstant:      output_S_LINE(); outConstant(); break;
//				case GlobalConstant:      doOutput(S_CONSTSPEC); break; // TESTING
				
				case GlobalVariable:      output_S_LINE(); doOutput(S_GLOBAL); break;
//				case SubModuleConstant:	  doOutput(S_CONSTSPEC); break;
//				//case SubModuleVariable:   currentModule.localdecl.add(this); break;
//				case SubModuleVariable:   break; // Alredy output through 'currentModule.localdecl'
//				case MainProgramConstant: doOutput(S_CONSTSPEC); break;
				case MainProgramVariable: doOutput(S_LOCAL); break;
				default:
					break;
			}
//			if(kind==VarKind.GlobalConstant) {
//				IO.println("VariableDeclaration.doSCodeDeclaration: kind="+kind+"   "+this);
//				Util.BREAK("");
//				Option.BREAKING=false;
//			}
		exitLine();
//		Comn.exitScope();
	}
	
	public void outConstant() {
		doOutput(S_CONST);
		if(sysid != null) ; // Nothing
		else if(initval == null) {
			type.toDefaultSCode();
		} else
			for(Value e:initval) {
				Type typ=e.doOutConst();
				if(typ != type) {
					Type res=Type.checkCompatible(typ,type);
					if(res==null) ERROR("Missing type conversion: "+typ+" --> "+type);
				}
			}
		initval=null;
		sCode.outcode();
	}
	
	static int count1=0;
	public void doOutput(int instr) {
		enterLine();
			sCode.outinst(instr); sCode.outtagid(getTag());
//			IO.println("Type="+type);
//			IO.println("VariableDeclaration: "+this+"    sysid="+sysid);
			this.type.toSCode();
			if(this.count != 1)  { sCode.outinst(S_REP); sCode.outnumber(this.count); }
			if(sysid != null && instr != S_CONSTSPEC) { sCode.outinst(S_SYSTEM); sCode.outstring(sysid); }
			sCode.outcode();
//			if(identifier.equalsIgnoreCase("xbuff")) {
//				if(count1++ > 0)STOP();
//			}
		exitLine();
	}

	public String toString() {
		StringBuilder s=new StringBuilder();
    	s.append("Line "+lineNumber+": ");
		if(visible) s.append("VISIBLE ");
		if(global) s.append("GLOBAL ");
		if(kind!=null) s.append(kind).append(' ');
		s.append(type).append(' ');
		s.append(identifier);
		if(sysid!=null) s.append('[').append(sysid).append(']');
		s.append("(tag=").append(getTag());
		if(read_only) s.append(",read_only ");
		s.append(",count=").append(count).append(')');
		if(initval!=null) s.append("  INITVAL=").append(initval);
		return(s.toString());
	}
	

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************

	public VariableDeclaration() { }
	
//	@Override
//	public void write(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Variable: "+identifier);
//		oupt.writeByte(Kind.kVariable);
//		oupt.writeShort(lineNumber);
//		oupt.writeIdent(identifier);
//		oupt.writeTag(getTag());
//		oupt.writeType(type);
//		oupt.writeBoolean(read_only);
//		oupt.writeShort(count);
//		//Util.TRACE_OUTPUT("END Write Variable: "+identifier);
//	}
//
//	public static VariableDeclaration createAndReadVariable(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		VariableDeclaration var=new VariableDeclaration();
//		var.lineNumber=inpt.readShort();
//		var.identifier=inpt.readIdent();
//		var.setTag(inpt.readTag());
//		var.type=inpt.readType();
//		var.read_only=inpt.readBoolean();
//		var.count=inpt.readShort();
//		Util.TRACE_INPUT("END Read VariableDeclaration: "+var);
//		return(var);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write Variable: "+identifier);
		out.writeShort(lineNumber);
		out.writeObject(identifier);
//		out.writeTag(getTag());
		getTag().writeTag(out);
//		IO.println("VariableDeclaration.writeExternal: "+this);
		out.writeObject(type);
		out.writeBoolean(read_only);
		out.writeShort(count);
		//Util.TRACE_OUTPUT("END Write Variable: "+identifier);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		lineNumber=in.readShort();
		identifier=(String) in.readObject();
		setTag(Tag.readTag(in));
		type=(Type) in.readObject();
		read_only=in.readBoolean();
		count=in.readShort();
		Util.TRACE_INPUT("END Read VariableDeclaration: "+this);
	}

}
