/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package sim.editor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import sim.compiler.Util;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;

/// A Console panel.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/EclipseWorkSpaces/blob/main/SimulaCompiler2/Simula/src/simula/compiler/utilities/ConsolePanel.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public final class ConsolePanel extends JPanel {
	
	public static ConsolePanel current;

	/// The text pane.
	private static JTextPane textPane;

	/// the StyledDocument showed in this panel
	private StyledDocument doc;

	/// Regular style
	private Style styleRegular;

	/// Warning style
	private Style styleWarning;

	/// Error style
	private Style styleError;

	/// the Popup Menu
	private JPopupMenu popupMenu;

	/// Menu item clear
	private JMenuItem clearItem;

	/// Menu item copy
	private JMenuItem copyItem;

	/// Used by KeyListener and read()
	private boolean reading;

	/// Used by KeyListener and read()
	private char keyin;

	/// the Reader to read input from the console
	private Reader consoleReader;

	// ****************************************************************
	// *** ConsolePanel: Main Entry for TESTING ONLY
	// ****************************************************************
    /// SimulaEditor: Main Entry for TESTING ONLY.
    /// @param args the arguments
     public static void main(String[] args) {
    	 JFrame frame = new JFrame("Testing");
         frame.setSize(800, 1000);
         frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         frame.setLocationRelativeTo(null); // center the frame on screen

         ConsolePanel console = new ConsolePanel();
     	 frame.add(console);
    	 console.write("BEGIN TESTING ConsolePanel\n");
    	 frame.setVisible(true);
    	 
    	 Writer writer = console.getWriter();
    	 LineNumberReader lineRreader = new LineNumberReader(console.getReader());
    	 LOOP:for(int i=0;i<50;i++) {
	    	 try {
	    		 writer.write("Input line:"); writer.flush();
	    		 String line = lineRreader.readLine();
	    		 if(line.length() == 0) {
		    		 writer.write("ALL READING DONE");
	    			 break LOOP;
	    		 }
	    		 IO.println("READING DONE: " + line);
	    	 } catch (IOException e) {}
    	 }

    	 while(true)
			try { Thread.sleep(10000); } catch (InterruptedException e) {} 
     }
	
	/// Create a new ConsolePanel.
	public ConsolePanel() {
		super(new BorderLayout());
		current = this;
		JScrollPane scrollPane;
		textPane = new JTextPane();
		textPane.addMouseListener(mouseListener);
		scrollPane = new JScrollPane(textPane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		doc = new DefaultStyledDocument();
		addStylesToDocument(doc);
		doc.putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
		textPane.setStyledDocument(doc);

		textPane.addKeyListener(listener);
		textPane.setFocusable(true);
		
		textPane.setEditable(false);
		popupMenu = new JPopupMenu();
		clearItem = new JMenuItem("Clear Console");
		// clearItem.setAccelerator(KeyStroke.getKeyStroke('X',
		// InputEvent.CTRL_DOWN_MASK));
		popupMenu.add(clearItem);
		clearItem.addActionListener(actionListener);
		copyItem = new JMenuItem("Copy to Clipboard");
		copyItem.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
		popupMenu.add(copyItem);
		copyItem.addActionListener(actionListener);
		this.add(scrollPane);
		this.setFocusable(true);
	}

	/// popup this Console Panel
	public void popup() {
		JFrame frame = new JFrame();
		frame.setSize(1000, 500); // Initial frame size
		frame.setTitle("Runtime Console");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(this);
		frame.setVisible(true);
	}

	/// Reads a single character.
	/// @return The character read
	char read() {
//		textPane.requestFocus();
		textPane.requestFocusInWindow();
		
		reading = true; // Enables KeyListener (see below)
		IO.println("ConsolePanel.read: ");
		while (reading)
			Thread.yield();
		return (keyin);
	}

	/// Get a reader suitable for reading from this panel
	/// @return a reader
	public Reader getReader() {
		if (consoleReader == null) {
			consoleReader = new Reader() {
				@Override
				public int read(final char[] cbuf, final int off, final int len) throws IOException {
					// reading=true;
					int firstPos = textPane.getCaretPosition();
					textPane.setEditable(true);
					// textPane.getCaret().setVisible(true);

					ConsolePanel.this.requestFocusInWindow();
					IO.println("ConsolePanel'read: ");
					
					// while(reading) Thread.yield();
					while (ConsolePanel.this.read() != '\n')
						;

					// textPane.getCaret().setVisible(false);
					textPane.setEditable(false);
					String input = textPane.getText().substring(firstPos);
					int pos = 0;
					for (char c : input.toCharArray())
						cbuf[off + (pos++)] = c;
					return (pos);
				}

				@Override
				public void close() throws IOException {
				}
			};
		}
		return (consoleReader);
	}

	/// Get a InputStream suitable for reading from this panel
	/// @return an InputStream
	public InputStream getInputStream() {
		InputStream in = new InputStream() {
			@Override
			public int read() throws IOException {
				textPane.requestFocus();
				reading = true; // Enables KeyListener (see below)
				while (reading)
					Thread.yield();
				return (keyin);
			}
		    @Override
		    public int read(byte[] b) throws IOException {
		    	Util.IERR("NOT IMPL");
		    	return 0;
//		        return readBytes(b, 0, b.length);
		    }
		    @Override
		    public int read(byte[] b, int off, int len) throws IOException {
		    	Util.IERR("NOT IMPL");
		    	return 0;
//		        return readBytes(b, off, len);
		    }
		    @Override
		    public byte[] readAllBytes() throws IOException {
		    	Util.IERR("NOT IMPL");
				return null;
		    }
		    @Override
		    public byte[] readNBytes(int len) throws IOException {
		    	Util.IERR("NOT IMPL");
				return null;
		    }
		    @Override
		    public long transferTo(OutputStream out) throws IOException {
		    	Util.IERR("NOT IMPL");
				return keyin;
		    }
		    @Override
		    public long skip(long n) throws IOException {
		        return super.skip(n);
		    }
		    @Override
		    public int available() throws IOException {
		    	Util.IERR("NOT IMPL");
		        return 0;
		    }
		    @Override
		    public void close() throws IOException {
		    	Util.IERR("NOT IMPL");
		    }
		};
		return in;
	}

	/// Get a PrintStream suitable for writing on this panel
	/// @return a OutputStream
	public PrintStream getOutputStream() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				String s = "" + (char) b;
//		    	IO.println("ConsolePanel'OutputStream.write: " + s);
				ConsolePanel.this.write(s, styleRegular);
			}
		    @Override
			public void write(byte[] b) throws IOException {
//		    	System.out.print("ConsolePanel'OutputStream.write: b, len="+b.length+" b="); String sep="[";
				for(int i = 0; i < b.length; i++) {
					write(b[i]);
//					System.out.print(sep+(char)b[i]); sep=",";
				}
//				IO.println("]");
//		    	Util.IERR("NOT IMPL");
		    }
		    @Override
		    public void write(byte[] b, int off, int len) throws IOException {
//		    	System.out.print("ConsolePanel'OutputStream.write: b, off="+off+", len="+len+" b="); String sep="[";
				for(int i = 0; i < len; i++) {
		    		write(b[off+i]);
//					System.out.print(sep+(char)b[off+i]); sep=",";
		    	}
//				IO.println("]");
//		    	Util.IERR("NOT IMPL");
		    }
		    @Override
		    public void close() throws IOException {
		    	Util.IERR("NOT IMPL");
		    }
		};
		return new PrintStream(out);
	}

	/// Get a writer suitable for writing on this panel
	/// @return a writer
	public Writer getWriter() {
		return (new Writer() {
			@Override
			public void write(String s) {
				ConsolePanel.this.write(s);
				ConsolePanel.this.repaint();
			}

			public void write(char[] cbuf, int off, int len) throws IOException {
				ConsolePanel.this.write(new String(cbuf, off, len));
				ConsolePanel.this.repaint();
			}

			@Override
			public void flush() throws IOException {
				ConsolePanel.this.repaint();
			}

			@Override
			public void close() throws IOException {
			}
		});
	}

//	/// Get a OutputStream suitable for writing errors on this panel
//	/// @return a OutputStream
//	public PrintStream getErrorStream() {
//		OutputStream out = new OutputStream() {
//			@Override
//			public void write(int b) throws IOException {
//				String s = "" + (char) b;
//				ConsolePanel.this.write(s, styleError);
//			}
//		};
//		return new PrintStream(out);
//	}

	/// Write a string on this panel using styleRegular.
	/// @param s a string to write
	public void write(final String s) {
		write(s, styleRegular);
	}

	/// Write a string on this panel using styleError.
	/// @param s a string to write
	void writeError(final String s) {
		write(s, styleError);
	}

	/// Write a string on this panel using styleWarning.
	/// @param s a string to write
	void writeWarning(final String s) {
		write(s, styleWarning);
	}

	/// Write a styled string onto this Console.
	/// @param s     the string to write
	/// @param style the style
	private void write(final String s, final Style style) {
		try {
			doc.insertString(doc.getLength(), s, style);
		} catch (BadLocationException e) {
			IERR("Impossible", e);
		}
		textPane.setCaretPosition(textPane.getDocument().getLength());
		textPane.repaint();  // TODO ???
//		this.repaint();  // TODO ???
	}

	/// Clear the Console.
	private void clear() {
		try {
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e) {
			IERR("Impossible", e);
		}
		textPane.setCaretPosition(textPane.getDocument().getLength());
		textPane.update(textPane.getGraphics());
	}

	/// Utility method IERR
	/// @param msg error message
	/// @param e   the underlying cause
	private static void IERR(final String msg, final Throwable e) {
		IO.println("IERR: " + msg + "  " + e);
		e.printStackTrace();
	}

	/// Utility to add styles to the document
	/// @param doc the document
	private void addStylesToDocument(final StyledDocument doc) {
		Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", defaultStyle);
		StyleConstants.setFontFamily(defaultStyle, "Courier New");

		Style s = doc.addStyle("warning", regular);
		StyleConstants.setItalic(s, true);
		StyleConstants.setForeground(s, new Color(255, 153, 0));

		s = doc.addStyle("error", regular);
		StyleConstants.setBold(s, true);
		StyleConstants.setForeground(s, Color.RED);

		styleRegular = doc.getStyle("regular");
		styleWarning = doc.getStyle("warning");
		styleError = doc.getStyle("error");
	}

	// ****************************************************************
	// *** MouseListener
	// ****************************************************************
	/// the MouseListener
	MouseListener mouseListener = new MouseListener() {
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}

		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == 3)
				popupMenu.show(textPane, e.getX(), e.getY());
		}
	};

	// ****************************************************************
	// *** ActionListener
	// ****************************************************************
	/// the ActionListener
	ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Object item = e.getSource();
			if (item == clearItem)
				clear();
			else if (item == copyItem) {
				String text = textPane.getSelectedText();
				if (text == null)
					text = textPane.getText();
				StringSelection stringSelection = new StringSelection(text);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		}
	};

	// ****************************************************************
	// *** KeyListener
	// ****************************************************************
	/// the KeyListener
	private KeyListener listener = new KeyListener() {
		public void keyPressed(KeyEvent event) {
//			IO.println("ConsolePanel'keyTyped: "+event);
		}

		public void keyReleased(KeyEvent event) {
//			IO.println("ConsolePanel'keyTyped: "+event);
		}

		public void keyTyped(KeyEvent event) {
//			IO.println("ConsolePanel'keyTyped: "+event);
			if (reading) {
				keyin = event.getKeyChar();
				reading = false;
			}
		}
	};

}