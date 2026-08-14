// JavaLine 1 <== SourceLine 3
package simprog;
// Simula-2.0 Compiled at Thu Aug 13 08:24:00 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst00 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=3, lastLine=3, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public simtst00(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 18 <== SourceLine 7
        new simtst00_SimulaTest_Begin(_USR,0,new RTS_TXT("--- Empty test"))._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("simtst00", args);
        RTS_UTIL.RUN_STM(new simtst00(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/simtst00.sim","SimulaProgram simtst00",1,3,18,7,28,3);
} // End of SubBlock
