// JavaLine 1 <== SourceLine 1
package simulaTestBatch;
// Simula-2.0 Compiled at Sun Apr 19 08:28:54 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=1, lastLine=15, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public adHoc00(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new adHoc00_Block1(_USR)._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("adHoc00", args);
        RTS_UTIL.RUN_STM(new adHoc00(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","SimulaProgram adHoc00",1,1,27,15);
} // End of SubBlock
