/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression;

import java.util.Vector;

import simuletta.type.Type;

/**
 * Expression List.
 * 
 * <pre>
 * Syntax:
 * 
 *	expression_list 
 *		::= ( expression < , expression >* )
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class ExpressionList extends Expression {
//	public int lineNumber;    // From SyntaxClass
	public Vector<Expression> exprset;
	
	public ExpressionList(Vector<Expression> exprset) {
		this.exprset=exprset;
	}

	public Type doSCodingDirect() {
		enterLine();
			Type type1=null;
			for(Expression x:exprset) {
				Type typ=x.doSCodingDirect();
				if(type1==null) type1=typ; else Type.tstconv(typ,type1);
			}
		exitLine();
        return(type1);
	}
	
	public String toString() {
		StringBuilder s=new StringBuilder();
		s.append('(');
		boolean first=true;
		for(Expression e:exprset) {
			if(!first) s.append(','); first=false;
			s.append(e);
		}
		s.append(')');
		return(s.toString());
	}
}
