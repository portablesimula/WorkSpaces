package simuletta.utilities;
/**
 * S-PORT FEC - DIV CODES
 * 
 * @author Øystein
 *
 */
public abstract class Quant {
	// TYPE:
	public static final int INOTY = 14; // no type
	public static final int IINTG =  4;  // integer
	public static final int ISHOR =  3; // short integer;
	public static final int IREAL =  5; // real;
	public static final int ILONG =  6; // long real;
	public static final int IBOOL =  1; // boolean;
	public static final int ICHAR =  2; // character;
	public static final int ILABE = 11; // label (switch);
	public static final int ITEXT =  8; // text;
	public static final int IREF  =  7; // ref;
	public static final int IPTR  =  9; //  pointer to record;
	public static final int IELSE = 15; //  universal

	// KIND:
    public static final int K_ident =  0; // simple variable
    public static final int K_proc  =  1; // procedure
    public static final int K_array =  2; // array ( standard )
    public static final int K_label =  3; // label
    public static final int K_switch=  4; // switch (type=ILABE)
    public static final int K_class =  5; // class
    public static final int K_rep   =  6; // repetition
    public static final int K_record=  7; // record
    public static final int K_subbl =  8; // simple block
    public static final int K_prefbl=  9; // prefixed block
    public static final int K_error = 10; // erroneous declaration
    public static final int K_labbl = 11; // declquant for labels of for- and inspect-statements:
    public static final int K_unknwn= 12; // unspecified parameter
    public static final int K_extnal= 13; //

    public static final int K_max   = 13;  //!NB *** must be < 16 ;

    // CATEG:
    public static final int C_unspec=0; //  !corresponds to RTS 'm_ref';
    public static final int C_value =1; //  !corresponds to RTS 'm_value';
    public static final int C_name  =2; //  !corresponds to RTS 'm_name';
    public static final int C_local =3; //  !corresponds to RTS 'm_local';
    public static final int C_extnal=4; //  !corresponds to RTS 'm_extr';
    public static final int C_unknwn=5; //
    public static final int C_virt  =6; //
    public static final int C_block =7; //

    public static final int C_max   =7;  //!NB *** must be < 8;

    
    public static final String edType(int k) {
    	switch(k) {
    		case INOTY: return("no type");
    		case IINTG: return("integer");
    		case ISHOR: return("short integer");
    		case IREAL: return("real");
    		case ILONG: return("long real");
    		case IBOOL: return("boolean");
    		case ICHAR: return("character");
    		case ILABE: return("label (switch)");
    		case ITEXT: return("text");
    		case IREF:  return("ref");
    		case IPTR:  return("pointer to record");
    		case IELSE: return("universal");
    		default:    return("illegal");
    	}
    }
    
    
    public static final String edKind(int k) {
    	switch(k) {
    		case K_error:  return("unknwn");
    		case K_ident:  return("ident");
    		case K_array:  return("array");
    		case K_rep:    return("infix array");
    		case K_proc:   return("proc");
    		case K_class:  return("class");
    		case K_label:  return("label");
    		case K_switch: return("switch");
    		case K_subbl:  return("subbl");
    		case K_prefbl: return("prefbl");
    		case K_record: return("record");
    		case K_labbl:  return("labbl");
    		case K_unknwn: return("unknwn");
    		case K_extnal: return("predef");
    		default:       return("illegal");
    	}
    }
    
    public static final String edCateg(int k) {
    	switch(k) {
            case C_unknwn: return("default");
            case C_local : return("local");
            case C_value : return("value");
            case C_name  : return("name");
            case C_unspec: return("unspec");
            case C_virt  : return("virt");
            case C_extnal: return("extnal");
            case C_block : return("block");
            default: return("illegal");
    	}
    }
}
