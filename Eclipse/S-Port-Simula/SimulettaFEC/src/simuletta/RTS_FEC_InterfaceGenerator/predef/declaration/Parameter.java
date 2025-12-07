package simuletta.RTS_FEC_InterfaceGenerator.predef.declaration;

import java.io.IOException;
import simuletta.RTS_FEC_InterfaceGenerator.predef.AttrFile;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Category;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Key;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Kind;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.RTS_FEC_Interface_Option;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

public class Parameter extends Quantity {
//	public int lineNumber;    // From Quantity
//	public String identifier; // From Quantity
//	public String quantInfo;  // From Quantity

	private static final boolean WithTagAndIdent=false;//true; // Skal være FALSE men brukes til DEBUG
	public boolean classParameter;
	
	public Parameter(String identifier) {
		super(identifier);
	}

    // ***********************************************************************************************
    // *** writeQuant
    // ***********************************************************************************************
	/**
	 *      The format of a quantity head descriptor is
	 *
	 *      <kind*8 + categ>1B  -- NOTE: this byte must be less than lowKey
	 *      <type + (if prefix or qual then 128 else 0)>1B
	 *      if prefix/qual marked then "qualification identifier"
	 *      <clf + (if procedure parameter then 128 else 0)>1B
	 *  if NOT standard procedure parameter then
	 *    ? if sysattrfile then <RTStagbase of quantity + 1>I
	 *    ? else                <exttag of quantity + 1>I
	 *  endif
	 */
	@Override
	public void writeQuant(String indent,AttrFile oupt) throws IOException {
		//*** basic quantity descriptor, size computed above ***;
		// - assume that categ <8 and kind < 16  ALWAYS ***;
		int kind=Kind.K_ident;  // simple variable
    	int xtag=quantInfo.getXtag(0);
//		IO.println("writeQuant: "+indent+"Parameter "+identifier+", exttag="+xtag+", type="+type+", kind="+Kind.edKind(kind)+", categ="+Category.edCateg(categ)+", clf="+clf);
		String parKind=(classParameter)?"classParameter ":"Parameter "; 
    	if(RTS_FEC_Interface_Option.TRACE_CODING>2) AttrFile.OUPUT_TRACE(parKind+identifier+", exttag="+xtag+", type="+type
    			                   +", kind="+Kind.edKind(kind)+", categ="+Category.edCateg(categ)+", clf="+clf+", quantInfo="+quantInfo);
		oupt.putByte((kind*8) + categ);
//		IO.println("writeQuant: PACKED="+((kind*8) + categ)+", kind="+kind+", categ="+categ);

		// - assume that type < 128 ALWAYS ***;
		String prefqual=this.type.prefqual;
		if(prefqual != null) {
			oupt.putByte(type.code+128);
			oupt.putText(prefqual);
		} else oupt.putByte(type.code);

		// - assume that clf < 128 ALWAYS ***;
//		int procdiff=(classParameter)?0:128;
//		if(WithTagAndIdent) {
//			oupt.putByte(clf+procdiff);
//			oupt.putNumber(xtag+1);
//			oupt.putText(identifier);
//		} else {
//			oupt.putByte(clf+procdiff);
//			oupt.putNumber(xtag+1);
//		}
		if(classParameter) {
			oupt.putByte(clf);
			oupt.putNumber(xtag+1);
			if(WithTagAndIdent) oupt.putText(identifier);
			oupt.putText(quantInfo.idents.firstElement());
		} else {
			oupt.putByte(clf+128);
			oupt.putNumber(xtag+1);
			if(WithTagAndIdent) oupt.putText(identifier);
		}
		if(classParameter) oupt.putKey(Key.forcMark);
//        if(quantInfo.ovlkind!=0) {
//        	oupt.putKey(Key.overMark);
//        	oupt.putByte(quantInfo.ovlkind);
//        }
	}
	
	@Override
    public String toString() {
//    	return("ParamSpec: ident="+ident+", symbval="+symbval);
    	StringBuilder s=new StringBuilder();
    	s.append("Line "+lineNumber+": ").append(Category.edCateg(categ));
    	if(type!=null) s.append(""+type+" ");
    	s.append(identifier+" == "+edQuantInfo());
    	return(s.toString());
    }
}
