package simuletta.RTS_FEC_InterfaceGenerator.predef.util;

public class Key {

	public static final int hikey    = 255;
    public static final int longSwap = 255;   //*** very long string (>bufsize);
    public static final int bufSwap  = 254;   //*** read next buffer;
    public static final int longText = 253;
	public static final int mainKey  = 252;
	public static final int begList  = 251;
	public static final int endlist  = 250;
	public static final int protMark = 249;
	public static final int hidMark  = 248;
	public static final int nestMark = 247;
	public static final int xMark    = 246;
	public static final int yMark    = 245;
	public static final int specMark = 244;
	public static final int overMark = 243;
	public static final int dimMark  = 242;
	public static final int forcMark = 241;
	public static final int thisMark = 240;
	public static final int lowKey   = 240;

	public static String edKey(int key) {
		switch(key) {
	    case longSwap : return("longSwap");   //*** very long string (>bufsize);
	    case bufSwap  : return("bufSwap");   //*** read next buffer;
	    case longText : return("longText");
		case mainKey  : return("mainKey");
		case begList  : return("begList");
		case endlist  : return("endList");
		case protMark : return("protMark");
		case hidMark  : return("hidMark");
		case nestMark : return("nestMark");
		case xMark    : return("xMark");
		case yMark    : return("yMark");
		case specMark : return("specMark");
		case overMark : return("overMark");
		case dimMark  : return("dimMark");
		case forcMark : return("forcMark");
		case thisMark : return("thisMark");
		}
		return("BYTE("+key+')');
	}
}
