/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.util;

/// EndProgram, an extension of RuntimeException used to signal End-of-Program.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/util/EndProgram.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public final class EndProgram extends RuntimeException {
	
	/// The exitCode
	public int exitCode;

	/// Constructs a new RTS_EndProgram exception with the specified detail message. 
	/// @param exitCode the exitCode.
	/// @param message the detail message.
	public EndProgram(final int exitCode, final String message) {
		super(message);
		this.exitCode = exitCode;
	}
	
	@Override
	public String toString() {
		return "EndProgram code=" + exitCode + " " + getMessage();
	}
}
