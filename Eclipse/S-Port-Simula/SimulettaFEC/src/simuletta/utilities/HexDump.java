/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public final class HexDump {
	
	
	public static void hexDump(File attributeFile) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(attributeFile);
		String hex="", chr=""; int count=0;
		LOOP: while(true) {
			int b=fileInputStream.read();
			if(b== -1) break LOOP;
			String hx=Integer.toHexString(b).toUpperCase();
			//String hx=Integer.toString(b);
			if(hx.length() == 0) hx="00";
			if(hx.length() == 1) hx="0"+hx;
//			if(hx.length() == 2) hx="0"+hx;
			hex=hex+hx+" ";
			if(!Character.isAlphabetic(b)) b='.';
			chr=chr+((char)b)+" ";
			count++;
			if(count==16) {
				IO.println("  "+hex+"   "+chr);
				count=0; hex=""; chr="";
			}
		}
		while(hex.length()<(16*4)) hex=hex+" ";
		IO.println("  "+hex+"   "+chr);
		fileInputStream.close();
	}
	
	@SuppressWarnings("unused")
	private static void list() {
		File file=new File("C:\\WorkSpaces\\SPort-Backend\\BackendCompiler\\src\\sport\\bec\\instructions");
		String[] names=file.list();
		int n=names.length;
		for(int i=0;i<n;i++) {
			String name=names[i];
			// E.g. 		case oprINFO:   instr=new INFO(); return(instr.readInstr(inpt));
			int lng=name.length();
			name=name.substring(0,lng-5);
			String lead="case opr"+name+": ";
			while(lead.length()<20) lead=lead+' ';
			IO.println(lead+"instr=new "+name+"(); return(instr.readInstr(inpt));");
		}
	}
	
	

}
