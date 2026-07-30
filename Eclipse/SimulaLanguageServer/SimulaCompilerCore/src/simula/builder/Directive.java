/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import simula.compiler.utilities.Util;
import simula.token.LexToken;

/// Utility class Directive.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaCompiler2/Simula/src/simula/compiler/parsing/Directive.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class Directive {
	/// Default constructor.
	Directive() {}

    //********************************************************************************
    //**	                                                            treatDirective
    //********************************************************************************
	/// Scan a %Directive line.
	/// <pre>
    ///  Reference-Syntax:
    ///  
    ///      directive =  % { any character except end-of-line }
	/// 
	///  A conditional line takes the form:
	///    
	/// 		%selector-expression <i>text-line</i>
	/// 
	/// 	where <i>text-line</i> represents the line to be conditionally included
	/// 	and the selector-expression has the form:
	/// 
	/// 		Selector-expression
	/// 			= selector-group { selector-group }
	/// 
	/// 		Selector-group
	/// 			= + letter_or_digit { letter_or_digit }
	/// 			| - letter_or_digit { letter_or_digit }
	/// 
	/// 	i.e. a string of letters and signs, with the first character being a sign.
	/// 	The selector-expression is terminated by a SPACE.
	/// 
	/// 
    /// End-Condition: current is last character of construct
    ///                getNext will return first character after construct
	/// </pre>
	/// @return a Comment Token
	static void treatDirective(SimulaBuilder simBuilder, LexToken lexToken, String line) {
		IO.println("Directive.treatDirective: \"" + line + '"');
		if(line.length() == 1 || Character.isWhitespace(line.charAt(1))) {
			IO.println("Directive.treatDirective: Comment: " + line);
			return;
		}
		
		List<String> tokens = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(line, "% ");
        while (st.hasMoreTokens()) tokens.add(st.nextToken());
        
		IO.println("Directive.treatDirective: Tokens: " + tokens);
		
//		String id = tokens.getFirst();	
//		if (id.equalsIgnoreCase("SELECT")) setSelectors();
//		else
			Util.warning(simBuilder, lexToken, "Unknown Compiler Directive: " + tokens);
	}
	
	/// Treat a directive line.
	/// @param scanner the scanner
	/// @param id      the directive identifier
	/// @param arg     the directive argument
//	static void treatDirectiveLine(final SimulaScanner scanner, final String id, final String arg) {
//		if (id.equalsIgnoreCase("OPTION"))			; // Ignored in this implementation
//		else if (id.equalsIgnoreCase("INSERT"))		Directive.insert(scanner, arg);
//		else if (id.equalsIgnoreCase("SPORT"))		; // Ignored in this implementation
//		else if (id.equalsIgnoreCase("TITLE"))		; // Ignored in this implementation
//		else if (id.equalsIgnoreCase("PAGE"))		; // Ignored in this implementation
//		else if (id.equalsIgnoreCase("KEEP_JAVA"))	; // Ignored in this implementation
//		else if (id.equalsIgnoreCase("EOF"))		scanner.sourceFileReader.forceEOF();
//		else Util.warning("Unknown Compiler Directive: " + id + ' ' + arg);
//	}

	/// %INSERT file-name
	/// 
	/// Will cause the compiler to include the indicated file at this place in the
	/// source input stream. INSERT may occur in the included file.
	/// @param scanner the SimulaScanner
	/// @param fileName the file to insert
//	private static void insert(final SimulaScanner scanner, final String fileName) {
//		Util.warning("%INSERT " + fileName);
//		File file = new File(fileName);
//		if (file.exists() && file.canRead()) {
//			scanner.insert(file);
//		} else
//			Util.error("Can't open " + fileName + " for reading");
//	}

}
