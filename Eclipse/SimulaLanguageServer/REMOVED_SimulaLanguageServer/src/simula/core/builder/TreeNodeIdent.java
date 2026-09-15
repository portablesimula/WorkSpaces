package simula.core.builder;

public class TreeNodeIdent {
	public Object object;
	String ident;
	
	public TreeNodeIdent(Object object, String ident) {
		this.object = object;
		this.ident = ident;
	}
	
	@Override public String toString() {
		return ident;
	}
}
