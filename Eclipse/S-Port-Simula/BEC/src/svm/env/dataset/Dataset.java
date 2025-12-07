/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.env.dataset;

import bec.util.Util;
import svm.env.FileAction;

/// Dataset Bridge.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/dataset/Dataset.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public class Dataset {
	
	/// The fileName
	public String fileName;
	
	/// The file type
	int fileType;
	
	/// The file action string
	public FileAction fileAction; // Used by OPEN
	
	/**File type*/public static final int FIL_INFILE         = 1; // record oriented sequential read access.
	/**File type*/public static final int FIL_OUTFILE        = 2; // record oriented sequential write access.
	/**File type*/public static final int FIL_PRINTFILE      = 3; // printer formatted outfile.
	/**File type*/public static final int FIL_DIRECTFILE     = 4; // record oriented random read/write access.
	/**File type*/public static final int FIL_INBYTEFILE     = 5; // stream oriented sequential read access.
	/**File type*/public static final int FIL_OUTBYTEFILE    = 6; // stream oriented sequential write access.
	/**File type*/public static final int FIL_DIRECTBYTEFILE = 7; // byte oriented random read/write access.
	
	/**File key*/public static final int KEY_SYSIN    = 1;
	/**File key*/public static final int KEY_SYSOUT   = 2;
	/**File key*/public static final int KEY_SYSTRACE = 3;

	/// Construct a new DatasetSpec with the given arguments.
	/// @param fileName the fileName
	/// @param type the fileType
	/// @param action the action string
	public Dataset(String fileName, int type, String action) {
		this.fileName = fileName;
		this.fileType = fileType;
		fileAction = new FileAction(action);
	}

	/// Close the underlying file
	public void clfile() { // Needs redefinition
		Util.IERR("Routine clfile need a redefinition in "+this.getClass().getSimpleName());
	}

	/// Utility: edit the given fileType
	/// @param fileType the fileType
	/// @return the edited file type
	public static String edFileType(int fileType) {
		switch(fileType) {
		case FIL_INFILE:         return("INFILE");
		case FIL_OUTFILE:        return("OUTFILE");
		case FIL_PRINTFILE:      return("PRINTFILE");
		case FIL_DIRECTFILE:     return("DIRECTFILE");
		case FIL_INBYTEFILE:     return("INBYTEFILE");
		case FIL_OUTBYTEFILE:    return("OUTBYTEFILE");
		case FIL_DIRECTBYTEFILE: return("DIRECTBYTEFILE");
		default:                 return"UNKNOWN";
		}
	}
		
	@Override
	public String toString() {
		return edFileType(fileType) + ": " + fileName;
	}

}
