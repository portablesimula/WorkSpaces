package simuletta.compiler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import simuletta.type.Type;
import simuletta.utilities.Tag;
import simuletta.utilities.Token;

public class AttributeOutput {
	public static boolean TESTING=false;//true;//false;
	public static final boolean USE_SYNCMARK=true;//false;
	public static final boolean USE_BINARY_floating_IO=true;//false;
	
//	private final OutputStream out;
    private final byte[] writeBuffer = new byte[8];
	private final DataOutputStream out;

	private AttributeOutput(OutputStream out) {
		this.out=new DataOutputStream(out);
	}

	
//    public final void writeBoolean(boolean v) throws IOException {
//    	if(TESTING) IO.println("AttributeDataOutputStream.writeBoolean: "+v);
//        out.write(v ? 1 : 0);
//    }
//
//	
//    public final void writeByte(int v) throws IOException {
//    	if(TESTING) IO.println("AttributeDataOutputStream.writeByte: "+v);
//        out.write(v);
//    }
//
//	
//    public final void writeShort(int v) throws IOException {
//    	if(TESTING) IO.println("AttributeDataOutputStream.writeShort: "+v);
////        writeBuffer[0] = (byte)(v >>> 8);
////        writeBuffer[1] = (byte)(v >>> 0);
////        out.write(writeBuffer, 0, 2);
//        out.writeShort(v);
//    }
//
//	
//    public final void writeChar(int v) throws IOException {
////        writeBuffer[0] = (byte)(v >>> 8);
////        writeBuffer[1] = (byte)(v >>> 0);
////        out.write(writeBuffer, 0, 2);
//        out.writeChar(v);
//    }
//
//	
//    public final void writeInt(int v) throws IOException {
//		if(TESTING) IO.println("AttributeDataOutputStream.writeInt: "+v);
////        writeBuffer[0] = (byte)(v >>> 24);
////        writeBuffer[1] = (byte)(v >>> 16);
////        writeBuffer[2] = (byte)(v >>>  8);
////        writeBuffer[3] = (byte)(v >>>  0);
////        out.write(writeBuffer, 0, 4);
//        out.writeInt(v);
//    }
//
//	
//    public final void writeLong(long v) throws IOException {
////        writeBuffer[0] = (byte)(v >>> 56);
////        writeBuffer[1] = (byte)(v >>> 48);
////        writeBuffer[2] = (byte)(v >>> 40);
////        writeBuffer[3] = (byte)(v >>> 32);
////        writeBuffer[4] = (byte)(v >>> 24);
////        writeBuffer[5] = (byte)(v >>> 16);
////        writeBuffer[6] = (byte)(v >>>  8);
////        writeBuffer[7] = (byte)(v >>>  0);
////        out.write(writeBuffer, 0, 8);
//        out.writeLong(v);
//    }
//
//	
//    public final void writeFloat(float v) throws IOException {
//		if(USE_BINARY_floating_IO)
//			 writeInt(Float.floatToIntBits(v));
//		else writeString(""+v);
//    }
//
//	
//    public final void writeDouble(double v) throws IOException {
//		if(USE_BINARY_floating_IO)
//			 writeLong(Double.doubleToLongBits(v));
//		else writeString(""+v);
//    }
//
//
//	
//    public final void writeByte(int v) throws IOException {
//    	if(TESTING) IO.println("AttributeDataOutputStream.writeByte: "+v);
//    	if(USE_SYNCMARK) {
//            out.write(91);
//            out.write(92);
//    	}
//        out.write(v);
//    }
//
//	
//	public void writeIdent(String s) throws IOException {
//		if(TESTING) IO.println("AttributeDataOutputStream.writeIdent: \""+s+'"');
//		Util.NOTIMPL("External I/O");
//        int len = (s==null)?0:s.length();
//        if(len > 255) throw new IllegalArgumentException("Too lomg identifier");
//        writeByte(len);
//        for (int i = 0 ; i < len ; i++) {
//            int v = s.charAt(i);
//            writeBuffer[0] = (byte)(v >>> 8);
//            writeBuffer[1] = (byte)(v >>> 0);
//            out.write(writeBuffer, 0, 2);
//        }
//	}
//
//	
//	public void writeString(String s) throws IOException {
//		if(TESTING) IO.println("AttributeDataOutputStream.writeString: \""+s+'"');
//		Util.NOTIMPL("External I/O");
//        int len = s.length();
//        writeShort(len);
//        for (int i = 0 ; i < len ; i++) {
//            int v = s.charAt(i);
//            writeBuffer[0] = (byte)(v >>> 8);
//            writeBuffer[1] = (byte)(v >>> 0);
//            out.write(writeBuffer, 0, 2);
//        }
//	}
//
//	
//	public void writeTag(Tag tag) throws IOException {
//    	if(TESTING) IO.println("AttributeOutputStream.writeTag: "+tag);
//		tag.writeTag(this);
//	}
//
//	
//	public void writeType(Type type) throws IOException {
//    	if(TESTING) IO.println("AttributeOutputStream.writeType: "+type);
//		if(type==null) Type.writeNullType(this);
//		else type.writeType(this);
//	}
//
//	
//	public void writeToken(Token token) throws IOException {
//    	if(TESTING) IO.println("AttributeOutputStream.writeToken: "+token.edToken());
//		token.writeToken(this);
//	}
//
//	
//	public void writeSyncMark(String mrk) throws IOException {
//    	if(USE_SYNCMARK) 
//    		writeIdent(mrk);
//	}
//
//	
//	public void close() throws IOException {
//		out.flush();
//		out.close();
//	}

}
