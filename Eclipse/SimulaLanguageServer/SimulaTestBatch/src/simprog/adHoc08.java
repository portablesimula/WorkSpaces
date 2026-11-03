package simprog;
// Simula-2.0 Compiled at Tue Nov 03 09:50:36 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc08 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=0, lastLine=0, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public adHoc08(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new adHoc08_Block0(_USR)._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("adHoc08", args);
        RTS_UTIL.RUN_STM(new adHoc08(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/adHoc08.sim","SimulaProgram adHoc08",26,0);
} // End of SubBlock
