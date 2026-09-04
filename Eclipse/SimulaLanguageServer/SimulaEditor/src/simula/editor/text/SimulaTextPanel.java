/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.editor.text;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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

import simula.Comn;
import simula.core.builder.export.LexToken;
import simula.core.builder.export.TokenManager;
import simula.editor.DiagnosticHandler;
import simula.editor.Palette;
import simula.editor.SourceModule;
import simula.editor.SimulaEditor.Language;
import simula.editor.utilities.ConsolePanel;
import simula.editor.utilities.Global;
import simula.editor.utilities.Option;
import simula.editor.utilities.Util;

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
public class SimulaTextPanel extends TabTextPanel {
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
	
	private static boolean TESTING_CONSOLE = true;
	// ****************************************************************
	// *** Constructor
	// ****************************************************************
	/// Create a new SimulaTextPanel.
	/// @param sourceFile the source file
	/// @param popupMenu the popupMenu
	public SimulaTextPanel(final SourceModule sourceModule, final JPopupMenu popupMenu) {
    	super(sourceModule, popupMenu);
//    	open();
	}

	@Override
	public void open() {
        editTextPane = new TooltipTextPane(); editTextPane.setEditable(false);
        editTextPane.addMouseListener(mouseListener);
        ToolTipManager.sharedInstance().registerComponent(editTextPane);
        ToolTipManager.sharedInstance().setDismissDelay(20000); // 20 sekunder
        
        lineNumbers = new TooltipTextPane(); lineNumbers.setEditable(false);
        lineNumbers.setForeground(Palette.TextPaneForeground);
        lineNumbers.setBackground(Palette.TextPaneBackground);
        
        JPanel tabContent=new JPanel();
        
        doc = new DefaultStyledDocument();
        addStylesToSourceDocument(doc);
        
        doc.putProperty(DefaultEditorKit.EndOfLineStringProperty,"\n");
    	doc.addUndoableEditListener(undoListener);
    	doc.addDocumentListener(documentListener);
        editTextPane.setStyledDocument(doc);
        editTextPane.setEditable(true);
        editTextPane.setForeground(Palette.TextPaneForeground);
        editTextPane.setBackground(Palette.TextPaneBackground);
        
        tabContent.setLayout(new BorderLayout());
        tabContent.add(lineNumbers,BorderLayout.WEST);
        tabContent.add(editTextPane,BorderLayout.CENTER);
//        tabContent.add(console,BorderLayout.SOUTH);
       
        styleScrollPane = new JScrollPane(tabContent);        
        styleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        if(TESTING_CONSOLE) {
        	ConsolePanel console = Global.console;
            JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, // Stack panels vertically
                styleScrollPane,           // Top component
                console                    // Bottom component
            );
            splitPane.setResizeWeight(0.75);
            splitPane.setOneTouchExpandable(true); // Add collapse/expand arrows to divider
        	
	        this.setLayout(new BorderLayout());
	        this.add(splitPane,BorderLayout.CENTER);        	
        } else {
	        this.setLayout(new BorderLayout());
	        this.add(styleScrollPane,BorderLayout.CENTER);
        }
    }

//	public String getText() {
//		try {
//			String text = doc.getText(0, doc.getLength());
////			IO.println("SimulaTextPanel.getText: |" + text.replace("\r", "\\r").replace("\n", "\\n")+'|');
//			return text;
//		} catch (BadLocationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
    
	private static boolean TESTING = false;
	private static boolean TRACE = false;//true;
	// ****************************************************************
	// *** fillTextPane  -- SEE: TokenListVerifyer.reconstruct
	// ****************************************************************
    /// Fill the text pane with text delivered from the semTokens.
    /// @param caretPosition the caretPosition after the operations
    /// @param preScanner the scanner to use
    /// @throws IOException 
    public void fillTextPane(int caretPosition, List<Integer> semTokens) throws IOException {
		StyledDocument lin = new DefaultStyledDocument();
		addStylesToLineDocument(lin);
        editTextPane.setEditable(false);
    	doc.removeUndoableEditListener(undoListener);
		try {
			doc.remove(0, doc.getLength());
//			Set<String> errorLines = null;
			String modifiedText = sourceModule.getModifiedText();
//	    	List<Integer> semTokens = tokenList.tokens;
	        int sourcePos = 0;
	        int prevTextLength = 0;
            DiagnosticHandler diagnosticHandler = sourceModule.diagnosticHandler;

	        int lineNumber = 0;
	        doRenderLine(diagnosticHandler, lin, lineNumber++);

	 		IO.println("\nLspTextPanel.fillTextPane: SOURCE:"+Comn.printable(modifiedText));
	        int x = 0;
	        int lexTokenIndex = 0;
	        int absColumn = 0;
			while(x < semTokens.size()) {
	            int deltaLine = semTokens.get(x++);
	            int deltaStartChar = semTokens.get(x++);
	            int length = semTokens.get(x++);
	            int tokenTypeIndex = semTokens.get(x++);
	            @SuppressWarnings("unused")
				int tokenModifiersBitmask = semTokens.get(x++);
	            if (deltaLine > 0) {
            		absColumn = deltaStartChar;
	            	while((deltaLine--) > 0) {
	            		doRenderLine(diagnosticHandler, lin, lineNumber++);
	                    // result.append(NEWLINE);
	    				doc.insertString(doc.getLength(), "\n", styleRegular);				    		
	            		if(TRACE) IO.println("APPEND tokenText|" + Comn.printable('\n') + "| ==> |" + Comn.printable(doc.getText(0, doc.getLength())) + '|');
	               	    sourcePos ++;
	            	}
	                prevTextLength = 0;
	                if(TESTING) IO.println("\nStart NEWLINE:" + lineNumber + " sourcePos="+sourcePos+", TAIL|"+Comn.printable(modifiedText.substring(sourcePos)));
	            } else {
            		absColumn += deltaStartChar;
	            }
	
	            // 3. Pad missing characters on the current line
	            int gap = deltaStartChar - prevTextLength;
	            if(gap != 0) {
	            	if(TESTING) IO.println("\nPAD SPACE Characters: gap = " + gap);  
	        		while((gap--) > 0) {
	        			
	                    // result.append(" ");
	    				doc.insertString(doc.getLength(), " ", styleRegular);				    		
	            		if(TRACE) IO.println("APPEND tokenText| | ==> |" + Comn.printable(doc.getText(0, doc.getLength())) + '|');
	            	    sourcePos++;
	            	    if(TESTING) IO.println("UPDATE LINE: sourcePos="+sourcePos+", TAIL|"+Comn.printable(modifiedText.substring(sourcePos)));
	        		}
	            }
	
	            // 4. Insert the token text
	            if(TESTING) IO.println("\nINSERT TEXT: length = " + length + ", TAIL|"+Comn.printable(modifiedText.substring(sourcePos)));
	            String tokenText = modifiedText.substring(sourcePos, sourcePos + length);
				SimpleAttributeSet attrs = diagnosticHandler.getTokenHoverAttrs(lineNumber-1, absColumn, length);
				if(attrs != null) {
					doc.insertString(doc.getLength(), tokenText, attrs);
				} else {
					doc.insertString(doc.getLength(), tokenText, getStyle(tokenTypeIndex));				    		
            		if(TRACE) IO.println("APPEND tokenText|" + Comn.printable(tokenText) + "| ==> |" + Comn.printable(doc.getText(0, doc.getLength())) + '|');
				}
//		    	IO.println("APPEND tokenText|" + Comn.printable(tokenText) + "| ==> |" + Comn.printable(""+result) + '|');
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
    
	private void doRenderLine(DiagnosticHandler diagnosticHandler, StyledDocument lin, int lineNumber) throws BadLocationException {
		SimpleAttributeSet attrs = diagnosticHandler.getLineHoverAttrs(lineNumber);
		String lineString = edLineNumber(lineNumber+1);
		if(attrs != null) {
			lin.insertString(lin.getLength(),lineString, attrs);					    		
//			errorLines = null;
		} else lin.insertString(lin.getLength(), lineString, styleLineNumber);
		if(lineNumber > 500) Global.currentModule.AUTO_REFRESH = false;
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
    	
//		Global.currentModule.doOpenSimulaModule();
    	List<Integer> semTokens = Global.currentModule.getSemTokens();
		
        try {
			fillTextPane(caretPosition, semTokens);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	Util.IERR("DETTE MÅ RETTES - ");
//    	Util.STOP();
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

