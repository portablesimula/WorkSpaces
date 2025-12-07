package sim.compiler;

import java.awt.Color;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class Option {

	public static boolean INLINE_TESTING = false;
	public static boolean verbose = false;
	public static boolean CaseSensitive = false;
	public static boolean nopopup = false;

	public static boolean fecListing;
	public static boolean fecSCodeTrace;
//	public static int     fecTraceLevel;

	public static boolean becSCodeTrace;
	public static boolean becTraceSVM_CODE;
	public static boolean becTraceSVM_DATA;
	
	/** Runtime Option */ public static int execTrace;
	/** Runtime Option */ public static int callTrace;
	/** Runtime Option */ public static boolean dumpsAtExit = false;

	// Editor Scanner Trace Options
	/** Debug option */	public static boolean TRACE_SCAN=false;
	/** Debug option */	public static boolean TRACE_COMMENTS=false;
	
	/// The default constructor
	private Option() {}
	
	/// Initiate Compiler options.
	public static void INIT() {
		Option.verbose = false;
		Option.CaseSensitive=false;
		Option.TRACE_SCAN = false;
		Option.TRACE_COMMENTS = false;
		Option.fecListing = false;
		Option.fecSCodeTrace = false;
//		Option.fecTraceLevel = 0;
		Option.becSCodeTrace = false;
		Option.becTraceSVM_CODE = false;
		Option.becTraceSVM_DATA = false;
		Option.execTrace = 0;
		Option.callTrace = 0;
		Option.dumpsAtExit = false;
	}

	/// Editor Utility: Select Runtime Options.
	public static void selectRuntimeOptions() {
	  	JPanel panel=new JPanel();
	  	panel.setBackground(Color.white);
	  	panel.add(Util.checkBox("Verbose", ""));
	  	panel.add(Util.checkBox("execTrace", ""));
	  	panel.add(Util.checkBox("callTrace", ""));
	  	panel.add(Util.checkBox("dumpsAtExit", ""));
	      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			Util.optionDialog(panel,"Select Runtime Options",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,"Ok");
	  	Global.storeSPortEditorProperties();
	}

    /// Add Runtime options to the argument vector.
    /// @param args the argument vector
	public static void addRTArguments(Vector<String> args) {
		if(Option.verbose)       args.add("-verbose");
		if(Option.execTrace > 0) args.add("-execTrace");
		if(Option.callTrace > 0) args.add("-callTrace");
		if(Option.dumpsAtExit)   args.add("-dumpsAtExit");
	}
	
	/// Get Compiler options from property file.
	/// @param properties the properties used.
	public static void getCompilerOptions(Properties properties) {
		Option.verbose          = Util.getBoolProperty(properties, "sPort.compiler.option.verbose", false);
		Option.CaseSensitive    = Util.getBoolProperty(properties, "sPort.compiler.option.CaseSensitive", false);
		Option.fecListing       = Util.getBoolProperty(properties, "sPort.compiler.option.fecListing", false);
		Option.fecSCodeTrace    = Util.getBoolProperty(properties, "sPort.compiler.option.fecSCodeTrace", false);
//		Option.fecTraceLevel    = Util.getIntProperty (properties, "sPort.compiler.option.fecTraceLevel", 0);
		Option.becSCodeTrace    = Util.getBoolProperty(properties, "sPort.compiler.option.becSCodeTrace", false);
		Option.becTraceSVM_CODE = Util.getBoolProperty(properties, "sPort.compiler.option.becTraceSVM_CODE", false);
		Option.becTraceSVM_DATA = Util.getBoolProperty(properties, "sPort.compiler.option.becTraceSVM_DATA", false);
		Option.execTrace        = Util.getIntProperty (properties, "sPort.runtime.option.callTrace", 0);
		Option.callTrace        = Util.getIntProperty (properties, "sPort.runtime.option.execVerbose", 0);
		Option.dumpsAtExit      = Util.getBoolProperty(properties, "sPort.runtime.option.dumpsAtExit", false);
	}
	
	/// Set Compiler options in property file.
	/// @param properties the properties used.
	public static void setCompilerOptions(Properties properties) {
		properties.setProperty("sPort.compiler.option.verbose",          ""+Option.verbose);
		properties.setProperty("sPort.compiler.option.CaseSensitive",    ""+Option.CaseSensitive);
		properties.setProperty("sPort.compiler.option.fecListing",       ""+Option.fecListing);
		properties.setProperty("sPort.compiler.option.fecSCodeTrace",    ""+Option.fecSCodeTrace);
//		properties.setProperty("sPort.compiler.option.fecTraceLevel",    ""+Option.fecTraceLevel);
		properties.setProperty("sPort.compiler.option.becSCodeTrace",    ""+Option.becSCodeTrace);
		properties.setProperty("sPort.compiler.option.becTraceSVM_CODE", ""+Option.becTraceSVM_CODE);
		properties.setProperty("sPort.compiler.option.becTraceSVM_DATA", ""+Option.becTraceSVM_DATA);
		properties.setProperty("sPort.runtime.option.execTrace",         ""+Option.execTrace);
		properties.setProperty("sPort.runtime.option.callTrace",         ""+Option.callTrace);
		properties.setProperty("sPort.runtime.option.dumpsAtExit",       ""+Option.dumpsAtExit);
	}

	/// Returns the option name 'id'
	/// @param id option id
	/// @return the option name 'id'
	public static boolean getOption(String id) {
		if(id.equalsIgnoreCase("verbose"))          return(verbose); 
		if(id.equalsIgnoreCase("CaseSensitive"))    return(CaseSensitive); 
		if(id.equalsIgnoreCase("TRACE_SCAN"))       return(TRACE_SCAN); 
		if(id.equalsIgnoreCase("TRACE_COMMENTS"))   return(TRACE_COMMENTS); 
		if(id.equalsIgnoreCase("fecListing"))       return(fecListing); 
		if(id.equalsIgnoreCase("fecSCodeTrace"))    return(fecSCodeTrace); 
//		if(id.equalsIgnoreCase("fecTraceLevel"))    return(fecTraceLevel > 0); 
		if(id.equalsIgnoreCase("becSCodeTrace"))    return(becSCodeTrace); 
		if(id.equalsIgnoreCase("becTraceSVM_CODE")) return(becTraceSVM_CODE); 
		if(id.equalsIgnoreCase("becTraceSVM_DATA")) return(becTraceSVM_DATA); 
		if(id.equalsIgnoreCase("execTrace"))        return(execTrace > 0); 
		if(id.equalsIgnoreCase("callTrace"))        return(callTrace > 0); 
		if(id.equalsIgnoreCase("dumpsAtExit"))      return(dumpsAtExit); 
		return(false);
	}

	/// Set the option named 'id' to the given value
	/// @param id option id
	/// @param val new option value
	public static void setOption(String id,boolean val) {
		if(id.equalsIgnoreCase("verbose"))          verbose=val; 
		if(id.equalsIgnoreCase("CaseSensitive"))    CaseSensitive=val; 
		if(id.equalsIgnoreCase("TRACE_SCAN"))       TRACE_SCAN=val; 
		if(id.equalsIgnoreCase("TRACE_COMMENTS"))   TRACE_COMMENTS=val; 
		if(id.equalsIgnoreCase("fecListing"))       fecListing=val; 
		if(id.equalsIgnoreCase("fecSCodeTrace"))    fecSCodeTrace=val; 
//		if(id.equalsIgnoreCase("fecTraceLevel"))    fecTraceLevel=(val)?1:0; 
		if(id.equalsIgnoreCase("becSCodeTrace"))    becSCodeTrace=val; 
		if(id.equalsIgnoreCase("becTraceSVM_CODE")) becTraceSVM_CODE=val; 
		if(id.equalsIgnoreCase("becTraceSVM_DATA")) becTraceSVM_DATA=val; 
		if(id.equalsIgnoreCase("execTrace"))        execTrace=(val)?1:0; 
		if(id.equalsIgnoreCase("callTrace"))        callTrace=(val)?1:0; 
		if(id.equalsIgnoreCase("dumpsAtExit"))      dumpsAtExit=val; 
	}

	/// Editor Utility: Select Compiler Options.
	public static void selectCompilerOptions() {
		JPanel panel=new JPanel();
		panel.setBackground(Color.white);
		panel.add(Util.checkBox("Verbose","Output messages about what the editor is doing"));
		panel.add(Util.checkBox("CaseSensitive","Source file is case sensitive."));
//		panel.add(Util.checkBox("TRACE_SCAN","Debug option"));
//		panel.add(Util.checkBox("TRACE_COMMENTS","Debug option"));
		panel.add(new JLabel(" "));
		panel.add(Util.checkBox("fecListing","Output source listing"));
		panel.add(Util.checkBox("fecSCodeTrace","Debug option"));
//		panel.add(Util.checkBox("fecTraceLevel","Debug option"));
		panel.add(new JLabel(" "));
		panel.add(Util.checkBox("BecSCodeTrace","Debug option"));
		panel.add(Util.checkBox("becTraceSVM_CODE","Debug option"));
		panel.add(Util.checkBox("becTraceSVM_DATA","Debug option"));
		panel.add(new JLabel(" "));
    	panel.add(Util.checkBox("execTrace", "Trace all instructions at runtime"));
    	panel.add(Util.checkBox("callTrace", "Routine call trace at runtime"));
    	panel.add(Util.checkBox("dumpsAtExit", "Print dumps at exit"));
		panel.add(new JLabel(" "));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Util.optionDialog(panel,"Select Compiler Options",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,"Ok");
    	Global.storeSPortEditorProperties();
	}

}
