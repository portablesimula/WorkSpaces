/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.value;

import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.expression.Expression;

/**
 * @author Øystein Myhre Andersen
 */
public class AttributeValue extends Declaration {
	public String ident;
	public Expression value;
	public boolean matched; // Used during StructuredConst.outstruct

	public AttributeValue(String ident,Expression value) {
		this.ident=ident; this.value=value; this.matched=false;
	}
	

	public String toString() {
		return ("(ATTR_VALUE:" + ident + '=' + value + " Matched=" + matched + ")");
	}

}
