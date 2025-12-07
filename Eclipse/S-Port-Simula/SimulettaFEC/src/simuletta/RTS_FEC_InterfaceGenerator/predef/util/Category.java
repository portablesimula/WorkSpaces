package simuletta.RTS_FEC_InterfaceGenerator.predef.util;

public class Category {
	//CATEG:
	public static final int C_unspec=0; //  !corresponds to RTS 'm_ref';
	public static final int C_value =1; //  !corresponds to RTS 'm_value';
	public static final int C_name  =2; //  !corresponds to RTS 'm_name';
	public static final int C_local =3; //  !corresponds to RTS 'm_local';
	public static final int C_extnal=4; //  !corresponds to RTS 'm_extr';
	public static final int C_unknwn=5; //
	public static final int C_virt  =6; //
	public static final int C_block =7; //

	public static final int C_max   =7;  //!NB *** must be < 8;


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
