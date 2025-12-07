/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.RTS_FEC_InterfaceGenerator.predef.util;

import simuletta.RTS_FEC_InterfaceGenerator.RTS_FEC_InterfaceGenerator;

/**
 * Compile Time Options
 * 
 * @author Øystein Myhre Andersen
 *
 */
public final class RTS_FEC_Interface_Option {
	
	public static boolean verbose=false; 
//    public static boolean sportOk=true; // Enables/disables special S-Port features,
//	public static boolean noExecution = false;  // Don't execute generated inline code.
	public static boolean WARNINGS=true;
    public static String  SELECT=null;   // Selection Switches
//
//	// Overall TRACING Options
	public static boolean TRACING=false;
//	public static boolean BREAKING=false; 
//	public static boolean DEBUGGING=false; // Set by EditorMenues - doDebugAction
//
//	// Scanner Trace Options
	public static boolean SOURCE_LISTING=false;//true;
	public static boolean TRACE_SELECTION=false;//true;
	public static boolean TRACE_SCAN=false;//true;
	public static boolean TRACE_MACRO_SCAN=false;//true;
//	public static boolean TRACE_MACRO_EXPAND=false;//true;
	public static boolean TRACE_COMMENTS=false;//true;
//
//	// Parser Trace Options
	public static boolean TRACE_PARSE_BREIF = false;
	public static boolean TRACE_PARSE=false;//true;
	public static boolean TRACE_ATTRIBUTE_OUTPUT=false;//true;
	public static boolean TRACE_ATTRIBUTE_INPUT=false;//true;

	// Coder Trace Options
	public static int TRACE_CODING=3;//0;
	
	public static int INTERFACE_TRACE_LEVEL=0; 
	
}