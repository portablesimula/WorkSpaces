// JavaLine 1 <== SourceLine 9
package simprog;
// Simula-2.0 Compiled at Fri Jul 31 10:32:52 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst114 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=9, lastLine=9, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public simtst114(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 18 <== SourceLine 70
        new simtst114_SimulaTest_Begin(_USR,114,new RTS_TXT("--- Test Switch Statement"))._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("simtst114", args);
        RTS_UTIL.RUN_STM(new simtst114(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/simtst114.sim","SimulaProgram simtst114",1,9,18,70,28,9);
} // End of SubBlock
