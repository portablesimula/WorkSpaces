// JavaLine 1 <== SourceLine 21
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:55:16 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst21 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=21, lastLine=199, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public simtst21(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new simtst21_SimulaTestBegin(_USR,21,new RTS_TXT("--- Test arrays of simple types and text."))._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("simtst21", args);
        RTS_UTIL.RUN_STM(new simtst21(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst21.sim","SimulaProgram simtst21",1,21,27,199);
} // End of SubBlock
