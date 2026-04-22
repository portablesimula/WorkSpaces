// JavaLine 1 <== SourceLine 39
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 09:05:59 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst104 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=39, lastLine=39, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public simtst104(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new simtst104_SimulaTestBegin(_USR,104,new RTS_TXT("--- Test procedure parameter 'F' by name."))._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("simtst104", args);
        RTS_UTIL.RUN_STM(new simtst104(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst104.sim","SimulaProgram simtst104",1,39,27,39);
} // End of SubBlock
