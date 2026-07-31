// JavaLine 1 <== SourceLine 10
package simulaTestBatch;
// Simula-2.0 Compiled at Fri Jul 31 11:16:48 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst114 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=10, lastLine=0, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public simtst114(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new simtst114_PBLK11(_USR,114,new RTS_TXT("--- Test Switch Statement"))._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("simtst114", args);
        RTS_UTIL.RUN_STM(new simtst114(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst114.sim","SimulaProgram simtst114",1,10,27,0);
} // End of SubBlock
