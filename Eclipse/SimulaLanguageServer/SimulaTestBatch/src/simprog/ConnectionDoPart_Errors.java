package simprog;
// Simula-2.0 Compiled at Wed Nov 11 15:49:34 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class ConnectionDoPart_Errors extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=0, lastLine=0, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public ConnectionDoPart_Errors(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        {
        }
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("ConnectionDoPart_Errors", args);
        RTS_UTIL.RUN_STM(new ConnectionDoPart_Errors(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaErrors/statement/ConnectionDoPart_Errors.sim","SimulaProgram ConnectionDoPart_Errors",27,0);
} // End of SubBlock
