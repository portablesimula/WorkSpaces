/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import bec.util.Array;
import bec.util.Terminal;
import svm.segment.DataSegment;
import svm.segment.ProgramSegment;
import svm.segment.Segment;
import svm.value.ProgramAddress;

/// Global variables.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/Global.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class Global {

	/** Default Constructor */ public Global() {} 

	/// Initiate Global data
	/// @param scodeSource the S-Code source file name
	public static void init(final String scodeSource) {
		Global.scodeSource = scodeSource;
		Global.SEGMAP = new HashMap<String, Segment>();
		Global.ifDepth = 0;		
	}
	
	/// The Terminal console or null
	public static Terminal console;
	
	/// Current S-Module
	public static S_Module currentModule;
	
	/// The S-Code source file name
	public static String scodeSource;
	
	/// S-Code PROG String
	public static String progIdent;
	
	/// S-Module ident String or MAIN
	public static String moduleID;
	
   	/// Ident of module being defined
	public static String modident;
	
	/// Check code of module being defined
	public static String modcheck;
	
	/// Ident of program being defined
	public static String PROGID;
	
	/// The Runtime system's directory 
	public final static String rtsDir = "C:/SPORT/RTS/";
	
	/// Attributes and SVM-Code output directory
	public static String outputDIR = rtsDir;
	
	/// The current sourceLineNumber during execution
	public static int sourceLineNumber;
//	public static int curline;		// Current source line number
	
	/// Number of tags, see: INSERT
	public static int nTags;

	/// Index xTag --> value iTag (during Module I/O)
	public static Array<Integer> iTAGTAB;
	
	/// Index iTag --> value xTag (during Module I/O)
	public static Array<Integer> xTAGTAB;
	
	/// Destination table
	public static ProgramAddress[] DESTAB = new ProgramAddress[64];
	
	/// Number of nested if
	public static int ifDepth;

	/// Current constant Segment
	public static DataSegment CSEG;
	
	/// Current constant TextValue Segment
	public static DataSegment TSEG;
	
	/// Current DataSegment
	public static DataSegment DSEG; 
	
	/// Current ProgramSegment
	public static ProgramSegment PSEG;
	
	/// Set of Routine Segments
	public static Vector<Segment> routineSegments;

	/// ProgramSequenceControl during execute
	public static ProgramAddress PSC;
	
	/// Returns true if during execution
	/// @return true if during execution
	public static boolean duringEXEC() { return PSC != null; }
	
	/// The Segment Map
	public static Map<String, Segment> SEGMAP;

	/// Returns the source file ident
	/// @return the source file ident
	public static String getSourceID() {
		File file = new File(scodeSource);
		String name = file.getName();
		int p = name.indexOf('.');
		String s = name.substring(0, p);
		return s;
	}

	/// Returns the Attribute filename based on the arguments
	/// @param modident the module ident
	/// @param suffix the suffix string
	/// @return the Attribute filename based on the arguments
	public static String getAttrFileName(String modident, String suffix) {
		if(modident == null) {
			int p = scodeSource.indexOf('.');
			String s = scodeSource.substring(0, p);
			return s + suffix;
		} else {
			return rtsDir + modident + suffix;
		}
	}
}
