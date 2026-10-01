package simula.core.builder;

import java.util.List;

public class SourceTextReader {
    private List<String> sourceLines;
    private int charIndex;
    private int lineIndex;

	/// The current character read.
    private int current;
    
	/// ISO EM(EndMedia) character used to denote end-of-input
	/// Unicode: 25 = \u0019 End of Medium
    public static final int EOF_MARK=25;
    
    /// Set 'true' when EOF-character EOF_MARK was read.
    private boolean EOF_SEEN=false;

    public SourceTextReader(List<String> sourceLines) {
    	this.sourceLines = sourceLines;
//		testGetNext();
    }
    
   	public boolean eof() {
   		return EOF_SEEN;
   	}

   	public int currentLineNumber() {
   		return lineIndex;
   	}
   	
   	public String currentLine() {
   		return sourceLines.get(lineIndex);
   	}
   	
   	public int prevLineLength() {
   		return sourceLines.get(lineIndex - 1).length();
   	}

   	public int nextPos() {
   		return charIndex;
   	}

   	public int getCurrent() {
   		return current;
   	}
   	
    /// Returns next input character.
    /// currentColumn is incremented to point to the next character
    /// @return next input character
    /// Retrieves the next character in the source code, 
    /// including the trailing newline (LF) at the end of each line.
    /// 
    /// Note: A single character line containing EOF_MARK was added
    ///       to List<String> sourceLines in DocumentManager.
    /// 
    /// @return The next character, or EOF_MARK if the end of the input is reached.
    public int getNext() {
    	if(current == SourceTextReader.EOF_MARK) {
    		EOF_SEEN = true;
    		return current;
    	}
        String currentLine = sourceLines.get(lineIndex);
        current = currentLine.charAt(charIndex++);

        // If we just returned the newline character, move to the start of the next line
        if (current == '\n') {
        	lineIndex++;
        	charIndex = 0;
        }
        return current;
    }
    
	
//	private void testGetNext() {
//		StringBuilder sb = new StringBuilder();
//		int c;
//		c = getNext(); sb.append((char)c);
//		IO.println("First character: " + c + ':' + (char)c);
//		c = getNext(); sb.append((char)c); IO.println("2. character: " + c + ':' + (char)c);
//		c = getNext(); sb.append((char)c); IO.println("3. character: " + c + ':' + (char)c);
//		c = getNext(); sb.append((char)c); IO.println("4. character: " + c + ':' + (char)c);
//		c = getNext(); sb.append((char)c); IO.println("6. character: " + c + ':' + (char)c);
//		pushBackPos(2);
//		c = getNext(); sb.append((char)c); IO.println("4. character: " + c + ':' + (char)c);
//		IO.println("nextCharIs('U'): " + nextCharIs('U'));
//		while(c != EOF_MARK) {
//			c = getNext(); sb.append((char)c); // IO.println("Line "+lineIndex+": Next character: " + c + ':' + (char)c);
//			if(c == '\n') {
//				IO.println("Line "+lineIndex+": |" + Comn.printable(sb.toString()) + '|');
//				sb = new StringBuilder();
//			}
//		}
//		Util.STOP();
//	}
    
    /// Checks if the next unread character matches the given expected value 
    /// without consuming or moving the reader pointer.
    /// 
    /// @param expected The character to look ahead and check for.
    /// @return true if the next character matches, false otherwise.
    public boolean nextCharIs(char expected) {
        if (lineIndex >= sourceLines.size()) {
            return false; // End of input reached, cannot match any character
        }
        char nextChar = sourceLines.get(lineIndex).charAt(charIndex);
        return nextChar == expected;
    }


	/// Moves the internal stream pointers back by 'count' positions.
	/// Also handles movement across line breaks.
	/// @param count number of positions to backtrack
    public void pushBackPos(int count) {
        while (count > 0) {
            if (charIndex >= count) {
                // The backup stays within the current line
            	charIndex -= count;
                count = 0;
            } else {
                // We need to back up into the previous line
                count -= charIndex; // Subtract what we can from the current line
                lineIndex--;
                
                if (lineIndex < 0) {
                    // Capped at the very beginning of the source code
                	lineIndex = 0;
                    charIndex = 0;
                    break;
                }
                
                // Set the character pointer to the end of the previous line
                charIndex = sourceLines.get(lineIndex).length();
            }
        }
        EOF_SEEN=false;
    }

}
