/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.env.dataset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import svm.RTUtil;

/// Inbytefile Bridge.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/dataset/Inbytefile.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public class Inbytefile extends ByteFile {
	
	/// The InputStream used.
	private InputStream inputStream;


	/// Construct a new Inbytefile with the given arguments.
	/// @param fileName the fileName
	/// @param type the fileType
	/// @param action the action string
	public Inbytefile(String fileName, int type, String action) {
		super(fileName, type, action);
		File file = fileAction.doCreateAction(fileName);
		if (!file.exists()) {
			RTUtil.set_STATUS(3); // File does not exist;
			return;
		}
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void clfile() {
		try {
			inputStream.close();
			inputStream = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/// Returns a byte from the underlying InputStream
	/// @return a byte from the underlying InputStream
	public int inbyte() {
		try {
			// read a single byte
			int b = inputStream.read();
			// _RT.TRACE("InbyteFile.inbyte: read byte: +" + b);
			if (b == -1) {
				RTUtil.set_STATUS(13); // End of file on input
				return (0);
			}
			return (b);
		} catch (IOException e) {
			IO.println("Inbyte failed" + e);
			RTUtil.set_STATUS(17);
			return 0;
		}
	}

}
