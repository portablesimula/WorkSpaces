/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.env.dataset;

/// ByteFile Bridge.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/dataset/ByteFile.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public class ByteFile extends Dataset {

	/// Construct a new Bytefile with the given arguments.
	/// @param fileName the fileName
	/// @param type the fileType
	/// @param action the action string
	public ByteFile(String fileName, int type, String action) {
		super(fileName, type, action);
	}

}
