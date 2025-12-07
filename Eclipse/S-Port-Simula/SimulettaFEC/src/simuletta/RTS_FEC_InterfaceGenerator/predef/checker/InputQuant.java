package simuletta.RTS_FEC_InterfaceGenerator.predef.checker;

import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Category;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Kind;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Type;

public class InputQuant {
	public Type type;
	public int xkind;
	public int xcateg;
	public int xclf;
	
	public String xidentstring=null;
	public String xident=null; // Class identifier
	public int xprotect=0;
	public int xdim=0;
	public int xlongindic=0;
	public int sxlanguage=0;
	public int sxextident=0;
	public int xspecial=0;

	public String xmodulid;  // moduleid: never notext when of interest;
	public String xcheck;    // checkcode: never notext when of interest;
	public String xlanguage; // language: zero if no language (i.e. SIMULA for main);
	public String xextident; // extident: DEFCONST("?") if no extident;

	public InputQuant(Type type,int xkind,int xcateg,int xclf) {
		this.type=type;
		this.xkind=xkind;
		this.xcateg=xcateg;
		this.xclf=xclf;
	}

	public String toString() {
		StringBuilder s=new StringBuilder();
		s.append("QUANT: ").append(Kind.edKind(xkind)).append("  ");
		s.append(xidentstring).append(" ").append("/").append(Category.edCateg(xcateg));
		s.append("/").append(xclf).append("/").append(type);
		if(xlongindic!=0) s.append(", lin:").append(xlongindic);
		if(xprotect==1) s.append(" P");
		return(s.toString());
	}
}
