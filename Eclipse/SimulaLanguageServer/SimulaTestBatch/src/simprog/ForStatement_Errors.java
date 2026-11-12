package simprog;
// Simula-2.0 Compiled at Thu Nov 12 09:38:40 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class ForStatement_Errors extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=0, lastLine=0, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public ForStatement_Errors(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new ForStatement_Errors_Block0(_USR)._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("ForStatement_Errors", args);
        RTS_UTIL.RUN_STM(new ForStatement_Errors(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaErrors/statement/ForStatement_Errors.sim","SimulaProgram ForStatement_Errors",26,0);
} // End of SubBlock
