// JavaLine 1 <== SourceLine 561
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 10:34:34 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst107 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=561, lastLine=561, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public simtst107(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new simtst107_SimulaTestBegin(_USR,107,new RTS_TXT("--- Test Process, activation statements, idle, terminated, time."))._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("simtst107", args);
        RTS_UTIL.RUN_STM(new simtst107(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst107.sim","SimulaProgram simtst107",1,561,27,561);
} // End of SubBlock
