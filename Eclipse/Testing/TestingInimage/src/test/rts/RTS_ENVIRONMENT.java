/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package test.rts;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/// System class ENVIRONMENT.
/// 
/// The purpose of the environmental class is to encapsulate all constants,
/// procedures and classes which are accessible to all source modules. It
/// contains procedures for mathematical functions, text generation, random
/// drawing, etc.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/SimulaCompiler2/Simula/src/simula/runtime/RTS_ENVIRONMENT.java"><b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public class RTS_ENVIRONMENT extends RTS_RTObject {
	/// The Simula release identification.
	/// 
	/// NOTE: When updating release id, change version in setup.SimulaExtractor and simula.Global
	static final String simulaReleaseID = "Simula-2.0";

	/// The start time from System.currentTimeMillis
	static long _STARTTIME = System.currentTimeMillis();
	
	/// The current lowten character.
	static char CURRENTLOWTEN = '&';
	
	/// The current decimal mark character.
	static char CURRENTDECIMALMARK = '.';
	
	/// A constant holding the maximum value a long real can have.
	public final static double maxlongreal = Double.MAX_VALUE;

	/// A constant holding the minimum value a long real can have.
	public final static double minlongreal = -maxlongreal;// Double.MIN_VALUE;
	
	/// A constant holding the maximum value a real can have.
	public final static float maxreal = Float.MAX_VALUE;
	
	/// A constant holding the minimum value a real can have.
	public final static float minreal = -maxreal;// Float.MIN_VALUE;
	
	/// A constant holding the maximum value an integer can have.
	public final static int maxint = Integer.MAX_VALUE;
	
	/// A constant holding the minimum value an integer can have.
	public final static int minint = Integer.MIN_VALUE;

	/// Normal Constructor
	/// @param staticLink static link
	public RTS_ENVIRONMENT(final RTS_RTObject staticLink) {
		super(staticLink);
	}

	
}
