package simuletta.type;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import simuletta.utilities.KeyWord;
import simuletta.utilities.Util;

public class FieldType extends Type implements Externalizable {
	Type type;
	
	public FieldType(Type type) {
		super(KeyWord.FIELD);
		this.type=type;
	}
	
	@Override
	public boolean equals(final Object other) {
		if(other==null) return(false);
		if(other instanceof FieldType field) {
			if(type==null) return(field.type==null);
			return(type.equals(field.type));
		}
		return(false);
	}
	  
	@Override
	public String toString() {
		return("Field("+type+')');
	}
	
	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************

	public FieldType() {}

//	@Override
//	public void writeType(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeByte(Kind.kField);
//		oupt.writeByte(keyWord);
//		oupt.writeType(type);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readFieldType(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		FieldType field=new FieldType();
//		field.keyWord=inpt.readUnsignedByte();
//		field.type=inpt.readType();
//		return(field);
//	}
//
//	@Override
//	public void writeType(ObjectOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeShort(Kind.kField);
//		oupt.writeByte(keyWord);
//		type.writeType(oupt);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readFieldType(ObjectInput inpt) throws IOException, ClassNotFoundException {
//		FieldType field=new FieldType();
//		field.keyWord=inpt.readUnsignedByte();
//		field.type=Type.readType(inpt);
//		return(field);
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
