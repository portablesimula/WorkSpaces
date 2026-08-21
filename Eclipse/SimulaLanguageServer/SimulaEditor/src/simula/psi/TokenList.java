package simula.psi;

import java.util.List;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import simula.compiler.SourceModule;
import simula.compiler.utilities.Util;
import simula.core.builder.export.TokenManager;

public class TokenList {
//	public List<LexToken> tokenList;
	private SourceModule sourceModule;
	private List<Integer> tokens;
	
	public TokenList(SourceModule sourceModule, List<Integer> tokens) {
		this.sourceModule = sourceModule;
		this.tokens = tokens;
	}
		
	public String getText() {
		// TODO Auto-generated method stub
		Util.IERR("");
		return sourceModule.getUpdatedText();
	}


	public void verifyTokenList() {
//        String originalText = sourceModule.getOriginalText();
		String originalText = sourceModule.getUpdatedText();
        int currentLine = 0;
        int currentStartChar = 0;
//        String documentLines = 
        int beginIndex = 0;
        StringBuilder sb = new StringBuilder();
		int x = 0;
		while(x < tokens.size()) {
            int deltaLine = tokens.get(x++);
            int deltaStart = tokens.get(x++);
            int length = tokens.get(x++);
            int tokenTypeIndex = tokens.get(x++);
            int tokenModifiersBitmask = tokens.get(x++);

            // 1. Calculate absolute line location
            currentLine += deltaLine;

            // 2. Calculate absolute character index within that line
            if (deltaLine == 0) {
                currentStartChar += deltaStart;
            } else {
                currentStartChar = deltaStart;
            }
            beginIndex += deltaStart; 
            // 3. Extract the underlying text fragment safely from your local document reference
//            if (currentLine < documentLines.length) {
//                String lineText = documentLines[currentLine];
//                if (currentStartChar + length <= lineText.length()) {
//                    reconstructedText = lineText.substring(currentStartChar, currentStartChar + length);
//                }
//            }
            String tokenText = originalText.substring(beginIndex, beginIndex + length);
            // Print tracking information
            System.out.printf("Token at [Line %d, Char %d] (Len: %d, TypeId: %d:%s) -> \"%s\"%n", 
                currentLine, currentStartChar, length, tokenTypeIndex, TokenManager.tokenTypes.get(tokenTypeIndex), tokenText);
            sb.append(tokenText);
//            Util.IERR("");
		}
        String reconstructedText = sb.toString();
		if(! reconstructedText.equals(originalText)) {
			String reconstr = Util.printable(reconstructedText);
			String original = Util.printable(originalText);
			int lng1 = original.length();
			int lng2 = reconstr.length();
			System.err.println("SimulaBuilder: VERIFIER FAILED: Reconstructed text differ from original text");
			System.err.println("Original Text(lng:"+lng1+"): " + original);
			System.err.println("Reconstr Text(lng:"+lng2+"): " + reconstr);
			int n = Math.min(lng1, lng2);
			LOOP:for(int i=0;i<n;i++) {
				if(reconstr.charAt(i) != original.charAt(i)) { 
	    			System.err.println("First deviation at pos " + i + ", original: " + original.charAt(i) + ", reconstr: " + reconstr.charAt(i));
					break LOOP;
				}
			}
			if(lng1 != lng2) {
				int pos = Math.max(0, n - 100);
    			System.err.println("Original Tail: " + original.substring(pos));
    			System.err.println("Reconstr Tail: " + reconstr.substring(pos));
			}
			Util.IERR("");

		}
	}

	/// NOTE: SEE: simula.editor.LspTextPanel
	public void fillLineAndTextPanel(StyledDocument lin, StyledDocument doc) {
//      String originalText = sourceModule.getOriginalText();
		String originalText = sourceModule.getUpdatedText();
      int currentLine = 0;
      int currentStartChar = 0;
//      String documentLines = 
      int beginIndex = 0;
      StringBuilder sb = new StringBuilder();
		int x = 0;
		while(x < tokens.size()) {
          int deltaLine = tokens.get(x++);
          int deltaStart = tokens.get(x++);
          int length = tokens.get(x++);
          int tokenTypeIndex = tokens.get(x++);
          int tokenModifiersBitmask = tokens.get(x++);

          // 1. Calculate absolute line location
          currentLine += deltaLine;

          // 2. Calculate absolute character index within that line
          if (deltaLine == 0) {
              currentStartChar += deltaStart;
          } else {
              currentStartChar = deltaStart;
          }
          beginIndex += deltaStart; 
          // 3. Extract the underlying text fragment safely from your local document reference
//          if (currentLine < documentLines.length) {
//              String lineText = documentLines[currentLine];
//              if (currentStartChar + length <= lineText.length()) {
//                  reconstructedText = lineText.substring(currentStartChar, currentStartChar + length);
//              }
//          }
          String tokenText = originalText.substring(beginIndex, beginIndex + length);
          // Print tracking information
          System.out.printf("Token at [Line %d, Char %d] (Len: %d, TypeId: %d:%s) -> \"%s\"%n", 
              currentLine, currentStartChar, length, tokenTypeIndex, TokenManager.tokenTypes.get(tokenTypeIndex), tokenText);

          
          if (deltaLine == 0) {
				String lineString = edLineNumber(currentLine+1);
  			// Should only be here AFTER a complete line is rendered
	    	    SimpleAttributeSet attrs = lexToken.getTooltipAttrs(errorLines);
		    	if(attrs != null) {
  				lin.insertString(lin.getLength(),lineString, attrs);					    		
  				errorLines = null;
  			} else {
	    			lin.insertString(lin.getLength(),lineString, styleLineNumber);
  			}
          }
          
          errorLines = lexToken.accumErrors(errorLines);
          SimpleAttributeSet attrs = lexToken.getTooltipAttrs(null);
          if(attrs != null) {
        	  doc.insertString(doc.getLength(), tokenText, attrs);
          } else {
        	  doc.insertString(doc.getLength(), tokenText, elt.getStyle(this));				    		
          }

//          Util.IERR("");
		}
		
		// Standard traversal loop  ====================== GAMMEL KODE:
		for(LexToken elt:psiTree.tokenList) {
			IO.println("PsiTextPanel.fillTextPane: GOT NEXT: " + elt);
		    if(elt != null) {
		    	if(elt instanceof LexToken lexToken) {
		    		if(lexToken.keyWord == KeyWord.EOF) {
			    	    SimpleAttributeSet attrs = lexToken.getTooltipAttrs(errorLines);
				    	if(attrs != null) {
		    				lin.insertString(lin.getLength(),edLineNumber(lineNumber++), attrs);					    		
		    				errorLines = null;
				    	}
		    		} else if(lexToken.keyWord == KeyWord.NEWLINE) {
						if(Option.LSP_VERIFY && elt.firstLineNumber() != lineNumber) {
							Util.IERR("GOT NEWLINE: " + lexToken + " -- PSI VERIFIER FAILED: " + elt.firstLineNumber() + " != lineNumber=" + lineNumber);
						}
	    				String lineString = edLineNumber(lineNumber++);
		    			// Should only be here AFTER a complete line is rendered
			    	    SimpleAttributeSet attrs = lexToken.getTooltipAttrs(errorLines);
				    	if(attrs != null) {
		    				lin.insertString(lin.getLength(),lineString, attrs);					    		
		    				errorLines = null;
		    			} else {
			    			lin.insertString(lin.getLength(),lineString, styleLineNumber);
		    			}
		    		}
		        	errorLines = lexToken.accumErrors(errorLines);
		    	    SimpleAttributeSet attrs = lexToken.getTooltipAttrs(null);
			    	if(attrs != null) {
				    	doc.insertString(doc.getLength(), elt.getText(), attrs);
			    	} else {
				    	doc.insertString(doc.getLength(), elt.getText(), elt.getStyle(this));				    		
			    	}
		    	}
		    }
	    }
	}
	

}
