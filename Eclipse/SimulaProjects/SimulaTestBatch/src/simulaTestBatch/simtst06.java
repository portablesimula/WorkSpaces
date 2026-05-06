// JavaLine 1 <== SourceLine 19
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=19, lastLine=314, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public simtst06(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new simtst06_SimulaTestBegin(_USR,6,new RTS_TXT("--- Test of math. functions"))._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("simtst06", args);
        RTS_UTIL.RUN_STM(new simtst06(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","SimulaProgram simtst06",1,19,27,314);
} // End of SubBlock
