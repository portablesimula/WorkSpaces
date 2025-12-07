/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.declaration;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

public class Variant implements Externalizable {
	public Vector<Declaration> atrset;
	
	

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	public Variant() { }

	
	// ***********************************************************************************************
	// *** Coding: prepareSCodeOutput
	// ***********************************************************************************************
	public void prepareSCodeOutput() {
		for(Declaration a:atrset) a.prepareSCodeOutput();
	}
	
	public String toString() {
		String s="Variant ";
		for(Declaration atr:atrset) s=s+atr.identifier+" ";
		return(s);
	}

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	
//	public void writeVariant(AttributeOutput oupt) throws IOException {
//		oupt.writeShort(atrset.size()); // nAttr
//		for(Declaration decl:atrset) decl.write(oupt);
//	}
//	
//	public static Variant createAndReadVariant(AttributeInput inpt) throws ClassNotFoundException, IOException {
//		Variant variant=new Variant();
//		int nAttr=inpt.readShort();
//		if(nAttr>0) variant.atrset=new Vector<Declaration>();
//		for(int i=0;i<nAttr;i++) variant.atrset.add(Declaration.createAndRead(inpt));
//		return(variant);
//	}


	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(atrset);
	}


	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		atrset=(Vector<Declaration>) in.readObject();
	}

}
