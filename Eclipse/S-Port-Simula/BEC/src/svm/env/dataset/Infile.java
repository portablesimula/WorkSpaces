/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.env.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import bec.Global;
import bec.Option;
import bec.scode.Type;
import bec.util.Util;
import svm.RTUtil;
import svm.value.IntegerValue;
import svm.value.ObjectAddress;

/// Infile Bridge.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/dataset/Infile.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public class Infile extends ImageFile {
	
	/// The BufferedReader used.
	private BufferedReader lineReader;
	
	/// Utility variable
	private String rest = null;


	/// Construct a new Infile with the given arguments.
	/// @param fileName the fileName
	/// @param type the fileType
	/// @param action the action string
	/// @param imglng the image length
	public Infile(String fileName, int type, String action, int imglng) {
		super(fileName, type, action, imglng);
		File file = fileAction.doCreateAction(fileName);
		if (!file.exists()) {
			RTUtil.set_STATUS(3); // File does not exist;
			return;
		}
		try {
			lineReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void clfile() {
		try {
			lineReader.close();
			lineReader = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/// Procedure inimage.
	///
	/// Reads a line from the underlying file into a character area
	///
	/// A record is read from the current position of the data set into the image. If the number of characters
	/// in the record exceeds the image length, the action taken is system dependent:
	///
	/// - If the system permits partial record read, image.length characters are read, filled := image.length,
	///   and status 34 is returned. In this case the next inimage (on this data set) should continue reading
	///   from the next position in the partially read record.
	///
	/// - If partial record reading is not possible, status error 12 is set and filled is set to zero; the remainder
	///   of the record is in this case skipped.
	///
	/// Except for the case of partial record reading the data set will be positioned at the sequentially next record.
	///
	/// Inimage is legal on infiles and directfiles only.
	///
	/// This routine may change the value of the global variable status to one of the values given in app. C.
	/// If the status returned is non-zero, filled must be zero, except for the partial read case discussed
	/// above (status 12).
	/// 
	/// @param chrAddr the start of the area
	/// @param nchr the length of the area
	public int inimage(ObjectAddress chrAddr, int nchr) {
		try {
			String line = (rest != null) ? rest : lineReader.readLine();
			rest = null;
			if(line == null) {
				RTUtil.set_STATUS(13); // End of file on input";
				return 0;
			}
			if(line.length() <= nchr) {
				for(int i=0;i<line.length();i++) {
					chrAddr.store(i, IntegerValue.of(Type.T_CHAR, line.charAt(i)));
				}
				return line.length();
			} else {
				for(int i=0;i<nchr;i++) {
					chrAddr.store(i, IntegerValue.of(Type.T_CHAR, line.charAt(i)));
				}
				rest = line.substring(nchr);
				return nchr;
			}
		} catch (IOException e) {
			Util.IERR("Inimage failed: " + e);
		}
		return 0;
	}
	
	/// Utility variable
	private static String sysinRest = null;
	
	/// Utility: read a line
	/// @return the line string read
	/// @throws IOException if IOException occur
	/// @throws TimeoutException if Timeout occur
	private static String readLine() throws IOException, TimeoutException {
		StringBuilder sb = new StringBuilder();
		if(Global.console != null) {
			int c = Global.console.read();
			while(c != '\n') {
				sb.append((char)c);
				c = Global.console.read();
			}
		} else {
			int c = IO_read(5, TimeUnit.SECONDS);
			while(c != '\n') {
				sb.append((char)c);
				c = IO_read(5, TimeUnit.SECONDS);
			}
		}
		return sb.toString();
	}
	
	/// Utility: read a character with timeout
	/// @param timeout the timeout duration
	/// @param unit the TimeUnit
	/// @return the line string read
	/// @throws IOException if IOException occur
	/// @throws TimeoutException if Timeout occur
	private static int IO_read(int timeout, TimeUnit unit) throws IOException, TimeoutException {
		long sleep = unit.toMillis(timeout);
		LOOP: while(true) {
			if(System.in.available() > 0) return System.in.read();
			if((--sleep) < 0) break LOOP;
			try { Thread.sleep(1); } catch (InterruptedException e) {}
		}
		if(Option.execVerbose)
			IO.println("RTInfile.readLine: throw new TimeoutException()");
		throw new TimeoutException();
	}

	/// Reads a line from the underlying file into a character area
	/// @param chrAddr the start of the area
	/// @param nchr the length of the area
	/// @return the number of characters filled
	public static int sysinInimage(ObjectAddress chrAddr, int nchr) {
		try {
			String line = (sysinRest != null) ? sysinRest : readLine();
			sysinRest = null;
			if(line == null) {
				RTUtil.set_STATUS(13); // End of file on input";
				return 0;
			}
			if(line.length() <= nchr) {
				for(int i=0;i<line.length();i++) {
					chrAddr.store(i, IntegerValue.of(Type.T_CHAR, line.charAt(i)));
				}
				return line.length();
			} else {
				for(int i=0;i<nchr;i++) {
					chrAddr.store(i, IntegerValue.of(Type.T_CHAR, line.charAt(i)));
				}
				sysinRest = line.substring(nchr);
				return nchr;
			}
		} catch (Exception e) {
			Util.IERR("sysinInimage failed: " + e);
		}
		return 0;
	}

}
