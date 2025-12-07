package simuletta.type;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import simuletta.utilities.KeyWord;
import simuletta.utilities.Util;

public class RefType extends Type implements Externalizable {
	String qual;
	
	public RefType(String qual) {
		super(KeyWord.REF);
		this.qual=(qual==null)?null:qual.toUpperCase();
	}
	
	@Override
	public boolean equals(final Object other) {
		if(other==null) return(false);
		if(other instanceof RefType ref) {
			if(qual==null) return(keyWord==ref.keyWord && ref.qual==null);
			return(keyWord==ref.keyWord && qual.equals(ref.qual));
		}
		return(false);
	}
	  
	@Override
	public String toString() {
		return("Ref("+qual+')');
	}
	
	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************

	public RefType() {}

//	@Override
//	public void writeType(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeByte(Kind.kRef);
//		oupt.writeByte(keyWord);
//		oupt.writeIdent(qual);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readRefType(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		RefType ref=new RefType();
//		ref.keyWord=inpt.readUnsignedByte();
//		ref.qual=inpt.readIdent();
//		return(ref);
//	}
//
//	@Override
//	public void writeType(ObjectOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeShort(Kind.kRef);
//		oupt.writeByte(keyWord);
//		oupt.writeObject(qual);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readRefType(ObjectInput inpt) throws IOException, ClassNotFoundException {
//		RefType ref=new RefType();
//		ref.keyWord=inpt.readUnsignedByte();
//		ref.qual=(String) inpt.readObject();
//		return(ref);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
		out.writeByte(keyWord);
		out.writeObject(qual);
		//Util.TRACE_OUTPUT("END Write Type: "+this);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		keyWord=in.readUnsignedByte();
		qual=(String) in.readObject();
	}

}
