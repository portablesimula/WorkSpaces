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

import java.util.Vector;

import simuletta.compiler.declaration.scope.Record;
import simuletta.compiler.expression.Expression;
import simuletta.compiler.expression.designator.Designator;
import simuletta.compiler.expression.designator.DesignatorElement;
import simuletta.compiler.expression.designator.Variable;
import simuletta.compiler.expression.value.SimpleValue;
import simuletta.type.Type;
import simuletta.utilities.Option;
import simuletta.utilities.Util;
import simuletta.utilities.VarSet;

// NOTE: See also  sysr.sml   Line 100 +/-
// NOTE: See also  libr.sml   Line 30  +/-
//
public class InlineRoutine extends Declaration {
//	public int lineNumber;    // From SyntaxClass
//	public String identifier; // From Declaration
//	public Tag tag;  		  // From Declaration
//	public boolean global;    // From Declaration
//	public boolean visible;   // From Declaration
	Kind kind;
	
	public enum Kind {
		ZEROAREA,INITAREA,DINITAREA,INITO,GETO,SETO,MAXTEMPS,PUSHLEN,
		SETOBJ,GETOBJ,DSIZE,REF,FIELD
	};


	public InlineRoutine(String ident,Kind kind) {
		super(ident,false);
		this.kind=kind;
    	defTag(false,ident);
	}
	
	public boolean isFunction() {
        switch(kind) {
		case DINITAREA: return(true);
//		case DSIZE:		return(true);
		case FIELD:		return(true);
		case GETO:		return(true);
		case GETOBJ:	return(true);
		case INITAREA:	return(true);
//		case INITO:		return(true);
		case MAXTEMPS:	return(true);
		case PUSHLEN:	return(true);
		case REF:		return(true);
//		case SETO:		return(true);
//		case SETOBJ:	return(true);
		case ZEROAREA:	return(true);
		default:		return(false);
        }
	}
	
	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		enterLine();//Global.sourceLineNumber = lineNumber;
//		Comn.enterScope(this);
		if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
//		Comn.exitScope();
		SET_SEMANTICS_CHECKED();
		exitLine();
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeDeclaration
	// ***********************************************************************************************
	public void doSCodeDeclaration() {
		ASSERT_SEMANTICS_CHECKED(this);
		if(IS_SCODE_EMITTED()) return;
//		Comn.enterScope(this);
		enterLine();
			output_S_LINE();
		exitLine();
//		Comn.exitScope();
	}

	// ***********************************************************************************************
	// *** Coding: callSystemFunction
	// ***********************************************************************************************
    public Type callSystemFunction(Vector<Expression> argset) {
        Type result=null;
          switch(kind) {
          case GETOBJ:
                invalue(argset,Type.Integer);
                sCode.outinst(S_GETOBJ); sCode.outcode();
                result=Type.Ref; break;

          case SETOBJ:
                invalue(argset,Type.Ref); invalue(argset,Type.Integer);
                sCode.outinst(S_SETOBJ); sCode.outcode();
                break;

          case DSIZE:
                String id=inid(argset);
//                Record record=getRecord(id);
                Record record=(Record) Declaration.findMeaning(id);
                invalue(argset,Type.Integer);
                if(!record.indefinite) ERROR("Illegal record in rec_size");
                sCode.outinst(S_DSIZE); sCode.outtag(record.getTag()); sCode.outcode();
                result=Type.Size; break;

          case INITO:
                invalue(argset,Type.Ref);
                sCode.outinst(S_T_INITO); sCode.outcode();
                break;

          case GETO:
                sCode.outinst(S_T_GETO); sCode.outcode();
                result=Type.Ref; break;

          case SETO:
                invalue(argset,Type.Ref);
                sCode.outinst(S_T_SETO); sCode.outcode();
                break;

          case MAXTEMPS:
          case PUSHLEN:
                result=Type.Size;
                sCode.outinst(S_PUSHLEN); sCode.outcode();
                break;

          case ZEROAREA:
                invalue(argset,Type.Ref); invalue(argset,Type.Ref);
                sCode.outinst(S_ZEROAREA); sCode.outcode();
                break;

          case INITAREA:
                id=inid(argset);
                invalue(argset,Type.Ref);
                sCode.outinst(S_INITAREA);
//                record=getRecord(id);
                record=(Record) Declaration.findMeaning(id);
                sCode.outtag(record.getTag()); sCode.outcode();
                break;

          case DINITAREA:
                id=inid(argset);
                int fixrep=grabFixrep(argset,Type.Integer);
                invalue(argset,Type.Ref);
                sCode.outinst(S_DINITAREA);
                record=(Record) Declaration.findMeaning(id);
                sCode.outtag(record.getTag());
                sCode.outinst(S_FIXREP); sCode.outnumber(fixrep); sCode.outcode();
                break;

          case REF:
                invalue(argset,Type.Name);
                sCode.outinst(S_CONVERT); sCode.outtype(Type.Ref); sCode.outcode();
                result=Type.Ref; break;

          case FIELD:
                invalue(argset,Type.Name);
                sCode.outinst(S_CONVERT); sCode.outtype(Type.Field); sCode.outcode();
                result=Type.Field; break;

            default: IERR();
          }

          if(argset!=null && argset.size() != 0) {
        	  ERROR("Wrong no. of arguments to " + identifier);
          }
          return(result);
    }

	
	private int grabFixrep(Vector<Expression> argset,Type type) {
//		IO.println("InlineRoutine.grabFixrep: "+argset+", size="+argset.size());
		if(argset.size() != 2) IERR("");
		Expression arg=argset.remove(0);
//		IO.println("InlineRoutine.grabFixrep: "+arg.getClass().getSimpleName()+"  "+arg);
		if(arg instanceof SimpleValue val) {
			if(val.instr!= S_C_INT) IERR("Fixrep is not Integer'number");
			sCode.outinst(S_PUSHC); sCode.outinst(S_C_INT); sCode.outstring(val.value); sCode.outcode();
			return(Integer.parseInt(val.value));
		} else IERR("Fixrep-Expression is not supported");
		return(0);
	}
	
	private void invalue(Vector<Expression> argset,Type type) {
		Type t2;
		if(argset.size() > 0) {
			Expression arg=argset.remove(0);
			t2=arg.doSCodingDirect();
			if(t2 !=type) Type.tstconv(t2,type);
		}
		else { sCode.outinst(S_PUSHC); type.toDefaultSCode(); }
	}

	
	private String inid(Vector<Expression> argset) {
		if(argset.size() > 0) {
			Expression arg=argset.remove(0);
			Designator d=(Designator)arg;
			VarSet varset=d.varset;
			Util.TRACE("arg="+arg+", QUAL="+arg.getClass().getSimpleName());
			Util.TRACE("varset="+varset+", QUAL="+varset.getClass().getSimpleName());
			if(varset.size()!=1) ERROR("Wrong kind of parameter to " + this.identifier);
			DesignatorElement elt=varset.firstElement();
			Util.TRACE("elt="+elt+", QUAL="+elt.getClass().getSimpleName());
			Variable var=(Variable)elt;
			return(var.identifier);
		}
        return(null);
	}

}
