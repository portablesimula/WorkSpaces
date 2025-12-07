package simuletta.RTS_FEC_InterfaceGenerator.predef.declaration;

import java.util.StringTokenizer;
import java.util.Vector;

import simuletta.RTS_FEC_InterfaceGenerator.predef.TagMap;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.RTS_FEC_Interface_Option;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

@SuppressWarnings("unused")
public class QuantInfo {
	int ovlkind; // Overload kind
	int nIdent;
	Vector<String> idents=new Vector<String>();

	public QuantInfo(String info) {
//		IO.println("NEW QuantInfo: \""+info+'"');
		StringTokenizer st = new StringTokenizer(info);
		String token=st.nextToken();
		try { int i=Integer.valueOf(token);
			  if(i < 0) {
				  ovlkind= -i;
				  token=st.nextToken();
				  nIdent=Integer.valueOf(token);
			  }	else nIdent=i;
		} catch(NumberFormatException e) {}
		while (st.hasMoreTokens()) {
	    	 token=st.nextToken();
	    	 Util.ASSERT(token.startsWith("*"),"");
	    	 if(token.startsWith("*")) token=token.substring(1);
	    	 idents.add(token);
//	         IO.println(token);
	    }
		Util.ASSERT(idents.size()==nIdent,"");
		Util.ASSERT(nIdent<=3,"nIdent<=3 GOT nIdent="+nIdent);
		if(RTS_FEC_Interface_Option.INTERFACE_TRACE_LEVEL > 0) IO.println("NEW "+this);
		if(idents.size() > 1) {
			// CHECK INVARIANT - Subsequent idents must have increasing tag numbers
			int t1=getXtag(0);
			for(int i=1;i<nIdent;i++) {
				int t2=getXtag(i);
				if(t2 != (t1+1)) {
					IO.println("NEW QuantInfo: ************************ INVARIANT FAILED !  ************************");
					IO.println("NEW QuantInfo: \""+info+'"');
					for(int j=0;j<nIdent;j++) {
						IO.println("NEW QuantInfo: "+idents.get(j)+"'tag="+getXtag(j));
					}
//					Util.STOP();					
				} t1=t2;
			}
		}
	}
	
	static int nERR=0;
	public int getXtag(int x) {
		if(RTS_FEC_Interface_Option.INTERFACE_TRACE_LEVEL > 0)  IO.println(""+this);
		try {
			String id=idents.elementAt(x);
			if(RTS_FEC_Interface_Option.INTERFACE_TRACE_LEVEL > 0)  IO.println("QuantInfo-getXtag: \""+id+"\"");
			int xtag=TagMap.tagMap.get(id);
			if(RTS_FEC_Interface_Option.INTERFACE_TRACE_LEVEL > 0)  IO.println("Quantity: "+id+" = "+xtag);
//			if((++nERR)>4) Util.IERR("");
			return(xtag);
		} catch(Exception e) {
//			if((++nERR)>4) Util.IERR("");
			return(0);
		}
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		if(nIdent>0) for(String s:idents) {
			sb.append(" *").append(s);
			sb.append('[').append(TagMap.tagMap.get(s)).append(']');
		}
		if(ovlkind>0) sb.append("  OVERLOAD: "+ovlkind);
		return("QuantInfo: "+nIdent+" "+sb.toString());
	}
}
