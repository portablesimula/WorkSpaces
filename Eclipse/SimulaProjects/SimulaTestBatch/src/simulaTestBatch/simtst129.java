// JavaLine 1 <== SourceLine 10
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 13 08:12:35 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst129 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=10, lastLine=41, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public simtst129(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new simtst129_Precompiled129Begin(_USR,129,new RTS_TXT("--- Test switch in separate compiled class Precompiled129"),44)._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("simtst129", args);
        RTS_UTIL.RUN_STM(new simtst129(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst129.sim","SimulaProgram simtst129",1,10,27,41);
} // End of SubBlock
