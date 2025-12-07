/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.designator;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.S_ASSPAR;
import static simuletta.compiler.common.S_Instructions.S_ASSREP;
import static simuletta.compiler.common.S_Instructions.S_POP;
import static simuletta.compiler.expression.Expression.getprim;
import static simuletta.compiler.expression.Expression.scan_expr;

import java.util.Vector;

import simuletta.compiler.declaration.Signatur;
import simuletta.compiler.declaration.VariableDeclaration;
import simuletta.compiler.expression.Expression;
import simuletta.compiler.expression.ExpressionList;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Util;

/**
 * SimpleDesignator.
 * 
 * <pre>
 * 
 * Syntax:
 *			SimpleDesignator  ::=  Identifier       <  ArgumentList  >?
 *							    |  VarExpression    <  ArgumentList  >?
 *							    |  CallExpression   <  ArgumentList  >?
 *
 *				VarExpression  ::=  VAR  (  GeneralReference'Expression  )
 *
 * 				CallExpression ::=  CALL  Profile'Identifier ( Entry'Expression )
 *
 *
 * 				ArgumentList ::=  (  Argument  < , Argument >*  )
 * 
 *					Argument ::=  Expression  |  ( Expression  <  ,  Expression  >*  ) 				
 * 
 * </pre>
 * @author Øystein Myhre Andersen
 */
public abstract class SimpleDesignator extends DesignatorElement {
//	public int lineNumber;    // From SyntaxClass
	public final Vector<Expression> argset;
	
	protected SimpleDesignator(Vector<Expression> argset) {
		this.argset=argset;
	}

	protected static Vector<Expression> parseArgumentSet() {
		Vector<Expression> argset=null;
		if(Parser.accept(KeyWord.BEGPAR)) {
			argset=new Vector<Expression>();
			do { argset.add(scan_expr(getprim()));
			} while(Parser.accept(KeyWord.COMMA));
			Parser.expect(KeyWord.ENDPAR);
		}
		return(argset);
	}

    public Type rutcall(Signatur spec,Vector<Expression> argset) {
    	//Util.TRACE("Designator.rutcall: Signatur="+spec);
    	int fpar=(spec.imports==null)?0:spec.imports.size();
    	int apar=(argset==null)?0:argset.size();
    	if(apar != fpar) ERROR("Wrong number of actual parameters: "+this);
    	if(argset != null) {
    		int i=1;
    		for(Expression a:argset)	{
    			VariableDeclaration par=(i>fpar)?null:(VariableDeclaration)spec.imports.elementAt(i-1); i++;
    			//println("SimpleDesignator.rutcall: BEGIN Parameter "+par);
    			if(par == null) {
    				a.doSCodingDirect(); //Expression.expression(ininstr());
    				sCode.outinst(S_POP); // Pop off extra parameter
    				sCode.outcode();
    			} else {
    				if(a instanceof ExpressionList) {
    					int nRep=((ExpressionList)a).exprset.size();
    					if(nRep > par.count) ERROR("Too many elts in repeated parameter: nRep="+nRep+", par.count="+par.count);
    					Type typ=a.doSCodingDirect(); //Expression.expression(b);
    					if(typ != par.type) Type.tstconv(typ,par.type);
        				sCode.outcode();
    					sCode.outinst(S_ASSREP); sCode.outbyt(nRep);
    				} else {
    					a.doSCodingDirect(); sCode.outcode();
    					sCode.outinst(S_ASSPAR);
    				}
    				sCode.outcode();
    				//Util.TRACE("Designator.rutcall: ASSPAR/ASSREP Done");
    			}
    			//Util.TRACE("Designator.rutcall: ENDOF Parameter "+i);
    		}
    	}
    	Util.TRACE("currentScope="+currentScope+", rutcall="+spec+", args="+argset);
    	if(currentScope!=null && currentScope.isRoutine() && spec.exit != null) ERROR("Illegal call on routine with spec. exit-label");
    	Type result=(spec.export==null)?null:((VariableDeclaration)spec.export).type;
    	return(result);
    }

	protected String edArgset() {
		StringBuilder s=new StringBuilder();
		if(argset != null) {
			boolean first=true;
			s.append('(');
			for(Expression x:argset) {
				if(!first) s.append(',');
				first=false; s.append(x);
			}
			s.append(')');
		}
		return(s.toString());
	}


}
