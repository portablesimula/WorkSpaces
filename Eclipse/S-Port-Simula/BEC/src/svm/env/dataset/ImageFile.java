/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.env.dataset;

import bec.util.Util;
import svm.value.ObjectAddress;

/// ImageFile Bridge.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/dataset/ImageFile.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public class ImageFile extends Dataset {
	
	/// The image length
	int imglng;

	/// Construct a new ImageFile with the given arguments.
	/// @param fileName the fileName
	/// @param type the fileType
	/// @param action the action string
	/// @param imglng the image length
	public ImageFile(String fileName, int type, String action, int imglng) {
		super(fileName, type, action);
		this.imglng = imglng;
	}

	/// Writes outimage to the underlying file
	/// @param image the image string to be written
	public void outimage(String image) { // Needs redefinition
		Util.IERR("Routine outimage need a redefinition in "+this.getClass().getSimpleName());
	}

	/// Writes breakOutimage to the underlying file
	/// @param image the image string to be written
	public void breakOutimage(String image) { // Needs redefinition
		Util.IERR("Routine breakOutimage need a redefinition in "+this.getClass().getSimpleName());
	}

	/// Reads a line from the underlying file into a character area
	/// @param chrAddr the start of the area
	/// @param nchr the length of the area
	/// @return the number of characters filled
	public int inimage(ObjectAddress chrAddr, int nchr) {
		Util.IERR("Routine inimage need a redefinition in "+this.getClass().getSimpleName());
		return 0;
	}



}
