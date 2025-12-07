/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.RTS_FEC_InterfaceGenerator.predef.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

import simuletta.RTS_FEC_InterfaceGenerator.predef.util.PredefGlobal;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.RTS_FEC_Interface_Option;

//*************************************************************************************
//**                                                             CLASS SourceFileReader
//*************************************************************************************

/**
* Input Utilities for the Simuletta Scanner.
* 
* @author Øystein Myhre Andersen
*
*/
public final class SourceFileReader {
	private final Stack<Reader> stack=new Stack<Reader>();
	private final Stack<String> nameStack=new Stack<String>();
	private Reader current; // current source file reader;
	private String sourceName;
	private StringBuilder line;
	private int lineNo;
	
	public SourceFileReader(final String name,final Reader reader) {
		current=reader;
		sourceName=name;
		line=new StringBuilder();
	}
	
	public int read() {
		int c= -1;
		try { c = current.read();
			  while (c == -1) {
				  close();
				  if (stack.isEmpty()) return (-1);
				  sourceName=nameStack.pop();
				  current = stack.pop();
				  c = current.read();
			  }
		} catch (IOException e) {}
		  if(c=='\n') {
			  lineNo++;
			  PredefGlobal.sourceLineNumber=lineNo;
			  PredefGlobal.currentSourceLine=line.toString();//.replace('\r',' ');
			  if(RTS_FEC_Interface_Option.SOURCE_LISTING) IO.println(sourceName+":Line "+lineNo+": "+PredefGlobal.currentSourceLine);
			  line=new StringBuilder();
		  } else if(!Character.isISOControl(c)) line.append((char)c);
		return (c);
	}
	
	public void insert(final Reader reader) {
		nameStack.push(sourceName);
		stack.push(current); current=reader;
	}
	    
	public void close() {
		try { current.close(); } catch (IOException e) {}
	}

}
