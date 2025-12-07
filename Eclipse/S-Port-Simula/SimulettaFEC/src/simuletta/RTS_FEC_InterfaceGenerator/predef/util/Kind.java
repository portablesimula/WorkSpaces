package simuletta.RTS_FEC_InterfaceGenerator.predef.util;

public class Kind {
	//KIND:
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

}
