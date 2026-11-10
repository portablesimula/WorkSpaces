package simprog;
// Simula-2.0 Compiled at Mon Nov 09 15:43:28 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class AdHoc00 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=0, lastLine=0, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public AdHoc00(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new AdHoc00_Block0(_USR)._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("AdHoc00", args);
        RTS_UTIL.RUN_STM(new AdHoc00(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaErrors/expression/AdHoc00.sim","SimulaProgram AdHoc00",26,0);
} // End of SubBlock
