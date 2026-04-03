// JavaLine 1 <== SourceLine 28
package simulaTestBatch;
// Simula-2.0 Compiled at Fri Apr 03 09:55:09 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst04 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=28, lastLine=385, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public simtst04(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 18 <== SourceLine 385
        new simtst04_SimulaTestBegin(_USR,4,new RTS_TXT("--- Test reading numbers from source text"))._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("simtst04", args);
        RTS_UTIL.RUN_STM(new simtst04(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst04.sim","SimulaProgram simtst04",1,28,18,385,28,385);
} // End of SubBlock
