package simuletta.type;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import simuletta.utilities.KeyWord;
import simuletta.utilities.Util;

public class NameType extends Type implements Externalizable {
	Type type;
	
	public NameType(Type type) {
		super(KeyWord.NAME);
		this.type=type;
	}
	
	@Override
	public boolean equals(final Object other) {
		if(other==null) return(false);
		if(other instanceof NameType name) {
			if(type==null) return(name.type==null);
			return(type.equals(name.type));
		}
		return(false);
	}
	  
	@Override
	public String toString() {
		return("Name("+type+')');
	}
	
	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************

	public NameType() {}

//	@Override
//	public void writeType(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeByte(Kind.kName);
//		oupt.writeByte(keyWord);
//		oupt.writeType(type);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readNameType(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		NameType name=new NameType();
//		name.keyWord=inpt.readUnsignedByte();
//		name.type=inpt.readType();
//		return(name);
//	}
//
//	@Override
//	public void writeType(ObjectOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeShort(Kind.kName);
//		oupt.writeByte(keyWord);
//		Type.writeType(type,oupt);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readNameType(ObjectInput inpt) throws IOException, ClassNotFoundException {
//		NameType name=new NameType();
//		name.keyWord=inpt.readUnsignedByte();
////		name.type=inpt.readType();
//		name.type=Type.readType(inpt);
//		return(name);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
		out.writeByte(keyWord);
		out.writeObject(type);
		//Util.TRACE_OUTPUT("END Write Type: "+this);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		keyWord=in.readUnsignedByte();
		type=(Type) in.readObject();
	}

}
