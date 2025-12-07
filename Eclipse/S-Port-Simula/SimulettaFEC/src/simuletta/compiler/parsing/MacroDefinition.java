/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.parsing;

import static simuletta.compiler.Global.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

import simuletta.compiler.Global;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Token;
import simuletta.utilities.Util;

/**
 * Macro Definition.
 * 
 * <pre>
 * Syntax:
 * 
 * 			MacroDefinition ::= MACRO macro'Identifier ( parcount'IntegerNumber )
 * 								BEGIN < MacroElement >* ENDMACRO
 * 
 * 				MacroElement ::= any BasicSymbol except ENDMACRO and %
 * 							   | % parnumber'IntegerNumber
 * </pre>
 *
 * @author Øystein Myhre Andersen
 *
 */
public class MacroDefinition extends Mnemonic implements Externalizable {
	//public String identifier; // Inherited: Mnemonic
	public int nPar;
	public Vector<Token> elts=new Vector<Token>();

	public int lastLineNumber;

	// ***********************************************************************************************
	// *** CONSTRUCTORS
	// ***********************************************************************************************
	public MacroDefinition(final boolean visible) {
		duringMacroDefinition=true;
		identifier=Parser.expectIdentifier().toUpperCase();
		SimulettaScanner.defineMnemonic(identifier.toUpperCase(),this);
		Parser.expect(KeyWord.BEGPAR);
		nPar=Parser.expectIntegerConst().intValue();
		Parser.expect(KeyWord.ENDPAR);
		Parser.expect(KeyWord.BEGIN);
		Util.BREAK("Declaration.doParseMacroDefinition: ident="+identifier+", nPar="+nPar);
		while(!Parser.accept(KeyWord.ENDMACRO)) {
			Token current=Parser.currentToken;
			Util.BREAK("Declaration.doParseMacroDefinition: Current Token="+current);
			if(current.getKeyWord()==KeyWord.PERCENT) {
				Parser.nextSymb();
				Long parNumber=Parser.expectIntegerConst();
				Util.BREAK("Declaration.doParseMacroDefinition: parNumber="+parNumber+", Current Token="+Parser.currentToken);
				current.putValue(parNumber);
				Util.BREAK("Declaration.doParseMacroDefinition: ADD Token="+current);
				elts.add(current);
			} else {
				Util.BREAK("Declaration.doParseMacroDefinition: ADD Token="+current);
				elts.add(current); Parser.nextSymb();
			}
		}
		lastLineNumber = Global.sourceLineNumber;
		if (Option.TRACE_PARSE)	Util.TRACE("NEW MacroDefinition: " + this);
		Util.BREAK("NEW MacroDefinition: " + this);
		if(Option.TRACE_MACRO_SCAN) IO.println("NEW MacroDefinition: " + this);
		duringMacroDefinition=false;
	}
	

	public String toString() {
		return ("MACRO " + identifier + '(' + nPar + ") TOKENS=" + elts);
	}

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	
	public MacroDefinition() { }
	
//	@Override
//	public void write(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write MacroDefinition: "+identifier);
//		oupt.writeByte(Kind.kMacro);
//		oupt.writeIdent(identifier);
//		oupt.writeShort(nPar);
//		oupt.writeShort(elts.size()); // nToken
//		for(Token token:elts) {
//			oupt.writeToken(token);
//		}
//		//Util.TRACE_OUTPUT("END Write MacroDefinition: "+identifier);
//	}
//
//	public static MacroDefinition readMacro(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		//Util.TRACE_OUTPUT("BEGIN Read LiteralMnemonic: "+identifier);
//		MacroDefinition macro=new MacroDefinition();
//		macro.identifier=inpt.readIdent();
//		macro.nPar=inpt.readShort();
//		int nToken=inpt.readShort();
//		for(int i=0;i<nToken;i++) macro.elts.add(inpt.readToken());
//		//Util.TRACE_OUTPUT("END Read LiteralMnemonic: "+identifier);
//		return(macro);
//	}


	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write MacroDefinition: "+identifier);
		out.writeObject(identifier);
		out.writeShort(nPar);
		out.writeShort(elts.size()); // nToken
		for(Token token:elts) {
			token.writeToken(out);
		}
		//Util.TRACE_OUTPUT("END Write MacroDefinition: "+identifier);
	}


	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		identifier=(String) in.readObject();
		nPar=in.readShort();
		int nToken=in.readShort();
		for(int i=0;i<nToken;i++) elts.add(Token.readToken(in));
		//Util.TRACE_OUTPUT("END Read LiteralMnemonic: "+identifier);
	}

}
