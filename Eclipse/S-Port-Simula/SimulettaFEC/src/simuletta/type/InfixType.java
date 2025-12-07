package simuletta.type;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import simuletta.utilities.KeyWord;
import simuletta.utilities.Util;

public class InfixType extends Type implements Externalizable {
	String qual;
	int repCount;
	
	public InfixType(String qual,int repCount) {
		super(KeyWord.INFIX);
		this.qual=(qual==null)?null:qual.toUpperCase();
		this.repCount=repCount;
		if(qual==null) Util.IERR("InfixType: missing qual");
	}
	
	@Override
	public boolean equals(final Object other) {
		if(other==null) return(false);
		if(other instanceof InfixType infix) {
			return(qual.equals(infix.qual) && repCount==infix.repCount);
		}
		return(false);
	}
	  
	@Override
	public String toString() {
		return("Infix("+qual+':'+repCount+')');
	}
	
	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************

	public InfixType() {}

//	@Override
//	public void writeType(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeByte(Kind.kInfix);
//		oupt.writeByte(keyWord);
//		oupt.writeIdent(qual);
//		oupt.writeShort(repCount);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readInfixType(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		InfixType infix=new InfixType();
//		infix.keyWord=inpt.readUnsignedByte();
//		infix.qual=inpt.readIdent();
//		infix.repCount=inpt.readShort();
//		return(infix);
//	}
//
//	@Override
//	public void writeType(ObjectOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeShort(Kind.kInfix);
//		oupt.writeByte(keyWord);
//		oupt.writeObject(qual);
//		oupt.writeShort(repCount);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readInfixType(ObjectInput inpt) throws IOException, ClassNotFoundException {
//		InfixType infix=new InfixType();
//		infix.keyWord=inpt.readUnsignedByte();
//		infix.qual=(String) inpt.readObject();
//		infix.repCount=inpt.readShort();
//		return(infix);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
		out.writeByte(keyWord);
		out.writeObject(qual);
		out.writeShort(repCount);
		//Util.TRACE_OUTPUT("END Write Type: "+this);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		keyWord=in.readUnsignedByte();
		qual=(String) in.readObject();
		repCount=in.readShort();
	}

}
