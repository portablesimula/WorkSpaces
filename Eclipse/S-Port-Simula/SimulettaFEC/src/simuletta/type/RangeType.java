package simuletta.type;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import simuletta.utilities.Util;

public class RangeType extends Type implements Externalizable {
	int lower,upper;
	
	public RangeType(int keyWord,int lower,int upper) {
		super(keyWord);
		this.lower=lower;
		this.upper=upper;
	}
	
	@Override
	public boolean equals(final Object other) {
		if(other==null) return(false);
		if(other instanceof RangeType range) {
			return(lower==range.lower && upper==range.upper);
		}
		return(false);
	}
	  
	@Override
	public String toString() {
		return("Range("+lower+':'+upper+')');
	}
	
	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************

	public RangeType() {}

//	@Override
//	public void writeType(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeByte(Kind.kRange);
//		oupt.writeByte(keyWord);
//		oupt.writeShort(lower);
//		oupt.writeShort(upper);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readRangeType(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		RangeType range=new RangeType();
//		range.keyWord=inpt.readUnsignedByte();
//		range.lower=inpt.readShort();
//		range.upper=inpt.readShort();
//		return(range);
//	}
//
//	@Override
//	public void writeType(ObjectOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeShort(Kind.kRange);
//		oupt.writeByte(keyWord);
//		oupt.writeShort(lower);
//		oupt.writeShort(upper);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readRangeType(ObjectInput inpt) throws IOException, ClassNotFoundException {
//		RangeType range=new RangeType();
//		range.keyWord=inpt.readUnsignedByte();
//		range.lower=inpt.readShort();
//		range.upper=inpt.readShort();
//		return(range);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
		out.writeByte(keyWord);
		out.writeShort(lower);
		out.writeShort(upper);
		//Util.TRACE_OUTPUT("END Write Type: "+this);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		keyWord=in.readUnsignedByte();
		lower=in.readShort();
		upper=in.readShort();
	}

}
