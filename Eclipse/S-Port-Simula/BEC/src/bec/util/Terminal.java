/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.util;

import java.awt.Color;

import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JFrame;

/// Terminal.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/util/Terminal.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public class Terminal extends JFrame {

	/// The text area.
	private static TextArea textArea;

	/// Used by KeyListener and read()
	private boolean reading;

	/// Used by KeyListener and read()
	private char keyin;


	// ****************************************************************
	// *** Main Entry for TESTING ONLY
	// ****************************************************************
    /// SimulaEditor: Main Entry for TESTING ONLY.
    /// @param args the arguments
    /// @throws IOException if an IOException occur
	public static void main(String[] args) throws IOException {
		Terminal terminal = new Terminal("TESTING");
		terminal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		terminal.println("BEGIN TESTING Terminal");
		terminal.print("Input Single Character: ");
		char c = terminal.read();  

		terminal.print("\nInput Text Line terminated by NL: ");
		String s = terminal.readLine();

		terminal.println("Single Character read was: " + c);
		terminal.println("Inputed Text Line was: " + s);

		terminal.print("\nTesting Reader -> Writer:\nInput Text Line terminated by NL: ");
		Reader reader = terminal.getReader();
		Writer writer = terminal.getWriter();
		StringBuilder sb = new StringBuilder();
		c = (char) reader.read();
		while(c != '\n') { sb.append(c); c = (char) reader.read(); }
		writer.write("Reader GOT: " + sb.toString()+'\n'); writer.flush();

		terminal.print("\nTesting InputStream -> OutputStream:\nInput Text Line terminated by NL: ");
		InputStream inpt = terminal.getInputStream();
		OutputStream oupt = terminal.getOutputStream();
		sb = new StringBuilder();
		c = (char) inpt.read();
		while(c != '\n') { sb.append(c); c = (char) inpt.read(); }
		terminal.println("InputStream GOT: "+sb);
		oupt.write(sb.toString().getBytes(), 0, 0); oupt.flush();
		terminal.println("ENDOF TESTING Terminal");
	}
	
	/// Create a new Terminal
	/// @param title the title of the Terminal frame
	public Terminal(String title) {
		setTitle(title);
		setSize(1000, 700);
		setBackground(Color.WHITE);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		textArea = new TextArea("", 50, 138, TextArea.SCROLLBARS_BOTH);
		textArea.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent event) {
				if (reading) {
					keyin = event.getKeyChar();
					reading = false;
				}
			}
		});
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		textArea.setEnabled(true);
		add(textArea);
//		validate();
	    setVisible(true);
	}

	/// Writes a string to this Terminal.
	/// @param s the String to write
	public void print(final String s) {
		write(s);
	}
	
	/// Writes a string to this Terminal and terminate the line.
	/// @param s the String to write
	public void println(final String s) {
		write(s + '\n');
	}

	/// Write a string
	/// @param s the string to write
	public void write(final String s) {
		Terminal.textArea.append(s);
	}

	/// Reads a single character.
	/// @return The character read
	public char read() {
		reading = true; // Enables KeyListener (see below)
		while (reading) Thread.yield();
		return (keyin);
	}

	/// Reads a line 
	/// @return the line String read
	public String readLine() {
		StringBuilder sb = new StringBuilder();
		char c = read();
		while(c != '\n') {
			sb.append(c);
			c = read();
		}
		return sb.toString();
	}

	/// Get a reader suitable for reading from this terminal
	/// @return a reader
	public Reader getReader() {
		Reader reader = new Reader() {
			@Override
			public int read(final char[] cbuf, final int off, final int len) throws IOException {
				cbuf[off] = Terminal.this.read(); return (1);
			}

			@Override
			public void close() throws IOException {}
		};
		return (reader);
	}

	/// Get a InputStream suitable for reading from this terminal
	/// @return a OutputStream
	public InputStream getInputStream() {
		InputStream inpt = new InputStream() {
			@Override
			public int read() throws IOException {
				return Terminal.this.read();
			}
		};
		return inpt;
	}

	/// Get a PrintStream suitable for writing on this terminal
	/// @return a OutputStream
	public PrintStream getOutputStream() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				String s = "" + (char) b;
				Terminal.this.write(s);
			}
		};
		return new PrintStream(out);
	}

	/// Get a writer suitable for writing on this terminal
	/// @return a writer
	public Writer getWriter() {
		return (new Writer() {
			@Override
			public void write(String s) {
				Terminal.this.write(s);
			}

			public void write(char[] cbuf, int off, int len) throws IOException {
				Terminal.this.write(new String(cbuf, off, len));
			}

			@Override
			public void flush() throws IOException {
			}

			@Override
			public void close() throws IOException {
			}
		});
	}
	
}
