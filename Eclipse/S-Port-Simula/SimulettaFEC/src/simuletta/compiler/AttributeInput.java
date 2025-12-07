package simuletta.compiler;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import simuletta.type.Type;
import simuletta.utilities.Tag;
import simuletta.utilities.Token;
import simuletta.utilities.Util;

public class AttributeInput {
	public static boolean TESTING=false;//true;//false;
	private String name;
//	private final InputStream in;
	private final DataInputStream in;
	
    private AttributeInput(String name,InputStream in) {
    	this.name=name;
        this.in=new DataInputStream(in);
    }

	
//    public final boolean readBoolean() throws IOException {
//        int ch = in.read();
//    	if(TESTING) IO.println("AttributeDataInputStream.readBoolean: "+ch);
//        if (ch < 0) throw new EOFException();
//        return (ch != 0);
//    }
//
//	
//    public final byte readUnsignedByte() throws IOException {
//        int ch = in.read();
//        if (ch < 0) throw new EOFException();
//    	if(TESTING) IO.println("AttributeDataInputStream.readUnsignedByte: "+ch);
//        return (byte)(ch);
//    }
//
////    public final int readUnsignedByte() throws IOException {
////        int ch = in.read();
////        if (ch < 0)
////            throw new EOFException();
////        return ch;
////    }
//
//	
//    public final short readShort() throws IOException {
//        int ch1 = in.read();
//        int ch2 = in.read();
//        if ((ch1 | ch2) < 0) throw new EOFException();
//        short s=(short)((ch1 << 8) + (ch2 << 0));
//    	if(TESTING) IO.println("AttributeDataInputStream.readShort: "+s);
//        return(s);
//    }
//
//	
//    public final char readChar() throws IOException {
////        int ch1 = in.read();
////        int ch2 = in.read();
////        if ((ch1 | ch2) < 0) throw new EOFException();
////        char c=(char)((ch1 << 8) + (ch2 << 0));
////        return(c);
//		return(in.readChar());
//    }
//
//	
//    public final int readInt() throws IOException {
////        int ch1 = in.read();
////        int ch2 = in.read();
////        int ch3 = in.read();
////        int ch4 = in.read();
////        if ((ch1 | ch2 | ch3 | ch4) < 0)
////            throw new EOFException();
////        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
//		return(in.readInt());
//    }
//
//    private byte readBuffer[] = new byte[8];
////    private final void readFully(byte b[], int off, int len) throws IOException {
////        Objects.checkFromIndexSize(off, len, b.length);
////        int n = 0;
////        while (n < len) {
////            int count = in.read(b, off + n, len - n);
////            if (count < 0)
////                throw new EOFException();
////            n += count;
////        }
////    }
//
//	
//	public final long readLong() throws IOException {
//        in.readFully(readBuffer, 0, 8);
//        return (((long)readBuffer[0] << 56) +
//                ((long)(readBuffer[1] & 255) << 48) +
//                ((long)(readBuffer[2] & 255) << 40) +
//                ((long)(readBuffer[3] & 255) << 32) +
//                ((long)(readBuffer[4] & 255) << 24) +
//                ((readBuffer[5] & 255) << 16) +
//                ((readBuffer[6] & 255) <<  8) +
//                ((readBuffer[7] & 255) <<  0));
//    }
//
//	
//	public float readFloat() throws IOException, ClassNotFoundException {
//		if(AttributeOutput.USE_BINARY_floating_IO)
//			 return(Float.intBitsToFloat(in.readInt()));
//		else return(Float.valueOf(readString()));
//	}
//
//	
//	public double readDouble() throws IOException, ClassNotFoundException {
//		if(AttributeOutput.USE_BINARY_floating_IO)
//			 return(Double.longBitsToDouble(in.readLong()));
//		else return(Float.valueOf(readString()));
//	}
//
//    private int kind=0;
//	
//    public final byte readUnsignedByte() throws IOException {
//    	if(AttributeOutput.USE_SYNCMARK) {
//            int b1=in.read();
//            int b2=in.read();
//            if(b1!=91 || b2!=92)     OUT_OF_SYNC(b1,b2);
//    	}
//        kind = in.read();
//        if (kind < 0) throw new EOFException();
//    	if(TESTING)
//    		IO.println("AttributeDataInputStream.readUnsignedByte: "+kind);
//        return (byte)(kind);
//    }
//    
//    private void OUT_OF_SYNC(int b1,int b2) throws IOException {
//    	IO.println("**************************************** ERROR: OUT OF SYNC WHILE READING "+name+": prevKind="+kind);
//    	IO.println("   b1:   "+b1);
//    	IO.println("   b2:   "+b2);
//    	IO.println("   NEXT: "+in.read());
//    	IO.println("   NEXT: "+in.read());
//    	IO.println("   NEXT: "+in.read());
//    	IO.println("   NEXT: "+in.read());
//    	IO.println("   NEXT: "+in.read());
//    	IO.println("   NEXT: "+in.read());
//    	IO.println("   NEXT: "+in.read());
//    	Util.IERR("OUT OF SYNC: prevKind="+kind);
//    }
//
//	
//	public String readIdent() throws IOException {
//		StringBuilder sb=new StringBuilder();
//        int len = readUnsignedByte();
//        for (int i = 0 ; i < len ; i++) {
//        	sb.append(readChar());
//        }
//        String s=sb.toString();
//        if(TESTING) IO.println("AttributeDataInputStream.readIdent: "+s);
//    	return(s);
//	}
//
//	
//	public String readString() throws IOException {
//		StringBuilder sb=new StringBuilder();
//        int len = readShort();
//        for (int i = 0 ; i < len ; i++) {
//        	sb.append(readChar());
//        }
//        String s=sb.toString();
//        if(TESTING) IO.println("AttributeDataInputStream.readString: "+s);
//    	return(s);
//	}
//
//	
//	public Tag readTag() throws IOException, ClassNotFoundException {
//		Tag tag=Tag.readTag(this);
//        if(TESTING) IO.println("AttributeInputStream.readTag: "+tag);
//		return(tag);
//	}
//
//	
//	public Type readType() throws IOException, ClassNotFoundException {
//		Type type=Type.readType(this);
//		if(TESTING) IO.println("AttributeInputStream.readType: "+type);
//		return(type);
//	}
//
//	
//	public Token readToken() throws IOException, ClassNotFoundException {
//		Token token=Token.readToken(this);
//		if(TESTING) IO.println("AttributeInputStream.readToken: "+token.edToken());
//		return(token);
//	}
//
//	
//	public void checkSyncMark(String mark) throws ClassNotFoundException, IOException {
//		//IO.println("checkSync: "+mark);
//    	if(AttributeOutput.USE_SYNCMARK) {
//    		String mrk=readIdent();
//    		if(mrk.equals(mark)) return;
//
//    		IO.println("\n\n*******************************************************************************************************************");
//    		IO.println("*** OUT OF SYNC WHILE READING "+name+": EXPECTING MARK '"+mark+"' GOT "+mrk.getClass().getSimpleName()+" '"+mrk+"'");
//    		IO.println("*** OUT OF SYNC("+mark+") THE FOLLOWING INPUT IS READ-AHEAD");
//    		IO.println("*******************************************************************************************************************");
//    		int obj=0; int n=0;
//    		while((obj=in.read())!=(-1) && (n++)<10) {
//    			IO.println("*** OUT OF SYNC: Next Byte: "+obj);
//    			//BREAK("OUT OF SYNC("+mark+")");
//    		}
//    		Util.IERR(name+":OUT OF SYNC("+mark+") "+mrk);
//    		Thread.dumpStack();
//    	}
//	}
//
//	
//	public void readAhead(String msg, int lim) throws ClassNotFoundException, IOException {
//		Util.NOTIMPL("External I/O");
//		Util.NOTIMPL("");
//		
//	}
//
//	
//	public void close() throws IOException {
//		in.close();
//	}

}
