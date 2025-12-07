/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.env.dataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import svm.RTUtil;

/// Outbytefile Bridge.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/dataset/Outbytefile.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public class Outbytefile extends ByteFile {
	
	/// The OutputStream used.
	private OutputStream outputStream;

	/// Construct a new Outbytefile with the given arguments.
	/// @param fileName the fileName
	/// @param type the fileType
	/// @param action the action string
	public Outbytefile(String fileName, int type, String action) {
		super(fileName, type, action);
		File file = fileAction.doCreateAction(fileName);
		if (!file.exists()) {
//			File selected = trySelectFile(file.getAbsoluteFile().toString());
//			if (selected != null)
//				file = selected;
			RTUtil.set_STATUS(3); // File does not exist;
			return;
		}
		try {
//			IO.println("NEW RTOutbytefile: fileName=" + fileName);
			outputStream = new FileOutputStream(file, fileAction. _APPEND);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void clfile() {
		try {
			outputStream.flush();
			outputStream.close();
			outputStream = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/// Writes a byte through the underlying OutputStream
	/// @param b the byte to be written
	public void outbyte(final int b) {
		try {
			outputStream.write(b);
			if (fileAction._SYNCHRONOUS)
				outputStream.flush();
		} catch (IOException e) {
			IO.println("Outbyte failed" + e);
			RTUtil.set_STATUS(17);
		}
	}

}
