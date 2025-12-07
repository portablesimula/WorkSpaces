/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.value;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;

import java.util.Vector;

import simuletta.type.Type;

/**
 * @author Øystein Myhre Andersen
 */
public class RepeatedConst extends Value {
	public Vector<Value> values;
	
	public RepeatedConst() {}

	// ***********************************************************************************************
	// *** Coding: doOutConst
	// ***********************************************************************************************
	public Type doOutConst() {
		enterLine();
			boolean first=true;
			Type type,type1 = null;
			for(Value val:values) {
				type=val.doOutConst(); sCode.outcode();
				if(first) type1=type; first=false;
				if(Type.checkCompatible(type1,type)==null)
					ERROR("Incompatible types in repeated constant: "+type1+" and "+type);
			}
		exitLine();
		return(type1);
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("RepeatedConst");
		char sep='(';
		for(Value val:values) {
			sb.append(sep).append(val);
			sep=',';
		}
		sb.append(')');
		return(sb.toString());
	}

}
