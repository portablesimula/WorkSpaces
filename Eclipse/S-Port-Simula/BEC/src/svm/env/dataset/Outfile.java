/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.env.dataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import svm.RTUtil;

/// Outfile Bridge.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/dataset/Outfile.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public class Outfile extends ImageFile {
	
	/// The underlying Writer
	FileWriter writer;

	/// Construct a new Outfile with the given arguments.
	/// @param fileName the fileName
	/// @param type the fileType
	/// @param action the action string
	/// @param imglng the image length
	public Outfile(String fileName, int type, String action, int imglng) {
		super(fileName, type, action, imglng);
		File file = fileAction.doCreateAction(fileName);
		if (!file.exists()) {
			RTUtil.set_STATUS(3); // File does not exist;
			return;
		}
		try {
			writer = new FileWriter(file, this.fileAction._APPEND);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void clfile() {
		try {
			writer.flush();
			writer.close();
			writer = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void outimage(String image) {
		try {
			writer.write(image);
			writer.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void breakOutimage(String image) {
		try {
			writer.write(image);
			writer.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Util.IERR(""+image);
	}

}
