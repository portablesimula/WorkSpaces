// JavaLine 1 <== SourceLine 28
package simulaTestBatch;
// Simula-2.0 Compiled at Tue May 12 10:50:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst40 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=28, lastLine=40, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public simtst40(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new simtst40_SimulaTestBegin(_USR,40,new RTS_TXT("--- Test separate compilation of procedures."))._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("simtst40", args);
        RTS_UTIL.RUN_STM(new simtst40(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst40.sim","SimulaProgram simtst40",1,28,27,40);
} // End of SubBlock
