/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.parsing;

import static simuletta.compiler.Global.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;

import simuletta.utilities.Option;
import simuletta.utilities.Tag;
import simuletta.utilities.Util;

/**
 * @author Øystein Myhre Andersen
 */
public class Directive {

	public static void treatDirectiveLine(final SimulettaScanner scanner,final String id,final String arg) {
		if (id.equalsIgnoreCase("OPTION"))				Directive.setOption();
		else if (id.equalsIgnoreCase("INSERT"))			Directive.insert(scanner,arg);
		else if (id.equalsIgnoreCase("VISIBLE"))		allVisible = !currentModule.isMainProgram();
		else if (id.equalsIgnoreCase("HIDDEN"))			allVisible=false;
		else if (id.equalsIgnoreCase("TAG")) 	     	Directive.setXTag(arg);
		else if (id.equalsIgnoreCase("SPORT"))      	Directive.setSport(arg);
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
	public static void insert(final SimulettaScanner scanner,final String fileName) {
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
     * %SPORT ON / OFF
     * <p>
     * Enables/disables special S-Port Simula features, such as inclusion of the S-Port Library.
     * <p>
     * The initial value is ON.
     */
    public static void setSport(final String onoff) {
    	Option.sportOk=(onoff.equalsIgnoreCase("ON"));
    	Util.WARNING("Compiler Directive: %SPORT sets Option.sport="+Option.sportOk);
    }
    
    /**
     * %TAG ( xtag'list )
     *
     * E.g: %tag (proces,prcPtp,PCSINR)
	 *
     */
    public static void setXTag(final String tagList) {
    	StringTokenizer st = new StringTokenizer(tagList,"(,)",false);
        while (st.hasMoreTokens()) {
        	Tag.preDefine(st.nextToken());
        }
    }

	/**
	 * %TITLE title-string
	 */
    public static void setTitle(final String title) {
//		Global.currentTitle = title;
//		Util.WARNING("NOT IMPLEMENTED: Compiler Directive: %TITLE");
	}

}
