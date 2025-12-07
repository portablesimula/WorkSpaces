/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.declaration;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import simuletta.utilities.Option;
import simuletta.utilities.Tag;
import simuletta.utilities.Util;

public class LabelDeclaration extends Declaration implements Externalizable {
//	public int lineNumber;    // From SyntaxClass
//	public String identifier; // From Declaration
//	public Tag tag;  		  // From Declaration
//	public boolean global;    // From Declaration
//	public boolean visible;   // From Declaration
	public int destinationIndex;  // destination index for local label.
    
    // ***********************************************************************************************
    // *** Constructor
    // ***********************************************************************************************
    public LabelDeclaration(final boolean visibleflag,final String identifier) {
    	super(identifier,visibleflag);
    	defTag(visibleflag,identifier);
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
	// *** Coding: doSCodeSpecCode
	// ***********************************************************************************************
    public void doSCodeSpecCode() {
		ASSERT_SEMANTICS_CHECKED(this);
//		if(IS_SCODE_EMITTED()) return;
//		Comn.enterScope(this);
		enterLine();
			output_S_LINE();
			if(global) {
				sCode.outinst(S_LABELSPEC); sCode.outtagid(getTag());
				sCode.outcode();
			}
        exitLine();
//		Comn.exitScope();
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeDeclaration
	// ***********************************************************************************************
	public void doSCodeDeclaration() {
//		ASSERT_SEMANTICS_CHECKED(this);
//		if(IS_SCODE_EMITTED()) return;
////		Comn.enterScope(this);
//		enterLine();
//			output_S_LINE();
//			if(global) {
//				sCode.outinst(S_LABELSPEC); sCode.outtagid(getTag());
//				sCode.outcode();
//			}
//        exitLine();
////		Comn.exitScope();
	}

	public String toString() {
		return("Line "+lineNumber+": Global Label: visible="+visible+", identifier="+identifier);
	}
	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	
	public LabelDeclaration() { }

//	@Override
//	public void write(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write LabelDeclaration: "+identifier);
//		oupt.writeByte(Kind.kLabel);
//		oupt.writeShort(lineNumber);
//		oupt.writeIdent(identifier);
//		getTag().writeTag(oupt);
//		//Util.TRACE_OUTPUT("END Write LabelDeclaration: "+identifier);
//	}
//
//	public static LabelDeclaration createAndReadLabel(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		LabelDeclaration var=new LabelDeclaration();
//		var.lineNumber=inpt.readShort();
//		var.identifier=inpt.readIdent();
//		var.setTag(inpt.readTag());
//		Util.TRACE_INPUT("END Read LabelDeclaration: "+var);
//		return(var);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write LabelDeclaration: "+identifier);
		out.writeShort(lineNumber);
		out.writeObject(identifier);
		getTag().writeTag(out);
		//Util.TRACE_OUTPUT("END Write LabelDeclaration: "+identifier);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		lineNumber=in.readShort();
		identifier=(String) in.readObject();
		setTag(Tag.readTag(in));
		Util.TRACE_INPUT("END Read LabelDeclaration: "+this);
	}
	
}
