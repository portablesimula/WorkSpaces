/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;

import simula.compiler.utilities.Option;

/// System class Infile.
/// 
/// <pre>
/// imagefile class infile;
///     begin Boolean ENDFILE;
///        Boolean procedure endfile;  endfile:= ENDFILE;
///        Boolean procedure open(fileimage); text fileimage;
///        Boolean procedure close;
///        procedure inimage; 
///        Boolean procedure inrecord; 
///        character procedure inchar; 
///        Boolean procedure lastitem; 
///        text procedure intext(w); integer w; 
///        integer procedure inint; 
///        long real procedure inreal;
///        integer procedure infrac;
/// 
///        ENDFILE:= true
///        ...
///     end infile;
/// </pre>
/// 
/// An object of the class "infile" is used to represent an image-oriented
/// sequential input file.
/// 
/// The variable ENDFILE is true whenever the file object is closed or the
/// external file is exhausted (i.e. "end of file" has been encountered). The
/// procedure "endfile" gives access to the value of ENDFILE.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/EclipseWorkSpaces/blob/main/SimulaCompiler2/Simula/src/simula/runtime/RTS_Infile.java"><b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public class RTS_Infile extends RTS_Imagefile {
	
	/// The BufferedReader used.
	private BufferedReader lineReader;
	
	/// Utility variable
	private String rest = null;

	// Constructor
	/// Create a new _Infile.
	/// @param SL staticLink
	/// @param FN FILENAME
	public RTS_Infile(RTS_RTObject SL, RTS_TXT FN) {
		super(SL, FN);
		_ENDFILE = true;
	}

	// Class Statements
	@Override
	public RTS_Infile _STM() {
		EBLK();
		return (this);
	}

	/// Procedure open.
	/// 
	/// <pre>
	///  Boolean procedure open(fileimage);  text fileimage;
	///      if ... then begin ...  ! see 10.1.2;
	///         ENDFILE := false;
	///         image   :- fileimage;
	///         image   := notext;
	///         setpos(length+1);
	///         open    := OPEN := true;
	///      end open;
	/// </pre>
	/// 
	/// Procedure "open" establishes the association with an external file (as
	/// identified by FILENAME), checks the access modes and causes corresponding
	/// opening actions on the external file. If the external file is closed, it is
	/// opened.
	/// 
	/// If successful, "open" returns true and sets ENDFILE false. In addition,
	/// "image" references the parameter "fileimage" which is space-filled.
	/// 
	/// @param image the givent image
	/// @return true if successful, otherwise false.
	public boolean open(final RTS_TXT image) {
		if (RTS_Option.VERBOSE)
			TRACE_OPEN("Open InFile");
		if (_OPEN)
			return (false);
		this.image = image;
		_ENDFILE = false;
		RTS_UTIL._ASGTXT(image, null); // image := NOTEXT;
		setpos(length() + 1);

		if (FILE_NAME.edText().equalsIgnoreCase("#sysin")) {
			// Nothing. Runtime Console is opened later
		} else {
			File file = doCreateAction();
			if (!file.exists()) {
				File selected = trySelectFile(file.getAbsoluteFile().toString());
				if (selected != null)
					file = selected;
			}
			try {
				Reader reader = new FileReader(file, _CHARSET);
				lineReader = new BufferedReader(reader);
			} catch (IOException e) {
				if (RTS_Option.VERBOSE)
					e.printStackTrace();
				_OPEN = false;
				return (false);
			}
		}
		_OPEN = true;
		return (true);
	}

	/// Procedure close.
	/// <pre>
	/// Boolean procedure close;
	///      if OPEN then
	///      begin ... ; ! perform closing actions ...;
	///         image :- notext;
	///         OPEN  := false;
	///         close := ENDFILE := true
	/// end close;
	/// </pre>
	/// 
	/// Procedure "close" causes closing actions on the external file, as specified
	/// by the access modes. In addition, the association between the file object and
	/// the external file is dissolved. If possible, the external file is closed.
	/// 
	/// If successful, "close" returns true. In addition, OPEN is false, ENDFILE is
	/// true and "image" references notext.
	/// 
	/// @return true if successful, otherwise false.
	public boolean close() {
		if (!_OPEN)
			return (false);
		if (!FILE_NAME.edText().equalsIgnoreCase("#sysin"))
			try {
				if (lineReader != null)
					lineReader.close();
			} catch (IOException e) {
				if (RTS_Option.VERBOSE)
					e.printStackTrace();
				return (false);
			}
		image = null; // image :- NOTEXT;
		_OPEN = false;
		_ENDFILE = true;
		doPurgeAction();
		return (true);
	}

	/// Procedure endfile.
	/// 
	/// Returns true whenever the file object is closed or the external file is exhausted
	/// (i.e. "end of file" has been encountered).
	/// @return the resulting boolean value
	public boolean endfile() {
		return (_ENDFILE);
	}

	/// Procedure Inimage.
	/// 
	/// <pre>
	/// procedure inimage;
	///   if ENDFILE then error("...")
	///   else begin
	///      ... ; ! attempt to transfer external image to "image";
	///      if ... ! "image" too short; then error("...")
	///      else if ... ! there was no more to read;
	///      then begin
	///             ENDFILE := true;
	///             image   :- "!25!" end
	///      else  ... ; ! pad "image" with space(s);
	///      setpos(1)
	/// end inimage;
	/// </pre>
	/// 
	/// The procedure "inimage" performs the transfer of an external file image into
	/// "image". A run-time error occurs if "image" is notext or too short to contain
	/// the external image. If it is longer than the external image, the latter is
	/// left-adjusted within "image" and the remainder of the text is filled with
	/// spaces. The position indicator is set to one.
	/// 
	/// Note: If an "end of file" is encountered, EM ('!25!') is generated as a
	/// single character external image, and the variable ENDFILE is given the value
	/// true. A call on "inimage" or "inrecord" when ENDFILE already has the value
	/// true constitutes a run-time error.
	/// 
	/// @throws RTS_SimulaRuntimeError if inimage fail
	@Override
	public void inimage() {
		if (!_OPEN || _ENDFILE)
			throw new RTS_SimulaRuntimeError(FILE_NAME.edText() + ": File not opened or attempt to read past EOF");
		try {
			String line = (rest != null) ? rest : readLine();
			rest = null;
			if (line != null) {
				if (line.length() > RTS_TXT.length(image))
					throw new RTS_SimulaRuntimeError(FILE_NAME.edText() + ": Image too short: input.length="
							+ line.length() + ", image.length=" + RTS_TXT.length(image));
				RTS_UTIL._ASGSTR(image, line);
			} else {
				RTS_UTIL._ASGSTR(image, "" + (char) 25);
				_ENDFILE = true;
			}
		} catch (IOException e) {
			throw new RTS_SimulaRuntimeError("Inimage failed", e);
		}
		setpos(1);
	}
    
	/// Utility: prompt.
    /// @param title the dialog title
    /// @param msg the prompt message
    /// @return the text entered or "?CANCELLED"
    private static String prompt(String title, String msg) {
        // Prompt the user for input
        String result = JOptionPane.showInputDialog(null, msg, title, JOptionPane.QUESTION_MESSAGE);
    	if(result == null) result = "?CANCELLED";
        return result;
    }

	/// Read and return next line.
	/// @return line String.
	/// @throws IOException if something went wrong.
	private String readLine() throws IOException {
		if (FILE_NAME.edText().equalsIgnoreCase("#sysin")) {
			if(RTS_Option.noPopup) {
				try {
					return readInputWithTimeout(30, TimeUnit.SECONDS);
				} catch (Exception e) {
					IO.println("RTS_Infile.readLine: got" + e);
			    	String line = prompt("Sorry - Sysin.inimage is Unavailable.", "Enter Input here:");
			    	System.out.println("You entered: " + line);
			    	return line;
				}
			} else {
				ensureSysinOpened();
				return lineReader.readLine();
			}
		} else {
			return lineReader.readLine();			
		}
	}
	
	/// Utility: read line of characters from System.in with timeout
	/// <p>
	/// Obtained by Google AI Search
	/// @param timeout the timeout duration
	/// @param unit the TimeUnit
	/// @return the line string read
    private static String readInputWithTimeout(long timeout, TimeUnit unit) {
        // ExecutorService with one thread for the input task
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // The task to read from the console
        Callable<String> inputTask = new Callable<String>() {
            @Override
            public String call() throws IOException {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String input = "";
                try {
                    // This loop with br.ready() avoids blocking indefinitely within the callable itself
                    while (!br.ready()) {
                        Thread.sleep(100); // Sleep briefly to prevent busy-waiting
                    }
                    input = br.readLine();
                } catch (InterruptedException e) {
                    return null; // Task was cancelled/interrupted
                }
                return input;
            }
        };

        Future<String> future = executor.submit(inputTask);
        String result = null;

        try {
//            System.out.println("Please enter input within " + timeout + " seconds:");
            result = future.get(timeout, unit); // Wait with a timeout
        } catch (TimeoutException e) {
            System.out.println("\nTimeout occurred. No input received within the time limit.");
            future.cancel(true); // Cancel the running task
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("An error occurred while reading input.");
            e.printStackTrace();
        } finally {
            executor.shutdownNow(); // Always shut down the executor
        }
//    	IO.println("readInputWithTimeout: result="+result);
        if(result == null) {
        	IO.println("Throw RuntimeException !!!");
        	throw new RuntimeException("");
        }
        return result;
    }
        
	/// Ensure that Sysin is open.
	private void ensureSysinOpened() {
		if (FILE_NAME.edText().equalsIgnoreCase("#sysin")) {
			if(lineReader == null) {
			if (RTS_UTIL.console == null) RTS_UTIL.ensureOpenRuntimeConsole();
				Reader reader = RTS_UTIL.console.getReader();
				lineReader = new BufferedReader(reader);
			}
		}		
	}

	/// Procedure inrecord.
	/// 
	/// <pre>
	///  Boolean procedure inrecord;
	///     if not OPEN or ENDFILE then error("...")
	///     else begin
	///        ... ; ! transfer external image to "image" (no space-filling);
	///        if ... ! no more to read;
	///        then begin
	///           ENDFILE        := true;
	///           setpos(1);
	///           image.putchar('!25!') end  Note - POS = 2 now
	///        else begin
	///           setpos(... !number of characters transferred + 1; );
	///           inrecord:= not ...! whole external image received?;
	///        end if
	///  end inrecord;
	/// </pre>
	/// 
	/// The procedure "inrecord" is similar to "inimage" with the following
	/// exceptions. Whenever the number of characters accessible in the external
	/// image is less than "length", the rest of "image" is left unchanged. The part
	/// of the "image" that was changed is from pos 1 upto (but not including) the
	/// resulting value of POS. Moreover, if the external image is too long, only the
	/// "length" first characters are input. The value returned by the procedure is
	/// true and the remaining characters may be input through subsequent "inrecord"
	/// (or possibly "inimage") statements. Otherwise, if the input of the external
	/// image was completed, the value false is returned.
	/// 
	/// Note: If an "end of file" is encountered, EM ('!25!') is generated as a
	/// single character external image, and the variable ENDFILE is given the value
	/// true. A call on "inimage" or "inrecord" when ENDFILE already has the value
	/// true constitutes a run-time error.
	/// @return true if a partial record is read, otherwise false
	/// @throws RTS_SimulaRuntimeError if inrecord fail
	public boolean inrecord() {
		if (!_OPEN || _ENDFILE)
			throw new RTS_SimulaRuntimeError(FILE_NAME.edText() + ": File not opened or attempt to read past EOF");
		try {
			RTS_TXT.setpos(image, 1);
//			String line = (rest != null) ? rest : lineReader.readLine();
			String line = (rest != null) ? rest : readLine();
			rest = null;
			if (line != null) {
				if (line.length() > RTS_TXT.length(image)) { // Return partial image
					rest = line.substring(RTS_TXT.length(image));
					line = line.substring(0, RTS_TXT.length(image));
				}
				RTS_TXT LINE = new RTS_TXT(line);
				while (RTS_TXT.more(LINE))
					RTS_TXT.putchar(image, RTS_TXT.getchar(LINE));
			} else {
				RTS_TXT.putchar(image, (char) 25);
				_ENDFILE = true;
			}
		} catch (IOException e) {
			throw new RTS_SimulaRuntimeError("Inrecord failed", e);
		}
		return (rest != null);
	}

}
