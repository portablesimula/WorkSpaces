/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.editor;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument.LeafElement;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import simula.compiler.SourceModule;
import simula.compiler.utilities.Global;
//import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.core.builder.export.LexToken;
import simula.core.builder.export.TokenManager;
import simula.editor.SimulaEditor.Language;
//import simula.psi.LexToken;
//import simula.psi.PsiElement;
import simula.psi.SemanticTokens;
//import simula.psi.PsiTreeIterator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/// The Source text Panel.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/editor/SourceTextPanel.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
/// @author Google AI
@SuppressWarnings({ "serial", "unused" })
public class LspTextPanel extends TabTextPanel {
	/// DEBUG on/off
	private static final boolean DEBUG=false;//true;
	
//	public SourceModule currentModule;
//
//	/// The line number side-panel.
//	private JTextPane lineNumbers;
//	
//	/// The ScrollPane
//	private JScrollPane styleScrollPane;

	/** Style name */ public final static String styleNameRegular = "regular";
	/** Style name */ public final static String styleNameKeyword = "keyword";
	/** Style name */ public final static String styleNameComment = "comment";
	/** Style name */ public final static String styleNameConstant = "constant";
	/** Style name */ public final static String styleNameClassIdent = "classIdent";
	/** Style name */ public final static String styleNameProcedure = "procedure";
	/** Style name */ public final static String styleNameError = "error";
 	
	/** Style */ public static Style styleRegular;
	/** Style */ public static Style styleKeyword;
	/** Style */ public static Style styleComment;
	/** Style */ public static Style styleConstant;
	/** Style */ public static Style styleClassIdent;
	/** Style */ public static Style styleProcedure;
	/** Style */ public static Style styleError;
	/** Style */ private Style styleLineNumber;
	
//	/// The popup Menu.
//	private JPopupMenu popupMenu;
//	
//	/// The StyledDocument.
//	public StyledDocument doc;
//	
//	/// Editable text pane with undo/redo history.
//	JTextPane editTextPane;
	
//	/// Current language.
//    SimulaEditor.Language lang;
//	
//	/// Signals auto refresh.
//    boolean AUTO_REFRESH=true;//false;
//
//	/// The undo manager.
//	private UndoManager undoManager = new UndoManager();
//	
//	/// Returns the undo manager.
//	/// @return the undo manager
//	UndoManager getUndoManager() { return(undoManager); }
//	
//    /// Indicates that the source file has changed.
//    boolean fileChanged = false;
//    
//    /// Indicates that refresh is needed.
//    boolean refreshNeeded = false;
//
//	// ****************************************************************
//	// *** UndoableEditListener
//	// ****************************************************************
//    /// The UndoableEditListener.
//    private UndoableEditListener undoListener=new UndoableEditListener() {
//		public void undoableEditHappened(UndoableEditEvent e) {
//			UndoableEdit edit=e.getEdit();
//			Global.currentModule.undoManager.addEdit(edit);
//			Global.currentModule.fileChanged=true;
//			Global.currentModule.refreshNeeded=true;
//			SimulaEditor.menuBar.updateMenuItems();
//		}
//	};
//
//	// ****************************************************************
//	// *** MouseListener
//	// ****************************************************************
//	/// The MouseListener.
//    MouseListener mouseListener = new MouseListener() {
//		public void mousePressed(MouseEvent e) {}
//		public void mouseReleased(MouseEvent e) {}
//		public void mouseEntered(MouseEvent e) {}
//		public void mouseExited(MouseEvent e) {}
//		public void mouseClicked(MouseEvent e) {
//    	    if(e.getButton()==3) popupMenu.show(editTextPane,e.getX(),e.getY());
//    	}
//    };
//
//	// ****************************************************************
//	// *** DocumentListener
//	// ****************************************************************
//    /// The DocumentListener.
//	DocumentListener documentListener=new DocumentListener() {
//		public void insertUpdate(DocumentEvent e)  { debugTrace("Insert",e); }
//		public void removeUpdate(DocumentEvent e)  { debugTrace("Remove",e); }
//		public void changedUpdate(DocumentEvent e) { debugTrace("Changed",e); }
//		
//		private void debugTrace(String id,DocumentEvent evt) {
//			if(DEBUG) {
//			    int ofst=evt.getOffset();
//			    int lng=evt.getLength();
//			    String styleName="UNKNOWN";
//			    String lastText="UNKNOWN";
//				try { // debugTrace
//				    StyledDocument doc=(StyledDocument)editTextPane.getDocument();
//				    if(id.equals("Insert")) lastText= doc.getText(ofst,lng);
//				    if(id.equals("addition")) lastText= doc.getText(ofst,lng);
//				    Element elt=doc.getCharacterElement(ofst);
//				    if(elt instanceof LeafElement leaf) {
//					    styleName=(String)leaf.getAttribute(StyleConstants.NameAttribute);
//				    }
//				    lastText=lastText.replace("\n","\\n");
//				} catch (Exception ex) { Util.IERR("Impossible",ex); }			
//				IO.println("DocumentListener: "+id + '[' + ofst + ',' + lng + "]="+styleName+"\"" + lastText + '"');
//			}
//		}	
//	};
	
	// ****************************************************************
	// *** Constructor
	// ****************************************************************
	/// Create a new PsiTextPanel.
	/// @param sourceFile the source file
	/// @param popupMenu the popupMenu
	LspTextPanel(final SourceModule sourceModule, final JPopupMenu popupMenu) {
    	super(sourceModule, popupMenu);
//   	this.currentModule = new SourceModule(this, sourceFile);
//    	this.sourceFile=sourceFile;
//    	this.lang=lang;
//    	this.popupMenu=popupMenu;
    	
//    	Palette.updatePalette(null, true);
//    	Palette.init();

        editTextPane = new TooltipTextPane(); editTextPane.setEditable(false);
        editTextPane.addMouseListener(mouseListener);
        ToolTipManager.sharedInstance().registerComponent(editTextPane);
        ToolTipManager.sharedInstance().setDismissDelay(20000); // 20 sekunder
        
        lineNumbers = new TooltipTextPane(); lineNumbers.setEditable(false);
//        lineNumbers.addMouseListener(mouseListener);
//        ToolTipManager.sharedInstance().registerComponent(lineNumbers);
        lineNumbers.setForeground(Palette.TextPaneForeground);
        lineNumbers.setBackground(Palette.TextPaneBackground);
        
        JPanel extra=new JPanel();
        
        doc=new DefaultStyledDocument();
//        setTabStopsToSourceDocument(doc);
        addStylesToSourceDocument(doc);
        
        doc.putProperty(DefaultEditorKit.EndOfLineStringProperty,"\n");
    	doc.addUndoableEditListener(undoListener);
    	doc.addDocumentListener(documentListener);
        editTextPane.setStyledDocument(doc);
        editTextPane.setEditable(true);
        editTextPane.setForeground(Palette.TextPaneForeground);
        editTextPane.setBackground(Palette.TextPaneBackground);
        
        
        extra.setLayout(new BorderLayout());
        extra.add(lineNumbers,BorderLayout.WEST);
        extra.add(editTextPane,BorderLayout.CENTER);
       
        styleScrollPane = new JScrollPane(extra);        
        styleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        this.setLayout(new BorderLayout());
        this.add(styleScrollPane,BorderLayout.CENTER);
    }

//	public String getText() {
//		try {
//			String text = doc.getText(0, doc.getLength());
////			IO.println("PsiTextPanel.getText: |" + text.replace("\r", "\\r").replace("\n", "\\n")+'|');
//			return text;
//		} catch (BadLocationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
    
	// ****************************************************************
	// *** fillTextPane  -- SEE: SemanticTextReconstructor.reconstruct  LspTextPanel.fillTextPane
	// ****************************************************************
    /// Fill the text pane with text delivered from the psiTree.
    /// @param caretPosition the caretPosition after the operations
    /// @param preScanner the scanner to use
    /// @throws IOException 
    private static final String NEWLINE = "\r\n";
    void fillTextPane(int caretPosition, SemanticTokens tokenList) throws IOException {
		StyledDocument lin = new DefaultStyledDocument();
		addStylesToLineDocument(lin);
        editTextPane.setEditable(false);
    	doc.removeUndoableEditListener(undoListener);
		try {
			doc.remove(0, doc.getLength());
			Set<String> errorLines = null;
			
//			tokenList.fillLineAndTextPanel(this, lin, doc, styleLineNumber);
			String originalText = sourceModule.getOriginalText();
//			String originalText = sourceModule.getUpdatedText();

	    	List<Integer> semanticTokens = tokenList.tokens;
	        int sourcePos = 0;
	        int lineNumber = 1;
	        int prevTextLength = 0;
	
	 		IO.println("\nTokenListVerifyer.verifyTokenList: SOURCE:"+Util.printable(originalText));
	        int x = 0;
	        int lexTokenIndex = 0;
			while(x < semanticTokens.size()) {
	            int deltaLine = semanticTokens.get(x++);
	            int deltaStartChar = semanticTokens.get(x++);
	            int length = semanticTokens.get(x++);
	            int tokenTypeIndex = semanticTokens.get(x++);
	            @SuppressWarnings("unused")
				int tokenModifiersBitmask = semanticTokens.get(x++);
	            if (deltaLine > 0) {
	            	while((deltaLine--) > 0) {
						String lineString = edLineNumber(lineNumber++);
						SimpleAttributeSet attrs = getTooltipAttrs(lineNumber, errorLines);
						if(attrs != null) {
							lin.insertString(lin.getLength(),lineString, attrs);					    		
							errorLines = null;
						} else lin.insertString(lin.getLength(),lineString, styleLineNumber);
						if(lineNumber > 500) Global.currentModule.AUTO_REFRESH = false;
	            		
	                    // result.append(NEWLINE);
	    				doc.insertString(doc.getLength(), NEWLINE, styleRegular);				    		
	                    
	            		IO.println("APPEND tokenText|" + Util.printable(NEWLINE) + "| ==> |" + Util.printable(NEWLINE) + '|');
	               	    sourcePos += NEWLINE.length();
	            	}
	                prevTextLength = 0;
	        		IO.println("\nStart NEWLINE:" + lineNumber + " sourcePos="+sourcePos+", TAIL|"+Util.printable(originalText.substring(sourcePos)));
	            }
	
	            // 3. Pad missing characters on the current line
	            int gap = deltaStartChar - prevTextLength;
	            if(gap != 0) {
	        		IO.println("\nPAD SPACE Characters: gap = " + gap);  
	        		while((gap--) > 0) {
	        			
	                    // result.append(" ");
	    				doc.insertString(doc.getLength(), " ", styleRegular);				    		
	
	                    
	            		IO.println("APPEND tokenText| | ==> |" + Util.printable(" ") + '|');
	            	    sourcePos++;
	            		IO.println("UPDATE LINE: sourcePos="+sourcePos+", TAIL|"+Util.printable(originalText.substring(sourcePos)));
	        		}
	            }
	
	            // 4. Insert the token text
	    		IO.println("\nINSERT TEXT: length = " + length + ", TAIL|"+Util.printable(originalText.substring(sourcePos)));
	            String tokenText = originalText.substring(sourcePos, sourcePos + length);
					
//		        result.append(tokenText);
//				errorLines = lexToken.accumErrors(errorLines);
//				SimpleAttributeSet attrs = lexToken.getTooltipAttrs(null);
//				if(attrs != null) {
//					doc.insertString(doc.getLength(), tokenText, attrs);
//				} else {
					doc.insertString(doc.getLength(), tokenText, getStyle(tokenTypeIndex));				    		
//				}

		            
		            
//		    	IO.println("APPEND tokenText|" + Util.printable(tokenText) + "| ==> |" + Util.printable(""+result) + '|');
		           sourcePos += length;

		        prevTextLength = length;
		    }
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert text into text pane.");
		}
		lineNumbers.setStyledDocument(lin);
    	doc.addUndoableEditListener(undoListener);
        editTextPane.setEditable(true);
	    editTextPane.setCaretPosition(caretPosition);
        setTabStopsToSourceDocument(editTextPane, doc);
    }
    
    private void setTabStopsToSourceDocument(JTextPane textPane, StyledDocument doc) {
    	int charactersPerTab = 4;
    	FontMetrics metrics = textPane.getFontMetrics(textPane.getFont());
    	// Calculate the pixel width of a single character
    	int chareWidth = metrics.charWidth('X'); 
    	IO.println("chareWidth: "+chareWidth);
    	int tabWidth = chareWidth * charactersPerTab;
    	// Create an array of sequential tab stops 
    	TabStop[] tabs = new TabStop[100]; 
    	for (int i = 0; i < tabs.length; i++) {
    	    tabs[i] = new TabStop((i + 1) * tabWidth);
    	}
    	TabSet tabSet = new TabSet(tabs);
    	SimpleAttributeSet attributes = new SimpleAttributeSet();
    	StyleConstants.setTabSet(attributes, tabSet);

    	// Update document attributes
    	doc.setParagraphAttributes(0, doc.getLength(), attributes, false);

    }

//    private void addLine(StyledDocument lin, int currentLine, Set<String> errorLines) throws BadLocationException {
//		String lineString = edLineNumber(currentLine+1);
//		// Should only be here AFTER a complete line is rendered
//		SimpleAttributeSet attrs = getTooltipAttrs(currentLine, errorLines);
//		if(attrs != null) {
//			lin.insertString(lin.getLength(),lineString, attrs);					    		
//			errorLines = null;
//		} else {
//			lin.insertString(lin.getLength(),lineString, styleLineNumber);
//		}
//    	
//    }

	// ****************************************************************
	// *** doRefresh
	// ****************************************************************
    /// Do refresh action.
    @Override
	public void doRefresh() {
	    int pos=editTextPane.getCaretPosition();
	    String txt=editTextPane.getText();
	    if(!txt.endsWith("\n")) txt=txt+'\n';
	    int count=countExtraControlCharacters(txt,pos);
    	int maxCaret=txt.length()-1;
    	if(pos>maxCaret) pos=maxCaret;
    	if(pos<0) pos=0;
    	
//	    fillTextPane(new StringReader(txt),pos+count);
    	int caretPosition = pos+count;
    	
		Global.currentModule.buildInitialTokenList();
		SemanticTokens psiTree = Global.currentModule.getTokenList();
		
        fillTextPane(caretPosition, psiTree);
//    	Util.IERR("DETTE MÅ RETTES - ");
//    	Util.STOP();
	}
    
    
	// ****************************************************************
	// *** Utilities
	// ****************************************************************
	public SimpleAttributeSet getTooltipAttrs(int lineNumber, Set<String> errorLines) {
	    	
//	    	if(errorLines != null || getErrors() != null) {
//	    		IO.println("\n\nPsiTextPanel.getTooltipText: BEGIN: errorLines: "+errorLines);
//	    		IO.println("PsiTextPanel.getTooltipText: BEGIN: lexErrors: "+getErrors());
//	    	}
	    	errorLines = accumErrors(lineNumber, errorLines);
	    	
	    	if(errorLines == null) return null;
//	    	IO.println("PsiTextPanel.getTooltipText: RENDER: errorLines: "+errorLines);
	    	
	    	String tooltipText = null;
	    	if(errorLines.size() == 1) {
//	    		tooltipText = errorLines.firstElement();
	    		for(String msg:errorLines) {
	    			tooltipText = msg;
	    		}    		
	    	} else {
	    		String res = "<html>Multiple markers on this line:<ul>";
	    		for(String msg:errorLines) {
	    			res = res + "<li>" + msg + "</li>";
	    		}
//	        	IO.println("PsiTextPanel.getTooltipText: RESULT: "+res);
	        	tooltipText = res + "</ul>";
	    	}

			SimpleAttributeSet attrs = new SimpleAttributeSet();
	        StyleConstants.setFontFamily(attrs, "Courier New");
			StyleConstants.setForeground(attrs, Palette.ErrorForeground);
			StyleConstants.setBackground(attrs, Palette.ErrorBackground);
	        StyleConstants.setBold(attrs, true);
			attrs.addAttribute("tooltip", tooltipText);
	    	return attrs;
	    }


	    public Set<String> accumErrors(int lineNumber, Set<String> errorLines) {
	    	Set<String> errors = null; // getErrors(lineNumber);
	    	if(errors != null) {
	    		if(errorLines == null) errorLines = new HashSet<String>();
	    		errorLines.addAll(errors);
	    	}
	    	return errorLines;
	    }

	// ****************************************************************
	// *** Utilities
	// ****************************************************************

	/// Utility: Edit right justified line number string.
	/// 
	/// @param n the length of line number field
	/// @return the resulting line number string
    private String edLineNumber(int n) {
	    String fill="";
	    if(n<10) fill="   ";
	    else if(n<100) fill="  ";
	    else if(n<1000) fill=" ";
    	return(fill+n+": \n");
    }
	
    /// Utility: Count extra control characters in the given string
    /// @param s the given string
    /// @param pos limitin position in s
    /// @return the resulting number of control characters
 	private int countExtraControlCharacters(final String s,int pos) {
		int count=0;
		for(int i=0;i<pos;i++) {
			if(s.charAt(i)=='\r') { count++; pos++; }
		}
		return(count);
	}
    
//	/// Utility: Get Style
//	/// @param code style code
//	/// @return the resuting Style
//    private Style getStyle(final Token.StyleCode code) {
//    	switch(code) {
//    		case regular: return(styleRegular);
//    		case keyword: return(styleKeyword);
//    		case comment: return(styleComment);
//    		case constant: return(styleConstant);
////    		case XlineNumber: return(styleLineNumber);
//    	}
//    	return(null);
//    }
    
    /// Add Styles to the line number document.
    /// @param doc the document
    private void addStylesToLineDocument(final StyledDocument doc) {
        //Initialize some styles.
        Style defaultStyle = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);
        
        Style regular = doc.addStyle(styleNameRegular, defaultStyle);
        StyleConstants.setFontFamily(defaultStyle, "Courier New");
 
        Style s = doc.addStyle(styleNameError, regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s,new Color(0xff0000));
        StyleConstants.setUnderline(s, true);

        s = doc.addStyle("lineNumber", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s,new Color(204,204,255));
        StyleConstants.setForeground(s,Palette.LineNumberForeground);
        
//        styleError=doc.getStyle(styleNameError);
        styleLineNumber=doc.getStyle("lineNumber");
    }

    
    /// Add Styles to the source text document.
    /// @param doc the document
    @Override
    protected void addStylesToSourceDocument(final StyledDocument doc) {
        //Initialize some styles.
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        
        Style regular = doc.addStyle(styleNameRegular, defaultStyle);
        StyleConstants.setFontFamily(defaultStyle, "Courier New");
 
        Style s = doc.addStyle(styleNameComment, regular);
        StyleConstants.setItalic(s, true);
        StyleConstants.setForeground(s, Palette.CommentForeground);

        s = doc.addStyle(styleNameKeyword, regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s, Palette.KeywordForeground);

        s = doc.addStyle(styleNameConstant, regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s, Palette.ConstantForeground);

        s = doc.addStyle(styleNameClassIdent, regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s, Palette.ClassIdentForeground);

        s = doc.addStyle(styleNameProcedure, regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s, Palette.ProcedureForeground);

        s = doc.addStyle(styleNameError, regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s,Palette.ErrorForeground);
        StyleConstants.setBackground(s,Palette.ErrorBackground);
        StyleConstants.setUnderline(s, true);

//        s = doc.addStyle("lineNumber", regular);
//        StyleConstants.setBold(s, true);
//        StyleConstants.setForeground(s,new Color(204,204,255));
        
        styleRegular = doc.getStyle(styleNameRegular);
        styleKeyword = doc.getStyle(styleNameKeyword);
        styleComment = doc.getStyle(styleNameComment);
        styleConstant = doc.getStyle(styleNameConstant);
    	styleClassIdent = doc.getStyle(styleNameClassIdent);
    	styleProcedure = doc.getStyle(styleNameProcedure);
    	styleError=doc.getStyle(styleNameError);
//        styleLineNumber=doc.getStyle("lineNumber");
    }
 
    /// NOTE: SEE: simula.core.builder.export.TokenManager
	public static Style getStyle(int tokenTypeIndex) {
		switch(tokenTypeIndex) {
			case TokenManager.SimulaTokenKeyword:	 return styleKeyword;
		    case TokenManager.SimulaTokenClass:	     return styleClassIdent;
		    case TokenManager.SimulaTokenAttribute:  return styleRegular;
		    case TokenManager.SimulaTokenProcedure:  return styleProcedure;
		    case TokenManager.SimulaTokenVariable:   return styleRegular;
		    case TokenManager.SimulaTokenParameter:  return styleRegular;
		    case TokenManager.SimulaTokenString:     return styleConstant;
		    case TokenManager.SimulaTokenCharacter:  return styleConstant;
		    case TokenManager.SimulaTokenNumber:     return styleConstant;
		    case TokenManager.SimulaTokenOperator:   return styleRegular;
		    case TokenManager.SimulaTokenLabel:      return styleRegular;
		    case TokenManager.SimulaTokenComment:    return styleComment;
		    case TokenManager.SimulaTokenWhiteSpace: return styleRegular;
		    case TokenManager.SimulaTokenSymbol:     return styleRegular;
		}
		return styleRegular;
	}	

    @Override
    public String toString() {
    	String s="SourceTextPanel(";
//        s=s+currentModule.getName();
//    	if(this.AUTO_REFRESH) s=s+",AUTO_REFRESH";
    	s=s+')';
    	return(s);
    }
 
}

