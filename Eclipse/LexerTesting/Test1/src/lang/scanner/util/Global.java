package lang.scanner.util;

import javax.swing.*;
import java.awt.*;
import java.io.Console;

public class Global {
//    static public Language simulaLanguage = new SimulaLanguage();

    public static Console console;
    public static int sourceLineNumber;

    public static void println(String s){
        console.printf(s);
    }


    /// Pop up an error message box.
    /// @param msg the error message
    public static void popUpMessage(final String msg) {
        int res=optionDialog(msg+"\nDo you want to continue ?","Break",JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, "Yes", "No");
        if(res!= JOptionPane.YES_OPTION) System.exit(0);
    }

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
        int answer = JOptionPane.showOptionDialog(null, msg, title, optionType, messageType, null, option, option[0]);
        // IO.println("doClose.saveDialog: answer="+answer);
        UIManager.put("OptionPane.background", OptionPaneBackground);
        UIManager.put("Panel.background", PanelBackground);
        return (answer);
    }

}
