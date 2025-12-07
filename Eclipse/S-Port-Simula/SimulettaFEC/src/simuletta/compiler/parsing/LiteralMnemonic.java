/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.parsing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import simuletta.utilities.Token;
import simuletta.utilities.Util;

public class LiteralMnemonic extends Mnemonic implements Externalizable {
	//public String identifier; // Inherited: Mnemonic
	public Token token;
	
	public LiteralMnemonic(String identifier,Token token) {
		this.identifier=identifier.toUpperCase();
		this.token=token;
	}

	public String toString() {
		return ("LITERAL " + identifier + '=' + token);
	}

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	
	public LiteralMnemonic() { }

//	@Override
//	public void write(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write LiteralMnemonic: "+identifier);
//		oupt.writeByte(Kind.kLiteral);
//		oupt.writeIdent(identifier);
//		oupt.writeToken(token);
//		//Util.TRACE_OUTPUT("END Write LiteralMnemonic: "+identifier);
//	}
//
//	public static LiteralMnemonic readLiteral(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		//Util.TRACE_OUTPUT("BEGIN Reaf LiteralMnemonic: "+identifier);
//		LiteralMnemonic mnemonic=new LiteralMnemonic();
//		mnemonic.identifier=inpt.readIdent();
//		mnemonic.token=inpt.readToken();
//		//Util.TRACE_INPUT("END Read LiteralMnemonic: "+mnemonic);
//		return(mnemonic);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write LiteralMnemonic: "+identifier);
		out.writeObject(identifier);
		token.writeToken(out);
		//Util.TRACE_OUTPUT("END Write LiteralMnemonic: "+identifier);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		identifier=(String) in.readObject();
		token=Token.readToken(in);
		//Util.TRACE_INPUT("END Read LiteralMnemonic: "+mnemonic);
	}

}
