/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.RTS_FEC_InterfaceGenerator.predef.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

/**
 * @author Øystein Myhre Andersen
 */
public class Directive {

	public static void treatDirectiveLine(final InterfaceScanner scanner,final String id,final String arg) {
		if (id.equalsIgnoreCase("OPTION"))				Directive.setOption();
		else if (id.equalsIgnoreCase("INSERT"))			Directive.insert(scanner,arg);
//		else if (id.equalsIgnoreCase("VISIBLE"))		allVisible = !currentModule.isMainProgram();
//		else if (id.equalsIgnoreCase("HIDDEN"))			allVisible=false;
		else if (id.equalsIgnoreCase("TAG")) 	     	Directive.setXTag(arg);
		else if (id.equalsIgnoreCase("TITLE"))			Directive.setTitle(arg);
		else if (id.equalsIgnoreCase("PAGE"))			Directive.page();
		else Util.WARNING("Unknown Compiler Directive: " + id+' '+arg);
	}
	
    /**
     * %INSERT file-name
     * <p>
     * Will cause the compiler to include the indicated file at this place in the
     * source input stream. INSERT may occur in the included file.
     * In contrast to COPY, the included lines are not counted (they will all be
     * numbered with the line number of the line containing the outermost INSERT).
	 * Furthermore, if the source is being listed, listing is turned off during 
	 * the inclusion and turned on again when reading continues after this directive. 
	 */
	public static void insert(final InterfaceScanner scanner,final String fileName) {
		File file=new File(fileName);
		if(file.exists() && file.canRead()) {
		    try {
				Reader reader=new InputStreamReader(new FileInputStream(file));
				scanner.insert(reader);
		    } catch(IOException e) { Util.IERR("Impossible",e); }
		} else Util.ERROR("Can't open "+fileName+" for reading");
	}

	/**
	 * %PAGE
	 * <p>
	 * Will cause the compiler to change to a new page in the listing file. If the
	 * page heading (as defined by %TITLE, see below) is non-empty, it will be
	 * printed on top of the new page.
	 */
	public static void page() {
//		Util.WARNING("NOT IMPLEMENTED: Compiler Directive: %PAGE");
	}
  
    /**
     * %OPTION  name  value
     * <p>
     * Will set compiler switch 'name' to the value 'value'.
     * The facility is intended for compiler maitenance,
     * and is not explained further.
     */
    public static void setOption() {
    	Util.NOTIMPL("Compiler Directive: %OPTION");
    }
     
    /**
     * %TAG xtag
     * <p>
     *
     * <p>
     *
     */
    public static void setXTag(final String tag) {
//    	if(rutlev==0) then {
//    		getbasic();
//    		if(ch != '(') percenterr();
//    		ch=',';
//    		while(ch == ',') {
//    			getbasic();
//    			if(symbol != S_IDENT) percenterr();
//    			int id=SymbolTable.DEFIDENTSYMB(curval);
//
//    			taglast=taglast.next=new seqtag(symtab[id]);
//    			getbasic();
//    		}
//    		if(ch != ')') percenterr();
//    	} else percenterr();
    	Util.WARNING("NOT IMPLEMENTED: Compiler Directive %TAG");  // TODO:
    }

	/**
	 * %TITLE title-string
	 */
    public static void setTitle(final String title) {
//		Global.currentTitle = title;
//		Util.WARNING("NOT IMPLEMENTED: Compiler Directive: %TITLE");
	}

}
