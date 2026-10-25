/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.runtime;

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/// RTS Dialog utilities
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/runtime/RTS_Dialog.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public interface RTS_Dialog {
	
	/// Brings up an option dialog.
	/// @param msg the message to display
	/// @param title the title string for the dialog
	/// @param optionType an integer designating the options available on the dialog
	/// @param messageType an integer designating the kind of message this is
	/// @param option an array of objects indicating the possible choices the user can make
	/// @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog
	public static int optionDialog(final Object msg, final String title, final int optionType, final int messageType, final String... option) {
		Object OptionPaneBackground = UIManager.get("OptionPane.background");
		Object PanelBackground = UIManager.get("Panel.background");
		UIManager.put("OptionPane.background", Color.WHITE);
		UIManager.put("Panel.background", Color.WHITE);
		int answer = JOptionPane.showOptionDialog(null, msg, title, optionType, messageType, RTS_UTIL.simicon, option, option[0]);
		// IO.println("doClose.saveDialog: answer="+answer);
		UIManager.put("OptionPane.background", OptionPaneBackground);
		UIManager.put("Panel.background", PanelBackground);
		return (answer);
	}
	
	/// Brings up an input dialog.
	/// @param msg the message to display
	/// @param title the title string for the dialog
	/// @param messageType an integer designating the kind of message this is
	/// @return the resulting String
	public static String inputDialog(final Object msg, final String title, final int messageType) {
		Object OptionPaneBackground = UIManager.get("OptionPane.background");
		Object PanelBackground = UIManager.get("Panel.background");
		UIManager.put("OptionPane.background", Color.WHITE);
		UIManager.put("Panel.background", Color.WHITE);
//		int answer = JOptionPane.showOptionDialog(null, msg, title, optionType, messageType, RTS_UTIL.simicon, option, option[0]);
        String answer = (String) JOptionPane.showInputDialog(null, msg, title, JOptionPane.QUESTION_MESSAGE, RTS_UTIL.simicon, null, null);
		// IO.println("doClose.saveDialog: answer="+answer);
		UIManager.put("OptionPane.background", OptionPaneBackground);
		UIManager.put("Panel.background", PanelBackground);
		return (answer);
	}
	
	/// Brings up an confirm dialog.
	/// @param msg the message to display
	/// @param title the title string for the dialog
	/// @param messageType an integer designating the kind of message this is
	/// @return the resulting String
	public static boolean confirmDialog(final Object msg, final String title, final int messageType) {
		Object OptionPaneBackground = UIManager.get("OptionPane.background");
		Object PanelBackground = UIManager.get("Panel.background");
		UIManager.put("OptionPane.background", Color.WHITE);
		UIManager.put("Panel.background", Color.WHITE);
		
//		public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon)
    	int result = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, RTS_UTIL.simicon);
		// IO.println("doClose.saveDialog: answer="+answer);
		UIManager.put("OptionPane.background", OptionPaneBackground);
		UIManager.put("Panel.background", PanelBackground);
		return result == JOptionPane.YES_OPTION;
	}

}
