/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.env.dataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import bec.scode.Type;
import bec.util.Util;
import svm.RTUtil;
import svm.value.IntegerValue;
import svm.value.ObjectAddress;

/// Directfile Bridge.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/dataset/Directfile.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public class Directfile extends ImageFile {
	
	/// The underlying RandomAccessFile used.
	private RandomAccessFile randomAccessFile;

	/// The variable "RECORDSIZE" is the fixed size of all external images. It is set
	/// by "open" and subdivide the external file into series of external images,
	/// without any image separating characters.
	int RECORDSIZE;

	/// The variable MAXLOC indicates the maximum possible location on the external
	/// file. If this is not meaningful MAXLOC has the value of "maxint"-1. The
	/// procedure "maxloc" gives access to the current MAXLOC value.
	public int MAXLOC;

//	/// The initial value of LAST_LOC
//	private int INITIAL_LAST_LOC;

	/// Construct a new DirectfileSpec with the given arguments.
	/// @param fileName the fileName
	/// @param type the fileType
	/// @param action the action string
	/// @param imglng the image length
	public Directfile(String fileName, int type, String action, int imglng) {
		super(fileName, type, action, imglng);
		File file = fileAction.doCreateAction(fileName);
		if (!file.exists()) {
			RTUtil.set_STATUS(3); // File does not exist;
			return;
		}
		RECORDSIZE = imglng;
		MAXLOC = Integer.MAX_VALUE - 1;
		try {
			String mode = "rws"; // mode is one of "r", "rw", "rws", or "rwd"
			if (fileAction._SYNCHRONOUS)
				mode = "rws";
			else
				mode = (fileAction._CANREAD & !fileAction._CANWRITE) ? "r" : "rw";
			randomAccessFile = new RandomAccessFile(file, mode);
		} catch (FileNotFoundException e) {
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

	public int inimage(ObjectAddress chrAddr, int nchr) {
		String line = readLine();
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
			Util.IERR("");
		}
		return 0;
	}
	
	/// Utility: read a line from the underlying RandomAccessFile
	/// @return the line read
	private String readLine() {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<this.imglng;i++) {
			try {
				int b = randomAccessFile.read();
				sb.append((char)b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	@Override
	public void outimage(String image) {
		try {
			randomAccessFile.write(image.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/// Locate the given 'pos' on the underlying RandomAccessFile
	/// @param pos the position to locate
	public void locate(int pos) {
		try {
			randomAccessFile.seek((pos - 1) * RECORDSIZE);
		} catch (IOException e) {
			RTUtil.set_STATUS(99);
		}
	}
	
	/// Returns the last location
	/// @return the last location
	public int lastloc() {
		try {
			// the length of this file, measured in bytes.
			long length = randomAccessFile.length();
			return ((int) length / RECORDSIZE);
		} catch (IOException e) {
			IO.println("Lastloc failed" + e);
			RTUtil.set_STATUS(17);
			return 0;
		}
	}

}
