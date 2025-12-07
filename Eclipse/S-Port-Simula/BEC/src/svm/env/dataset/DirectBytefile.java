/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.env.dataset;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import svm.RTUtil;
import svm.env.FileAction._CreateAction;

/// DirectBytefile Bridge.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/dataset/DirectBytefile.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public class DirectBytefile extends ByteFile {
	
	/// The underlying RandomAccessFile used.
	private RandomAccessFile randomAccessFile;

	/// The variable MAXLOC indicates the maximum possible location on the external file.
	///
	/// If this is not meaningful MAXLOC has the value of "maxint"-1.
	///
	/// The procedure "maxloc" gives access to the current MAXLOC value.
	public int MAXLOC;

//	/// The initial value of LAST_LOC.
//	private int INITIAL_LAST_LOC;

	/// Construct a new DirectBytefileSpec with the given arguments.
	/// @param fileName the fileName
	/// @param type the fileType
	/// @param action the action string
	public DirectBytefile(String fileName, int type, String action) {
		super(fileName, type, action);
		File file = fileAction.doCreateAction(fileName);
		if (!file.exists()) {
			RTUtil.set_STATUS(3); // File does not exist;
			return;
		}
		MAXLOC = Integer.MAX_VALUE - 1;
		try {
			String mode = "rws"; // mode is one of "r", "rw", "rws", or "rwd"
			if (fileAction._SYNCHRONOUS)
				mode = "rws";
			else
				mode = (fileAction._CANREAD & !fileAction._CANWRITE) ? "r" : "rw";
			randomAccessFile = new RandomAccessFile(file, mode);
			if (fileAction._CREATE == _CreateAction.create) {
				try {
					randomAccessFile.setLength(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			if (fileAction._APPEND)
//				INITIAL_LAST_LOC = lastloc();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void clfile() {
		try {
			if (fileAction._PURGE) {
				randomAccessFile.setLength(0);
				randomAccessFile.close();
			} else
				randomAccessFile.close();
			randomAccessFile = null;
		} catch (IOException e) {
			IO.println("clfile failed" + e);
			RTUtil.set_STATUS(17);
		}
	}

	/// Returns a byte from the underlying RandomAccessFile
	/// @return a byte from the underlying RandomAccessFile
	public int inbyte() {
		try {
			int b = randomAccessFile.read();
			return (b == -1) ? 0 : b;
		} catch (EOFException e) {
			return (0);

		} catch (IOException e) {
			IO.println("Outbyte failed" + e);
			RTUtil.set_STATUS(17);
			return 0;
		}
	}

	/// Writes a byte through the underlying RandomAccessFile
	/// @param b the byte to be written
	public void outbyte(final int b) {
		try {
			randomAccessFile.write(b);
		} catch (IOException e) {
			IO.println("Outbyte failed" + e);
			RTUtil.set_STATUS(17);
		}
	}

	/// Locate the given 'pos' on the underlying RandomAccessFile
	/// @param pos the position to locate
	public void locate(final int pos) {
		try {
			randomAccessFile.seek(pos - 1);
		} catch (IOException e) {
			IO.println("locate failed" + e);
			RTUtil.set_STATUS(17);
		}
	}

	/// Returns the last location
	/// @return the last location
	public int lastloc() {
		try {
			// the length of this file, measured in bytes.
			long length = randomAccessFile.length();
			return ((int) length);
		} catch (IOException e) {
			IO.println("Lastloc failed" + e);
			RTUtil.set_STATUS(17);
			return 0;
		}
	}

}
