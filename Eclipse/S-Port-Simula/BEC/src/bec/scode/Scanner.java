/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.scode;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

import bec.Global;
import bec.util.Util;

/// A Simple Scanner.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/scode/Scanner.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class Scanner { 
    
    /// The pushBack stack
    private Stack<Character> puchBackStack=new Stack<Character>();

    /// The current source file reader;
    private Reader sourceFileReader;

	/// Constructs a new Scanner that produces Items scanned from the specified source.
	/// @param reader The character source to scan
	public Scanner(final Reader reader) {
		this.sourceFileReader = reader;
	}

	/// Close the scanner.
	void close() {
		try {
			sourceFileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			Util.IERR("");
		}
		sourceFileReader=null;
	}

	/// Scan source and return the next token: Identifier, Number or Character
	/// @return next token
    /// @throws IOException if an I/O error occurs
	public Object nextToken() throws IOException {
    	while(true)	{
    		if(Character.isLetter(getNext())) return(scanIdentifier());
    		switch(current) {
	            case '0':case '1':case '2':case '3':case '4':
	            case '5':case '6':case '7':case '8':case '9':return(scanNumber());
	            case '\n':			/* NL (LF) */
	            case ' ':
	            case '\r':			/* CR */
	            	break;
	            default:
	            	return Character.valueOf((char) current);
    		}
    	}
    }

	/// Scan source and return an Integer Number
	/// @return next token
    /// @throws IOException if an I/O error occurs
    private Integer scanNumber() throws IOException {
       	StringBuilder number=new StringBuilder();
    	
    	number.append((char)current);
    	while (Character.isDigit(getNext()))
    		number.append((char)current);
    	pushBack (current);
    	return Integer.decode(number.toString());
    }

	/// Scan source and return an Identifier String.
    /// <pre>
    /// Reference-Syntax:
    /// 
    ///    identifier = letter  { letter  |  digit  |  _  }
    ///    
    ///    
    /// End-Condition: current is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return the resulting identifier
    /// @throws IOException if an I/O error occurs
    ////
    private String scanIdentifier() throws IOException {
    	StringBuilder name=new StringBuilder();
//    	Util.ASSERT(Character.isLetter((char)(current)),"Expecting a Letter");
    	name.append((char)current);
    	while ((Character.isLetter(getNext()) || Character.isDigit(current) || current == '_'))
    		name.append((char)current);
    	pushBack(current);
//    	IO.println("Scanner: Name "+name);
    	return(name.toString());
    }
	
    
    //********************************************************************************
    //**	                                                                 UTILITIES 
    //********************************************************************************
	
	/// The current character read.
    private int current;
    
    
    /// Returns next input character.
    /// @return next input character
    /// @throws IOException if an I/O error occurs
    ///
    private int getNext() throws IOException {
    	if(puchBackStack.empty()) {
    		int c=sourceFileReader.read();
    		if(c=='\n') Global.sourceLineNumber++;
//    		else if(c<0) { EOF_SEEN=true; c=EOF_MARK; }
    		else if(c<0) throw new RuntimeException("EOF");
    		else if(c<32) c=' '; // Whitespace
    		current=c;
    	} else current=puchBackStack.pop();
    	return(current);
    }

    
    /// Push a character onto the puchBackStack.
    /// @param chr character to be pushed
    ///
    private void pushBack(final int chr) {
	    // push given value back into the input stream
	    puchBackStack.push((char)chr);
	    current=' ';
    }
 
}
