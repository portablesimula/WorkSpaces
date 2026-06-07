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
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import simula.compiler.ModuleManager;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.editor.SimulaEditor.Language;
import simula.psi.LexToken;
import simula.psi.PsiElement;
import simula.psi.PsiTree;
import simula.psi.PsiTreeIterator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Set;
import java.util.Vector;

/// The Source text Panel.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/editor/SourceTextPanel.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
/// @author Google AI
@SuppressWarnings("serial")
public class PsiTextPanel extends JPanel {
	/// DEBUG on/off
	private static final boolean DEBUG=false;//true;
	
	public ModuleManager moduleManager;

	/// The line number side-panel.
	private JTextPane lineNumbers;
	
	/// The ScrollPane
	private JScrollPane styleScrollPane;

	/** Style name */ public final static String styleNameRegular = "regular";
	/** Style name */ public final static String styleNameKeyword = "keyword";
	/** Style name */ public final static String styleNameComment = "comment";
	/** Style name */ public final static String styleNameConstant = "constant";
	/** Style name */ public final static String styleNameClassIdent = "classIdent";
	/** Style name */ public final static String styleNameProcedure = "procedure";
	/** Style name */ public final static String styleNameError = "error";
 	
	/** Style */ public Style styleRegular;
	/** Style */ public Style styleKeyword;
	/** Style */ public Style styleComment;
	/** Style */ public Style styleConstant;
	/** Style */ public Style styleClassIdent;
	/** Style */ public Style styleProcedure;
	/** Style */ public Style styleError;
	/** Style */ private Style styleLineNumber;
	
	/// The StyledDocument.
	public StyledDocument doc;
	
	/// Editable text pane with undo/redo history.
	JTextPane editTextPane;
	
	/// Current language.
    SimulaEditor.Language lang;
	
	/// Signals auto refresh.
    boolean AUTO_REFRESH=true;//false;

	/// The undo manager.
	private UndoManager undoManager = new UndoManager();
	
	/// Returns the undo manager.
	/// @return the undo manager
	UndoManager getUndoManager() { return(undoManager); }
	
    /// Indicates that the source file has changed.
    boolean fileChanged = false;
    
    /// Indicates that refresh is needed.
    boolean refreshNeeded = false;

	// ****************************************************************
	// *** UndoableEditListener
	// ****************************************************************
    /// The UndoableEditListener.
    private UndoableEditListener undoListener=new UndoableEditListener() {
		public void undoableEditHappened(UndoableEditEvent e) {
			UndoableEdit edit=e.getEdit();
			undoManager.addEdit(edit);
			fileChanged=true; refreshNeeded=true;
			SimulaEditor.menuBar.updateMenuItems();
		}
	};

	// ****************************************************************
	// *** MouseListener
	// ****************************************************************
	/// The MouseListener.
//    MouseListener mouseListener = new MouseListener() {
//		public void mousePressed(MouseEvent e) {}
//		public void mouseReleased(MouseEvent e) {}
//		public void mouseEntered(MouseEvent e) {}
//		public void mouseExited(MouseEvent e) {}
//		public void mouseClicked(MouseEvent e) {
//    	    if(e.getButton()==3) popupMenu.show(editTextPane,e.getX(),e.getY());
//    	}
//    };

	// ****************************************************************
	// *** DocumentListener
	// ****************************************************************
    /// The DocumentListener.
	DocumentListener documentListener=new DocumentListener() {
		public void insertUpdate(DocumentEvent e)  { debugTrace("Insert",e); }
		public void removeUpdate(DocumentEvent e)  { debugTrace("Remove",e); }
		public void changedUpdate(DocumentEvent e) { debugTrace("Changed",e); }
		
		private void debugTrace(String id,DocumentEvent evt) {
			if(DEBUG) {
			    int ofst=evt.getOffset();
			    int lng=evt.getLength();
			    String styleName="UNKNOWN";
			    String lastText="UNKNOWN";
				try { // debugTrace
				    StyledDocument doc=(StyledDocument)editTextPane.getDocument();
				    if(id.equals("Insert")) lastText= doc.getText(ofst,lng);
				    if(id.equals("addition")) lastText= doc.getText(ofst,lng);
				    Element elt=doc.getCharacterElement(ofst);
				    if(elt instanceof LeafElement leaf) {
					    styleName=(String)leaf.getAttribute(StyleConstants.NameAttribute);
				    }
				    lastText=lastText.replace("\n","\\n");
				} catch (Exception ex) { Util.IERR("Impossible",ex); }			
				IO.println("DocumentListener: "+id + '[' + ofst + ',' + lng + "]="+styleName+"\"" + lastText + '"');
			}
		}	
	};
	
	// ****************************************************************
	// *** Constructor
	// ****************************************************************
	/// Create a new PsiTextPanel.
	/// @param sourceFile the source file
	/// @param lang the language
	/// @param popupMenu the popupMenu
//    PsiTextPanel(File sourceFile, SimulaEditor.Language lang, JPopupMenu popupMenu) {
//    PsiTextPanel(PsiTree psiTree, Language lang, JPopupMenu popupMenu) {
    PsiTextPanel(Language lang, JPopupMenu popupMenu) {
//   	this.moduleManager = new ModuleManager(this, sourceFile);
//    	this.sourceFile=sourceFile;
//    	this.lang=lang;
//    	this.popupMenu=popupMenu;
    	
//    	Palette.updatePalette(null, true);
//    	Palette.init();

        editTextPane = new TooltipTextPane(); editTextPane.setEditable(false);
//        editTextPane.addMouseListener(mouseListener);
        ToolTipManager.sharedInstance().registerComponent(editTextPane);
        ToolTipManager.sharedInstance().setDismissDelay(20000); // 20 sekunder
        
        lineNumbers = new TooltipTextPane(); lineNumbers.setEditable(false);
//        lineNumbers.addMouseListener(mouseListener);
//        ToolTipManager.sharedInstance().registerComponent(lineNumbers);
        lineNumbers.setForeground(Palette.TextPaneForeground);
        lineNumbers.setBackground(Palette.TextPaneBackground);
        
        JPanel extra=new JPanel();
        
        doc=new DefaultStyledDocument(); addStylesToSourceDocument(doc);
        
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
    

	public String getText() {
		try {
			String text = doc.getText(0, doc.getLength());
			IO.println("SourceTextPanel.getText: |" + text.replace("\r", "\\r").replace("\n", "\\n")+'|');
			return text;
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
	// ****************************************************************
	// *** fillTextPane
	// ****************************************************************
    /// Fill the text pane with text delivered from the scanner.
    /// @param caretPosition the caretPosition after the operations
    /// @param preScanner the scanner to use
    void fillTextPane(int caretPosition, PsiTree psiTree) {
		int lineNumber=1;
		StyledDocument lin = new DefaultStyledDocument(); addStylesToLineDocument(lin);
        editTextPane.setEditable(false);
    	doc.removeUndoableEditListener(undoListener);
		try {
			doc.remove(0, doc.getLength());
			
			boolean TESTING = false;
			if(TESTING) {
//				PsiTreeIterator.TRACING = true;
				psiTree.printPsiTree("++++++++++++++++ PSI TREE ++++++++++++++++++");
				PsiTreeIterator itr = new PsiTreeIterator(psiTree);
				while (itr.hasNext()) {
					PsiElement elt = itr.next();
//					IO.println("PsiTextPanel.fillTextPane: GOT NEXT: " + elt.edText());
					IO.println("PsiTextPanel.fillTextPane: GOT NEXT: " + elt);
				}
				PsiTreeIterator.TRACING = false;
			}
			
			PsiTreeIterator iterator = new PsiTreeIterator(psiTree);
			
			Set<String> errorLines = null;
			// Standard traversal loop
			while (iterator.hasNext()) {
			    PsiElement elt = iterator.next();
//				IO.println("PsiTextPanel.fillTextPane: GOT NEXT: " + elt);
			    if(elt != null) {
			    	if(elt instanceof LexToken lexToken) {
			    		if(lexToken.keyWord == KeyWord.EOF) {
				    	    SimpleAttributeSet attrs = lexToken.getTooltipAttrs(errorLines);
					    	if(attrs != null) {
			    				lin.insertString(lin.getLength(),edLineNumber(lineNumber++), attrs);					    		
			    				errorLines = null;
					    	}
			    		} else if(lexToken.keyWord == KeyWord.NEWLINE) {
							if(Option.PSI_VERIFY && elt.firstLineNumber() != lineNumber) {
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
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert text into text pane.");
		}
		if(lineNumber>500) this.AUTO_REFRESH=false;
		lineNumbers.setStyledDocument(lin);
    	doc.addUndoableEditListener(undoListener);
        editTextPane.setEditable(true);
	    editTextPane.setCaretPosition(caretPosition);
    }

	// ****************************************************************
	// *** doRefresh
	// ****************************************************************
    /// Do refresh action.
	void doRefresh() {
	    int pos=editTextPane.getCaretPosition();
	    String txt=editTextPane.getText();
	    if(!txt.endsWith("\n")) txt=txt+'\n';
	    int count=countExtraControlCharacters(txt,pos);
    	int maxCaret=txt.length()-1;
    	if(pos>maxCaret) pos=maxCaret;
    	if(pos<0) pos=0;
    	
//	    fillTextPane(new StringReader(txt),pos+count);
    	Util.IERR("DETTE MÅ RETTES - ");
    	Util.STOP();
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
    private void addStylesToSourceDocument(final StyledDocument doc) {
        //Initialize some styles.
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        
        Style regular = doc.addStyle(styleNameRegular, defaultStyle);
        StyleConstants.setFontFamily(defaultStyle, "Courier New");
 
        Style s = doc.addStyle(styleNameComment, regular);
        StyleConstants.setItalic(s, true);
        StyleConstants.setForeground(s, Palette.CommentForeground);

        s = doc.addStyle(styleNameKeyword, regular);
        StyleConstants.setBold(s, true);
        IO.println("PsiTextPanel.addStylesToSourceDocument: KeywordForeground: " + Palette.toHex(Palette.KeywordForeground));
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
    
    @Override
    public String toString() {
    	String s="SourceTextPanel(";
        s=s+moduleManager.getName();
    	if(this.AUTO_REFRESH) s=s+",AUTO_REFRESH";
    	s=s+')';
    	return(s);
    }
 
}

