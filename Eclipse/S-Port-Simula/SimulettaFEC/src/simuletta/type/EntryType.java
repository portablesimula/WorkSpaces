package simuletta.type;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import simuletta.utilities.KeyWord;
import simuletta.utilities.Util;

public class EntryType extends Type implements Externalizable {
	String qual;
	
	public EntryType(String qual) {
		super(KeyWord.ENTRY);
		this.qual=(qual==null)?null:qual.toUpperCase();
	}
	
	@Override
	public boolean equals(final Object other) {
		if(other==null) return(false);
		if(other instanceof EntryType entry) {
			return(qual.equals(entry.qual));
		}
		return(false);
	}
	  
	@Override
	public String toString() {
		return("Entry("+qual+')');
	}
	
	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************

	public EntryType() {}

//	@Override
//	public void writeType(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeByte(Kind.kEntry);
//		oupt.writeByte(keyWord);
//		oupt.writeIdent(qual);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readEntryType(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		EntryType entry=new EntryType();
//		entry.keyWord=inpt.readUnsignedByte();
//		entry.qual=inpt.readIdent();
//		return(entry);
//	}
//
//	@Override
//	public void writeType(ObjectOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeShort(Kind.kEntry);
//		oupt.writeByte(keyWord);
//		oupt.writeObject(qual);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readEntryType(ObjectInput inpt) throws IOException, ClassNotFoundException {
//		EntryType entry=new EntryType();
//		entry.keyWord=inpt.readUnsignedByte();
//		entry.qual=(String) inpt.readObject();
//		return(entry);
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
